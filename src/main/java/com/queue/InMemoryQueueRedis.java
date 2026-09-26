package com.queue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link QueueRedis} 的内存实现（本机/CI 无 Redis 时使用）。
 *
 * <p>List 语义与 Redis 完全一致（LPUSH/RPUSH/LPOP/RPOPLPUSH/LREM/LLEN）。
 * {@link #eval} 无法执行真正的 Lua，改为识别 {@link EventQueue} 中的三个脚本常量，
 * 按 Lua 脚本的<b>相同语义</b>用 Java 逐步执行 —— 测试断言因此与真实 Redis 行为一致。</p>
 *
 * <p>所有方法 synchronized，等效 Redis 的单线程命令执行模型（含脚本原子性）。</p>
 */
public class InMemoryQueueRedis implements QueueRedis {

    private final Map<String, Deque<String>> lists = new HashMap<>();

    private Deque<String> list(String key) {
        Deque<String> list = lists.get(key);
        if (list == null) {
            list = new ArrayDeque<>();
            lists.put(key, list);
        }
        return list;
    }

    @Override
    public synchronized String rpoplpush(String source, String destination) {
        String value = list(source).pollLast();
        if (value != null) {
            list(destination).addFirst(value);
        }
        return value;
    }

    @Override
    public synchronized Long lrem(String key, long count, String value) {
        Deque<String> list = list(key);
        long removed = 0;
        if (count == 0) { // 移除所有
            while (list.remove(value)) {
                removed++;
            }
        } else if (count > 0) { // 从头开始移除前 count 个
            java.util.Iterator<String> it = list.iterator();
            while (it.hasNext() && removed < count) {
                if (it.next().equals(value)) {
                    it.remove();
                    removed++;
                }
            }
        } else { // 从尾开始
            java.util.Iterator<String> it = list.descendingIterator();
            while (it.hasNext() && removed < -count) {
                if (it.next().equals(value)) {
                    it.remove();
                    removed++;
                }
            }
        }
        return removed;
    }

    @Override
    public synchronized Long rpush(String key, String value) {
        Deque<String> list = list(key);
        list.addLast(value);
        return (long) list.size();
    }

    @Override
    public synchronized Long lpush(String key, String value) {
        Deque<String> list = list(key);
        list.addFirst(value);
        return (long) list.size();
    }

    @Override
    public synchronized String lpop(String key) {
        return list(key).pollFirst();
    }

    @Override
    public synchronized Long llen(String key) {
        Deque<String> list = lists.get(key);
        return list == null ? 0L : (long) list.size();
    }

    @Override
    public synchronized Object eval(String script, List<String> keys) {
        if (EventQueue.ENQUEUE_TO_LEFT_REDIS_SCRIPT.equals(script)) {
            return evalEnqueueToLeft(keys);
        }
        if (EventQueue.ADD_TO_BACK_QUEUE_SCRIPT.equals(script)
                || EventQueue.ADD_TO_FAIL_QUEUE_SCRIPT.equals(script)) {
            // lrem KEYS[1] 0 KEYS[2]; rpush KEYS[3] KEYS[2]
            lrem(keys.get(0), 0, keys.get(1));
            rpush(keys.get(2), keys.get(1));
            return null;
        }
        throw new UnsupportedOperationException("InMemoryQueueRedis 只实现了 EventQueue 使用的脚本: " + script);
    }

    /**
     * 与书中 ENQUEUE_TO_LEFT_REDIS_SCRIPT 完全相同的语义：
     * 队列 &lt;10000 时先 lrem 排重；lpush 入队；maxBakSize&lt;=0 或本次为排重命中则不做镜像；
     * 备份队列超长则 lpop 最老的一条再 rpush（FIFO 裁剪）。
     */
    private Object evalEnqueueToLeft(List<String> keys) {
        String queueName = keys.get(0);
        String id = keys.get(1);
        String bakQueueName = keys.get(2);
        long maxBakSize = Long.parseLong(keys.get(3));

        long remCount = 0;
        if (llen(queueName) < 10000) {
            remCount = lrem(queueName, 1, id);
        }
        lpush(queueName, id);
        if (maxBakSize <= 0) {
            return null;
        }
        if (remCount > 0) {
            return null;
        }
        long len = llen(bakQueueName);
        if (len > maxBakSize) {
            lpop(bakQueueName);
        }
        rpush(bakQueueName, id);
        return null;
    }
}
