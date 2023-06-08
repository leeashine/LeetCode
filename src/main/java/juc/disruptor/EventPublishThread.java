package juc.disruptor;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import juc.disruptor.miaosha.SeckillEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * eventQueue#next方法将从Redis等待队列pop一个任务，然后推送到本地任务队列(该队列名称是:queueName+jvm实例所在机器IP),
 * 并发布到Disruptor RingBuffer
 *
 * @author hahaha
 */
public class EventPublishThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(EventPublishThread.class);

    private String eventType;
    private EventQueue eventQueue;
    private RingBuffer ringBuffer;
    private volatile boolean running = true;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private EventTranslatorTwoArg<Event, String, String> EVENT_TRANSLATOR = (event, sequence, key, eventType) -> {
        event.setKey(key);
        event.setType(eventType);
        System.out.println("event 发布:sequence:" + sequence + ",key:" + key + ",type:" + eventType);
    };

    public EventPublishThread(String eventType, EventQueue eventQueue, RingBuffer ringBuffer) {
        this.eventType = eventType;
        this.eventQueue = eventQueue;
        this.ringBuffer = ringBuffer;
    }

    public static void translate(SeckillEvent event, long sequence, ByteBuffer buffer) {
        event.setSeckillId(buffer.getLong(1));
        event.setUserId(buffer.getLong(0));
    }


    @Override
    public void run() {
        while (running) {
            String nextKey = null;
            try {
                // 从EventQueue获取下一个任务
                if (nextKey == null) {
                    nextKey = eventQueue.next();
                }
                if (nextKey != null) {
                    ringBuffer.publishEvent(EVENT_TRANSLATOR, nextKey, eventType);
                }
//                Thread.sleep(300);
            } catch (Exception e) {
                logger.error("EventPublishThread error,key:{}", nextKey, e);
            }
        }

    }


    public void shutdown() {
        running = false;
    }
}
