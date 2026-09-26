package com.queue;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * {@link EventQueue} 单元测试：队列流转（等待 → 处理 → 成功/失败/失败队列）、
 * 排重、备份队列镜像与 FIFO 裁剪。
 *
 * <p>使用 {@link InMemoryQueueRedis}（无 Redis 环境）；其 Lua 语义与真实 Redis 一致，
 * 因此断言等价于集成测试。</p>
 */
public class EventQueueTest {

    private QueueRedis redis;
    private EventQueue queue;

    @Before
    public void setUp() {
        redis = new InMemoryQueueRedis();
        queue = new EventQueue(redis, "test:event:" + UUID.randomUUID());
        queue.setProcessingErrorRetryCount(2);
        queue.setMaxBakSize(3);
    }

    @Test
    public void nextMovesTaskFromWaitQueueToProcessingQueue() throws Exception {
        queue.enqueueToLeft("task-1");
        assertEquals(1L, redis.llen(queue.getQueueName()).longValue());
        assertEquals(0L, redis.llen(queue.getProcessingQueueName()).longValue());

        String id = queue.next();

        assertEquals("task-1", id);
        assertEquals(0L, redis.llen(queue.getQueueName()).longValue());
        assertEquals(1L, redis.llen(queue.getProcessingQueueName()).longValue());
    }

    @Test
    public void successRemovesTaskFromProcessingQueue() throws Exception {
        queue.enqueueToLeft("task-1");
        String id = queue.next();

        queue.success(id);

        assertEquals(0L, redis.llen(queue.getProcessingQueueName()).longValue());
    }

    @Test
    public void failRequeuesUntilRetryCountThenMovesToFailedQueue() throws Exception {
        queue.enqueueToLeft("task-x");

        // 第 1 次失败（failedCount=1 < 2）：从处理队列挪回等待队列队尾
        String id = queue.next();
        queue.fail(id);
        assertEquals(0L, redis.llen(queue.getProcessingQueueName()).longValue());
        assertEquals(1L, redis.llen(queue.getQueueName()).longValue());
        assertEquals(0L, redis.llen(queue.getFailedQueueName()).longValue());

        // 第 2 次失败（failedCount=2 不 < 2）：进入失败队列
        String id2 = queue.next();
        assertEquals("task-x", id2);
        queue.fail(id2);
        assertEquals(0L, redis.llen(queue.getProcessingQueueName()).longValue());
        assertEquals(0L, redis.llen(queue.getQueueName()).longValue());
        assertEquals(1L, redis.llen(queue.getFailedQueueName()).longValue());
        assertEquals("task-x", redis.lpop(queue.getFailedQueueName()));
    }

    @Test
    public void enqueueDeduplicatesWhileQueueBelowThreshold() {
        queue.enqueueToLeft("task-dup");
        queue.enqueueToLeft("task-dup"); // lrem 排重命中：队列中仍只有一条，且不重复镜像

        assertEquals(1L, redis.llen(queue.getQueueName()).longValue());
        assertEquals(1L, redis.llen(queue.makeBakQueueName()).longValue());
    }

    @Test
    public void backupQueueMirrorsEnqueueAndTrimsFifo() {
        // maxBakSize=3，按书中脚本语义：len > maxBakSize 时才 lpop 最老一条再 rpush
        for (int i = 1; i <= 5; i++) {
            queue.enqueueToLeft("task-" + i);
        }

        String bak = queue.makeBakQueueName();
        // id1 被裁剪，剩 [task-2, task-3, task-4, task-5]（脚本是先判断再追加，故为 maxBakSize+1）
        assertEquals(4L, redis.llen(bak).longValue());
        assertEquals("task-2", redis.lpop(bak));
        assertEquals("task-3", redis.lpop(bak));
        assertEquals("task-4", redis.lpop(bak));
        assertEquals("task-5", redis.lpop(bak));
    }

    @Test
    public void queueNamesFollowBookConventions() {
        assertEquals(queue.getQueueName() + "_failed_queue", queue.getFailedQueueName());
        assertEquals(queue.getQueueName() + "_processing_queue_", queue.getProcessingQueueName()
                .substring(0, queue.getProcessingQueueName().lastIndexOf('_') + 1));
        String bak = queue.makeBakQueueName();
        assertEquals(queue.getQueueName() + "_bak_queue_"
                + java.time.LocalTime.now().getHour(), bak);
    }
}
