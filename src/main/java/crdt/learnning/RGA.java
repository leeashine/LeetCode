package crdt.learnning;

import java.util.Map;
import java.util.Set;

/** 第 06 章练习骨架，按文档推导补全 TODO。 */
public final class RGA implements CvRDT<RGA> {
    // TODO 第 06 章：建议字段：replicaId（String）、nodes（Map<NodeId, Node>）、tombstones（Set<NodeId>），保存正文结构与删除身份；第 09 章将墓碑升级为 Map<NodeId, CausalStamp> 并增加 version。

    public static final class NodeId implements Comparable<NodeId> {
        public final long timestamp;
        public final String replicaId;

        public NodeId(long timestamp, String replicaId) {
            // TODO 第 06 章：定义所需状态并构造不可变实例。
            throw new UnsupportedOperationException("TODO 第 06 章：NodeId");
        }

        @Override
        public int compareTo(NodeId other) {
            // TODO 第 06 章：为标识定义稳定且一致的比较顺序。
            throw new UnsupportedOperationException("TODO 第 06 章：compareTo");
        }

        @Override
        public boolean equals(Object other) {
            // TODO 第 06 章：按类型契约比较状态内容。
            throw new UnsupportedOperationException("TODO 第 06 章：equals");
        }

        @Override
        public int hashCode() {
            // TODO 第 06 章：与 equals 使用相同的状态内容。
            throw new UnsupportedOperationException("TODO 第 06 章：hashCode");
        }

        @Override
        public String toString() {
            // TODO 第 06 章：提供便于观察的状态描述。
            throw new UnsupportedOperationException("TODO 第 06 章：toString");
        }
    }

    public static final class Node {
        public final NodeId id;
        public final NodeId parentId;
        public final char value;

        public Node(NodeId id, NodeId parentId, char value) {
            // TODO 第 06 章：定义所需状态并构造不可变实例。
            throw new UnsupportedOperationException("TODO 第 06 章：Node");
        }

        @Override
        public boolean equals(Object other) {
            // TODO 第 06 章：按类型契约比较状态内容。
            throw new UnsupportedOperationException("TODO 第 06 章：equals");
        }

        @Override
        public int hashCode() {
            // TODO 第 06 章：与 equals 使用相同的状态内容。
            throw new UnsupportedOperationException("TODO 第 06 章：hashCode");
        }
    }

    public RGA(String replicaId) {
        // TODO 第 06 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 06 章：RGA");
    }

    private RGA(String replicaId, Map<NodeId, Node> nodes, Set<NodeId> tombstones) {
        // TODO 第 06 章：保存基本正文状态，只记录删除的字符身份。
        throw new UnsupportedOperationException("TODO 第 06 章：RGA");
    }

    private RGA(String replicaId, Map<NodeId, Node> nodes,
                Map<NodeId, CausalStamp> tombstones, VersionVector version) {
        // TODO 第 09 章：为基本正文状态增加删除进度与见证。
        throw new UnsupportedOperationException("TODO 第 09 章：RGA");
    }

    public String replicaId() {
        // TODO 第 06 章：读取本地写入身份。
        throw new UnsupportedOperationException("TODO 第 06 章：replicaId");
    }

    public VersionVector version() {
        // TODO 第 09 章：读取已经观察到的设备修改记录。
        throw new UnsupportedOperationException("TODO 第 09 章：version");
    }

    public int tombstoneCount() {
        // TODO 第 09 章：读取当前保留的删除记录数量。
        throw new UnsupportedOperationException("TODO 第 09 章：tombstoneCount");
    }

    public RGA insert(int index, char c) {
        // TODO 第 06 章：在可见正文的指定位置插入字符，保留稳定身份。
        throw new UnsupportedOperationException("TODO 第 06 章：insert");
    }

    public RGA delete(int index) {
        // TODO 第 06 章：删除可见正文的指定字符，保留同步所需记录。
        throw new UnsupportedOperationException("TODO 第 06 章：delete");
    }

    public String text() {
        // TODO 第 06 章：按稳定顺序读取可见正文。
        throw new UnsupportedOperationException("TODO 第 06 章：text");
    }

    public int size() {
        // TODO 第 06 章：读取可见内容的数量。
        throw new UnsupportedOperationException("TODO 第 06 章：size");
    }

    @Override
    public RGA merge(RGA other) {
        // TODO 第 06 章：合并双方状态，满足三条合并定律且不改动输入。
        throw new UnsupportedOperationException("TODO 第 06 章：merge");
    }

    public RGA gc(VersionVector frontier) {
        // TODO 第 09 章：只回收满足稳定条件且不会破坏可见内容的删除记录。
        throw new UnsupportedOperationException("TODO 第 09 章：gc");
    }

    @Override
    public boolean equals(Object other) {
        // TODO 第 06 章：按类型契约比较状态内容。
        throw new UnsupportedOperationException("TODO 第 06 章：equals");
    }

    @Override
    public int hashCode() {
        // TODO 第 06 章：与 equals 使用相同的状态内容。
        throw new UnsupportedOperationException("TODO 第 06 章：hashCode");
    }

    @Override
    public String toString() {
        // TODO 第 06 章：提供便于观察的状态描述。
        throw new UnsupportedOperationException("TODO 第 06 章：toString");
    }
}
