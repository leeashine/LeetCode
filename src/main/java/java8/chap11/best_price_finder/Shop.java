package java8.chap11.best_price_finder;

import java.security.SecureRandom;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Shop {

    private String name;

    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //同步api
    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[new SecureRandom().nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price , code);
    }

    //单个调用远程服务 同步变异步api
    public Future<Double> getPriceAsync(String product) {
//        CompletableFuture<Double> futurePrice = new CompletableFuture<>();
//        new Thread(()->{
//            try {
//                //如果价格计算正常结束，完成Future操作 并设置商品价格
//                double price = calculatePrice(product);
//                futurePrice.complete(price);
//            }catch (Exception e){
//                //否则就抛出导致失败的异常，完成这次Future操作
//                futurePrice.completeExceptionally(e);
//            }
//
//        }).start();
//        return futurePrice;
        //和上面的完全等价
        //它提供了同样的错误管理机制
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
    }

    private double calculatePrice(String product) {
        ranDomDelay();
        return new SecureRandom().nextDouble() * product.charAt(0) + product.charAt(1);
    }

    //模拟一秒钟延迟
    public static void delay() {

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    public static void ranDomDelay(){

        int delay = 500 + new SecureRandom().nextInt(2000);
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
