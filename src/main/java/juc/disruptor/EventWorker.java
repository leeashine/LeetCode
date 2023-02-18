package juc.disruptor;

import com.google.common.collect.Maps;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 业务组件，用于创建Disruptor相关组件，包含如下配置项目
 * init/stop:init用于初始化并启动Disruptor.Stop用于当JVM终止时停止Disruptor组件
 * ringBufferSize:环形队列大小，大小必须是2的倍数
 * eventHandlerMap:映射EventQueue与EventHandler的关系，从特定的EventQueue中获取的任务将被关联的EventHandler处理
 */
public class EventWorker {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventWorker.class);

    private Map<EventQueue, EventHandler> eventHandlerMap;
    private Map<String, EventQueue> eventQueueMap;
    private Disruptor<Event> disruptor;
    private RingBuffer<Event> ringBuffer;
    private List<EventPublishThread> eventPublishThreads;
    private static final int ringBufferSize = 2048;
    private static final int threadPollSize = 4;

    /**
     * 初始化Disruptor、WorkHandler、EventHandler、EventPublishThread等组件，
     * 并启动Disruptor、EventPublishThread
     *
     * @throws Exception
     */
    public void init() throws Exception {

        //1.创建Disruptor
        disruptor = new Disruptor<Event>(
                new DefaultEventFactory(),
                ringBufferSize,
                Executors.newFixedThreadPool(threadPollSize), //消费者线程池
                ProducerType.MULTI,//支持多事件发布者
                new BlockingWaitStrategy() //阻塞等待策略
        );
        //获取ringBuffer
        ringBuffer = disruptor.getRingBuffer();
        //1.全局异常处理器
        final DisruptorExceptionHandler<Event> exceptionHandler = new DisruptorExceptionHandler<>("main", (ex, seq) -> {
            LOGGER.error("exception thrown on seq={}", seq, ex);
        });

        disruptor.setDefaultExceptionHandler(exceptionHandler);

        //4.创建工作者处理器 会将Event交给相应的EventHandler处理
        WorkHandler<Event> workHandler = new WorkHandler<Event>() {
            @Override
            public void onEvent(Event event) throws Exception {
                String type = event.getType();
                EventQueue queue = eventQueueMap.get(type);
                EventHandler<ProductEventHandler> handler = eventHandlerMap.get(queue);
//                handler.onEvent(event.getKey(), type, queue);
            }
        };

        //5.1创建工作者处理器（数量为线程池大小
        WorkHandler[] workHandlers = new WorkHandler[threadPollSize];
        for (int i = 0; i < threadPollSize; i++) {
            workHandlers[i] = workHandler;
        }

        //5.2告知Disruptor由工作者处理器处理
        disruptor.handleEventsWithWorkerPool(workHandlers);

        //6.启动Disruptor
        disruptor.start();

        //7.启动发布者线程（每个EventQueue一个，可以优化为只有一个）
        for (Map.Entry<String, EventQueue> eventQueueEntry : eventQueueMap.entrySet()) {
            String eventType = eventQueueEntry.getKey();
            EventQueue eventQueue = eventQueueEntry.getValue();
            //每个类型的队列创建一个发布者
            EventPublishThread thread = new EventPublishThread(eventType, eventQueue, ringBuffer);
            thread.start();
        }
    }

    public void stop() {
        //1.停止发布者线程
        for (EventPublishThread eventPublishThread : eventPublishThreads) {
            eventPublishThread.shutdown();
        }
        //2.停止Disruptor
        disruptor.shutdown();
    }


    public void setEventHandlerMap(Map<EventQueue, EventHandler> eventHandlerMap) {
        if (MapUtils.isNotEmpty(eventHandlerMap)) {
            this.eventHandlerMap = Maps.newHashMap();
            for (Map.Entry<EventQueue, EventHandler> entry : eventHandlerMap.entrySet()) {
                EventQueue queue = entry.getKey();
                this.eventQueueMap.put(queue.getQueueName(), queue);
            }
        }
    }
}
