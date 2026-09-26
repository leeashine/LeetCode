package com.queue;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * 端到端测试：EventPublishThread（RPOPLPUSH → RingBuffer）→ WorkerPool → EventHandler。
 *
 * <p>全部基于 {@link InMemoryQueueRedis}，无需真实 Redis；确定性（CountDownLatch 超时等待，
 * 无 sleep 断言）。</p>
 */
public class EventWorkerEndToEndTest {

    private static EventQueue newQueue(QueueRedis redis, int retryCount) {
        EventQueue queue = new EventQueue(redis, "test:worker:" + UUID.randomUUID());
        queue.setProcessingErrorRetryCount(retryCount);
        queue.setMaxBakSize(10000);
        return queue;
    }

    private static EventWorker newWorker(EventQueue queue, EventHandler handler,
                                         int threadPoolSize) {
        EventWorker worker = new EventWorker();
        worker.setThreadPoolSize(threadPoolSize);
        worker.setRingBufferSize(64);
        worker.setEventHandlerMap(Collections.singletonMap(queue, handler));
        return worker;
    }

    @Test
    public void allTasksHandledExactlyOnceAndProcessingQueueDrained() throws Exception {
        int taskCount = 200;
        QueueRedis redis = new InMemoryQueueRedis();
        EventQueue queue = newQueue(redis, 3);

        Map<String, AtomicInteger> handled = new ConcurrentHashMap<>();
        CountDownLatch latch = new CountDownLatch(taskCount);
        EventHandler handler = (key, eventType, q) -> {
            try {
                handled.computeIfAbsent(key, k -> new AtomicInteger()).incrementAndGet();
                latch.countDown();
                q.success(key);
            } catch (Exception e) {
                q.fail(key);
            }
        };

        EventWorker worker = newWorker(queue, handler, 4);
        worker.init();
        try {
            for (int i = 0; i < taskCount; i++) {
                queue.enqueueToLeft("task-" + i);
            }
            assertTrue("timed out waiting for all tasks",
                    latch.await(15, TimeUnit.SECONDS));
        } finally {
            worker.stop();
        }

        // 每个任务恰好处理一次（WorkerPool 竞争消费语义）
        assertEquals(taskCount, handled.size());
        for (Map.Entry<String, AtomicInteger> e : handled.entrySet()) {
            assertEquals("task handled more than once: " + e.getKey(), 1, e.getValue().get());
        }
        // 全部 success：处理队列清空；等待队列清空
        assertEquals(0L, redis.llen(queue.getProcessingQueueName()).longValue());
        assertEquals(0L, redis.llen(queue.getQueueName()).longValue());
        assertEquals(0L, redis.llen(queue.getFailedQueueName()).longValue());
    }

    @Test
    public void failingTaskIsRetriedThenMovedToFailedQueue() throws Exception {
        QueueRedis redis = new InMemoryQueueRedis();
        EventQueue queue = newQueue(redis, 2); // 重试 2 次后入失败队列

        int goodTasks = 5;
        CountDownLatch goodLatch = new CountDownLatch(goodTasks);
        CountDownLatch badFailedLatch = new CountDownLatch(2); // 坏任务两次 fail 均完成
        EventHandler handler = (key, eventType, q) -> {
            try {
                if ("bad-task".equals(key)) {
                    throw new RuntimeException("模拟业务处理失败");
                }
                goodLatch.countDown();
                q.success(key);
            } catch (Exception e) {
                q.fail(key);
                if ("bad-task".equals(key)) {
                    badFailedLatch.countDown();
                }
            }
        };

        EventWorker worker = newWorker(queue, handler, 2);
        worker.init();
        try {
            queue.enqueueToLeft("bad-task");
            for (int i = 0; i < goodTasks; i++) {
                queue.enqueueToLeft("good-" + i);
            }
            assertTrue("timed out waiting for good tasks", goodLatch.await(15, TimeUnit.SECONDS));
            assertTrue("timed out waiting for bad task retries", badFailedLatch.await(15, TimeUnit.SECONDS));
        } finally {
            worker.stop();
        }

        assertEquals(1L, redis.llen(queue.getFailedQueueName()).longValue());
        assertEquals("bad-task", redis.lpop(queue.getFailedQueueName()));
        assertEquals(0L, redis.llen(queue.getProcessingQueueName()).longValue());
        assertEquals(0L, redis.llen(queue.getQueueName()).longValue());
    }

    /**
     * WorkHandler 路由逻辑（纯 Disruptor 分发，无需 Redis）：
     * 按 eventType（队列名）找到对应 EventQueue 与 EventHandler。
     */
    @Test
    public void workHandlerRoutesByEventType() throws Exception {
        QueueRedis redis = new InMemoryQueueRedis();
        EventQueue queueA = newQueue(redis, 1);
        EventQueue queueB = newQueue(redis, 1);

        Map<String, String> keyToEventType = new ConcurrentHashMap<>();
        Map<String, EventQueue> keyToQueue = new ConcurrentHashMap<>();
        CountDownLatch latchA = new CountDownLatch(3);
        CountDownLatch latchB = new CountDownLatch(2);

        EventHandler handlerA = (key, eventType, q) -> {
            keyToEventType.put("A:" + key, eventType);
            keyToQueue.put("A:" + key, q);
            latchA.countDown();
            q.success(key);
        };
        EventHandler handlerB = (key, eventType, q) -> {
            keyToEventType.put("B:" + key, eventType);
            keyToQueue.put("B:" + key, q);
            latchB.countDown();
            q.success(key);
        };

        EventWorker worker = new EventWorker();
        worker.setThreadPoolSize(2);
        worker.setRingBufferSize(32);
        Map<EventQueue, EventHandler> map = new ConcurrentHashMap<>();
        map.put(queueA, handlerA);
        map.put(queueB, handlerB);
        worker.setEventHandlerMap(map);
        worker.init();
        try {
            for (int i = 0; i < 3; i++) {
                queueA.enqueueToLeft("a-" + i);
            }
            for (int i = 0; i < 2; i++) {
                queueB.enqueueToLeft("b-" + i);
            }
            assertTrue(latchA.await(15, TimeUnit.SECONDS));
            assertTrue(latchB.await(15, TimeUnit.SECONDS));
        } finally {
            worker.stop();
        }

        for (int i = 0; i < 3; i++) {
            assertEquals(queueA.getQueueName(), keyToEventType.get("A:a-" + i));
            assertSame(queueA, keyToQueue.get("A:a-" + i));
        }
        for (int i = 0; i < 2; i++) {
            assertEquals(queueB.getQueueName(), keyToEventType.get("B:b-" + i));
            assertSame(queueB, keyToQueue.get("B:b-" + i));
        }
    }
}
