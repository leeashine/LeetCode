package iguava;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class Limiter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Limiter.class);

    private static final int CAPACITY_SIZE = 1000;

    private long LIMIT_TIME = 1000;
    /**
     * qps
     */
    private static final double limit = 5;

    private Cache<String, RateLimiter> CACHE = CacheBuilder.newBuilder().initialCapacity(CAPACITY_SIZE)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build();

    public boolean isLimit(String accessKeyID) {
        RateLimiter limiter = null;
        try {
            limiter = CACHE.get(accessKeyID, new Callable<RateLimiter>() {
                @Override
                public RateLimiter call() throws Exception {
                    return RateLimiter.create(limit);
                }
            });

        } catch (ExecutionException e) {
            LOGGER.error("create limit fail", e);
        }
        if (limiter != null && !limiter.tryAcquire(LIMIT_TIME, TimeUnit.MILLISECONDS)) {
            LOGGER.error("access_key_id:{} limited", accessKeyID);
            return true;
        }
        return false;
    }


}
