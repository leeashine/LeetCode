package java8.chap11;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java8.chap11.best_price_finder.Shop;
import work.domain.FcInvoiceDO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

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
public class CompletableFutureAndFutureDemo {

    static ExecutorService executor = new ThreadPoolExecutor(
            200,
            400,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("search-pool-%d").setDaemon(true).build(),
            new ThreadPoolExecutor.DiscardPolicy());

    public static void main(String[] args) {

        ExecutorService executor = new ThreadPoolExecutor(
                20,
                30,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2000),
                new ThreadFactoryBuilder().setNameFormat("retryClient-pool-%d").setDaemon(true).build(),
                new ThreadPoolExecutor.DiscardPolicy());

//        test01(executor);
//        test02(executor);
//        compFutureTest(executor);
        List<Future> futureList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
//                test03(40);
//                test04(40);
                test05(40);
                return null;
            }, executor).exceptionally(e -> {
                e.printStackTrace();
                return null;
            });
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

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
        for (int i = 0; i < 1; i++) {
            int finalI = i;
            CompletableFuture<List<FcInvoiceDO>> future = CompletableFuture.supplyAsync(() -> {
                        try {
                            Thread.sleep(1100);
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
            CompletableFuture<List<FcInvoiceDO>> future2 = CompletableFuture.supplyAsync(() -> {
                        try {
                            Thread.sleep(1200);
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
            futures.add(future2);
        }
        //等待所有结果返回
//        这样可能会导致效率较低，如果某个 CompletableFuture 比其他 CompletableFuture 更慢或失败，则其他的操作将被阻塞等待。
//        for (CompletableFuture<List<FcInvoiceDO>> future : futures) {
//            List<FcInvoiceDO> fcInvoiceDOS = null;
//            //join()抛出未经检查的异常 get()会抛出经检查的异常，可被捕获，自定义处理或者直接抛出。
//            fcInvoiceDOS = future.join();
//            list.addAll(fcInvoiceDOS);
//        }
//        我们将一个空的String类型数组作为参数传递给toArray()方法：new String[0]。由于该数组的长度为0，因此即使集合为空，也不会浪费任何内存。
//        但是，当集合非空时，toArray()方法将创建一个新的String类型数组，并将集合中的所有元素复制到该新数组中。这种做法比直接传递一个和集合大小相同的数组更有效率，因为它允许toArray()方法根据集合的大小动态分配所需的内存空间。
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        // 汇总结果
        for (CompletableFuture<List<FcInvoiceDO>> future : futures) {
            List<FcInvoiceDO> result = future.join();
            list.addAll(result);
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

            private Double doSomeLongComutation() throws InterruptedException {
                Thread.sleep(1100);
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

    private static void test02(ExecutorService executor) {
        long start = System.currentTimeMillis();
        //以异步的方式在新的线程中执行耗时的操作
        List<Future<Double>> futureList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Future<Double> future = executor.submit(new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    return doSomeLongComutation();
                }

                private Double doSomeLongComutation() throws InterruptedException {
                    Thread.sleep(1100);
                    return 0.0;
                }

            });
            Future<Double> future2 = executor.submit(new Callable<Double>() {
                @Override
                public Double call() throws Exception {
                    return doSomeLongComutation();
                }

                private Double doSomeLongComutation() throws InterruptedException {
                    Thread.sleep(1500);
                    return 1.0;
                }

            });
            futureList.add(future);
            futureList.add(future2);
        }
        //异步操作进行的同时，你可以做其他的事情
        doSomethingElse();

        try {
            //可以调用它的get方法去获取操作的结果。
            // 如果操作已经完成，该方法会立刻返回操作的结果，否则它会阻塞你的线程，直到操作完成，返回相应的结果。
            //获取异步操作的结果，如果最终被阻塞，无法得到结果，那么在最多等待一秒后退出
            List<Double> result = new ArrayList<>();
            for (Future<Double> doubleFuture : futureList) {
                Double aDouble = doubleFuture.get();
                result.add(aDouble);
            }
            System.out.println("总共耗时：" + (System.currentTimeMillis() - start));
            System.out.println(result);
        } catch (ExecutionException e) {
            //计算抛出一个异常
        } catch (InterruptedException e) {
            //当前线程在等待过程中被中断
        }
    }

    /**
     * 此处代码有几点可以优化，请注意
     * 优化点1：线程安全的集合可以考虑ConcurrentLinkedDeque
     * ConcurrentLinkedDeque除了实现Queue接口外，还实现了Deque(双端队列)接口，因此它具有Deque的所有特性，如push、pop、peekFirst和peekLast等操作。而ConcurrentLinkedQueue只实现了Queue接口。
     * 最后，ConcurrentLinkedDeque比ConcurrentLinkedQueue更加灵活，因为它可以用作栈或队列，而ConcurrentLinkedQueue只能用作队列。
     * 优化点2：可以等待future结果然后汇总，没必要用ConcurrentLinkedDeque
     */
    public static void test03(int loopCnt) {
        long start = System.currentTimeMillis();
        final List<Shop> shopList = Collections.synchronizedList(new ArrayList<>(1 << 5));
        final CountDownLatch latch = new CountDownLatch(loopCnt);
        Random random = new Random();
        for (int i = 0; i < loopCnt; i++) {
            executor.execute(() -> {
                try {
                    Shop shop = new Shop("jack");
                    Thread.sleep(random.nextInt(2000) + 500L);
//                    System.out.println("调用接口获取结果");
                    shopList.add(shop);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        System.out.println("总共耗时：" + (System.currentTimeMillis() - start));

    }

    /**
     * 针对test3优化点1优化过后的代码
     */
    public static void test04(int loopCnt) {
        long start = System.currentTimeMillis();
        ConcurrentLinkedDeque<Shop> concurrentLinkedDeque = new ConcurrentLinkedDeque();
        final CountDownLatch latch = new CountDownLatch(loopCnt);
        Random random = new Random();
        for (int i = 0; i < loopCnt; i++) {
            executor.execute(() -> {
                try {
                    Shop shop = new Shop("jack");
                    Thread.sleep(random.nextInt(2000) + 500L);
//                    System.out.println("调用接口获取结果");
                    concurrentLinkedDeque.add(shop);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        System.out.println("总共耗时：" + (System.currentTimeMillis() - start));

    }

    /**
     * 针对test3终极优化
     */
    public static void test05(int loopCnt) {
        long start = System.currentTimeMillis();
        List<CompletableFuture<Shop>> futureList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < loopCnt; i++) {
            CompletableFuture<Shop> future = CompletableFuture.supplyAsync(() -> {
                Shop shop = null;
                try {
                    Thread.sleep(random.nextInt(2000) + 500L);
                    shop = new Shop("jack");
//                    System.out.println("调用接口获取结果");
                    return shop;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                }
                return shop;
            }, executor);
            futureList.add(future);
        }

        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

        System.out.println("总共耗时：" + (System.currentTimeMillis() - start));

    }

    private static void doSomethingElse() {
    }

}
