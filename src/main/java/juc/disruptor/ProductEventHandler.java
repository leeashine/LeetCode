package juc.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * key就是有变更的skuId,我们根据skuId拉取最新的变更内容，然后更新到线上异构集群，如果成功，则从本地任务队列删除任务。
 * 如果失败，则将重试或者推送到失败队列。
 */
public class ProductEventHandler implements EventHandler {
    public void onEvent(String skuId, String eventType, EventQueue queue) {
        try {
            queue.success(skuId);
        } catch (Exception e) {
            queue.fail(skuId);
        }
    }

    @Override
    public void onEvent(Object event, long sequence, boolean endOfBatch) throws Exception {

    }
}
