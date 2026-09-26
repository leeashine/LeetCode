package boss;

/**
 * 一次攻击在权威战斗线程中执行后的结果。
 */
public final class DamageResult {

    private final long appliedDamage;
    private final long remainingHealth;
    private final boolean killed;

    DamageResult(long appliedDamage, long remainingHealth, boolean killed) {
        this.appliedDamage = appliedDamage;
        this.remainingHealth = remainingHealth;
        this.killed = killed;
    }

    public long getAppliedDamage() {
        return appliedDamage;
    }

    public long getRemainingHealth() {
        return remainingHealth;
    }

    public boolean isKilled() {
        return killed;
    }
}
