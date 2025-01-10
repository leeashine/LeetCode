package juc;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ThreadPoolTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolTest.class);


    public static void main(String[] args) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("async-pool-%d").build();
//        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
//                5,
//                20,
//                2,
//                TimeUnit.SECONDS,
//                new LinkedBlockingQueue<>(3000),
//                threadFactory);
//
//        for (int i =0;i<10;i++){
//            poolExecutor.submit(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        System.out.println(Thread.currentThread().getName()+"执行任务");
//                        Thread.sleep(2000);
//                        System.out.println(Thread.currentThread().getName()+"执行完成");
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
//        }

        // 测试存活时间超过会如何？ 结论会正常执行 只不过会需要频繁创建销毁线程
        ThreadPoolExecutor poolExecutor2 = new ThreadPoolExecutor(
                5,
                1000,
                200,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                threadFactory);
        for (int i = 0; i < 30; i++) {
            poolExecutor2.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        LOGGER.info(Thread.currentThread().getName()+"执行任务");
                        Thread.sleep(2000);
                        LOGGER.info(Thread.currentThread().getName()+"执行完成");
                    } catch (InterruptedException e) {
                        LOGGER.error("error ", e);
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

//        poolExecutor2.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    System.out.println(Thread.currentThread().getName()+"执行任务2222");
//                    Thread.sleep(3000);
//                    System.out.println(Thread.currentThread().getName()+"执行完成2222");
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//
//        poolExecutor2.submit(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    System.out.println(Thread.currentThread().getName()+"执行任务");
//                    Thread.sleep(2000);
//                    System.out.println(Thread.currentThread().getName()+"执行完成");
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });



    }
}
