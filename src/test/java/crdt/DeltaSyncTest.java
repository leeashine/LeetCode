package crdt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;

/**
 * delta-state 同步的收敛性测试：证明"只传增量"和"传全量"一样安全。
 *
 * <p>与 {@link ConvergenceTest} 的区别只有一个：同步时不再交换整个状态，
 * 而是走 {@code pending() → apply(delta) → ack()} 这条增量通道。</p>
 *
 * <p><b>本测试采用的通道模型：广播 + 全员确认。</b>
 * 每个副本把自己的 {@code pending()} 增量广播给所有其它副本，只有当这一轮<b>每一个</b>
 * 副本都成功收到（没有丢包）时才 {@code ack()} 清缓冲；否则下一轮继续重发。
 * 这与真实 gossip 系统的"至少一次广播 + 幂等接收"一致。</p>
 *
 * <p>注意这个模型为什么必要：一个<b>中转</b>副本不会把收到的别人的增量再转发出去
 * （它只发自己 {@code pending()} 里的本地贡献），所以"随便发一发"的拓扑下信息只能走一跳。
 * 要支持树形/链式拓扑，必须在同步时交换因果上下文（版本向量），
 * 算出"对方缺哪些 delta"再补发——那是另一层协议，不属于 CRDT 本身。</p>
 */
public class DeltaSyncTest {

    private static final int REPLICAS = 5;
    private static final int SEEDS = 15;
    private static final int LOCAL_OPS = 60;
    private static final int ROUNDS = 200;
    private static final double LOSS = 0.35;
    private static final double DUPLICATE = 0.4;

    // ---------------------------------------------------------------- GCounter

    @Test
    public void gCounterDeltaSyncConverges() {
        for (long seed = 1; seed <= SEEDS; seed++) {
            Random rnd = new Random(seed);
            final long[] expected = new long[1];
            List<DeltaGCounter> replicas = new ArrayList<DeltaGCounter>();
            for (int i = 0; i < REPLICAS; i++) {
                replicas.add(new DeltaGCounter("R" + i));
            }

            for (int i = 0; i < LOCAL_OPS; i++) {
                int idx = rnd.nextInt(REPLICAS);
                long d = 1 + rnd.nextInt(3);
                expected[0] += d;
                replicas.set(idx, replicas.get(idx).increment(d));
            }

            for (int round = 0; round < ROUNDS; round++) {
                for (int from = 0; from < REPLICAS; from++) {
                    DeltaGCounter source = replicas.get(from);
                    DeltaGCounter.Delta delta = source.pending();
                    if (delta.isEmpty()) {
                        continue;
                    }
                    boolean deliveredToAll = true;
                    for (int to = 0; to < REPLICAS; to++) {
                        if (to == from) {
                            continue;
                        }
                        if (rnd.nextDouble() < LOSS) {
                            deliveredToAll = false; // 丢包：不 apply，也不允许 ack
                            continue;
                        }
                        DeltaGCounter target = replicas.get(to).apply(delta);
                        if (rnd.nextDouble() < DUPLICATE) {
                            target = target.apply(delta); // 重复投递
                        }
                        replicas.set(to, target);
                    }
                    if (deliveredToAll) {
                        replicas.set(from, source.ack());
                    }
                }
            }

            for (int i = 0; i < REPLICAS; i++) {
                assertTrue("GCounter delta(种子 " + seed + ") 副本" + i + " 仍有未确认增量",
                        replicas.get(i).pending().isEmpty());
            }
            for (int i = 1; i < REPLICAS; i++) {
                assertEquals("GCounter delta(种子 " + seed + ") 未收敛", replicas.get(0), replicas.get(i));
            }
            assertEquals("GCounter delta(种子 " + seed + ") 总计数错误", expected[0], replicas.get(0).value());
        }
    }

    // ------------------------------------------------------------------ ORSet

