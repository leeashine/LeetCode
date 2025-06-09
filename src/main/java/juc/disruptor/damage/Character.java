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

    public void applyDamage(int damage) {
        if (damage <= 0) {
            return;
        }
        health = Math.max(health - damage, 0);
        // 可以在这里添加生命值变动的通知逻辑
        System.out.println("health:" + health + ", damage:" + damage);
    }

    public synchronized int getHealth() {
        return health;
    }
}