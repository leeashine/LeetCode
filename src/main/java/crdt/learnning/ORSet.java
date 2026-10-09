package crdt.learnning;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/** 第 05 章练习骨架，按文档推导补全 TODO。 */
public final class ORSet<E> implements CvRDT<ORSet<E>> {
    // TODO 第 05 章：建议字段：replicaId（String）、seq（long）、entries（Map<E, Set<Tag>>）、tombstones（Set<Tag>）；第 09 章补 version（VersionVector）、tombstoneStamps（Map<Tag, CausalStamp>）。

    private ORSet(String replicaId, long seq, Map<E, Set<Tag>> entries, Set<Tag> tombstones) {
        // TODO 第 05 章：保存基本标签状态，不依赖第 09 章删除见证。
        throw new UnsupportedOperationException("TODO 第 05 章：ORSet");
    }

    private ORSet(String replicaId, long seq, Map<E, Set<Tag>> entries, Set<Tag> tombstones,
                  VersionVector version, Map<Tag, CausalStamp> tombstoneStamps) {
        // TODO 第 09 章：为基本状态增加删除进度与见证。
        throw new UnsupportedOperationException("TODO 第 09 章：ORSet");
    }

    public static <E> ORSet<E> empty(String replicaId) {
        // TODO 第 05 章：创建没有任何记录的起点。
        throw new UnsupportedOperationException("TODO 第 05 章：empty");
    }

    public String replicaId() {
        // TODO 第 05 章：读取本地写入身份。
        throw new UnsupportedOperationException("TODO 第 05 章：replicaId");
    }

    public VersionVector version() {
        // TODO 第 09 章：读取已经观察到的设备修改记录。
        throw new UnsupportedOperationException("TODO 第 09 章：version");
    }

    public int tombstoneCount() {
        // TODO 第 09 章：读取当前保留的删除记录数量。
        throw new UnsupportedOperationException("TODO 第 09 章：tombstoneCount");
    }

    public ORSet<E> add(E element) {
        // TODO 第 05 章：为标签新增一次独立添加记录。
        throw new UnsupportedOperationException("TODO 第 05 章：add");
    }

    public ORSet<E> remove(E element) {
        // TODO 第 05 章：删除本设备已经观察到的标签添加记录。
        throw new UnsupportedOperationException("TODO 第 05 章：remove");
    }

    public boolean contains(E element) {
        // TODO 第 05 章：判断标签是否仍可见。
        throw new UnsupportedOperationException("TODO 第 05 章：contains");
    }

    public Set<E> elements() {
        // TODO 第 05 章：提供可见标签的只读集合。
        throw new UnsupportedOperationException("TODO 第 05 章：elements");
    }

    public int size() {
        // TODO 第 05 章：读取可见内容的数量。
        throw new UnsupportedOperationException("TODO 第 05 章：size");
    }

    public ORSet<E> gc(VersionVector frontier) {
        // TODO 第 09 章：只回收满足稳定条件且不会破坏可见内容的删除记录。
        throw new UnsupportedOperationException("TODO 第 09 章：gc");
    }

    @Override
    public ORSet<E> merge(ORSet<E> other) {
        // TODO 第 05 章：合并双方状态，满足三条合并定律且不改动输入。
        throw new UnsupportedOperationException("TODO 第 05 章：merge");
    }

    @Override
    public boolean equals(Object other) {
        // TODO 第 05 章：按类型契约比较状态内容。
        throw new UnsupportedOperationException("TODO 第 05 章：equals");
    }

    @Override
    public int hashCode() {
        // TODO 第 05 章：与 equals 使用相同的状态内容。
        throw new UnsupportedOperationException("TODO 第 05 章：hashCode");
    }

    @Override
    public String toString() {
        // TODO 第 05 章：提供便于观察的状态描述。
        throw new UnsupportedOperationException("TODO 第 05 章：toString");
    }

    public static final class Tag implements Serializable {
        // TODO 第 05 章：建议字段：replicaId（String）、seq（long），绑定一次标签添加的唯一身份。

        public Tag(String replicaId, long seq) {
            // TODO 第 05 章：定义所需状态并构造不可变实例。
            throw new UnsupportedOperationException("TODO 第 05 章：Tag");
        }

        public String replicaId() {
            // TODO 第 05 章：读取本地写入身份。
            throw new UnsupportedOperationException("TODO 第 05 章：replicaId");
        }

        public long seq() {
            // TODO 第 05 章：读取该设备操作的序号。
            throw new UnsupportedOperationException("TODO 第 05 章：seq");
        }

        @Override
        public boolean equals(Object other) {
            // TODO 第 05 章：按类型契约比较状态内容。
            throw new UnsupportedOperationException("TODO 第 05 章：equals");
        }

        @Override
        public int hashCode() {
            // TODO 第 05 章：与 equals 使用相同的状态内容。
            throw new UnsupportedOperationException("TODO 第 05 章：hashCode");
        }

        @Override
        public String toString() {
            // TODO 第 05 章：提供便于观察的状态描述。
            throw new UnsupportedOperationException("TODO 第 05 章：toString");
        }
    }
}
