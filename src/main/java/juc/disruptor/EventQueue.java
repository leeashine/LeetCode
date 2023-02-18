package juc.disruptor;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class EventQueue {

    private String queueName;
    private String nextKey;
    private LinkedBlockingDeque queue = new LinkedBlockingDeque();

    public String next() throws Exception {
        while (true) {
            //暂停Queue消费

        }
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public LinkedBlockingDeque getQueue() {
        return queue;
    }

    public void setQueue(LinkedBlockingDeque queue) {
        this.queue = queue;
    }

    public String getNextKey() {
        return nextKey;
    }

    public void setNextKey(String nextKey) {
        this.nextKey = nextKey;
    }

    public void success(String skuId) {

    }

    public void fail(String skuId) {

    }
}
