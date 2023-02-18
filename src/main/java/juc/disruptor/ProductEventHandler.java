package juc.disruptor;

import com.lmax.disruptor.EventHandler;

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
