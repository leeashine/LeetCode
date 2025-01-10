package juc.disruptor.damage;

/**
 * 定义角色类
 */
public class Character {
    private final int maxHealth;
    private int health;

    public Character(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    // 扣血逻辑 这里其实可以无需加锁volatile atoInteger都是个不错的选择！
    public synchronized void applyDamage(int damage) {
        if (damage <= 0) {
            return;
        }
        health = Math.max(health - damage, 0);
        // 可以在这里添加生命值变动的通知逻辑
    }

    public synchronized int getHealth() {
        return health;
    }
}