package java8.chap11.best_price_finder;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BestPriceFinder {

    private final static List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"));

    private final Executor executor = new ThreadPoolExecutor(
            4,
            20,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadFactoryBuilder().setNameFormat("retryClient-pool-%d").setDaemon(true).build(),
            new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 调用同步api查询各个商店的价格
     *
     * @param product
     * @return
     */
    public List<String> findPrice(String product) {

        //对这4个商店查询是顺序进行的 改用并行流能非常容易提升该程序的性能，不过在商店的数目增加时扩展不好
//        return shops./*stream()*/parallelStream()
//                .map(shop -> String.format("%s price is %.2f",
//                                            shop.getName(), shop.getPrice(product)))
//                .collect(Collectors.toList());
        //使用CompletableFuture发起异步请求 计算每种商品的价格
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f",
                                shop.getName(), shop.getPrice(product))
                        , executor)).collect(Collectors.toList());

        //等待所有异步操作结束
        //join方法和Future接口中的get有相同的含义，并且也声明在
        //Future接口中，它们唯一的不同是join不会抛出任何检测到的异常。使用它你不再需要使用
        //try/catch语句块让你传递给第二个map方法的Lambda表达式变得过于臃肿。
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

    }

    //discount市一中远程服务，模拟延迟

    /**
     * 第二个CompletableFuture对象的结果取决于第一个CompletableFuture，所以无论你使用哪个版
     * 本的方法来处理CompletableFuture对象，对于最终的结果，或者大致的时间而言都没有多少
     * 差别。我们选择thenCompose方法的原因是因为它更高效一些，因为少了很多线程切换的开销。
     * @param product
     * @return
     */
    public List<String> findPrices(String product) {

//        return shops.stream()
//                //取得每个shop对象中商品的原始价格
//                .map(shop -> shop.getPrice(product))
//                .map(Quote::parse)
//                //调用discount服务，获取折扣
//                .map(Discount::applyDiscount)
//                .collect(Collectors.toList());
        List<CompletableFuture<String>> priceFuture = shops.stream()
                //以异步方式取得每个shop中指定产品的原始价格
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor))
                //Quote对象存在时，对其返回的值进行转换
                .map(future -> future.thenApply(Quote::parse))
                //调用远程的Discount服务，获取折扣率
                //到目前为止，已经进行了2次异步操作。
                // thenCompose方法允许你对两个异步操作进行流水线，第一个操作完成时，将其结果作为参数传递给第二个操作。
                //使用这种方式，即使Future在向不同的商店收集报价，主线程还是能继续执行其他重要的操作，比如响应UI事件。
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() ->
                                Discount.applyDiscount(quote), executor)))
                .collect(Collectors.toList());

        //通常情况下你需要将两个完全不相干的CompletableFuture对象的结果整合起来，而且你也不希望等到第一个任务完全结束才开始第二项任务。该使用thenCombine方法
//        Future<Double> futurePriceInUSD =
//                CompletableFuture.supplyAsync(() -> shop.getPrice(product))
//                        .thenCombine(
//                                CompletableFuture.supplyAsync(
//                                        () -> exchangeService.getRate(Money.EUR, Money.USD)),
//                                (price, rate) -> price * rate
//                        );
        //等待流中的所有Future执行完毕，并提取各自的返回值
        return priceFuture.stream().map(CompletableFuture::join).collect(Collectors.toList());

    }

    public Stream<CompletableFuture<String>> findPricesStream(String product) {
        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPrice(product), executor))
                .map(future -> future.thenApply(Quote::parse))
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor)));
    }

    public static void main(String[] args) {
        BestPriceFinder bestPriceFinder = new BestPriceFinder();
        long start = System.nanoTime();
//        System.out.println(bestPriceFinder.findPrice("myPhone27s"));
//        System.out.println(bestPriceFinder.findPrices("myPhone27s"));

        //执行这段代码你会看到不同商店的价格不再像之前那样总是在一个时刻返回，而是随着商店折扣价格返回的顺序逐一地打印输出。
        //这就是响应事件
        CompletableFuture[] futures = bestPriceFinder.findPricesStream("myPhone")
                .map(f -> f.thenAccept(System.out::println))
                .toArray(size -> new CompletableFuture[size]);
        CompletableFuture.allOf(futures).join();
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("All shops have now responded in "
                + duration + " msecs");
    }

}
