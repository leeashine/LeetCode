package iguava;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * guava cache
 * 基于ConcurrentMap
 * ConcurrentMap保存所有添加的元素，除非显示删除之（比如调用remove方法）。而本地缓存一般会配置自动剔除策略，为了保护应用程序，限制内存占用情况，防止内存溢出。
 * 它不会把数据存储在文件或外部的服务器上(和ehcache redis分布式缓存区别)
 * <p>
 * Caffeine是基于JAVA 1.8 Version的高性能缓存库。(spring5 boot2.0开始用caffeine)
 * Caffeine提供的内存缓存使用参考Google guava的API。Caffeine是基于Google Guava Cache设计经验上改进的成果。
 * <p>
 * GuavaCache和Caffeine差异
 * 1.剔除算法方面，GuavaCache采用的是「LRU」算法，而Caffeine采用的是「Window TinyLFU」算法，这是两者之间最大，也是根本的区别。
 * <p>
 * 2.立即失效方面，Guava会把立即失效 (例如：expireAfterAccess(0) and expireAfterWrite(0)) 转成设置最大Size为0。这就会导致剔除提醒的原因是SIZE而不是EXPIRED。Caffiene能正确识别这种剔除原因。
 * <p>
 * 3.取代提醒方面，Guava只要数据被替换，不管什么原因，都会触发剔除监听器。而Caffiene在取代值和先前值的引用完全一样时不会触发监听器。
 * <p>
 * 4.异步化方方面，Caffiene的很多工作都是交给线程池去做的（默认：ForkJoinPool.commonPool()），例如：剔除监听器，刷新机制，维护工作等。
 */
public class CacheTest {


    private LoadingCache<Long, String> batchQuerySpuAvgUvLast7Cache;
    private Cache<Long, Object> cache;

    {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .maximumSize(10)//最大条数
                .build(new com.github.benmanes.caffeine.cache.CacheLoader<Long, Object>() {
                    @Override
                    public @Nullable Object load(@NonNull Long key) throws Exception {
                        return null;
                    }
                });//定义cache

        batchQuerySpuAvgUvLast7Cache = CacheBuilder.newBuilder()
                // 自条目被创建或最近替换了值之后，过了指定时间后，把这些条目过期。如果缓存中的数据在一段时间之后变的陈旧不可用的话，那么使用这个策略将是十分可取的
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(10000)
                .build(new CacheLoader<Long, String>() {
                    @Override
                    public String load(Long aLong) throws Exception {
                        System.out.println("load data " + aLong + " to cache.......");
//                        queryFromDatabase()
                        return new Date().toString();
                    }
                });
    }

    public Map<Long, String> doSomething(Long spuId, Date statDate) {
//        String key = StringUtils.join(new Object[] {statDate.toString(), spuId}, '_');
        Map<Long, String> result = new HashMap<>();
        try {
            System.out.println("get from cache, spuId:" + spuId);
//            get(K k): 内部调用getOrLoad(K key)方法，缓存中有对应的值则返回，没有则使用CacheLoader load方法
//            getOrLoad(K key)方法为线程安全方法，内部加锁
            String uv = batchQuerySpuAvgUvLast7Cache.get(spuId);
            result.put(spuId, uv);
        } catch (ExecutionException e) {
            System.out.println("cache " + spuId + " is not exist!try query From DataBase");
            String uv = queryFromDatabase(spuId, statDate);
            result.put(spuId, uv);
        }
        return result;
    }

    private String queryFromDatabase(Long spuId, Date statDate) {
        System.out.println("get from database");
        return statDate.toString();
    }

    public static void main(String[] args) throws InterruptedException {

        CacheTest test = new CacheTest();
        for (int i = 0; i < 10; i++) {
            test.doSomething((long) i, new Date());
        }

        test.doSomething((long) 7, new Date());

//        Thread.sleep(60 * 1000L + 1);
//        System.out.println("********* one minute later *************");
//        test.doSomething((long) 7, new Date());

        long id=10001;
        test.cache.get(id, value->query(id));



    }

    private static Object query(long id) {

        return null;
    }


}
