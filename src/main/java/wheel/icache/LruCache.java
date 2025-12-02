package wheel.icache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 阶段 2：加上“容量限制 + LRU 淘汰”
 * * 需求：
 * * 指定一个 maxSize
 * * 当 put 时，如果超过容量，需要自动淘汰最久未使用的数据（LRU）
 * * 最方便的办法是：利用 LinkedHashMap 的一个隐藏技能：
 * * 重写 removeEldestEntry。
 */
public class LruCache<K,V> implements ICache<K,V> {

    private final int maxSize;
    private final LinkedHashMap<K, V> map;

    public LruCache(int maxSize) {
        this.maxSize = maxSize;
        // accessOrder = true 代表按访问顺序（LRU）
        this.map = new LinkedHashMap<K,V>(16, 0.75f, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
    }

    @Override
    public synchronized V get(K key) {
        return map.get(key);
    }

    @Override
    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public synchronized void remove(K key) {
        map.remove(key);
    }

    @Override
    public synchronized void clear() {
        map.clear();
    }

    @Override
    public synchronized int size() {
        return map.size();
    }

    public static void main(String[] args) {
        ICache<Integer, String> cache = new LruCache<>(3);

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        // 当前缓存：[1:A, 2:B, 3:C]  （最近最少使用是 1）

        cache.get(1);   // 访问一下 1，使得 1 变成最近使用
        cache.put(4, "D"); // 放入 4，会淘汰“最久未使用”的 key 2

        // 验证
        System.out.println(cache.get(1)); // A  (还在)
        System.out.println(cache.get(2)); // null (被淘汰了)
        System.out.println(cache.get(3)); // C
        System.out.println(cache.get(4)); // D
    }
}
