package crdt;

import java.util.Objects;

/**
 * 不可变的 last-writer-wins 寄存器：用 (时间戳、副本 ID、值) 表示一次写入，
 * 以 (Long.MIN_VALUE, "", null) 表示半格底元素。合并按时间戳从小到大排序；
 * 时间戳相同则按 replicaId 的字符串自然序排序，较大者获胜，这是并发写的 tie-break。
 * {@code merge} 是该全序上的取最大值，因此满足交换律、结合律和幂等律，底元素是单位元。
 *
 * <p>局限：这是“丢并发写”的 CRDT；只有最后写入者本来就该赢时才适用，
 * 例如用户偏好或单字段覆盖。需要保留并发写时应使用 MVRegister。
 * 同一个 replicaId 在同一个时间戳必须代表同一次写（相同的值）；否则无法
 * 仅凭时间戳和副本 ID 区分两个不同的值，收敛性不成立。跨副本的物理时间戳
 * 也不一定代表真实写入顺序。</p>
 *
 * @param <T> 寄存器中存储的值类型
 */
public final class LWWRegister<T> implements CvRDT<LWWRegister<T>> {
    private static final LWWRegister<?> EMPTY = new LWWRegister<Object>(null, Long.MIN_VALUE, "");

    private final T value;
    private final long timestamp;
    private final String replicaId;

    private LWWRegister(T value, long timestamp, String replicaId) {
        this.value = value;
        this.timestamp = timestamp;
        this.replicaId = replicaId;
    }

    @SuppressWarnings("unchecked")
    public static <T> LWWRegister<T> empty() {
        return (LWWRegister<T>) EMPTY;
    }

    public LWWRegister<T> set(T value, long timestamp, String replicaId) {
        if (timestamp == Long.MIN_VALUE) {
            throw new IllegalArgumentException("The minimum timestamp is reserved for bottom");
        }
        if (replicaId == null || replicaId.isEmpty()) {
            throw new IllegalArgumentException("replicaId must not be null or empty");
        }
        return new LWWRegister<T>(value, timestamp, replicaId);
    }

    public T value() {
        return value;
    }

    public long timestamp() {
        return timestamp;
    }

    public String replicaId() {
        return replicaId;
    }

    public boolean isEmpty() {
        return timestamp == Long.MIN_VALUE;
    }

    @Override
    public LWWRegister<T> merge(LWWRegister<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        if (timestamp > other.timestamp) {
            return this;
        }
        if (timestamp < other.timestamp) {
            return other;
        }
        return replicaId.compareTo(other.replicaId) >= 0 ? this : other;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LWWRegister)) {
            return false;
        }
        LWWRegister<?> that = (LWWRegister<?>) other;
        return timestamp == that.timestamp
                && replicaId.equals(that.replicaId)
                && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        int result = Long.hashCode(timestamp);
        result = 31 * result + replicaId.hashCode();
        return 31 * result + Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "LWWRegister{timestamp=" + timestamp + ", replicaId='" + replicaId
                + "', value=" + value + '}';
    }
}
