package java8.chap11.best_price_finder;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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
     * @param product
     * @return
     */
    public List<String> findPrice(String product){

        //对这4个商店查询是顺序进行的 改用并行流
//        return shops./*stream()*/parallelStream()
//                .map(shop -> String.format("%s price is %.2f",
//                                            shop.getName(), shop.getPrice(product)))
//                .collect(Collectors.toList());
        //使用CompletableFuture发起异步请求 计算每种商品的价格
        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f",
                                shop.getName(), shop.getPrice(product))
                ,executor)).collect(Collectors.toList());

        //等待所有异步操作结束
        //join方法和Future接口中的get有相同的含义，并且也声明在
        //Future接口中，它们唯一的不同是join不会抛出任何检测到的异常。使用它你不再需要使用
        //try/catch语句块让你传递给第二个map方法的Lambda表达式变得过于臃肿。
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

    }


    public static void main(String[] args) {
        BestPriceFinder bestPriceFinder = new BestPriceFinder();
        long start = System.nanoTime();
        System.out.println(bestPriceFinder.findPrice("myPhone27s"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration +" msecs");
    }

}
