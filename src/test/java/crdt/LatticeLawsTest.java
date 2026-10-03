package crdt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * CRDT 的"及格线"测试：把每个 CRDT 当作半格元素，直接验证 join 的三条代数律。
 *
 * <p>这是一切收敛性讨论的地基——只要下面的断言成立，
 * "消息乱序 / 重复 / 丢失" 就不再是正确性问题，而只是性能问题。
 * 反过来，任何一个断言挂掉，该类就不是 CRDT，无论它的业务语义看起来多合理。</p>
 *
 * <p>三律的含义（{@code ⊔} 记作 {@code merge}）：</p>
 * <pre>
 *   幂等：a ⊔ a = a                     → 重复投递安全
 *   交换：a ⊔ b = b ⊔ a                 → 乱序到达安全
 *   结合：(a ⊔ b) ⊔ c = a ⊔ (b ⊔ c)     → 经多跳/中转同步安全
 * </pre>
 * 再加上单位元（bottom）{@code e ⊔ a = a}，说明"什么都没发生的副本"不会污染同步结果。
 */
public class LatticeLawsTest {

    /** 通用定律检查：identity 必须是 merge 的单位元。 */
    private static <T extends CvRDT<T>> void assertLatticeLaws(String name, T a, T b, T c, T identity) {
        assertEquals(name + " 违反幂等律: a ⊔ a != a", a, a.merge(a));
        assertEquals(name + " 违反幂等律: b ⊔ b != b", b, b.merge(b));
        // 交换律必须对**每一对**都成立：只测一对会漏掉"时间戳相同时靠 replicaId 裁决"这类分支
        assertEquals(name + " 违反交换律: a ⊔ b != b ⊔ a", a.merge(b), b.merge(a));
        assertEquals(name + " 违反交换律: b ⊔ c != c ⊔ b", b.merge(c), c.merge(b));
        assertEquals(name + " 违反交换律: a ⊔ c != c ⊔ a", a.merge(c), c.merge(a));
        assertEquals(name + " 违反结合律: (a ⊔ b) ⊔ c != a ⊔ (b ⊔ c)", a.merge(b).merge(c), a.merge(b.merge(c)));
        assertEquals(name + " 违反结合律: (b ⊔ c) ⊔ a != b ⊔ (c ⊔ a)", b.merge(c).merge(a), b.merge(c.merge(a)));
        assertEquals(name + " 违反单位元(右): a ⊔ e != a", a, a.merge(identity));
        assertEquals(name + " 违反单位元(左): e ⊔ a != a", a, identity.merge(a));

        // 单位元自身也必须与"任意状态"交换，否则同步会引入伪变更
        assertEquals(name + " 单位元不交换: e ⊔ b != b ⊔ e", identity.merge(b), b.merge(identity));
    }

    @Test
    public void gCounterIsALattice() {
        GCounter a = new GCounter("A").increment(2);
        GCounter b = new GCounter("B").increment(1);
        GCounter c = new GCounter("C").increment(5).merge(new GCounter("A").increment(1));
        GCounter bottom = new GCounter("Z");
        assertLatticeLaws("GCounter", a, b, c, bottom);
        assertEquals(8L, a.merge(b).merge(c).value());
    }

    @Test
    public void pnCounterIsALattice() {
        PNCounter a = new PNCounter("A").increment(3);
        PNCounter b = new PNCounter("B").decrement(2);
        PNCounter c = new PNCounter("C").increment(1).decrement(4);
        PNCounter bottom = new PNCounter("Z");
        assertLatticeLaws("PNCounter", a, b, c, bottom);
        assertEquals(-2L, a.merge(b).merge(c).value());
    }

    @Test
    public void lwwRegisterIsALattice() {
        LWWRegister<String> a = LWWRegister.<String>empty().set("a", 1L, "A");
        LWWRegister<String> b = LWWRegister.<String>empty().set("b", 2L, "B");
        // 与 b 时间戳相同、replicaId 不同：用于压测 tie-break 分支的结合律
        LWWRegister<String> c = LWWRegister.<String>empty().set("c", 2L, "A");
        LWWRegister<String> bottom = LWWRegister.empty();
        assertLatticeLaws("LWWRegister", a, b, c, bottom);
        assertEquals("b", a.merge(b).merge(c).value());
    }

    @Test
    public void mvRegisterIsALattice() {
        MVRegister<String> a = MVRegister.<String>empty().assign("a", "A");
        MVRegister<String> b = MVRegister.<String>empty().assign("b", "B");
        MVRegister<String> c = MVRegister.<String>empty().assign("a", "A").assign("c", "C");
        MVRegister<String> bottom = MVRegister.empty();
        assertLatticeLaws("MVRegister", a, b, c, bottom);

        // c 因果上覆盖了 a：合并后 a 的值应当被支配掉，只剩 b 与 c 两个并发值
        assertEquals(2, a.merge(b).merge(c).size());
    }

    @Test
    public void orSetIsALattice() {
        ORSet<String> a = ORSet.<String>empty("A").add("x").add("y");
        ORSet<String> b = ORSet.<String>empty("B").add("x").remove("x").add("z");
        ORSet<String> c = ORSet.<String>empty("C").add("x");
        ORSet<String> bottom = ORSet.empty("Z");
        assertLatticeLaws("ORSet", a, b, c, bottom);

        // add-wins：b 的 remove 只墓碑了它自己看到的 tag，a/c 的并发 tag 让 x 存活；
        // b 自己新增的 z 也不受 a/c 影响
        ORSet<String> merged = a.merge(b).merge(c);
        assertTrue("x 应因并发 add 而存活", merged.contains("x"));
        assertTrue(merged.contains("y"));
        assertTrue(merged.contains("z"));
    }

    @Test
    public void rgaIsALattice() {
        RGA a = new RGA("A").insert(0, 'a').insert(1, 'b');
        RGA b = new RGA("B").insert(0, 'x');
        RGA c = new RGA("C").insert(0, 'q').insert(0, 'p').delete(1);
        RGA bottom = new RGA("Z");
        assertLatticeLaws("RGA", a, b, c, bottom);

        // 根节点下按 (timestamp 降序, replicaId 降序)：p(2,C) → q(1,C,墓碑) → x(1,B) → a(1,A) → a 的孩子 b
        assertEquals("pxab", a.merge(b).merge(c).text());
    }
}
