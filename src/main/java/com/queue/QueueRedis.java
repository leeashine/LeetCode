package com.queue;

import java.util.List;

/**
 * EventQueue 依赖的最小 Redis list 操作抽象。
 *
 * <p>书中使用 Spring Data Redis 的 RedisTemplate（opsForList() / execute(RedisScript)）；
 * 本实现有两个适配：</p>
 * <ul>
 *   <li>{@link JedisQueueRedis} —— 生产实现，委托 Jedis（pom 中已有 jedis 3.0.1），
 *       Lua 脚本走 {@code eval}（书中把值也放在 KEYS 里传，保持一致）。</li>
 *   <li>{@link InMemoryQueueRedis} —— 无 Redis 环境下的等价实现，
 *       对书中的三个 Lua 脚本按相同语义用 Java 执行，保证端到端测试确定性。</li>
 * </ul>
 */
public interface QueueRedis {

    /** RPOPLPUSH source destination：弹出 source 尾部元素并压入 destination 头部。 */
    String rpoplpush(String source, String destination);

    /** LREM key count value。 */
    Long lrem(String key, long count, String value);

    /** RPUSH key value。 */
    Long rpush(String key, String value);

    /** LPUSH key value。 */
    Long lpush(String key, String value);

    /** LPOP key。 */
    String lpop(String key);

    /** LLEN key。 */
    Long llen(String key);

    /**
     * EVAL script —— 与书中一致，所有参数都通过 KEYS 传入（无 ARGV）。
     *
     * @param script Lua 脚本原文
     * @param keys   KEYS[1..n]
     */
    Object eval(String script, List<String> keys);
}
