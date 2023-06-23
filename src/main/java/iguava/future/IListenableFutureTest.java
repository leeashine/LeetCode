package iguava.future;

import com.google.common.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class IListenableFutureTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IListenableFutureTest.class);

    public static void main(String[] args) throws InterruptedException {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("async-pool-%d").build();
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
                5,
                20,
                0,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(3000),
                threadFactory);
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(poolExecutor);

        ListenableFuture<String> future = executorService.submit(() -> {
            try {
                Thread.sleep(3000);
                return "hello";
            } catch (Exception e) {
                LOGGER.error("查询失败");
            }
            return null;
        });

        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // 在任务成功完成时的处理逻辑
                System.out.println("Task result: " + result);
            }

            @Override
            public void onFailure(Throwable t) {
                // 在任务失败时的处理逻辑
                System.err.println("Task failed with exception: " + t.getMessage());
            }
        });

        LOGGER.info("*********************************");
        LOGGER.info("Main method end");
        Thread.sleep(3000);
    }

}
