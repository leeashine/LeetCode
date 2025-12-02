package wheel.icache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 阶段3：给缓存加上“过期时间 TTL”
 * put(key, value, ttlMillis)：数据在 ttlMillis 毫秒后失效
 * get 时如果已过期：
 * 不再返回值
 * 顺便删掉这个 key（懒删除）
 * @param <K>
 * @param <V>
 */
public class TtlCache<K,V> implements ICache<K, V> {

    private final Map<K,CacheValue<V>> map = new HashMap<K,CacheValue<V>>();

    @Override
    public synchronized V get(K key) {
        CacheValue<V> cv = map.get(key);
        if (cv == null) {
            return null;
        }
        if (cv.isExpired()) {
            map.remove(key);
            return null;
        }
        return cv.value;
    }

    /**
     * 不带过期时间：永不过期
     * @param key
     * @param value
     */
    @Override
    public synchronized void put(K key, V value) {
        put(key, value, 0);
    }

    /**
     * 自己扩展的：带 TTL
     */
    public synchronized void put(K key, V value, long ttlMillis) {
        long expireAt = 0;
        if (ttlMillis > 0) {
            expireAt = System.currentTimeMillis() + ttlMillis;
        }
        map.put(key, new CacheValue<>(value, expireAt));
    }

    @Override
    public synchronized void remove(K key) {
        CacheValue<V> cv = map.remove(key);
    }

    @Override
    public synchronized void clear() {
        map.clear();
    }

    @Override
    public int size() {
        // 顺便做一次“过期清理”
        cleanupExpired();
        return map.size();
    }

    private void cleanupExpired() {
        Iterator<Map.Entry<K, CacheValue<V>>> it = map.entrySet().iterator();
        long now = System.currentTimeMillis();
        while (it.hasNext()) {
            Map.Entry<K, CacheValue<V>> e = it.next();
            CacheValue<V> cv = e.getValue();
            if (cv.expireTime > 0 && now > cv.expireTime) {
                it.remove();
            }
        }
    }


    class CacheValue<V> {
        final V value;

        /**
         * 过期时间(毫秒)
         */
        final long expireTime;

        public CacheValue(V value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        boolean isExpired() {
            return expireTime > 0 && System.currentTimeMillis() > expireTime;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TtlCache<String, String> cache = new TtlCache<>();

        cache.put("k1", "hello", 1000); // 1 秒过期
        System.out.println(cache.get("k1")); // 立刻拿：hello

        Thread.sleep(1500);

        System.out.println(cache.get("k1")); // 已过期：null
        System.out.println(cache.size());    // 0 （顺带清理）
    }

}
