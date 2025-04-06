package juc.disruptor.damage;

import com.lmax.disruptor.EventFactory;

/**
 * 创建事件工厂
 */
public class DamageEventFactory implements EventFactory<DamageEvent> {
    @Override
    public DamageEvent newInstance() {
        return new DamageEvent();
    }
}