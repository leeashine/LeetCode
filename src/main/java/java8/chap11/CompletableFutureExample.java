package java8.chap11;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * CompletableFuture 终止任务
 */
public class CompletableFutureExample {
    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        List<CompletableFuture<String>> futureList = new ArrayList<>();
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 500; i++) {
            int finalI = i;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                // 模拟任务执行
                try {
                    Thread.sleep(500); // 模拟任务耗时
                    // 模拟任务异常
                    if (finalI == 2) {
                        throw new RuntimeException("任务" + finalI + "发生异常");
                    }
                    System.out.println("任务" + finalI + "完成");
                    return "任务" + finalI + "完成";
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return null;
                }
            }, executor).exceptionally(e -> {
                // 处理异常
                e.printStackTrace();
                // 取消所有任务
                cancelAllTasks(futureList);
                return null;
            });

            futureList.add(future);
        }

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        try {
            allOf.join(); // 等待所有任务完成
        } catch (CompletionException e) {
            System.err.println("任务发生异常：" + e.getCause().getMessage());
        } finally {
            executor.shutdown(); // 关闭线程池
        }
    }

    private static void cancelAllTasks(List<CompletableFuture<String>> futureList) {
        for (CompletableFuture<String> future : futureList) {
            if (!future.isDone()) {
                future.cancel(true);
            }
        }
    }
}