package java8.chap11;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import work.domain.FcInvoiceDO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 使用Future中提供的方法完成这样
 * 的操作又是另外一回事。这也是我们需要更具描述能力的特性的原因，比如下面这些。
 *  将两个异步计算合并为一个——这两个异步计算之间相互独立，同时第二个又依赖于第
 * 一个的结果。
 *  等待Future集合中的所有任务都完成。
 *  仅等待Future集合中最快结束的任务完成（有可能因为它们试图通过不同的方式计算同
 * 一个值），并返回它的结果。
 *  通过编程方式完成一个Future任务的执行（即以手工设定异步操作结果的方式）。
 *  应对Future的完成事件（即当Future的完成事件发生时会收到通知，并能使用Future
 * 计算的结果进行下一步的操作，不只是简单地阻塞等待操作的结果）。
 */
public class FutureDemo {

    public static void main(String[] args) {

        ExecutorService executor = new ThreadPoolExecutor(
                10,
                10,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryBuilder().setNameFormat("retryClient-pool-%d").setDaemon(true).build(),
                new ThreadPoolExecutor.DiscardPolicy());

//        test01(executor);
        compFutureTest(executor);


    }

    /**
     * 多次调用RPC接口可以考虑用future
     * 之前是调用一次接口耗时1秒，如果调用10此就是10秒
     * 而现在用CompletableFuture可以10次请求并行调用返回结果，只要1秒
     * 注意：(1)返回的结果是无序的 (2)合理的设置线程池大小，如果线程池满了的化请求还是会"同步"执行
     */
    private static void compFutureTest(ExecutorService executor) {

        long start = System.currentTimeMillis();
        //多线程环境下要注意线程安全
        List<FcInvoiceDO> list = new ArrayList<>();
        List<CompletableFuture<List<FcInvoiceDO>>> futures = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            CompletableFuture<List<FcInvoiceDO>> future = CompletableFuture.supplyAsync(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("调用第三方接口获取结果。。。");
                        FcInvoiceDO invoiceDO = new FcInvoiceDO();
                        invoiceDO.setId((long) finalI);
                        invoiceDO.setName("" + finalI);
                        List<FcInvoiceDO> rpcResult = Lists.newArrayList(invoiceDO);
                        return rpcResult;
                    }, executor)
                    .exceptionally(e -> {
                        e.printStackTrace();
                        return new ArrayList<>();
                    });
            futures.add(future);
        }
        //等待所有结果返回
        for (CompletableFuture<List<FcInvoiceDO>> future : futures) {
            List<FcInvoiceDO> fcInvoiceDOS = null;
            //join()抛出未经检查的异常 get()会抛出经检查的异常，可被捕获，自定义处理或者直接抛出。
            fcInvoiceDOS = future.join();
            list.addAll(fcInvoiceDOS);
        }

        System.out.println("总共耗时：" + (System.currentTimeMillis() - start));
        System.out.println(JSONObject.toJSONString(list));
        System.out.println(list.size());

    }

    private static void test01(ExecutorService executor) {
        //以异步的方式在新的线程中执行耗时的操作
        Future<Double> future = executor.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return doSomeLongComutation();
            }

            private Double doSomeLongComutation() {
                return 0.0;
            }

        });
        //异步操作进行的同时，你可以做其他的事情
        doSomethingElse();

        try {
            //可以调用它的get方法去获取操作的结果。
            // 如果操作已经完成，该方法会立刻返回操作的结果，否则它会阻塞你的线程，直到操作完成，返回相应的结果。
            //获取异步操作的结果，如果最终被阻塞，无法得到结果，那么在最多等待一秒后退出
            Double result = future.get(1, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            //计算抛出一个异常
        } catch (InterruptedException e) {
            //当前线程在等待过程中被中断
        } catch (TimeoutException e) {
            //在Future对象完成之前超过已过期
        }
    }

    private static void doSomethingElse() {
    }

}
