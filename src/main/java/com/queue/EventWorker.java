package com.queue;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 事件消费者（书中 EventWorker，p307–p310）：把若干 {@link EventQueue} 的任务
 * 通过 Disruptor 分发给对应的 {@link EventHandler}。
 *
 * <p>结构：每个队列一个 {@link EventPublishThread}（RPOPLPUSH → RingBuffer）；
 * WorkerPool 中 threadPoolSize 个 WorkHandler 竞争消费，按 eventType（=队列名）
 * 路由到注册的 EventHandler。</p>
 *
 * <p>生命周期：{@link #init()} 启动（书中 Spring init-method），{@link #stop()} 停止
 * （destroy-method）。</p>
 */
public class EventWorker {

    private static final Logger log = LoggerFactory.getLogger(EventWorker.class);

    private int threadPoolSize;
    private int ringBufferSize;
    /** EventQueue → 处理该队列任务的 EventHandler（书中通过 Spring map 注入）。 */
    private Map<EventQueue, EventHandler> eventHandlerMap;

    /** 派生：eventType（队列名）→ EventQueue。 */
    private Map<String, EventQueue> eventQueueMap;

    private Disruptor<Event> disruptor;
    private RingBuffer<Event> ringBuffer;
    private Executor executor;
    private final List<EventPublishThread> publishThreads = new ArrayList<>();

    @SuppressWarnings({"unchecked", "rawtypes", "deprecation"})
    public void init() throws Exception {
        eventQueueMap = new HashMap<>();
        for (EventQueue eventQueue : eventHandlerMap.keySet()) {
            eventQueueMap.put(eventQueue.getQueueName(), eventQueue);
        }

        // 1) 创建 Disruptor（书中 3.2.1 的 Executor 构造器在 3.4.2 中仍可用，已标记 deprecated）
        executor = Executors.newFixedThreadPool(threadPoolSize);
        disruptor = new Disruptor<>(new DefaultEventFactory(), ringBufferSize, executor,
                ProducerType.MULTI, new BlockingWaitStrategy());

        // 2) RingBuffer
        ringBuffer = disruptor.getRingBuffer();

        // 3) 全局异常处理：WorkHandler 抛出的未捕获异常兜底 —— 记录并把任务标记失败重试
        disruptor.handleExceptionsWith(new ExceptionHandler() {
            // disruptor 3.4.2 的 ExceptionHandler 抽象方法签名（旧接口，已 deprecated 但书中结构保留）
            @Override
            public void handleEventException(Throwable ex, long sequence, Object event) {
                if (isShutdownSignal(ex)) {
                    // Disruptor 关闭过程中触发的异常属正常停机，不应把任务标记失败
                    log.info("Disruptor 停机中, 忽略异常: {}", ex.toString());
                    return;
                }
                Event e = (Event) event;
                log.error("处理事件异常, event={}", e, ex);
                EventQueue queue = eventQueueMap.get(e.getEventType());
                if (queue != null) {
                    queue.fail(e.getKey());
                }
            }

            @Override
            public void handleOnStartException(Throwable ex) {
                log.error("Disruptor 启动异常", ex);
            }

            @Override
            public void handleOnShutdownException(Throwable ex) {
                log.error("Disruptor 关闭异常", ex);
            }
        });
        // 4) 业务分发：按 eventType 找到队列与处理器
        WorkHandler<Event> workHandler = new WorkHandler<Event>() {
            @Override
            public void onEvent(Event event) {
                EventQueue queue = eventQueueMap.get(event.getEventType());
                EventHandler handler = eventHandlerMap.get(queue);
                handler.onEvent(event.getKey(), event.getEventType(), queue);
            }
        };

        // 5) WorkerPool：threadPoolSize 个 WorkHandler 竞争消费（同一实例复用，书中写法）
        WorkHandler<Event>[] workerHandlers = new WorkHandler[threadPoolSize];
        Arrays.fill(workerHandlers, workHandler);
        disruptor.handleEventsWithWorkerPool(workerHandlers);

        // 6) 启动 Disruptor
        disruptor.start();

        // 7) 每个队列启动一个发布线程
        for (Map.Entry<String, EventQueue> entry : eventQueueMap.entrySet()) {
            EventPublishThread publishThread =
                    new EventPublishThread(entry.getKey(), entry.getValue(), ringBuffer);
            publishThread.start();
            publishThreads.add(publishThread);
        }
    }

    /** 停止：先停发布线程（不再取新任务），再关闭 Disruptor 排空 RingBuffer。 */
    public void stop() throws InterruptedException {
        for (EventPublishThread publishThread : publishThreads) {
            publishThread.shutdown();
            publishThread.interrupt(); // 唤醒 next() 中的条件等待
        }
        for (EventPublishThread publishThread : publishThreads) {
            publishThread.join(5000L);
        }
        if (disruptor != null) {
            disruptor.shutdown();
        }
        if (executor != null) {
            ((java.util.concurrent.ExecutorService) executor).shutdown();
        }
    }

    private static boolean isShutdownSignal(Throwable ex) {
        // halt() 触发 AlertException；其余关闭相关异常按类名兜底判断
        return ex instanceof com.lmax.disruptor.AlertException
                || ex.getClass().getSimpleName().contains("Shutdown");
    }

    // ---- Spring bean 风格注入 ----

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public void setRingBufferSize(int ringBufferSize) {
        this.ringBufferSize = ringBufferSize;
    }

    public void setEventHandlerMap(Map<EventQueue, EventHandler> eventHandlerMap) {
        this.eventHandlerMap = eventHandlerMap;
    }

    public RingBuffer<Event> getRingBuffer() {
        return ringBuffer;
    }
}
