package com.queue;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.List;

/**
 * {@link QueueRedis} 的 Jedis 生产实现（替代书中的 Spring Data Redis RedisTemplate）。
 *
 * <p>每次操作从 {@link JedisPool} 借连接，线程安全。</p>
 */
public class JedisQueueRedis implements QueueRedis {

    private final JedisPool jedisPool;

    public JedisQueueRedis(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public String rpoplpush(String source, String destination) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpoplpush(source, destination);
        }
    }

    @Override
    public Long lrem(String key, long count, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lrem(key, count, value);
        }
    }

    @Override
    public Long rpush(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpush(key, value);
        }
    }

    @Override
    public Long lpush(String key, String value) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpush(key, value);
        }
    }

    @Override
    public String lpop(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.lpop(key);
        }
    }

    @Override
    public Long llen(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.llen(key);
        }
    }

    @Override
    public Object eval(String script, List<String> keys) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.eval(script, keys.size(), keys.toArray(new String[0]));
        }
    }

    /** 便于直接以可变参数调用。 */
    public Object eval(String script, String... keys) {
        return eval(script, Arrays.asList(keys));
    }
}
