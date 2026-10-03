package crdt;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 只增计数器的增量同步版本：用副本 ID 到非零、非负累计次数的映射表示复制状态，
 * 用 {@code acked} 记录本地分量上次确认送达时的值；待发送增量只含本地分量的绝对累计值。
 * 全量状态及增量都按 key 并集、逐分量 max 合并，因而满足幂等律、交换律、结合律；
 * 重复、乱序投递不会重复计数。{@code replicaId} 是本地写入身份，{@code acked} 是本地
 * 发送水位，均不属于复制状态；equals/hashCode 只比较分量映射。
 * 局限：不能递减，同一 ID 不可由多个写者使用；需要调用方确认送达后才 ack，
 * 丢包时需重传未确认增量；结果必须在 long 的表示范围内。
 */
public final class DeltaGCounter implements CvRDT<DeltaGCounter> {
    private final String replicaId;
    private final Map<String, Long> counts;
    private final long acked;

    public DeltaGCounter(String replicaId) {
        if (replicaId == null || replicaId.isEmpty()) {
            throw new IllegalArgumentException("replicaId must not be null or empty");
        }
        this.replicaId = replicaId;
        this.counts = Collections.emptyMap();
        this.acked = 0;
    }

    private DeltaGCounter(String replicaId, Map<String, Long> counts, long acked) {
        this.replicaId = replicaId;
        this.counts = Collections.unmodifiableMap(counts);
        this.acked = acked;
    }

    public String replicaId() {
        return replicaId;
    }

    public DeltaGCounter increment() {
        return increment(1);
    }

    public DeltaGCounter increment(long delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("delta must be nonnegative");
        }
        if (delta == 0) {
            return new DeltaGCounter(replicaId, counts, acked);
        }
        long updated = Math.addExact(countOf(replicaId), delta);
        Map<String, Long> next = new TreeMap<>(counts);
        next.put(replicaId, updated);
        return new DeltaGCounter(replicaId, next, acked);
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

    public Map<String, Long> state() {
        return Collections.unmodifiableMap(new TreeMap<>(counts));
    }

    public Delta pending() {
        long local = countOf(replicaId);
        if (local <= acked) {
            return Delta.empty();
        }
        Map<String, Long> pending = new TreeMap<>();
        pending.put(replicaId, local);
        return new Delta(pending);
    }

    public DeltaGCounter ack() {
        return new DeltaGCounter(replicaId, counts, countOf(replicaId));
    }

    public DeltaGCounter apply(Delta delta) {
        Objects.requireNonNull(delta, "delta");
        return new DeltaGCounter(replicaId, join(counts, delta.counts), acked);
    }

    @Override
    public DeltaGCounter merge(DeltaGCounter other) {
        Objects.requireNonNull(other, "other");
        return new DeltaGCounter(replicaId, join(counts, other.counts), acked);
    }

    private static Map<String, Long> join(Map<String, Long> left, Map<String, Long> right) {
        Map<String, Long> joined = null;
        for (Map.Entry<String, Long> entry : right.entrySet()) {
            Long previous = left.get(entry.getKey());
            if (previous == null || previous < entry.getValue()) {
                if (joined == null) {
                    joined = new TreeMap<>(left);
                }
                joined.put(entry.getKey(), entry.getValue());
            }
        }
        return joined == null ? left : joined;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DeltaGCounter)) {
            return false;
        }
        return counts.equals(((DeltaGCounter) other).counts);
    }

    @Override
    public int hashCode() {
        return counts.hashCode();
    }

    @Override
    public String toString() {
        return "DeltaGCounter{replicaId='" + replicaId + "', state=" + counts + ", acked=" + acked + '}';
    }

    /**
     * 待传输的累计分量：映射中的值是绝对累计值而非增量差值。
     * 逐 key max 合并仍是同一半格的 join，满足幂等、交换、结合律，允许重传与乱序。
     * 局限：只包含已产生的分量；发送确认及丢包重传由调用方负责。
     */
    public static final class Delta {
        private static final Delta EMPTY = new Delta(Collections.<String, Long>emptyMap());
        private final Map<String, Long> counts;

        private Delta(Map<String, Long> counts) {
            this.counts = Collections.unmodifiableMap(counts);
        }

        public static Delta empty() {
            return EMPTY;
        }

        public Map<String, Long> counts() {
            return Collections.unmodifiableMap(new TreeMap<>(counts));
        }

        public Delta merge(Delta other) {
            Objects.requireNonNull(other, "other");
            return new Delta(join(counts, other.counts));
        }

        public boolean isEmpty() {
            return counts.isEmpty();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Delta)) {
                return false;
            }
            return counts.equals(((Delta) other).counts);
        }

        @Override
        public int hashCode() {
            return counts.hashCode();
        }

        @Override
        public String toString() {
            return "Delta{counts=" + counts + '}';
        }
    }
}
