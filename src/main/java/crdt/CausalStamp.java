package crdt;

/**
 * 因果时间戳：标识"某个副本的第 seq 次本地操作"。
 *
 * <p>墓碑（tombstone）本身只是一个"这个元素/字符已经死了"的标记，但它<b>什么时候可以安全丢掉</b>
 * 取决于一个因果问题：删除操作有没有被所有副本都观察到？只靠一个计数器或一个集合是答不出来的，
 * 所以每个墓碑都要记住"是哪次操作删掉它的"。</p>
 *
 * <p>{@code CausalStamp} 就是这条因果坐标。配合 {@link CausalStability} 计算出的稳定前沿，
 * 就能判断一个墓碑是否已"人人可见"，从而安全回收——这是墓碑不会无限膨胀的前提。</p>
 *
 * <p>自然序：先比 {@code seq}，再比 {@code replicaId}。这个全序只用于让不同副本对"同一个墓碑由谁删除"
 * 达成<b>确定性</b>的一致选择（取较小者），本身不代表任何业务先后。</p>
 */
public final class CausalStamp implements Comparable<CausalStamp> {

    private final String replicaId;
    private final long seq;

    public CausalStamp(String replicaId, long seq) {
        if (replicaId == null || replicaId.isEmpty()) {
            throw new IllegalArgumentException("replicaId must not be null or empty");
        }
        if (seq <= 0) {
            throw new IllegalArgumentException("seq must be positive");
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
    public int compareTo(CausalStamp other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null");
        }
        int bySeq = Long.compare(seq, other.seq);
        return bySeq != 0 ? bySeq : replicaId.compareTo(other.replicaId);
    }

    /** 取两个时间戳里较小的一个：用于"同一墓碑被多个副本并发删除"时的确定性收敛。 */
    public static CausalStamp min(CausalStamp left, CausalStamp right) {
        return left.compareTo(right) <= 0 ? left : right;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CausalStamp)) {
            return false;
        }
        CausalStamp that = (CausalStamp) other;
        return seq == that.seq && replicaId.equals(that.replicaId);
    }

    @Override
    public int hashCode() {
        return 31 * replicaId.hashCode() + Long.hashCode(seq);
    }

    @Override
    public String toString() {
        return "CausalStamp{" + replicaId + "#" + seq + '}';
    }
}
