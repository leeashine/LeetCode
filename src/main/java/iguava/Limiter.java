package iguava;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.function.Function;

public class Limiter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Limiter.class);

    private static final int CAPACITY_SIZE = 1000;

    private static final long LIMIT_TIME = 1000;
    /**
     * qps
     */
    private static final double limit = 5;

    private static final Cache<String, RateLimiter> CACHE = Caffeine.newBuilder().initialCapacity(CAPACITY_SIZE)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build();

    public boolean isLimit(String accessKeyID) {
        RateLimiter limiter = CACHE.get(accessKeyID, new Function<String, RateLimiter>() {
            @Override
            public RateLimiter apply(String s) {
                return RateLimiter.create(limit);
            }
        });
        // tryAcquire不等待立即返回
        if (limiter != null && !limiter.tryAcquire()) {
            LOGGER.error("access_key_id:{} limited", accessKeyID);
            LOGGER.info("access_key_id:" + accessKeyID + " limited");
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long now = System.currentTimeMillis();
        Stopwatch stopwatch = Stopwatch.createStarted();
        Limiter limiter = new Limiter();
        for (int i = 0; i < 20; i++) {

            boolean b = limiter.isLimit("123");
            LOGGER.info("limit:{}", b);
        }

        stopwatch.stop();
        LOGGER.info("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        Thread.sleep(500);
        stopwatch = Stopwatch.createStarted();
        boolean b = limiter.isLimit("123");
        stopwatch.stop();
        LOGGER.info("limit:{}", b);
        LOGGER.info("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));
        Thread.sleep(500);
        stopwatch = Stopwatch.createStarted();
        b = limiter.isLimit("123");
        stopwatch.stop();
        LOGGER.info("limit:{}", b);
        LOGGER.info("cost:" + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        b = limiter.isLimit("123");
        LOGGER.info("*********,{}", b);

    }


}
