package crdt;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 解决副本间集合增删冲突的不可变 observed-remove set（add-wins）。状态由元素到唯一
 * {@link Tag} 集合的映射和墓碑集合表示；映射只保留未被墓碑标记的 tag，
 * 且绝不保存空的 tag 集合。未 GC 时，{@code merge} 对 tag 和墓碑分别取并集、
 * 再剔除墓碑，因此是按集合包含关系定义的半格 join：并集和剔除同一批墓碑使合并
 * 满足交换、结合和幂等。并发 {@code add(x)} 与 {@code remove(x)} 时，删除只能
 * 墓碑它已观察到的 tag，未观察到的并发添加仍可见，所以添加获胜。
 *
 * <p>{@code equals}/{@code hashCode} 只比较映射和墓碑：本地 {@code replicaId}、
 * 递增计数器 {@code seq}、版本向量和墓碑时间戳不参与比较。因果稳定后可通过
 * {@link #gc(VersionVector)} 回收墓碑；副本身份必须唯一且不能在丢失历史后
 * 无条件复用，否则独立创建的相同 tag 无法区分。</p>
 *
 * @param <E> 元素类型
 */
public final class ORSet<E> implements CvRDT<ORSet<E>> {
    private final Map<E, Set<Tag>> entries;
    private final Set<Tag> tombstones;
    private final VersionVector version;
    private final Map<Tag, CausalStamp> tombstoneStamps;
    private final String replicaId;
    private final long seq;

    private ORSet(String replicaId, long seq, Map<E, Set<Tag>> entries, Set<Tag> tombstones,
                  VersionVector version, Map<Tag, CausalStamp> tombstoneStamps) {
        this.replicaId = replicaId;
        this.seq = seq;
        this.entries = entries;
        this.tombstones = tombstones;
        this.version = version;
        this.tombstoneStamps = tombstoneStamps;
    }

    public static <E> ORSet<E> empty(String replicaId) {
        if (replicaId == null || replicaId.isEmpty()) {
            throw new IllegalArgumentException("replicaId must not be empty");
        }
        return new ORSet<E>(replicaId, 0L, Collections.<E, Set<Tag>>emptyMap(),
                Collections.<Tag>emptySet(), VersionVector.empty(),
                Collections.<Tag, CausalStamp>emptyMap());
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

    public ORSet<E> add(E element) {
        if (seq == Long.MAX_VALUE) {
            throw new IllegalStateException("tag sequence exhausted");
        }
        long nextSeq = seq + 1;
        Tag tag = new Tag(replicaId, nextSeq);
        Map<E, Set<Tag>> nextEntries = new HashMap<E, Set<Tag>>(entries);
        Set<Tag> tags = new HashSet<Tag>();
        Set<Tag> existing = entries.get(element);
        if (existing != null) {
            tags.addAll(existing);
        }
        tags.add(tag);
        nextEntries.put(element, Collections.unmodifiableSet(tags));
        return new ORSet<E>(replicaId, nextSeq, Collections.unmodifiableMap(nextEntries),
                tombstones, version.increment(replicaId), tombstoneStamps);
    }

    public ORSet<E> remove(E element) {
        Set<Tag> tags = entries.get(element);
        if (tags == null) {
            return new ORSet<E>(replicaId, seq, entries, tombstones, version, tombstoneStamps);
        }
        VersionVector nextVersion = version.increment(replicaId);
        CausalStamp stamp = new CausalStamp(replicaId, nextVersion.get(replicaId));
        Map<E, Set<Tag>> nextEntries = new HashMap<E, Set<Tag>>(entries);
        nextEntries.remove(element);
        Set<Tag> nextTombstones = new HashSet<Tag>(tombstones);
        nextTombstones.addAll(tags);
        Map<Tag, CausalStamp> nextStamps = new HashMap<Tag, CausalStamp>(tombstoneStamps);
        for (Tag tag : tags) {
            CausalStamp previous = nextStamps.get(tag);
            nextStamps.put(tag, previous == null ? stamp : CausalStamp.min(previous, stamp));
        }
        return new ORSet<E>(replicaId, seq, Collections.unmodifiableMap(nextEntries),
                Collections.unmodifiableSet(nextTombstones), nextVersion,
                Collections.unmodifiableMap(nextStamps));
    }

    public boolean contains(E element) {
        return entries.containsKey(element);
    }

    public Set<E> elements() {
        return entries.keySet();
    }

    public int size() {
        return entries.size();
    }

    /**
     * 回收已被所有副本观察到的删除操作的墓碑。每个墓碑的时间戳是删除它的那次操作的
     * 因果坐标；稳定前沿覆盖该坐标时，每个副本都已收到并应用删除，因而没有副本的
     * {@code entries} 仍保留该 tag（应用墓碑时已剔除），单调递增的 tag 序号也不会
     * 重用。丢掉墓碑不会令元素复活。前沿未覆盖时绝不能回收；空前沿不会删除任何墓碑。
     *
     * <p>前沿必须代表所有存活副本真实观察到的版本。GC 后副本的内部墓碑集合可以不同，
     * {@code equals} 可能为 false，但 {@code elements()} 仍一致。与尚未 GC 的副本
     * 合并会重新带回墓碑，需要再次 GC。</p>
     *
     * @param frontier 所有存活副本的稳定前沿
     * @return 回收后的新实例
     */
    public ORSet<E> gc(VersionVector frontier) {
        if (frontier == null) {
            throw new IllegalArgumentException("frontier must not be null");
        }
        Set<Tag> remaining = null;
        Map<Tag, CausalStamp> remainingStamps = null;
        for (Tag tag : tombstones) {
            if (CausalStability.covers(frontier, tombstoneStamps.get(tag))) {
                if (remaining == null) {
                    remaining = new HashSet<Tag>(tombstones);
                    remainingStamps = new HashMap<Tag, CausalStamp>(tombstoneStamps);
                }
                remaining.remove(tag);
                remainingStamps.remove(tag);
            }
        }
        return remaining == null
                ? new ORSet<E>(replicaId, seq, entries, tombstones, version, tombstoneStamps)
                : new ORSet<E>(replicaId, seq, entries, Collections.unmodifiableSet(remaining),
                        version, Collections.unmodifiableMap(remainingStamps));
    }

    @Override
    public ORSet<E> merge(ORSet<E> other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        Map<E, Set<Tag>> joinedEntries = new HashMap<E, Set<Tag>>(entries);
        Set<Tag> joinedTombstones = new HashSet<Tag>(tombstones);
        joinedTombstones.addAll(other.tombstones);
        Map<Tag, CausalStamp> joinedStamps = new HashMap<Tag, CausalStamp>(tombstoneStamps);
        for (Map.Entry<Tag, CausalStamp> entry : other.tombstoneStamps.entrySet()) {
            CausalStamp previous = joinedStamps.get(entry.getKey());
            joinedStamps.put(entry.getKey(), previous == null ? entry.getValue()
                    : CausalStamp.min(previous, entry.getValue()));
        }

        long nextSeq = seq;
        if (replicaId.equals(other.replicaId)) {
            nextSeq = Math.max(nextSeq, other.seq);
        }
        for (Tag tag : other.tombstones) {
            if (replicaId.equals(tag.replicaId)) {
                nextSeq = Math.max(nextSeq, tag.seq);
            }
        }
        for (Map.Entry<E, Set<Tag>> entry : other.entries.entrySet()) {
            for (Tag tag : entry.getValue()) {
                if (replicaId.equals(tag.replicaId)) {
                    nextSeq = Math.max(nextSeq, tag.seq);
                }
            }
            Set<Tag> present = joinedEntries.get(entry.getKey());
            if (present == null) {
                joinedEntries.put(entry.getKey(), entry.getValue());
            } else {
                Set<Tag> union = new HashSet<Tag>(present);
                union.addAll(entry.getValue());
                joinedEntries.put(entry.getKey(), Collections.unmodifiableSet(union));
            }
        }
        for (Iterator<Map.Entry<E, Set<Tag>>> it = joinedEntries.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<E, Set<Tag>> entry = it.next();
            Set<Tag> tags = entry.getValue();
            if (Collections.disjoint(tags, joinedTombstones)) {
                continue;
            }
            Set<Tag> surviving = new HashSet<Tag>(tags);
            surviving.removeAll(joinedTombstones);
            if (surviving.isEmpty()) {
                it.remove();
            } else {
                entry.setValue(Collections.unmodifiableSet(surviving));
            }
        }
        return new ORSet<E>(replicaId, nextSeq, Collections.unmodifiableMap(joinedEntries),
                Collections.unmodifiableSet(joinedTombstones), version.merge(other.version),
                Collections.unmodifiableMap(joinedStamps));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ORSet)) {
            return false;
        }
        ORSet<?> that = (ORSet<?>) other;
        return entries.equals(that.entries) && tombstones.equals(that.tombstones);
    }

    @Override
    public int hashCode() {
        return 31 * entries.hashCode() + tombstones.hashCode();
    }

    @Override
    public String toString() {
        return "ORSet{" + "entries=" + entries + ", tombstones=" + tombstones
                + ", replicaId='" + replicaId + '\'' + ", seq=" + seq + '}';
    }

    /** 由副本身份和单调递增序号组成的单次添加的唯一标识。 */
    public static final class Tag implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String replicaId;
        private final long seq;

        public Tag(String replicaId, long seq) {
            if (replicaId == null || replicaId.isEmpty()) {
                throw new IllegalArgumentException("replicaId must not be empty");
            }
            this.replicaId = replicaId;
            this.seq = seq;
        }

        public String replicaId() {
            return replicaId;
        }

        public long seq() {
            return seq;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Tag)) {
                return false;
            }
            Tag that = (Tag) other;
            return seq == that.seq && replicaId.equals(that.replicaId);
        }

        @Override
        public int hashCode() {
            return 31 * replicaId.hashCode() + Long.hashCode(seq);
        }

        @Override
        public String toString() {
            return "Tag{" + "replicaId='" + replicaId + '\'' + ", seq=" + seq + '}';
        }
    }
}
