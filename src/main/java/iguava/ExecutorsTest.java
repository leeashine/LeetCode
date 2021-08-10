package iguava;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 上例中，如果没有调用MoreExecutors.addDelayedShutdownHook()方法的话，只会打印888，不会打印666。
 * 因为打印888的线程是用户线程，打印666的线程是守护线程(setDaemon(true))，用户线程一执行完，jvm就关闭了，所以不会有2000ms之后的666打印
 * 。假如创建ThreadFactory实例时，没有调用setDaemon(true)，即创建的线程是非守护线程，那么会先打印main@888，2000ms后会打印async-pool-0@666，因为main线程和async-pool-0线程都是用户线程。调用MoreExecutors.addDelayedShutdownHook()方法后，jvm会在用户线程结束后等待一段时间再关闭，这段之间守护线程可以工作，到了时间，jvm关闭，守护线程也完蛋了，事情干多少是多少，没干完就没干完。
 */
public class ExecutorsTest {

    public static void main(String[] args) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("async-pool-%d").build();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 20, 0, TimeUnit.MINUTES, new LinkedBlockingQueue<>(3000), threadFactory);
        ListeningExecutorService listeningExecutor = MoreExecutors.listeningDecorator(poolExecutor);
        listeningExecutor.submit(() -> {
            try {
                Thread.sleep(10000);
                System.out.println(Thread.currentThread().getName() + "@666");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(Thread.currentThread().getName() + "@888");
        //获得一个随着jvm关闭而关闭的线程池，通过Runtime.getRuntime().addShutdownHook(hook)实现
        //修改ThreadFactory为创建守护线程，默认jvm关闭时最多等待120秒关闭线程池，重载方法可以设置时间
        MoreExecutors.getExitingExecutorService(poolExecutor,10,TimeUnit.SECONDS);
        //只增加关闭线程池的钩子，不改变ThreadFactory
        //在线程池中的线程是守护线程(daemon thread)时有用，用户线程(user thread)执行完后，jvm不会立即关闭，而是等待一定时间。
        MoreExecutors.addDelayedShutdownHook(poolExecutor, 120, TimeUnit.SECONDS);
        System.out.println("main thread end.");
    }


}
