package juc.disruptor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ExceptionHandlerWrapper;
import com.lmax.disruptor.dsl.ProducerType;
import juc.disruptor.miaosha.EventQueue;
import juc.disruptor.miaosha.SeckillEvent;
import juc.disruptor.miaosha.SeckillEventFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.*;

public class IDisruptorDemo {

    private static final LinkedList<EvenetPublishThread> evenetPublishThreads = new LinkedList<>();
    private static final Map<String, EventQueue> eventQueueMap = new HashMap<>();
    private static final Map<String, EventHandler> eventHandlerMap = new HashMap<>();

    private static final int threadPoolSize = 256;
    private static final Disruptor<SeckillEvent> disruptor = new Disruptor<SeckillEvent>(
            new SeckillEventFactory(), //事件工厂
            1024, //
            Executors.newFixedThreadPool(threadPoolSize), //消费者线程池
            ProducerType.MULTI, //支持多事件发布者
            new BlockingWaitStrategy() //阻塞等待策略
    );

    public static void main(String[] args) {

        RingBuffer<SeckillEvent> ringBuffer = disruptor.getRingBuffer();

        //创建工作者处理器
        WorkHandler<SeckillEvent> workHandler = new WorkHandler<SeckillEvent>() {
            @Override
            public void onEvent(SeckillEvent event) throws Exception {
                //获取该队列的事件处理器
                EventHandler eventHandler = eventHandlerMap.get(event.getUserId());
//                eventHandler.onEvent(event.getSeckillId(),);
            }
        };

        //数量为线程池大小
        WorkHandler[] workHandlers = new WorkHandler[threadPoolSize];

        for (int i = 0; i < threadPoolSize; i++) {
            workHandlers[i] = workHandler;
        }
        //告知Disruptor由工作者处理器处理
        disruptor.handleEventsWithWorkerPool(workHandlers);

        //启动disruptor
        disruptor.start();

        //启动发布者线程(每个EventQueue一个，可以优化为只有一个)
        eventQueueMap.forEach((eventType, eventQueue) -> {
            //每个类型的队列创建一个发布者
            EvenetPublishThread thread = new EvenetPublishThread(eventType, eventQueue, ringBuffer);
            thread.start();
        });


    }

    public void stop() {
        for (EvenetPublishThread evenetPublishThread : evenetPublishThreads) {
            evenetPublishThread.shutdown();
        }
        disruptor.shutdown();
    }
}
