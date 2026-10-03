package crdt;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 多值寄存器用 (版本向量, 值) 的规范集合保存未被因果覆盖的写入：例如收藏夹的同一个 key
 * 被两台离线设备改成不同 URL 时，两个值都保留，冲突交给上层或用户解决，不会像 LWW 那样丢数据。
 * 它提供了 OR-Set、文本 CRDT 所需的“保留并发、淘汰因果旧值”语义基础。
 * merge 对两边集合取并、去重并删除被严格支配的项；集合并与偏序极大元筛选使其
 * 满足交换、结合、幂等，即该状态半格的 join。取舍是因果元数据和并发值会增长，
 * 且不能自动解决冲突；为保证不可变性，调用方传入的值自身也应是不可变对象。
 *
 * @param <T> 寄存器存储的值类型
 */
public final class MVRegister<T> implements CvRDT<MVRegister<T>> {
    private final Set<Entry<T>> entries;

    private MVRegister(Set<Entry<T>> entries) {
        this.entries = Collections.unmodifiableSet(entries);
    }

    public static <T> MVRegister<T> empty() {
        return new MVRegister<T>(new HashSet<Entry<T>>());
    }

    public MVRegister<T> assign(T value, String replicaId) {
        // 所有旧 entry 的版本均 <= version()；递增后的版本严格支配它们。
        VersionVector next = version().increment(replicaId);
        Set<Entry<T>> assigned = new HashSet<Entry<T>>();
        assigned.add(new Entry<T>(next, value));
        return new MVRegister<T>(assigned);
    }

    public Set<T> values() {
        Set<T> values = new HashSet<T>();
        for (Entry<T> entry : entries) {
            values.add(entry.value);
        }
        return Collections.unmodifiableSet(values);
    }

    public int size() {
        return values().size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public VersionVector version() {
        VersionVector joined = VersionVector.empty();
        for (Entry<T> entry : entries) {
            joined = joined.merge(entry.vv);
        }
        return joined;
    }

    @Override
    public MVRegister<T> merge(MVRegister<T> other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        Set<Entry<T>> union = new HashSet<Entry<T>>(entries);
        union.addAll(other.entries);
        Set<Entry<T>> maximal = new HashSet<Entry<T>>();
        for (Entry<T> candidate : union) {
            boolean obsolete = false;
            for (Entry<T> contender : union) {
                if (contender.vv.dominates(candidate.vv)) {
                    obsolete = true;
                    break;
                }
            }
            if (!obsolete) {
                maximal.add(candidate);
            }
        }
        return new MVRegister<T>(maximal);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MVRegister)) {
            return false;
        }
        MVRegister<?> other = (MVRegister<?>) obj;
        return entries.equals(other.entries);
    }

    @Override
    public int hashCode() {
        return entries.hashCode();
    }

    @Override
    public String toString() {
        return "MVRegister" + entries;
    }

    private static final class Entry<T> {
        private final VersionVector vv;
        private final T value;

        private Entry(VersionVector vv, T value) {
            this.vv = vv;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry<?> other = (Entry<?>) obj;
            return vv.equals(other.vv) && Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            return 31 * vv.hashCode() + Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return "(" + vv + ", " + value + ")";
        }
    }
}
