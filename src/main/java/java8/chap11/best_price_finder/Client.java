package java8.chap11.best_price_finder;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 如何正确地管理
 * 异步任务执行过程中可能出现的错误?
 */
public class Client {

    public static void main(String[] args) {

        Shop shop = new Shop("best shop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favourite product");

        long invocationTime = (System.nanoTime() - start ) / 1_000_000;
        System.out.println("Invocation returned after " + invocationTime + " msecs");

        //执行更多的任务，比如查询其他商店
        doSomethingElse();

        //在计算价格的同时
        try {
            //有可能会被阻塞 所以加上时间的重载版本 如果最终被阻塞，无法得到结果，那么在最多等待一秒后退出
            //不过，也因为如此，你不会有机会发现计算商品价格的线程内到底发生了什么问题
            //才引发了这样的失效。为了让客户端能了解商店无法提供请求商品价格的原因，你需要使用
            //CompletableFuture的completeExceptionally方法将导致CompletableFuture内发生问
            //题的异常抛出。
            Double price = futurePrice.get(5, TimeUnit.SECONDS);
            System.out.printf("Price is %.2f%n ", price);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
//        CompletableFuture futurePrice1 = (CompletableFuture) futurePrice;
//        ((CompletableFuture<Double>) futurePrice).thenAccept(price->{
//            System.out.printf("Price is %.2f%n ", price);
//        });
//        ((CompletableFuture<Double>) futurePrice).join();
        long retrievalTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Price returned after " + retrievalTime + " msecs");

    }

    private static void doSomethingElse() {
        System.out.println("do something else......");
    }

}
