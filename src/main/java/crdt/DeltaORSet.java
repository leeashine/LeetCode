package crdt;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 解决 OR-Set 全量同步需反复传输全部 tag 和墓碑的问题：本地新增的 tag 与新墓碑
 * 分别保存在未确认发送缓冲中，{@link #pending()} 只传未确认的变化。
 * 复制状态由元素到存活的 {@link ORSet.Tag} 集合的映射和只增不减的墓碑集合表示；
 * 每个映射值非空且不含已墓碑的 tag。增量的两部分分别以集合并集作 join，
 * 合并后统一剔除墓碑，因此 delta 的重复、乱序、分组传递满足幂等、交换、结合律；
 * 未观察到的并发添加不会被删除（add-wins）。
 *
 * <p>{@code equals}/{@code hashCode} 只比较映射和墓碑：{@code replicaId}、
 * {@code seq} 和两个未确认发送缓冲只属于本地身份及发送状态，不参与复制状态的相等判断。
 * 发送方必须在确认送达后才 {@link #ack()}，否则丢包可能导致更新永久丢失；
 * 墓碑持续增长，只有确认删除因果稳定后才可以安全回收。副本身份必须唯一，
 * 且不能在丢失历史后无条件复用，否则 tag 可能冲突。</p>
 *
 * @param <E> 元素类型
 */
public final class DeltaORSet<E> implements CvRDT<DeltaORSet<E>> {
    private final Map<E, Set<ORSet.Tag>> entries;
    private final Set<ORSet.Tag> tombstones;
    private final String replicaId;
    private final long seq;
    private final Set<ORSet.Tag> unackedAdded;
    private final Set<ORSet.Tag> unackedRemoved;

    private DeltaORSet(String replicaId, long seq, Map<E, Set<ORSet.Tag>> entries,
                       Set<ORSet.Tag> tombstones, Set<ORSet.Tag> unackedAdded,
                       Set<ORSet.Tag> unackedRemoved) {
        this.replicaId = replicaId;
        this.seq = seq;
        this.entries = entries;
        this.tombstones = tombstones;
        this.unackedAdded = unackedAdded;
        this.unackedRemoved = unackedRemoved;
    }

    public static <E> DeltaORSet<E> empty(String replicaId) {
        if (replicaId == null || replicaId.isEmpty()) {
            throw new IllegalArgumentException("replicaId must not be empty");
        }
        return new DeltaORSet<E>(replicaId, 0L, Collections.<E, Set<ORSet.Tag>>emptyMap(),
                Collections.<ORSet.Tag>emptySet(), Collections.<ORSet.Tag>emptySet(),
                Collections.<ORSet.Tag>emptySet());
    }

    public String replicaId() {
        return replicaId;
    }

    public DeltaORSet<E> add(E element) {
        if (seq == Long.MAX_VALUE) {
            throw new IllegalStateException("tag sequence exhausted");
        }
        long nextSeq = seq + 1;
        ORSet.Tag tag = new ORSet.Tag(replicaId, nextSeq);
        Map<E, Set<ORSet.Tag>> nextEntries = new HashMap<E, Set<ORSet.Tag>>(entries);
        Set<ORSet.Tag> tags = new HashSet<ORSet.Tag>();
        Set<ORSet.Tag> existing = entries.get(element);
        if (existing != null) {
            tags.addAll(existing);
        }
        tags.add(tag);
        nextEntries.put(element, Collections.unmodifiableSet(tags));
        Set<ORSet.Tag> nextAdded = new HashSet<ORSet.Tag>(unackedAdded);
        nextAdded.add(tag);
        return new DeltaORSet<E>(replicaId, nextSeq, Collections.unmodifiableMap(nextEntries),
                tombstones, Collections.unmodifiableSet(nextAdded), unackedRemoved);
    }

    public DeltaORSet<E> remove(E element) {
        Set<ORSet.Tag> tags = entries.get(element);
        if (tags == null) {
            return this;
        }
        Map<E, Set<ORSet.Tag>> nextEntries = new HashMap<E, Set<ORSet.Tag>>(entries);
        nextEntries.remove(element);
        Set<ORSet.Tag> nextTombstones = new HashSet<ORSet.Tag>(tombstones);
        nextTombstones.addAll(tags);
        Set<ORSet.Tag> nextAdded = new HashSet<ORSet.Tag>(unackedAdded);
        nextAdded.removeAll(tags);
        Set<ORSet.Tag> nextRemoved = new HashSet<ORSet.Tag>(unackedRemoved);
        nextRemoved.addAll(tags);
        return new DeltaORSet<E>(replicaId, seq, Collections.unmodifiableMap(nextEntries),
                Collections.unmodifiableSet(nextTombstones), Collections.unmodifiableSet(nextAdded),
                Collections.unmodifiableSet(nextRemoved));
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

    /** 读取尚未确认送达的变化，不清除发送缓冲。 */
    public Delta<E> pending() {
        if (unackedAdded.isEmpty() && unackedRemoved.isEmpty()) {
            return Delta.empty();
        }
        Map<E, Set<ORSet.Tag>> added = new HashMap<E, Set<ORSet.Tag>>();
        if (!unackedAdded.isEmpty()) {
            for (Map.Entry<E, Set<ORSet.Tag>> entry : entries.entrySet()) {
                if (!Collections.disjoint(entry.getValue(), unackedAdded)) {
                    Set<ORSet.Tag> tags = new HashSet<ORSet.Tag>(entry.getValue());
                    tags.retainAll(unackedAdded);
                    added.put(entry.getKey(), Collections.unmodifiableSet(tags));
                }
            }
        }
        if (added.isEmpty() && unackedRemoved.isEmpty()) {
            return Delta.empty();
        }
        return new Delta<E>(Collections.unmodifiableMap(added), unackedRemoved);
    }

    /** 确认发送缓冲中的变化已送达；不改变复制状态。 */
    public DeltaORSet<E> ack() {
        return new DeltaORSet<E>(replicaId, seq, entries, tombstones,
                Collections.<ORSet.Tag>emptySet(), Collections.<ORSet.Tag>emptySet());
    }

    /** 并入增量而不改动本地未确认发送缓冲。 */
    public DeltaORSet<E> apply(Delta<E> delta) {
        if (delta == null) {
            throw new IllegalArgumentException("delta must not be null");
        }
        return join(delta.added, delta.removed, seq);
    }

    /** 全量同步：合并复制状态，保留接收方身份与发送缓冲。 */
    @Override
    public DeltaORSet<E> merge(DeltaORSet<E> other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        long nextSeq = replicaId.equals(other.replicaId) ? Math.max(seq, other.seq) : seq;
        return join(other.entries, other.tombstones, nextSeq);
    }

    private DeltaORSet<E> join(Map<E, Set<ORSet.Tag>> incoming,
                               Set<ORSet.Tag> removed, long nextSeq) {
        Map<E, Set<ORSet.Tag>> joinedEntries = new HashMap<E, Set<ORSet.Tag>>(entries);
        Set<ORSet.Tag> joinedTombstones = new HashSet<ORSet.Tag>(tombstones);
        joinedTombstones.addAll(removed);
        for (ORSet.Tag tag : removed) {
            if (replicaId.equals(tag.replicaId())) {
                nextSeq = Math.max(nextSeq, tag.seq());
            }
        }
        for (Map.Entry<E, Set<ORSet.Tag>> entry : incoming.entrySet()) {
            for (ORSet.Tag tag : entry.getValue()) {
                if (replicaId.equals(tag.replicaId())) {
                    nextSeq = Math.max(nextSeq, tag.seq());
                }
            }
            Set<ORSet.Tag> present = joinedEntries.get(entry.getKey());
            if (present == null) {
                joinedEntries.put(entry.getKey(), entry.getValue());
            } else {
                Set<ORSet.Tag> union = new HashSet<ORSet.Tag>(present);
                union.addAll(entry.getValue());
                joinedEntries.put(entry.getKey(), Collections.unmodifiableSet(union));
            }
        }
        for (Iterator<Map.Entry<E, Set<ORSet.Tag>>> it = joinedEntries.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<E, Set<ORSet.Tag>> entry = it.next();
            Set<ORSet.Tag> tags = entry.getValue();
            if (Collections.disjoint(tags, joinedTombstones)) {
                continue;
            }
            Set<ORSet.Tag> surviving = new HashSet<ORSet.Tag>(tags);
            surviving.removeAll(joinedTombstones);
            if (surviving.isEmpty()) {
                it.remove();
            } else {
                entry.setValue(Collections.unmodifiableSet(surviving));
            }
        }
        return new DeltaORSet<E>(replicaId, nextSeq, Collections.unmodifiableMap(joinedEntries),
                Collections.unmodifiableSet(joinedTombstones), unackedAdded, unackedRemoved);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DeltaORSet)) {
            return false;
        }
        DeltaORSet<?> that = (DeltaORSet<?>) other;
        return entries.equals(that.entries) && tombstones.equals(that.tombstones);
    }

    @Override
    public int hashCode() {
        return 31 * entries.hashCode() + tombstones.hashCode();
    }

    @Override
    public String toString() {
        return "DeltaORSet{" + "entries=" + entries + ", tombstones=" + tombstones
                + ", replicaId='" + replicaId + '\'' + ", seq=" + seq
                + ", unackedAdded=" + unackedAdded + ", unackedRemoved=" + unackedRemoved + '}';
    }

    /**
     * 新增 tag 与墓碑组成的不可变增量，两部分以并集作半格 join，因而满足幂等、
     * 交换和结合律。{@code added} 不保存空的 tag 集合。
     *
     * @param <E> 元素类型
     */
    public static final class Delta<E> {
        private final Map<E, Set<ORSet.Tag>> added;
        private final Set<ORSet.Tag> removed;

        private Delta(Map<E, Set<ORSet.Tag>> added, Set<ORSet.Tag> removed) {
            this.added = added;
            this.removed = removed;
        }

        public static <E> Delta<E> empty() {
            return new Delta<E>(Collections.<E, Set<ORSet.Tag>>emptyMap(),
                    Collections.<ORSet.Tag>emptySet());
        }

        public Map<E, Set<ORSet.Tag>> added() {
            return added;
        }

        public Set<ORSet.Tag> removed() {
            return removed;
        }

        public Delta<E> merge(Delta<E> other) {
            if (other == null) {
                throw new IllegalArgumentException("other must not be null");
            }
            Map<E, Set<ORSet.Tag>> joined = new HashMap<E, Set<ORSet.Tag>>(added);
            for (Map.Entry<E, Set<ORSet.Tag>> entry : other.added.entrySet()) {
                Set<ORSet.Tag> present = joined.get(entry.getKey());
                if (present == null) {
                    joined.put(entry.getKey(), entry.getValue());
                } else {
                    Set<ORSet.Tag> union = new HashSet<ORSet.Tag>(present);
                    union.addAll(entry.getValue());
                    joined.put(entry.getKey(), Collections.unmodifiableSet(union));
                }
            }
            Set<ORSet.Tag> removedUnion = new HashSet<ORSet.Tag>(removed);
            removedUnion.addAll(other.removed);
            return new Delta<E>(Collections.unmodifiableMap(joined),
                    Collections.unmodifiableSet(removedUnion));
        }

        public boolean isEmpty() {
            return added.isEmpty() && removed.isEmpty();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Delta)) {
                return false;
            }
            Delta<?> that = (Delta<?>) other;
            return added.equals(that.added) && removed.equals(that.removed);
        }

        @Override
        public int hashCode() {
            return 31 * added.hashCode() + removed.hashCode();
        }

        @Override
        public String toString() {
            return "Delta{" + "added=" + added + ", removed=" + removed + '}';
        }
    }
}
