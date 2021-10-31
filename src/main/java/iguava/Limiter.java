package iguava;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
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
        if (limiter != null && !limiter.tryAcquire(LIMIT_TIME, TimeUnit.MILLISECONDS)) {
            LOGGER.error("access_key_id:{} limited", accessKeyID);
            return true;
        }
        return false;
    }


}
