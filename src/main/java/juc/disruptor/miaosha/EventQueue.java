package juc.disruptor.miaosha;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class EventQueue {

    long awaitInMillis;
    Condition notEmpty;
    ReentrantLock lock;

    public String next() {
        while (true) {
            //暂停Queue消费

            String id = null;
            try {
//                id = redis.opsForList().rightPopAndLeftPush(queueName, processQueueName);
            } catch (Exception e) {
                //发生了网络异常后告警，然后人工或定期检测
                //将本地任务队列长时间未消费的任务推送回等待队列
                continue;

            }
            //返回获取的任务
            if (id != null) {
                awaitInMillis = 10000;
                return id;
            }
            lock.lock();
            try {
                //如果没有任务 择休息一下稍后处理，防止死循环耗死CPU
                if (awaitInMillis < 1000) {
                    awaitInMillis = awaitInMillis + awaitInMillis;
                }
                notEmpty.await(awaitInMillis, TimeUnit.MILLISECONDS);
            } catch (Exception e) {

            }finally {
                lock.unlock();
            }
        }
    }

    //从本地队列移除该任务
    public void success(String id) {
//        redis.opsForList().remove(processQueueName, 0, id);
    }

    public void fail(final String id) {
//        final int failedCount = failedCache.getUnchecked(id).incrementAndGet();
        final int failedCount = 0;
        int processingErrorRetryCount = 0;
        if (failedCount < processingErrorRetryCount) {
            //如果小于重试次数，择直接添加到等待队列尾

        }
    }

}
