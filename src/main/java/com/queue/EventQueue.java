package com.queue;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 事件队列（《亿级流量网站架构核心技术》§15.9，p303–p311）。
 *
 * <p>由 4 类 Redis list 构成：</p>
 * <ul>
 *   <li>等待队列 {@code queueName} —— 生产者 {@link #enqueueToLeft(String)} LPUSH 写入；</li>
 *   <li>本地处理队列 {@code queueName_processing_queue_<本机IP>} —— {@link #next()} 通过
 *       RPOPLPUSH 把任务从等待队列挪到本 JVM 的处理队列，处理成功 {@link #success(String)}
 *       才移除，崩溃后由人工/定时任务把僵死任务挪回等待队列；</li>
 *   <li>失败队列 {@code queueName_failed_queue} —— 重试 {@code processingErrorRetryCount}
 *       次仍失败的任务进入；</li>
 *   <li>备份队列 {@code queueName_bak_queue_<小时>} —— 按小时轮转的镜像队列
 *       （最多保留 maxBakSize 条，FIFO 裁剪），用于重构/数据回滚。</li>
 * </ul>
 *
 * <p>任务流向：wait →(RPOPLPUSH)→ local processing → Disruptor → 成功 LREM 出处理队列 /
 * 失败重回等待队列队尾（超过重试次数则进失败队列）。</p>
 */
public class EventQueue {

    private static final Logger log = LoggerFactory.getLogger(EventQueue.class);

    private static final long DEFAULT_AWAIT_IN_MILLIS = 1L;
    private static final long MAX_AWAIT_IN_MILLIS = 1000L;

    /** 失败重试：从处理队列移除并放回等待队列队尾（lrem processing + rpush wait）。 */
    static final String ADD_TO_BACK_QUEUE_SCRIPT =
            "redis.call('lrem', KEYS[1], 0, KEYS[2]) redis.call('rpush', KEYS[3], KEYS[2])";

    /** 超过重试次数：从处理队列移除并放入失败队列（lrem processing + rpush failed）。 */
    static final String ADD_TO_FAIL_QUEUE_SCRIPT =
            "redis.call('lrem', KEYS[1], 0, KEYS[2]) redis.call('rpush', KEYS[3], KEYS[2])";

    /**
     * 入队脚本（书中原文，verbatim）：等待队列 &lt;10000 时先 lrem 排重；
     * 无条件 lpush；maxBakSize&lt;=0 或排重命中则不做备份镜像；
     * 备份队列超长则 lpop 最老一条再 rpush（FIFO 裁剪）。
     */
    static final String ENQUEUE_TO_LEFT_REDIS_SCRIPT =
            " local remCount = 0"
                    + " if redis.call('llen', KEYS[1]) < 10000 then"
                    + " remCount = redis.call('lrem', KEYS[1], 1, KEYS[2])"
                    + " end"
                    + " redis.call('lpush', KEYS[1], KEYS[2])"
                    + "  if tonumber(KEYS[4]) <=0 then"
                    + " return nil"
                    + " end"
                    + "  if remCount > 0 then"
                    + " return nil"
                    + " end"
                    + "  local len = redis.call('llen', KEYS[3])"
                    + "  if len > tonumber(KEYS[4]) then"
                    + " redis.call('lpop', KEYS[3])"
                    + " end"
                    + " redis.call('rpush', KEYS[3], KEYS[2]) ";

    private static final EventQueueScript ADD_TO_BACK_QUEUE =
            new EventQueueScript(ADD_TO_BACK_QUEUE_SCRIPT);
    private static final EventQueueScript ADD_TO_FAIL_QUEUE =
            new EventQueueScript(ADD_TO_FAIL_QUEUE_SCRIPT);
    private static final EventQueueScript ENQUEUE_TO_LEFT =
            new EventQueueScript(ENQUEUE_TO_LEFT_REDIS_SCRIPT);

    private QueueRedis queueRedis;
    private String queueName;
    private String processingQueueName;
    private String failedQueueName;
    /** 单任务处理失败后的最大重试次数（超过则进失败队列）。 */
    private int processingErrorRetryCount = 3;
    /** 备份队列最大长度（&lt;=0 表示不做备份）。 */
    private long maxBakSize = 10000L;

    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();
    private volatile long awaitInMillis = DEFAULT_AWAIT_IN_MILLIS;

    /** 每个任务的已失败次数（Guava LoadingCache，线程安全）。 */
    private final LoadingCache<String, AtomicInteger> failedCache =
            CacheBuilder.newBuilder().build(new CacheLoader<String, AtomicInteger>() {
                @Override
                public AtomicInteger load(String key) {
                    return new AtomicInteger();
                }
            });

    public EventQueue() {
    }

    public EventQueue(QueueRedis queueRedis, String queueName) {
        setQueueRedis(queueRedis);
        setQueueName(queueName);
    }

    /**
     * 取下一个任务：RPOPLPUSH 等待队列 → 本地处理队列。
     *
     * <p>取不到时在 notEmpty 条件上等待，等待时长指数退避（1ms 起翻倍，上限 1000ms），
     * 避免空队列时 CPU 空转；入队时会 signal 唤醒。</p>
     *
     * <p>RPOPLPUSH 抛异常（网络错误）时告警并重试 —— 书中提示此时可能有任务僵死在
     * 本地处理队列，需人工/定时任务挪回等待队列。</p>
     */
    public String next() throws InterruptedException {
        for (;;) {
            PauseUtils.pauseQueue(queueName);
            String id = null;
            try {
                id = queueRedis.rpoplpush(queueName, processingQueueName);
            } catch (Exception e) {
                // 网络错误：告警后继续重试；可能已有任务被挪到本地处理队列但未消费，
                // 需人工/定时任务把该 JVM 的僵死任务挪回等待队列（见 README 注意事项）
                log.error("rpoplpush 异常, queueName={}, 稍后重试", queueName, e);
                continue;
            }
            if (id != null) {
                awaitInMillis = DEFAULT_AWAIT_IN_MILLIS;
                return id;
            }
            lock.lock();
            try {
                notEmpty.await(awaitInMillis, TimeUnit.MILLISECONDS);
            } finally {
                lock.unlock();
            }
            awaitInMillis = Math.min(awaitInMillis * 2, MAX_AWAIT_IN_MILLIS);
        }
    }

    /** 任务处理成功：LREM 从本地处理队列移除。 */
    public void success(String id) {
        queueRedis.lrem(processingQueueName, 0, id);
    }

    private static volatile String localIp;

    private static String getLocalIp() {
        if (localIp == null) {
            synchronized (EventQueue.class) {
                if (localIp == null) {
                    String ip;
                    try {
                        ip = InetAddress.getLocalHost().getHostAddress();
                    } catch (Exception e) {
                        ip = "127.0.0.1";
                    }
                    localIp = ip;
                }
            }
        }
        return localIp;
    }
    /**
     * 任务处理失败：失败次数+1；未达 {@code processingErrorRetryCount} 则放回
     * 等待队列队尾重试，否则放入失败队列。
     */
    public void fail(String id) {
        int failedCount = failedCache.getUnchecked(id).incrementAndGet();
        if (failedCount < processingErrorRetryCount) {
            ADD_TO_BACK_QUEUE.exec(queueRedis, processingQueueName, id, queueName);
        } else {
            ADD_TO_FAIL_QUEUE.exec(queueRedis, processingQueueName, id, failedQueueName);
        }
    }

    /** 生产任务：LPUSH 入等待队列（队列 &lt;10000 时排重），并镜像到当前小时的备份队列。 */
    public void enqueueToLeft(String id) {
        ENQUEUE_TO_LEFT.exec(queueRedis, queueName, id, makeBakQueueName(), String.valueOf(maxBakSize));
        signalNotEmpty();
    }

    /** 备份队列按小时轮转：queueName_bak_queue_&lt;0..23&gt;，每天 24 个备份队列。 */
    public String makeBakQueueName() {
        return queueName + "_bak_queue_" + LocalTime.now().getHour();
    }

    private void signalNotEmpty() {
        lock.lock();
        try {
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    // ---- Spring bean 风格注入（书中 XML 配置） ----

    public void setQueueRedis(QueueRedis queueRedis) {
        this.queueRedis = queueRedis;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
        // 书中 p311 命名约定
        this.processingQueueName = queueName + "_processing_queue_" + getLocalIp();
        this.failedQueueName = queueName + "_failed_queue";
    }

    public void setProcessingErrorRetryCount(int processingErrorRetryCount) {
        this.processingErrorRetryCount = processingErrorRetryCount;
    }

    public void setMaxBakSize(long maxBakSize) {
        this.maxBakSize = maxBakSize;
    }

    public QueueRedis getQueueRedis() {
        return queueRedis;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getProcessingQueueName() {
        return processingQueueName;
    }

    public String getFailedQueueName() {
        return failedQueueName;
    }

    public int getProcessingErrorRetryCount() {
        return processingErrorRetryCount;
    }

    public long getMaxBakSize() {
        return maxBakSize;
    }
}