    @Test
    public void orSetDeltaSyncConverges() {
        for (long seed = 1; seed <= SEEDS; seed++) {
            Random rnd = new Random(seed);
            List<DeltaORSet<String>> replicas = new ArrayList<DeltaORSet<String>>();
            for (int i = 0; i < REPLICAS; i++) {
                replicas.add(DeltaORSet.<String>empty("R" + i));
            }

            for (int i = 0; i < LOCAL_OPS; i++) {
                int idx = rnd.nextInt(REPLICAS);
                String element = "e" + rnd.nextInt(6);
                DeltaORSet<String> state = replicas.get(idx);
                replicas.set(idx, rnd.nextInt(3) == 0 ? state.remove(element) : state.add(element));
            }

            for (int round = 0; round < ROUNDS; round++) {
                for (int from = 0; from < REPLICAS; from++) {
                    DeltaORSet<String> source = replicas.get(from);
                    DeltaORSet.Delta<String> delta = source.pending();
                    if (delta.isEmpty()) {
                        continue;
                    }
                    boolean deliveredToAll = true;
                    for (int to = 0; to < REPLICAS; to++) {
                        if (to == from) {
                            continue;
                        }
                        if (rnd.nextDouble() < LOSS) {
                            deliveredToAll = false;
                            continue;
                        }
                        DeltaORSet<String> target = replicas.get(to).apply(delta);
                        if (rnd.nextDouble() < DUPLICATE) {
                            target = target.apply(delta);
                        }
                        replicas.set(to, target);
                    }
                    if (deliveredToAll) {
                        replicas.set(from, source.ack());
                    }
                }
            }

            for (int i = 0; i < REPLICAS; i++) {
                assertTrue("ORSet delta(种子 " + seed + ") 副本" + i + " 仍有未确认增量",
                        replicas.get(i).pending().isEmpty());
            }
            for (int i = 1; i < REPLICAS; i++) {
                assertEquals("ORSet delta(种子 " + seed + ") 未收敛", replicas.get(0), replicas.get(i));
            }
            assertTrue("ORSet delta(种子 " + seed + ") 元素集合不应为空", replicas.get(0).size() > 0);
        }
    }

    // ------------------------------------------------- 增量到底小在哪里

    @Test
    public void gCounterDeltaStaysConstantSize() {
        DeltaGCounter counter = new DeltaGCounter("A");
        for (int i = 0; i < 1000; i++) {
            counter = counter.increment();
        }
        assertEquals(1000L, counter.value());
        // 关键：1000 次本地自增，未确认增量仍然只有 1 个条目（本地分量的绝对累计值），
        // 而不是 1000 条操作日志。这正是 delta-state 与 op-based CRDT 的区别。
        assertEquals(1, counter.pending().counts().size());
        assertEquals(1000L, counter.pending().counts().get("A").longValue());

        DeltaGCounter merged = counter.merge(new DeltaGCounter("B").increment(7));
        assertEquals(2, merged.state().size());           // 全量状态有 2 个分量
        assertEquals(1, merged.pending().counts().size()); // 增量仍然只有 1 个

        assertTrue(counter.ack().pending().isEmpty());
    }

    @Test
    public void orSetDeltaIsEmptyWhenIdle() {
        DeltaORSet<String> local = DeltaORSet.<String>empty("A").add("x").add("y").remove("x");
        DeltaORSet.Delta<String> delta = local.pending();
        assertFalse(delta.isEmpty());
        assertEquals(1, delta.added().size());   // 只有 y 的新 tag 需要发
        assertEquals(1, delta.removed().size()); // x 的 tag 已被墓碑

        DeltaORSet<String> acked = local.ack();
        assertTrue(acked.pending().isEmpty());

        DeltaORSet<String> remote = DeltaORSet.<String>empty("B").apply(delta);
        assertEquals(acked, remote);
        assertTrue(remote.contains("y"));
        assertFalse(remote.contains("x"));

        // 稳态零传输：没有新操作，同步 100 轮也一个字节都不用发
        for (int i = 0; i < 100; i++) {
            assertTrue(acked.pending().isEmpty());
            assertTrue(remote.pending().isEmpty());
        }
    }

    @Test
    public void repeatedDeliveryOfSameDeltaIsIdempotent() {
        DeltaGCounter.Delta counterDelta = new DeltaGCounter("A").increment(5).pending();
        DeltaGCounter once = new DeltaGCounter("B").apply(counterDelta);
        DeltaGCounter many = new DeltaGCounter("B");
        for (int i = 0; i < 100; i++) {
            many = many.apply(counterDelta);
        }
        assertEquals(once, many);
        assertEquals(5L, many.value());

        DeltaORSet.Delta<String> setDelta = DeltaORSet.<String>empty("A").add("x").pending();
        DeltaORSet<String> setOnce = DeltaORSet.<String>empty("B").apply(setDelta);
        DeltaORSet<String> setMany = DeltaORSet.empty("B");
        for (int i = 0; i < 100; i++) {
            setMany = setMany.apply(setDelta);
        }
        assertEquals(setOnce, setMany);
        assertTrue(setMany.contains("x"));
    }
}
