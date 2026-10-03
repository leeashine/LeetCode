package crdt;

import java.util.List;

/**
 * 因果稳定（causal stability）：墓碑回收的唯一安全判据。
 *
 * <p><b>问题</b>：ORSet 的墓碑、RGA 的删除标记都是"只增不减"的，这是它们能收敛的原因，
 * 也是它们的内存会无限膨胀的原因。想删掉一个墓碑，必须确认<b>没有任何副本还认为那条记录活着</b>，
 * 否则删掉墓碑会让死数据复活（resurrection）——比内存泄漏严重得多。</p>
 *
 * <p><b>判据</b>：如果每个副本都已经观察到"删除操作"本身，那么这个删除就已经尘埃落定，
 * 它对应的墓碑可以安全回收。于是需要一个"人人可见"的因果坐标，也就是稳定前沿：</p>
 *
 * <pre>
 *   frontier = 所有副本 version 向量的逐分量最小值
 * </pre>
 *
 * <p>逐分量取 min 的含义：对副本 Q 的第 n 次操作，只有当<b>每一个副本</b>的 version 里
 * {@code Q >= n} 时，才认为它稳定。任何一个副本还没看到，前沿就会把它挡在门外。</p>
 *
 * <p><b>前置条件</b>：{@code observed} 必须来自当前<b>所有存活副本</b>的真实 version。
 * 新副本加入（version 全 0）会让前沿退回到 0，此时不会再有任何 GC 发生——
 * 这是保守但安全的行为；副本成员变更需要单独的机制处理。</p>
 */
public final class CausalStability {

    private CausalStability() {
    }

    /**
     * 计算稳定前沿：所有副本 version 向量的逐分量最小值。
     *
     * @param observed 所有存活副本的 version 向量；为空列表时返回空向量（= 什么都不稳定）
     * @return 稳定前沿
     */
    public static VersionVector frontier(List<VersionVector> observed) {
        if (observed == null) {
            throw new IllegalArgumentException("observed must not be null");
        }
        if (observed.isEmpty()) {
            return VersionVector.empty();
        }
        VersionVector result = observed.get(0);
        for (int i = 1; i < observed.size(); i++) {
            VersionVector other = observed.get(i);
            if (other == null) {
                throw new IllegalArgumentException("observed must not contain null");
            }
            result = componentMin(result, other);
        }
        return result;
    }

    /** 前沿是否已经覆盖某个因果时间戳（即该操作是否人人可见）。 */
    public static boolean covers(VersionVector frontier, CausalStamp stamp) {
        if (frontier == null || stamp == null) {
            throw new IllegalArgumentException("frontier and stamp must not be null");
        }
        return frontier.get(stamp.replicaId()) >= stamp.seq();
    }

    private static VersionVector componentMin(VersionVector left, VersionVector right) {
        // 逐分量取 min：某个副本缺某个 key 时 get() 返回 0，自然成为下界
        java.util.TreeSet<String> keys = new java.util.TreeSet<String>();
        keys.addAll(left.asMap().keySet());
        keys.addAll(right.asMap().keySet());
        java.util.Map<String, Long> min = new java.util.LinkedHashMap<String, Long>();
        for (String key : keys) {
            long value = Math.min(left.get(key), right.get(key));
            if (value > 0L) {
                min.put(key, value);
            }
        }
        return VersionVector.of(min);
    }
}
