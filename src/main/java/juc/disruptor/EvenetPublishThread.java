package juc.disruptor;

import com.lmax.disruptor.InsufficientCapacityException;
import com.lmax.disruptor.RingBuffer;
import juc.disruptor.miaosha.EventQueue;
import juc.disruptor.miaosha.SeckillEvent;

import java.nio.ByteBuffer;

public class EvenetPublishThread extends Thread{

    private String eventType;
    private EventQueue eventQueue;
    private RingBuffer ringBuffer;

    public EvenetPublishThread(String eventType, EventQueue eventQueue, RingBuffer ringBuffer) {
        this.eventType = eventType;
        this.eventQueue = eventQueue;
        this.ringBuffer = ringBuffer;
    }

    public static void translate(SeckillEvent event, long sequence, ByteBuffer buffer)
    {
        event.setSeckillId(buffer.getLong(1));
        event.setUserId(buffer.getLong(0));
    }


    @Override
    public void run() {

        //获取数据 写
        try {
            long next = ringBuffer.tryNext();
            Object o = ringBuffer.get(next);
            //转成对应处理数据结构体
            ByteBuffer buffer = ByteBuffer.allocate(8);
            translate((SeckillEvent) o, next, buffer);
            //发布消息
            ringBuffer.publish(next);

        } catch (InsufficientCapacityException e) {
            e.printStackTrace();
        }


    }
}
