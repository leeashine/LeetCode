package juc.disruptor;

import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import juc.disruptor.miaosha.SeckillEvent;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public class EventPublishThread extends Thread {

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
            try {
                ringBuffer.publishEvent(EVENT_TRANSLATOR, String.valueOf(atomicInteger.incrementAndGet()), eventType);
//                Thread.sleep(300);
                if (atomicInteger.get() >= 10000) {
                    running = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void shutdown() {
        running = false;
    }
}
