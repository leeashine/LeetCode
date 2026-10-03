package crdt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * 墓碑 GC 的安全性测试：GC 的目标是省内存，但**绝不能复活已删除的数据**。
 *
 * <p>墓碑回收的唯一判据是因果稳定（{@link CausalStability}）：只有当"删除操作本身"
 * 已被<b>所有存活副本</b>观察到，对应的墓碑才能丢。本测试验证三件事：</p>
 *
 * <ol>
 *   <li><b>不该收的绝不动</b>：只要还有一个副本没同步到那次删除，稳定前沿就覆盖不到它，GC 必须什么都不删。</li>
 *   <li><b>可观察语义不变</b>：GC 前后 {@code elements()} / {@code text()} / {@code size()} 必须完全一致。</li>
 *   <li><b>不复活</b>：GC 之后与"还没 GC 的老副本"合并，墓碑会被带回来（这是取舍），
 *       但被删除的元素/字符绝不能重新出现。</li>
 * </ol>
 *
 * <p>第 3 条是安全性的核心：一个已经 GC 的副本并不知道哪些墓碑被丢过，
 * 它能不复活，靠的是"老副本手里墓碑和记录是一起存在的"——所以 merge 时墓碑也会一起回来。</p>
 */
public class TombstoneGcTest {

    private static VersionVector frontierOf(List<VersionVector> versions) {
        return CausalStability.frontier(versions);
    }

    @Test
    public void orSetGcIsBlockedUntilEveryReplicaObservedTheDelete() {
        ORSet<String> r0 = ORSet.<String>empty("R0");
        for (int i = 0; i < 20; i++) {
            r0 = r0.add("e" + i);
        }
        for (int i = 0; i < 10; i++) {
            r0 = r0.remove("e" + i);
        }
        assertEquals(10, r0.tombstoneCount());
        assertEquals(10, r0.size());

        // R1 还没同步过任何东西 → 前沿里 R0 的分量是 0 → 一个墓碑都不能收
        ORSet<String> unsynced = ORSet.<String>empty("R1");
        VersionVector notStable = frontierOf(Arrays.asList(r0.version(), unsynced.version()));
        ORSet<String> untouched = r0.gc(notStable);
        assertEquals(10, untouched.tombstoneCount());
        assertEquals(r0.elements(), untouched.elements());

        // R1、R2 都同步过之后，删除操作才"人人可见"
        ORSet<String> r1 = ORSet.<String>empty("R1").merge(r0);
        ORSet<String> r2 = ORSet.<String>empty("R2").merge(r0);
        VersionVector stable = frontierOf(Arrays.asList(r0.version(), r1.version(), r2.version()));
        ORSet<String> collected = r0.gc(stable);

        assertEquals(0, collected.tombstoneCount());
        assertEquals("GC 不能改变可观察语义", r0.elements(), collected.elements());
        assertEquals(10, collected.size());
    }

    @Test
    public void orSetGcDoesNotResurrectWhenMergingAStaleReplica() {
        ORSet<String> r0 = ORSet.<String>empty("R0");
        for (int i = 0; i < 5; i++) {
            r0 = r0.add("x" + i);
        }
        for (int i = 0; i < 5; i++) {
            r0 = r0.remove("x" + i);
        }
        assertEquals(0, r0.size());
        ORSet<String> stale = ORSet.<String>empty("R1").merge(r0);

        ORSet<String> collected = r0.gc(frontierOf(Arrays.asList(r0.version(), stale.version())));
        assertEquals(0, collected.tombstoneCount());

        // 与"还没 GC 的老副本"合并：墓碑被带回来（GC 部分失效），但元素绝不复活
        ORSet<String> merged = collected.merge(stale);
        assertTrue("老副本的墓碑会回来，这是文档里写明的取舍", merged.tombstoneCount() > 0);
        assertEquals("删除过的元素绝不能复活", 0, merged.size());
        assertEquals(collected.elements(), merged.elements());
    }

    @Test
    public void rgaGcKeepsTextAndCollapsesDeadTail() {
        RGA r0 = new RGA("R0");
        String base = "abcdef";
        for (int i = 0; i < base.length(); i++) {
            r0 = r0.insert(r0.size(), base.charAt(i));
        }
        r0 = r0.delete(5).delete(4).delete(3); // 删掉尾部的 f、e、d
        assertEquals("abc", r0.text());
        assertEquals(3, r0.tombstoneCount());

        // 还有一个副本没同步 → 不能回收
        RGA unsynced = new RGA("R2");
        RGA untouched = r0.gc(frontierOf(Arrays.asList(r0.version(), unsynced.version())));
        assertEquals(3, untouched.tombstoneCount());

        RGA r1 = new RGA("R1").merge(r0);
        RGA collected = r0.gc(frontierOf(Arrays.asList(r0.version(), r1.version())));
        assertEquals("GC 不能改变文本", "abc", collected.text());
        assertEquals(3, collected.size());
        assertEquals("f→e→d 是链式死叶子，一次 GC 应收完", 0, collected.tombstoneCount());

        // 与未 GC 的老副本合并：节点和墓碑一起回来 → 文本仍然正确
        RGA merged = collected.merge(r1);
        assertEquals("abc", merged.text());
        assertEquals(3, merged.size());
    }

    @Test
    public void rgaGcCannotCollectTombstonesThatStillAnchorLiveText() {
        RGA r0 = new RGA("R0");
        String base = "abcdef";
        for (int i = 0; i < base.length(); i++) {
            r0 = r0.insert(r0.size(), base.charAt(i));
        }
        r0 = r0.delete(1).delete(1).delete(1); // 删掉中段的 b、c、d
        assertEquals("aef", r0.text());
        assertEquals(3, r0.tombstoneCount());

        RGA collected = r0.gc(frontierOf(Arrays.asList(r0.version(), r0.version())));

        // 关键限制：b/c/d 是 e、f 的结构锚点（不是叶子），一旦移除，
        // 它们的孩子就得重新挂到祖父下并重新排序 → 总顺序会变。
        // 所以 RGA 的墓碑 GC 只能回收"死叶子"，中段删除的墓碑必须保留。
        assertEquals("中段墓碑是结构锚点，不能回收", 3, collected.tombstoneCount());
        assertEquals("aef", collected.text());
        assertEquals(3, collected.size());
    }

    @Test
    public void frontierIsTheComponentwiseMinimumOfAllReplicas() {
        VersionVector a = VersionVector.empty().increment("A").increment("A").increment("B");
        VersionVector b = VersionVector.empty().increment("A");
        VersionVector frontier = frontierOf(Arrays.asList(a, b));

        assertEquals(1L, frontier.get("A")); // min(2, 1)
        assertEquals(0L, frontier.get("B")); // b 没见过 B 的任何操作 → 不稳定
        assertTrue(CausalStability.covers(frontier, new CausalStamp("A", 1)));
        assertTrue(!CausalStability.covers(frontier, new CausalStamp("A", 2)));
        assertTrue(!CausalStability.covers(frontier, new CausalStamp("B", 1)));
        assertEquals(VersionVector.empty(), frontierOf(Arrays.asList()));
    }
}
