package juc.disruptor;

import com.lmax.disruptor.EventFactory;

public class DefaultEventFactory implements EventFactory<Event> {
    @Override
    public Event newInstance() {
        return new Event();
    }

}
