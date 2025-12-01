package juc.disruptor.damage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定义角色类
 */
public class Character {
    private static final Logger LOGGER = LoggerFactory.getLogger(Character.class);
    private final int maxHealth;
    private int health;

    public Character(int maxHealth) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    // 扣血逻辑 这里其实可以无需加锁volatile atoInteger都是个不错的选择！
    public void applyDamage(int damage) {
        if (damage <= 0) {
            return;
        }
        health = Math.max(health - damage, 0);
        // 可以在这里添加生命值变动的通知逻辑
        LOGGER.info("Apply damage health {}.", health);
    }

    public int getHealth() {
        return health;
    }
}