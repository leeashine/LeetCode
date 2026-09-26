package com.queue;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 队列暂停工具：被暂停的队列在 {@link EventQueue#next()} 中会被跳过（阻塞等待恢复）。
 *
 * <p>书中用于运维场景（如下游系统维护时暂停消费某队列）。</p>
 */
public final class PauseUtils {

    private static final long PAUSED_CHECK_INTERVAL_MILLIS = 50L;

    private static final Set<String> PAUSED_QUEUES = ConcurrentHashMap.newKeySet();

    private PauseUtils() {
    }

    public static void pause(String queueName) {
        PAUSED_QUEUES.add(queueName);
    }

    public static void resume(String queueName) {
        PAUSED_QUEUES.remove(queueName);
    }

    public static boolean isPaused(String queueName) {
        return PAUSED_QUEUES.contains(queueName);
    }

    /** 若队列处于暂停状态则阻塞，直到被 {@link #resume(String)}。 */
    public static void pauseQueue(String queueName) throws InterruptedException {
        while (isPaused(queueName)) {
            Thread.sleep(PAUSED_CHECK_INTERVAL_MILLIS);
        }
    }
}
