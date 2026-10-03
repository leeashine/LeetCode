package crdt;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 版本向量用每个副本的写入计数表示因果历史，把 Lamport 时钟的“全序”细化为因果“偏序”：
 * {@link #isConcurrent(VersionVector)} 能识别互不包含的真正并发写，这是 LWW 仅凭 Lamport 时间戳判不出的。
 * 逐分量取最大值是半格上的 join，因此 merge 交换、结合、幂等；计数只增不减。
 * 代价是状态随副本数量增长，且要求副本 ID 稳定、唯一；它不能为并发事件排序或替用户解决冲突。
 */
public final class VersionVector {
    private final SortedMap<String, Long> counters;

    private VersionVector(SortedMap<String, Long> counters) {
        this.counters = Collections.unmodifiableSortedMap(counters);
    }

    public static VersionVector empty() {
        return new VersionVector(new TreeMap<String, Long>());
    }

    /**
     * 从"副本 ID → 已观察到的操作次数"构造版本向量。
     *
     * <p>值为 0 的条目会被丢弃，以维持规范形式（与 {@link #asMap()} 的输出一致）。
     * 供 {@link CausalStability} 这类需要直接构造向量的工具使用，
     * 避免用反复 {@code increment} 来拼装（那是 O(操作数) 的）。</p>
     *
     * @param counters 副本 ID → 计数；值为 null/负数或 key 为空串时抛 {@link IllegalArgumentException}
     */
    public static VersionVector of(Map<String, Long> counters) {
        if (counters == null) {
            throw new IllegalArgumentException("counters must not be null");
        }
        SortedMap<String, Long> copy = new TreeMap<String, Long>();
        for (Map.Entry<String, Long> entry : counters.entrySet()) {
            String replicaId = entry.getKey();
            Long value = entry.getValue();
            if (replicaId == null || replicaId.isEmpty()) {
                throw new IllegalArgumentException("replicaId must not be null or empty");
            }
            if (value == null || value < 0L) {
                throw new IllegalArgumentException("count must be nonnegative: " + replicaId);
            }
            if (value > 0L) {
                copy.put(replicaId, value);
            }
        }
        return new VersionVector(copy);
    }

    public long get(String replicaId) {
        Long count = counters.get(replicaId);
        return count == null ? 0L : count;
    }

    public VersionVector increment(String replicaId) {
        if (replicaId == null || replicaId.isEmpty()) {
            throw new IllegalArgumentException("replicaId must not be null or empty");
        }
        long previous = get(replicaId);
        if (previous == Long.MAX_VALUE) {
            throw new IllegalStateException("version counter overflow for " + replicaId);
        }
        SortedMap<String, Long> next = new TreeMap<String, Long>(counters);
        next.put(replicaId, previous + 1L);
        return new VersionVector(next);
    }

    public VersionVector merge(VersionVector other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        SortedMap<String, Long> joined = new TreeMap<String, Long>(counters);
        for (Map.Entry<String, Long> entry : other.counters.entrySet()) {
            Long current = joined.get(entry.getKey());
            if (current == null || entry.getValue() > current) {
                joined.put(entry.getKey(), entry.getValue());
            }
        }
        return new VersionVector(joined);
    }

    public boolean greaterOrEqual(VersionVector other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        for (Map.Entry<String, Long> entry : other.counters.entrySet()) {
            if (get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public boolean dominates(VersionVector other) {
        return greaterOrEqual(other) && !equals(other);
    }

    public boolean isConcurrent(VersionVector other) {
        return !greaterOrEqual(other) && !other.greaterOrEqual(this);
    }

    public Map<String, Long> asMap() {
        return counters;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VersionVector)) {
            return false;
        }
        VersionVector other = (VersionVector) obj;
        return counters.equals(other.counters);
    }

    @Override
    public int hashCode() {
        return counters.hashCode();
    }

    @Override
    public String toString() {
        return "VersionVector" + counters;
    }
}
