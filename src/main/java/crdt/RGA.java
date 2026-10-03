package crdt;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 协同文本编辑的序列 CRDT：每个字符是带 parent 指针的节点，删除仅增加墓碑。
 * 从虚拟根开始，按子节点 ID 降序做前序 DFS；新时间戳的同位置插入排在左边，
 * 而已有节点的整棵子树随其父节点一起排列，因此并发插入不会丢失或撕裂字符。
 * 未 GC 时状态按节点和墓碑的集合包含关系增长；合法状态的 merge 是两个集合的并
 * （同一墓碑的删除时间戳取 min），因此满足交换、结合、幂等。节点 ID 的时间戳
 * 直接从当前状态中的最大时间戳推导，不存本地时钟；merge 后下一次写自动跟上已知时间戳。
 * GC 需外部提供所有存活副本共同确认的稳定前沿；不支持富文本或字符属性。
 * <p><b>前置条件：</b>{@code replicaId} 必须全局唯一且稳定。两个逻辑副本共用同一个
 * {@code replicaId} 会生成相同的 {@link NodeId}，{@code merge} 时抛
 * {@code IllegalArgumentException: Conflicting node ID}（健全性检查，而不是静默撕裂文本）。
 * 副本加入一份已有文档的正确写法是 {@code new RGA(myId).merge(sharedState)}：
 * {@code merge} 保留调用方的 {@code replicaId}，因此之后新插入的节点都带本地 ID。</p>
 */
public final class RGA implements CvRDT<RGA> {
    /** 由时间戳与副本 ID 组成的稳定字符标识。 */
    public static final class NodeId implements Comparable<NodeId> {
        public final long timestamp;
        public final String replicaId;

        public NodeId(long timestamp, String replicaId) {
            this.timestamp = timestamp;
            this.replicaId = Objects.requireNonNull(replicaId, "replicaId");
        }

