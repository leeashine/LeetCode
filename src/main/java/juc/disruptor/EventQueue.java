package juc.disruptor;

import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.v2.model.core.ID;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class EventQueue {

    private String queueName;
    private String nextKey;
    private LinkedBlockingDeque queue = new LinkedBlockingDeque();

    /**
     * 如果等待队列数量小于10000，则会进行去重(通过lrem先删除，然后再通过lpush进行排重)。
     * 如果等待队列数量大于10000，因为便利list性能回变得很差，则此时不会进行去重。
     * 数据同时回被放入备份队列，当备份队列满了时，使用FIFO移除最先插入的任务。
     */
//    static EventQueueScript ENQUEUE_TO_LEFT_REDIS_SCRIPT = new EventQueueScript(
//            " local remCount = 0 if redis.call('llen',KEYS[1]<10000 then remCount = redis.call('lrem',KETS[1],1,KEYS[2])" +
//                    " end redis.call('lpush',KEYS[1],KEYS[2]) " +
//                    " if tonumber(KEYS[4]) <=0 then return nil end "+
//                    " if remCount > 0 then return nil end "+
//                    " local len = redis.call('llen',KEYS[3]) "+
//                    " if len>tonumber(KEYS[4]) then redis.call('lpop',KEYS[3]) end redis.call('rpush',KEYS[3],KEYS[2]) "+
//                    ")"
//    );

    /**
     * next 用于从Redis等待队列获取任务并推送到本地处理队列，然后返回此任务。放入本地处理队列使用了rightPopAndLeftPush，
     * 目的是防止因为网络异常导致任务丢失(因为Redis本身是没有事务的)，当发生网络异常时需要告警，然后人工介入处理，获取
     * 启动一个Worker定期检查队列内容是否长时间未消费，如果长时间未消费，则应该再转移回等待队列处理。
     * 如果队列中没有任务，则应该短暂休息一会，然后重试，不要造成死循环耗死CPU。
     * @return
     * @throws Exception
     */
    public String next() throws Exception {
        while (true) {
            // 1.暂停Queue消费
            // PauseUtils.pauseQueue(queueName)
//            String id = null;
//            try {
//                // 2.从等待Queue POP，然后PUSH到本地处理队列
//                id = queueRedis.opsForList().rightPopAndLeftPush(queueName, processingQueueName);
//            } catch (Exception e) {
//                // 3.发生了网络异常后告警，然后人工或定期检测，
//                // 将本地任务队列长时间未消费的任务推送回等待队列
//                continue;
//            }
//            // 4.返回获取的任务
//            if (id != null) {
//                awiatInMillis = DEFAULT_AWAIT_IN_MILLIS;
//                return id;
//            }
//            local.local();
//            try {
//                // 如果没有任务，则休息一下稍后处理，防止死循环耗死cpu
//                if (awiatInMillis < 1000) {
//                    awiatInMillis = awiatInMillis + awiatInMillis;
//                }
//                notEmpty.await(awiatInMillis, TimeUnit.MILLISECONDS);
//            } catch (Exception e) {
//                //ignore
//            }finally {
//                lock.unlock();
//            }

        }
    }

    /**
     * 当任务成功处理后，从本地任务队列移除该任务
     * @param skuId
     */
    public void success(String skuId) {
//        queueRedis.opsForList().remove(processsingQueueName, 0, id);
    }

    /**
     * fail方法根据失败重试次数决定是放入等待队列重试，还是超过了失败重试次数直接转移到失败队列，然后告警，人工介入处理
     *
     * @param skuId
     */
    public void fail(String skuId) {
//        final int failedCount = failedCache.getUnchecked(id).incrementAndGet();
//        if (failedCount < processingErrorRetryCount) {
//            // 如果小于重试次数，则直接添加到等待队列尾
//            ADD_TO_BACK_REDIS_SCRIPT.exec(queueRedis, Lists.newArrayList(processingQueueName, queueName), id);
//        } else {
//            // 如果超过失败重试次数，则加入失败队列
//            ADD_TO_FAIL_QUEUE_REDIS_SCRIPT.exec(queueRedis, Lists.newArrayList(processingQueueName, failedQueueName), id);
//        }
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public LinkedBlockingDeque getQueue() {
        return queue;
    }

    public void setQueue(LinkedBlockingDeque queue) {
        this.queue = queue;
    }

    public String getNextKey() {
        return nextKey;
    }

    public void setNextKey(String nextKey) {
        this.nextKey = nextKey;
    }


}
