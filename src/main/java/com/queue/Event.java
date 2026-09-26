package com.queue;

/**
 * 事件对象（《亿级流量网站架构核心技术》§15.9，p303）。
 *
 * <p>Disruptor RingBuffer 中预分配、反复复用的可变 holder：
 * key = 任务 id（如变更的 skuId），eventType = 事件类型（本实现中等于队列名）。</p>
 */
public class Event {

    private String key;
    private String eventType;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "Event{key='" + key + "', eventType='" + eventType + "'}";
    }
}
