package com.task.handler;

import com.queue.EventHandler;
import com.queue.EventQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 书中示例处理器（p311）：key 为发生变更的 skuId，
 * “将 skuId 最新变更更新到线上异构数据集群”。
 *
 * <p>此处用内存 Map 模拟线上异构数据集群（skuId → 最后同步时间戳）。</p>
 */
public class ProductEventHandler implements EventHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductEventHandler.class);

    /** 模拟线上异构数据集群。 */
    private final Map<String, Long> onlineCluster = new ConcurrentHashMap<>();

    @Override
    public void onEvent(String key, String eventType, EventQueue queue) {
        try {
            doBusiness(key);
            queue.success(key);
        } catch (Exception e) {
            log.error("处理商品变更事件失败, skuId={}", key, e);
            queue.fail(key);
        }
    }

    /** 真实系统：调用搜索/缓存/页面静态化等异构集群的更新接口。 */
    private void doBusiness(String skuId) {
        onlineCluster.put(skuId, System.currentTimeMillis());
        log.info("skuId={} 最新变更已同步到线上异构数据集群", skuId);
    }

    public Long lastSyncTimeOf(String skuId) {
        return onlineCluster.get(skuId);
    }
}
