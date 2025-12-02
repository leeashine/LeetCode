package wheel.icache;

import java.util.HashMap;
import java.util.Map;

/**
 * 阶段 1：写一个最简单的缓存（基于 HashMap）
 * 先不考虑：
 * 淘汰策略
 * 过期时间
 * 并发问题
 * 就是一个“包装了 HashMap 的工具类”。
 *
 * 阶段 2：加上“容量限制 + LRU 淘汰”
 * 需求：
 * 指定一个 maxSize
 * 当 put 时，如果超过容量，需要自动淘汰最久未使用的数据（LRU）
 * 最方便的办法是：利用 LinkedHashMap 的一个隐藏技能：
 * 重写 removeEldestEntry。
 * @param <K>
 * @param <V>
 */
public class SimpleCache<K,V> implements  ICache<K,V> {

    private final Map<K, V> map = new HashMap<>();

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    public static void main(String[] args) {
        ICache<String, String> cache = new SimpleCache<>();

        cache.put("name", "Tom");
        cache.put("city", "Shanghai");

        System.out.println(cache.get("name"));   // Tom
        System.out.println(cache.size());        // 2

        cache.remove("city");
        System.out.println(cache.get("city"));   // null
        System.out.println(cache.size());        // 1

        cache.clear();
        System.out.println(cache.size());        // 0
    }
}
