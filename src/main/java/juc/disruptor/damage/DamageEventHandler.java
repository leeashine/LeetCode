package juc.disruptor.damage;

import com.lmax.disruptor.EventHandler;

/**
 * 编写事件处理器
 */
public class DamageEventHandler implements EventHandler<DamageEvent> {
    private final Character character;

    public DamageEventHandler(Character character) {
        this.character = character;
    }

    @Override
    public void onEvent(DamageEvent event, long sequence, boolean endOfBatch) {
        character.applyDamage(event.getDamage());
    }
}