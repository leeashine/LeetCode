package crdt.learnning;

import java.util.Map;
import java.util.Set;

/** 第 08 章练习骨架，按文档推导补全 TODO。 */
public final class DeltaORSet<E> implements CvRDT<DeltaORSet<E>> {
    // TODO 第 08 章：建议字段：replicaId（String）、seq（long）、entries（Map<E, Set<ORSet.Tag>>）、tombstones、unackedAdded、unackedRemoved（Set<ORSet.Tag>），区分复制状态与待确认内容。

    private DeltaORSet(String replicaId, long seq, Map<E, Set<ORSet.Tag>> entries,
                       Set<ORSet.Tag> tombstones, Set<ORSet.Tag> unackedAdded,
                       Set<ORSet.Tag> unackedRemoved) {
        // TODO 第 08 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 08 章：DeltaORSet");
    }

    public static <E> DeltaORSet<E> empty(String replicaId) {
        // TODO 第 08 章：创建没有任何记录的起点。
        throw new UnsupportedOperationException("TODO 第 08 章：empty");
    }

    public String replicaId() {
        // TODO 第 08 章：读取本地写入身份。
        throw new UnsupportedOperationException("TODO 第 08 章：replicaId");
    }

    public DeltaORSet<E> add(E element) {
        // TODO 第 08 章：为标签新增一次独立添加记录。
        throw new UnsupportedOperationException("TODO 第 08 章：add");
    }

    public DeltaORSet<E> remove(E element) {
        // TODO 第 08 章：删除本设备已经观察到的标签添加记录。
        throw new UnsupportedOperationException("TODO 第 08 章：remove");
    }

    public boolean contains(E element) {
        // TODO 第 08 章：判断标签是否仍可见。
        throw new UnsupportedOperationException("TODO 第 08 章：contains");
    }

    public Set<E> elements() {
        // TODO 第 08 章：提供可见标签的只读集合。
        throw new UnsupportedOperationException("TODO 第 08 章：elements");
    }

    public int size() {
        // TODO 第 08 章：读取可见内容的数量。
        throw new UnsupportedOperationException("TODO 第 08 章：size");
    }

    public Delta<E> pending() {
        // TODO 第 08 章：读取尚未确认送达的增量，不清除发送记录。
        throw new UnsupportedOperationException("TODO 第 08 章：pending");
    }

    public DeltaORSet<E> ack() {
        // TODO 第 08 章：确认当前待发内容，保留复制状态及后续写入能力。
        throw new UnsupportedOperationException("TODO 第 08 章：ack");
    }

    public DeltaORSet<E> apply(Delta<E> delta) {
        // TODO 第 08 章：接收增量，允许重复和乱序投递。
        throw new UnsupportedOperationException("TODO 第 08 章：apply");
    }

    @Override
    public DeltaORSet<E> merge(DeltaORSet<E> other) {
        // TODO 第 08 章：合并双方状态，满足三条合并定律且不改动输入。
        throw new UnsupportedOperationException("TODO 第 08 章：merge");
    }

    @Override
    public boolean equals(Object other) {
        // TODO 第 08 章：按类型契约比较状态内容。
        throw new UnsupportedOperationException("TODO 第 08 章：equals");
    }

    @Override
    public int hashCode() {
        // TODO 第 08 章：与 equals 使用相同的状态内容。
        throw new UnsupportedOperationException("TODO 第 08 章：hashCode");
    }

    @Override
    public String toString() {
        // TODO 第 08 章：提供便于观察的状态描述。
        throw new UnsupportedOperationException("TODO 第 08 章：toString");
    }

    public static final class Delta<E> {
        // TODO 第 08 章：建议字段：added（Map<E, Set<ORSet.Tag>>）、removed（Set<ORSet.Tag>），保存添加与删除的增量内容。

        private Delta(Map<E, Set<ORSet.Tag>> added, Set<ORSet.Tag> removed) {
            // TODO 第 08 章：定义所需状态并构造不可变实例。
            throw new UnsupportedOperationException("TODO 第 08 章：Delta");
        }

        public static <E> Delta<E> empty() {
            // TODO 第 08 章：创建没有任何记录的起点。
            throw new UnsupportedOperationException("TODO 第 08 章：empty");
        }

        public Map<E, Set<ORSet.Tag>> added() {
            // TODO 第 08 章：提供增量中的标签添加记录只读表。
            throw new UnsupportedOperationException("TODO 第 08 章：added");
        }

        public Set<ORSet.Tag> removed() {
            // TODO 第 08 章：提供增量中的标签删除记录只读集合。
            throw new UnsupportedOperationException("TODO 第 08 章：removed");
        }

        public Delta<E> merge(Delta<E> other) {
            // TODO 第 08 章：合并双方状态，满足三条合并定律且不改动输入。
            throw new UnsupportedOperationException("TODO 第 08 章：merge");
        }

        public boolean isEmpty() {
            // TODO 第 08 章：判断是否没有记录。
            throw new UnsupportedOperationException("TODO 第 08 章：isEmpty");
        }

        @Override
        public boolean equals(Object other) {
            // TODO 第 08 章：按类型契约比较状态内容。
            throw new UnsupportedOperationException("TODO 第 08 章：equals");
        }

        @Override
        public int hashCode() {
            // TODO 第 08 章：与 equals 使用相同的状态内容。
            throw new UnsupportedOperationException("TODO 第 08 章：hashCode");
        }

        @Override
        public String toString() {
            // TODO 第 08 章：提供便于观察的状态描述。
            throw new UnsupportedOperationException("TODO 第 08 章：toString");
        }
    }
}
