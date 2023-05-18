package iguava;


import com.google.common.base.Stopwatch;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class Limiter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Limiter.class);

    private static final int CAPACITY_SIZE = 1000;
    private static final int LIMIT_TIME = 1000;
    private static Cache<String, RateLimiter> cache = CacheBuilder.newBuilder()
            .initialCapacity(CAPACITY_SIZE).expireAfterAccess(1, TimeUnit.MINUTES)
            .build();

    /**
     * qps 5
     */
    private static double limit = 5;

    static {
        try {
            String limitTimeStr = System
                    .getProperty("limitTime", String.valueOf(limit));
            limit = Double.parseDouble(limitTimeStr);
            LOGGER.info("limitTime:{}", limit);
        } catch (Exception e) {
            LOGGER.error("init limitTime fail", e);
        }
    }

    public static boolean isLimit(String accessKeyID) {
        RateLimiter rateLimiter = null;
        try {
            rateLimiter = cache.get(accessKeyID, new Callable<RateLimiter>() {
                @Override
                public RateLimiter call() throws Exception {
                    /**
                     * 创建一个速率限制器（RateLimiter）的方法，可以指定一个稳定的吞吐量，以“每秒许可数”（通常称为QPS，即每秒查询数）的形式给出。
                     * 返回的速率限制器将平均每秒发出不超过 permitsPerSecond 个许可证，并且持续的请求被平滑地分布在每一秒钟内。
                     * 当传入请求速率超过 permitsPerSecond 时，速率限制器将每隔 (1.0 / permitsPerSecond) 秒释放一个许可证。
                     * 当速率限制器未使用时，允许最多 permitsPerSecond 个许可证的突发，随后的请求会以 permitsPerSecond 的稳定速率进行平滑限制。
                     * 简单来说，就是这个函数可以创建一个速率限制器对象，限制某个操作的频率，保证操作的稳定性和可靠性。
                     */
                    return RateLimiter.create(limit);
                }
            });
        } catch (ExecutionException e) {
            LOGGER.error("create limit fail", e);
        }
        if (rateLimiter != null && !rateLimiter.tryAcquire()) {
            LOGGER.error("access_key_id:{} limited", accessKeyID);
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long now = System.currentTimeMillis();
        Stopwatch stopwatch = Stopwatch.createStarted();
        ExecutorService executorService = Executors.newFixedThreadPool(20);
//        Limiter.isLimit("123");
//        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) {
            CompletableFuture.supplyAsync(() -> {
                boolean b = Limiter.isLimit("123");
                LOGGER.info("limit:{}", b);
                return b;
            }, executorService);
        }
//        for (int i = 0; i < 20; i++) {
//            Thread.sleep(1000);
//            boolean b = Limiter.isLimit("123");
//            LOGGER.info("limit:{}", b);
//        }

//        stopwatch.stop();
//        LOGGER.info("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
//        Thread.sleep(500);
//        stopwatch = Stopwatch.createStarted();
//        boolean b = limiter.isLimit("123");
//        stopwatch.stop();
//        LOGGER.info("limit:{}", b);
//        LOGGER.info("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
//        Thread.sleep(500);
//        stopwatch = Stopwatch.createStarted();
//        b = limiter.isLimit("123");
//        stopwatch.stop();
//        LOGGER.info("limit:{}", b);
//        LOGGER.info("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
//
//        b = limiter.isLimit("123");
//        LOGGER.info("*********,{}", b);

    }


}
