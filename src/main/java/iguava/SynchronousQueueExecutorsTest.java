package iguava;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * SynchronousQueue探究
 * hystrix默认线程池的配置是使用SynchronousQueue和CoreSize
 * 明明SynchronousQueue不存储元素，为什么还要设置CoreSize，有什么意义？coreSize是否会预先创建线程？缓存线程？
 * 1. 即时任务响应
 * SynchronousQueue 不存储任务，每个任务的提交都必须直接被一个线程接收和执行。因此，它适用于需要快速响应的场景。设置 coreSize 确保有一定数量的线程始终处于就绪状态，能够立即处理新提交的任务。
 *
 * 2. 动态线程管理
 * 当使用 SynchronousQueue：
 * 如果线程池中的所有核心线程都在忙碌，对新任务的提交将导致线程池创建新的线程，直到达到 maximumPoolSize。
 * 这种方式确保在高负载下，线程池可以灵活扩展至最大容量，以处理突增的任务负载。
 * 3. 避免任务排队
 * 由于 SynchronousQueue 的特性，任务不会在队列中排队等待，而是必须立即被处理。这减少了任务的等待时间，提高了处理效率。同时，这也意味着系统的反应速度和线程调度的效率至关重要。
 *
 * 4. 资源利用优化
 * 设置 coreSize：
 * 确保了有足够的线程可用于处理并发任务，减少了因任务等待线程而延迟的情况。
 * 核心线程由于不受 keepAliveTime 影响（除非显式设置 allowCoreThreadTimeOut），因此在空闲时不会被立即终止，这有助于提高对突发负载的响应能力。
 * 5. 减少线程创建与销毁的开销
 * 通过维持一定数量的核心线程，coreSize 的设置有助于减少频繁创建和销毁线程的开销，特别是在任务执行时间较短或者任务频繁提交的场景中。
 *
 * 总结
 * 结合使用 SynchronousQueue 和适当的 coreSize，可以为高并发环境下的任务处理提供高效、灵活和快速的线程管理策略。这种配置特别适用于处理大量短生命周期的任务，可以有效地提升任务处理的速度和系统的吞吐能力，同时优化资源的使用效率。
 */
public class SynchronousQueueExecutorsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SynchronousQueueExecutorsTest.class);

    public static void main(String[] args) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("async-pool-%d").build();
        // 配置了SynchronousQueue，coreSize会预先创建5个核心线程
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                50,
                100,
                1,
                TimeUnit.MINUTES,
                new SynchronousQueue<>(),
                threadFactory);
        // 打印线程池初始状态
        LOGGER.info("Initial Pool Size: " + poolExecutor.getPoolSize());

//        ListeningExecutorService listeningExecutor = MoreExecutors.listeningDecorator(poolExecutor);
        // 打印线程池状态
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            LOGGER.info("Active Threads: " + poolExecutor.getActiveCount());
            LOGGER.info("Core Pool Size: " + poolExecutor.getCorePoolSize());
            LOGGER.info("Pool Size: " + poolExecutor.getPoolSize());
            LOGGER.info("Queue Size: " + poolExecutor.getQueue().size());
        }, 0, 1, TimeUnit.SECONDS);
        // 超过核心直接执行任务，不用等待。 超过最大 拒绝，
        for (int i = 0; i < 100; i++) {
            final int taskNumber = i;
            poolExecutor.submit(() -> {
                try {
                    LOGGER.info("Task " + taskNumber + " executed by thread: " + Thread.currentThread().getName());
                    // 模拟任务执行时间
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            try {
                // 增加一点延迟来观察线程创建
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        LOGGER.info(Thread.currentThread().getName() + "@888");
        //获得一个随着jvm关闭而关闭的线程池，通过Runtime.getRuntime().addShutdownHook(hook)实现
        //修改ThreadFactory为创建守护线程，默认jvm关闭时最多等待120秒关闭线程池，重载方法可以设置时间
//        MoreExecutors.getExitingExecutorService(poolExecutor, 10, TimeUnit.SECONDS);
//        //只增加关闭线程池的钩子，不改变ThreadFactory
//        //在线程池中的线程是守护线程(daemon thread)时有用，用户线程(user thread)执行完后，jvm不会立即关闭，而是等待一定时间。
//        MoreExecutors.addDelayedShutdownHook(poolExecutor, 120, TimeUnit.SECONDS);
        LOGGER.info("main thread end.");
    }


}
