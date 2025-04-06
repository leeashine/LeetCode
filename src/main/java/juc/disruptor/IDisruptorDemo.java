package juc.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import net.openhft.affinity.AffinityStrategies;
import net.openhft.affinity.AffinityThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class IDisruptorDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventWorker.class);

    private static Disruptor<Event> disruptor;
    private static RingBuffer<Event> ringBuffer;
    private static final int ringBufferSize = 2048;
    private static final int threadPollSize = 4;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {
        //1.创建Disruptor
        long now = System.currentTimeMillis();
        //创建环形下单队列
        disruptor = new Disruptor<>(
                //事件工厂类
                new DefaultEventFactory(),
                //队列大小 2n次幂 会进行位运算
                ringBufferSize,
                //亲和线程池 线程亲和性能够强制使你的应用线程运行在特定的一个或多个cpu上
                new AffinityThreadFactory("aft_engine_core", AffinityStrategies.SAME_CORE),
                //一个生产者
                ProducerType.SINGLE,
                //等待策略 阻塞队列
                new BlockingWaitStrategy()
        );
        //获取ringBuffer
        ringBuffer = disruptor.getRingBuffer();

        WorkHandler<Event> workHandler = new WorkHandler<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                System.out.println(Thread.currentThread().getName() + ":" + event + "开始处理event");
            }
        };

        //5.1创建工作者处理器（数量为线程池大小
        WorkHandler[] workHandlers = new WorkHandler[threadPollSize];
        for (int i = 0; i < threadPollSize; i++) {
            workHandlers[i] = workHandler;
        }

        EventHandler<Event> eventHandler = new EventHandler<Event>() {
            @Override
            public void onEvent(Event event, long sequence, boolean endOfBatch) throws Exception {
                System.out.println(Thread.currentThread().getName() + ":" + event + "开始处理");
            }
        };

//        disruptor.handleEventsWithWorkerPool(workHandlers);
        disruptor.handleEventsWith(eventHandler);

        //6.启动Disruptor
        disruptor.start();

//        new EventPublishThread("iEvent", new EventQueue(), ringBuffer).start();
        for (int i = 0; i < 1000000; i++) {
            ringBuffer.publishEvent(EVENT_TRANSLATOR, String.valueOf("1"), "eventType");
        }

        System.out.println("cost " + (System.currentTimeMillis() - now));

        disruptor.shutdown();

    }

    private static EventTranslatorTwoArg<Event, String, String> EVENT_TRANSLATOR = (event, sequence, key, eventType) -> {
        event.setKey(key);
        event.setType(eventType);
        System.out.println("event 发布:sequence:" + sequence + ",key:" + key + ",type:" + eventType);
    };

}


