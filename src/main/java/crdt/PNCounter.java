package crdt;

import java.util.Objects;

/**
 * 可增可减计数器：状态为两个独立的只增计数器 P（增量）与 N（减量），读数为 P-N。
 * 合并分别对 P、N 按副本分量取最大值；积半格的逐分量 join 保留了交换律、
 * 结合律与幂等律，重复/乱序合并不会把同一更新重复计入。副本 ID 是本地写入身份，
 * 不属于复制状态；相等性只比较两个只增计数器的复制状态。
 * 局限：每个分量仍要求单写者；历史总增量、总减量持续增长，数值须在 long 范围内。
 */
public final class PNCounter implements CvRDT<PNCounter> {
    private final GCounter positive;
    private final GCounter negative;

    public PNCounter(String replicaId) {
        this(new GCounter(replicaId), new GCounter(replicaId));
    }

    private PNCounter(GCounter positive, GCounter negative) {
        this.positive = positive;
        this.negative = negative;
    }

    public PNCounter increment() {
        return increment(1);
    }

    public PNCounter increment(long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("delta must be nonnegative");
        }
        return new PNCounter(positive.increment(delta), negative);
    }

    public PNCounter decrement() {
        return decrement(1);
    }

    public PNCounter decrement(long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("delta must be nonnegative");
        }
        return new PNCounter(positive, negative.increment(delta));
    }

    public long value() {
        return Math.subtractExact(positive.value(), negative.value());
    }

    @Override
    public PNCounter merge(PNCounter other) {
        Objects.requireNonNull(other, "other");
        return new PNCounter(positive.merge(other.positive), negative.merge(other.negative));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PNCounter)) {
            return false;
        }
        PNCounter that = (PNCounter) other;
        return positive.equals(that.positive) && negative.equals(that.negative);
    }

    @Override
    public int hashCode() {
        return 31 * positive.hashCode() + negative.hashCode();
    }

    @Override
    public String toString() {
        return "PNCounter{positive=" + positive + ", negative=" + negative + '}';
    }
}
