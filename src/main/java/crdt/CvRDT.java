package crdt;

/**
 * 状态型 CRDT（CvRDT, convergent replicated data type）的收敛契约。
 *
 * <p>核心思想：把每个副本的状态当作一个<b>半格（join-semilattice）</b>中的元素，
 * 副本之间同步时不做"覆盖"，只做半格上的<b>并（join / least upper bound）</b>，
 * 记作 {@code merge}。只要 {@code merge} 同时满足三条代数律，
 * 那么无论消息以什么顺序到达、重复多少次、丢多少次，只要最终所有副本两两同步过一次，
 * 它们的状态必然收敛到同一个值：</p>
 *
 * <ul>
 *   <li><b>幂等</b> {@code a.merge(a) == a}：重复投递天然安全，不需要去重。</li>
 *   <li><b>交换</b> {@code a.merge(b) == b.merge(a)}：乱序到达天然安全，不需要排序缓冲。</li>
 *   <li><b>结合</b> {@code (a.merge(b)).merge(c) == a.merge(b.merge(c))}：中转/级联同步天然安全。</li>
 * </ul>
 *
 * <p>这三条正好是"消息可能重复、乱序、经多跳转发"的分布式网络最需要的东西，
 * 因此 CRDT 是 AP 系统（可用性 + 分区容忍）下"无需共识即可收敛"的基础。</p>
 *
 * <p>本接口只约束语义，不约束实现：实现类一律<b>不可变</b>，
 * {@code merge} 返回新实例，从而让上述定律可以直接用 {@code equals} 断言。</p>
 *
 * @param <T> 自身的类型（自限定泛型），保证 {@code merge} 的两边是同一种 CRDT
 */
public interface CvRDT<T extends CvRDT<T>> {

    /**
     * 半格上的并：计算当前状态与 {@code other} 的最小上界。
     *
     * <p>实现要求：不修改 {@code this}，也不修改 {@code other}；失败时抛 {@link IllegalArgumentException}。
     *
     * @param other 远端副本状态
     * @return 合并后的新状态
     */
    T merge(T other);
}