        @Override
        public int compareTo(NodeId other) {
            int byTimestamp = Long.compare(timestamp, other.timestamp);
            return byTimestamp != 0 ? byTimestamp : replicaId.compareTo(other.replicaId);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof NodeId)) return false;
            NodeId that = (NodeId) other;
            return timestamp == that.timestamp && replicaId.equals(that.replicaId);
        }

        @Override
        public int hashCode() {
            return 31 * Long.hashCode(timestamp) + replicaId.hashCode();
        }

        @Override
        public String toString() {
            return "NodeId(" + timestamp + ", " + replicaId + ")";
        }
    }

    /** 一个不可变字符节点；根节点的 parentId 为 null。 */
    public static final class Node {
        public final NodeId id;
        public final NodeId parentId;
        public final char value;

        public Node(NodeId id, NodeId parentId, char value) {
            this.id = Objects.requireNonNull(id, "id");
            this.parentId = parentId;
            this.value = value;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof Node)) return false;
            Node that = (Node) other;
            return value == that.value && id.equals(that.id)
                    && Objects.equals(parentId, that.parentId);
        }

        @Override
        public int hashCode() {
            int result = 31 * id.hashCode() + (parentId == null ? 0 : parentId.hashCode());
            return 31 * result + value;
        }
    }

    private final String replicaId;
    private final Map<NodeId, Node> nodes;
    private final Map<NodeId, CausalStamp> tombstones;
    private final VersionVector version;

    public RGA(String replicaId) {
        this(replicaId, new HashMap<NodeId, Node>(), new HashMap<NodeId, CausalStamp>(),
                VersionVector.empty());
    }

    // Collections are freshly owned or existing immutable snapshots; never expose or mutate them afterward.
    private RGA(String replicaId, Map<NodeId, Node> nodes,
                Map<NodeId, CausalStamp> tombstones, VersionVector version) {
        this.replicaId = Objects.requireNonNull(replicaId, "replicaId");
        this.nodes = Collections.unmodifiableMap(nodes);
        this.tombstones = Collections.unmodifiableMap(tombstones);
        this.version = version;
    }

    public String replicaId() {
        return replicaId;
    }

    public VersionVector version() {
        return version;
    }

    public int tombstoneCount() {
        return tombstones.size();
    }

    public RGA insert(int index, char c) {
        List<Node> visible = visibleNodes();
        if (index < 0 || index > visible.size()) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + visible.size());
        }
        NodeId parentId = index == 0 ? null : visible.get(index - 1).id;
        long maximum = 0;
        for (NodeId id : nodes.keySet()) {
            maximum = Math.max(maximum, id.timestamp);
        }
        NodeId id = new NodeId(maximum + 1, replicaId);
        Map<NodeId, Node> updated = new HashMap<NodeId, Node>(nodes);
        updated.put(id, new Node(id, parentId, c));
        return new RGA(replicaId, updated, tombstones, version.increment(replicaId));
    }

    public RGA delete(int index) {
        List<Node> visible = visibleNodes();
        if (index < 0 || index >= visible.size()) {
            throw new IndexOutOfBoundsException("index: " + index + ", size: " + visible.size());
        }
        VersionVector updatedVersion = version.increment(replicaId);
        CausalStamp stamp = new CausalStamp(replicaId, updatedVersion.get(replicaId));
        Map<NodeId, CausalStamp> deleted = new HashMap<NodeId, CausalStamp>(tombstones);
        NodeId id = visible.get(index).id;
        CausalStamp previous = deleted.get(id);
        deleted.put(id, previous == null ? stamp : CausalStamp.min(previous, stamp));
        return new RGA(replicaId, nodes, deleted, updatedVersion);
    }

    public String text() {
        StringBuilder text = new StringBuilder();
        for (Node node : visibleNodes()) {
            text.append(node.value);
        }
        return text.toString();
    }

    public int size() {
        return visibleNodes().size();
    }

    // Traverse tombstones too: their descendants remain visible and in their original position.
    private List<Node> visibleNodes() {
        Map<NodeId, List<Node>> children = new HashMap<NodeId, List<Node>>();
        for (Node node : nodes.values()) {
            List<Node> siblings = children.get(node.parentId);
            if (siblings == null) {
                siblings = new ArrayList<Node>();
                children.put(node.parentId, siblings);
            }
            siblings.add(node);
        }
        for (List<Node> siblings : children.values()) {
            Collections.sort(siblings, (left, right) -> left.id.compareTo(right.id));
        }
        Deque<Node> pending = new ArrayDeque<Node>();
        pushChildren(children.get(null), pending);
        List<Node> visible = new ArrayList<Node>();
        while (!pending.isEmpty()) {
            Node node = pending.pop();
            if (!tombstones.containsKey(node.id)) {
                visible.add(node);
            }
            pushChildren(children.get(node.id), pending);
        }
        return visible;
    }

    // Children are pushed in ascending order, hence popped in descending order.
    private static void pushChildren(List<Node> siblings, Deque<Node> pending) {
        if (siblings != null) {
            for (Node node : siblings) {
                pending.push(node);
            }
        }
    }

    @Override
    public RGA merge(RGA other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        Map<NodeId, Node> merged = new HashMap<NodeId, Node>(nodes);
        for (Node node : other.nodes.values()) {
            Node existing = merged.putIfAbsent(node.id, node);
            if (existing != null && !existing.equals(node)) {
                throw new IllegalArgumentException("Conflicting node ID: " + node.id);
            }
        }
        Map<NodeId, CausalStamp> deleted = new HashMap<NodeId, CausalStamp>(tombstones);
        for (Map.Entry<NodeId, CausalStamp> entry : other.tombstones.entrySet()) {
            CausalStamp previous = deleted.get(entry.getKey());
            deleted.put(entry.getKey(), previous == null ? entry.getValue()
                    : CausalStamp.min(previous, entry.getValue()));
        }
        return new RGA(replicaId, merged, deleted, version.merge(other.version));
    }

    /**
     * 仅回收因果稳定的墓碑叶子，反复剥除叶子直到不动点（例如 a→b→c 都被删时，
     * 先删 c，b 和 a 才会依次变成叶子）。非叶节点是子节点的父锚点：移除它会
     * 让子节点成为孤儿，而重挂到祖父会改变按父节点确定的兄弟顺序。叶子没有
     * 子节点，且本来不出现在可见文本中，因此移除不改变其它节点的相对顺序。
     *
     * <p>GC 后副本的内部状态可能不再逐字节相同（{@code equals} 可能为 false），
     * 但 {@code text()} / {@code size()} 仍一致。与未 GC 的副本 merge 时，
     * 节点及其墓碑会一起重新出现，不会复活字符；需要再次 GC。</p>
     *
     * @param frontier 所有存活副本都已观察到的删除操作的稳定前沿
     * @return 回收后的新状态
     */
    public RGA gc(VersionVector frontier) {
        if (frontier == null) {
            throw new IllegalArgumentException("frontier must not be null");
        }
        Map<NodeId, Integer> childCounts = new HashMap<NodeId, Integer>();
        for (Node node : nodes.values()) {
            if (node.parentId != null) {
                Integer count = childCounts.get(node.parentId);
                childCounts.put(node.parentId, count == null ? 1 : count + 1);
            }
        }
        Deque<NodeId> leaves = new ArrayDeque<NodeId>();
        for (Map.Entry<NodeId, CausalStamp> entry : tombstones.entrySet()) {
            if (nodes.containsKey(entry.getKey()) && !childCounts.containsKey(entry.getKey())
                    && CausalStability.covers(frontier, entry.getValue())) {
                leaves.add(entry.getKey());
            }
        }
        if (leaves.isEmpty()) {
            return new RGA(replicaId, nodes, tombstones, version);
        }
        Map<NodeId, Node> remaining = new HashMap<NodeId, Node>(nodes);
        Map<NodeId, CausalStamp> deleted = new HashMap<NodeId, CausalStamp>(tombstones);
        while (!leaves.isEmpty()) {
            NodeId id = leaves.remove();
            Node node = remaining.remove(id);
            deleted.remove(id);
            NodeId parent = node.parentId;
            if (parent != null) {
                int count = childCounts.get(parent);
                if (count == 1) {
                    childCounts.remove(parent);
                    CausalStamp parentStamp = deleted.get(parent);
                    if (parentStamp != null && CausalStability.covers(frontier, parentStamp)) {
                        leaves.add(parent);
                    }
                } else {
                    childCounts.put(parent, count - 1);
                }
            }
        }
        return new RGA(replicaId, remaining, deleted, version);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof RGA)) return false;
        RGA that = (RGA) other;
        return nodes.equals(that.nodes) && tombstones.equals(that.tombstones);
    }

    @Override
    public int hashCode() {
        return 31 * nodes.hashCode() + tombstones.hashCode();
    }

    @Override
    public String toString() {
        return text();
    }
}
