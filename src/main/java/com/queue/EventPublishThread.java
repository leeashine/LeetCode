package com.queue;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 事件发布线程（书中 EventPublishThread，p306）：
 * 循环 {@link EventQueue#next()} 阻塞取任务 → 发布进 Disruptor RingBuffer。
 *
 * <p>每个事件类型（队列）一个实例。</p>
 */
public class EventPublishThread extends Thread {

    private static final Logger log = LoggerFactory.getLogger(EventPublishThread.class);

    private static final EventTranslatorTwoArg<Event, String, String> EVENT_TRANSLATOR =
            new EventTranslatorTwoArg<Event, String, String>() {
                @Override
                public void translateTo(Event event, long sequence, String key, String eventType) {
                    event.setKey(key);
                    event.setEventType(eventType);
                }
            };

    private final String eventType;
    private final EventQueue eventQueue;
    private final RingBuffer<Event> ringBuffer;
    private volatile boolean running = true;

    public EventPublishThread(String eventType, EventQueue eventQueue, RingBuffer<Event> ringBuffer) {
        super("event-publish-" + eventType);
        this.eventType = eventType;
        this.eventQueue = eventQueue;
        this.ringBuffer = ringBuffer;
        setDaemon(true); // 书中未设置；daemon 避免阻止 JVM 退出
    }

    @Override
    public void run() {
        while (running) {
            try {
                String nextKey = eventQueue.next();
                if (nextKey != null) {
                    ringBuffer.publishEvent(EVENT_TRANSLATOR, nextKey, eventType);
                }
            } catch (Exception e) {
                log.error("发布事件失败, eventType={}", eventType, e);
            }
        }
    }

    public void shutdown() {
        running = false;
    }
}
