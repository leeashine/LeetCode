package crdt;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 只增计数器：用副本 ID 到非零、非负累计次数的映射表示状态，每个副本只写自己的分量。
 * 合并取 key 并集及每个分量的最大值，即逐分量半格上的 join；max 和集合并集均满足
 * 交换律、结合律、幂等律，因而重复或乱序同步也能收敛。
 * {@code replicaId} 仅是本地写入身份，不是复制状态；equals/hashCode 只比较分量映射。
 * 局限：不能递减，同一 ID 不可由多个写者使用；结果必须在 long 的表示范围内。
 */
public final class GCounter implements CvRDT<GCounter> {
    private final String replicaId;
    private final Map<String, Long> counts;

    public GCounter(String replicaId) {
        this(replicaId, Collections.<String, Long>emptyMap());
    }

    private GCounter(String replicaId, Map<String, Long> counts) {
        this.replicaId = Objects.requireNonNull(replicaId, "replicaId");
        this.counts = Collections.unmodifiableMap(counts);
    }

    public String replicaId() {
        return replicaId;
    }

    public GCounter increment() {
        return increment(1);
    }

    public GCounter increment(long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("delta must be nonnegative");
        }
        Map<String, Long> next = new TreeMap<>(counts);
        long updated = Math.addExact(countOf(replicaId), delta);
        if (updated != 0) {
            next.put(replicaId, updated);
        }
        return new GCounter(replicaId, next);
    }

    public long value() {
        long sum = 0;
        for (long count : counts.values()) {
            sum = Math.addExact(sum, count);
        }
        return sum;
    }

    public long countOf(String replicaId) {
        Long count = counts.get(replicaId);
        return count == null ? 0 : count;
    }

    @Override
    public GCounter merge(GCounter other) {
        Objects.requireNonNull(other, "other");
        Map<String, Long> joined = new TreeMap<>(counts);
        for (Map.Entry<String, Long> entry : other.counts.entrySet()) {
            long maximum = Math.max(countOf(entry.getKey()), entry.getValue());
            joined.put(entry.getKey(), maximum);
        }
        return new GCounter(replicaId, joined);
    }

    public Map<String, Long> state() {
        return Collections.unmodifiableMap(new TreeMap<>(counts));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof GCounter)) {
            return false;
        }
        return counts.equals(((GCounter) other).counts);
    }

    @Override
    public int hashCode() {
        return counts.hashCode();
    }

    @Override
    public String toString() {
        return "GCounter{replicaId='" + replicaId + "', state=" + counts + '}';
    }
}
