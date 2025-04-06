package juc.disruptor.damage;

/**
 * 定义扣血事件
 */
public class DamageEvent {
    private int damage;

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }
}