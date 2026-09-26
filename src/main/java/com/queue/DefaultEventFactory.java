package com.queue;

import com.lmax.disruptor.EventFactory;

/**
 * {@link Event} 的预分配工厂（书中 DefaultEventFactory，p303）。
 */
public class DefaultEventFactory implements EventFactory<Event> {

    @Override
    public Event newInstance() {
        return new Event();
    }
}
