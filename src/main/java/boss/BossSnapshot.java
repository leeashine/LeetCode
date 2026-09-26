package boss;

/**
 * 在 Boss 所属战斗线程中生成的一致性快照。
 */
public final class BossSnapshot {

    private final long currentHealth;

    BossSnapshot(long currentHealth) {
        this.currentHealth = currentHealth;
    }

    public long getCurrentHealth() {
        return currentHealth;
    }

    public boolean isDead() {
        return currentHealth == 0;
    }
}
