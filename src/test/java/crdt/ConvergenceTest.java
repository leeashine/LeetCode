package crdt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import org.junit.Test;

/**
 * 分布式网络仿真测试：在不依赖任何中间件的前提下，证明 CRDT 的收敛性。
 *
 * <p>仿真刻意注入三种"真实网络一定会发生"的恶劣行为：</p>
 * <ul>
 *   <li><b>乱序</b>：副本按任意顺序、任意方向互相同步；</li>
 *   <li><b>重复</b>：同一条状态消息被投递两次（连续 merge 两次）；</li>
 *   <li><b>丢失</b>：35% 的同步被直接跳过。</li>
 * </ul>
 *
 * <p>断言只有一句话：所有副本两两 {@code equals}。如果某天有人往 CRDT 里加了一个
 * "看起来合理"的字段却漏了 merge，这个测试会立刻抓住它。</p>
 *
 * <p>除了收敛，每个类型还断言一个<b>语义性质</b>（总计数、最大时间戳、可见长度），
 * 防止出现"大家都收敛到同一个错误值"这种假通过。</p>
 */
public class ConvergenceTest {

    /** 一次本地操作：读入当前状态，返回应用操作后的新状态。 */
    private interface Op<T> {
        T apply(T state, Random rnd);
    }

    private static final int REPLICAS = 5;
    private static final int SEEDS = 25;
    private static final int LOCAL_OPS = 60;
    private static final int GOSSIP_ROUNDS = 40;

    /**
     * 跑一轮：随机本地操作 → 40 轮带丢包/重复的随机反熵 → 断言全部副本相等。
     *
     * @param name       断言信息里用的类型名
     * @param factory    副本工厂（每次调用产出一个全新副本，携带各自的 replicaId）
     * @param op         本地操作生成器
     * @param seed       随机种子（固定种子保证可复现）
     * @return 收敛后的副本列表
     */
    private static <T extends CvRDT<T>> List<T> run(String name, Supplier<T> factory, Op<T> op, long seed) {
        Random rnd = new Random(seed);
        List<T> replicas = new ArrayList<T>();
        for (int i = 0; i < REPLICAS; i++) {
            replicas.add(factory.get());
        }

        // 阶段一：各副本独立地乱写（此阶段没有同步，必然产生分叉）
        for (int i = 0; i < LOCAL_OPS; i++) {
            int idx = rnd.nextInt(REPLICAS);
            replicas.set(idx, op.apply(replicas.get(idx), rnd));
        }

        // 阶段二：反熵（anti-entropy），只有 merge，没有任何"协商/回滚"
        for (int round = 0; round < GOSSIP_ROUNDS; round++) {
            for (int from = 0; from < REPLICAS; from++) {
                for (int to = 0; to < REPLICAS; to++) {
                    if (from == to) {
                        continue;
                    }
                    if (rnd.nextDouble() < 0.35) {
                        continue; // 消息丢失
                    }
                    T merged = replicas.get(to).merge(replicas.get(from));
                    if (rnd.nextDouble() < 0.4) {
                        merged = merged.merge(replicas.get(from)); // 重复投递
                    }
                    replicas.set(to, merged);
                }
            }
        }

        for (int i = 1; i < REPLICAS; i++) {
            assertEquals(name + "(种子 " + seed + ") 未收敛: 副本0 vs 副本" + i,
                    replicas.get(0), replicas.get(i));
        }
        return replicas;
    }

    @Test
    public void rgaForkedReplicasConverge() {
        // 副本生命周期：一份基线文档 + 两个各自"加入"它的副本，然后并发编辑。
        // merge 保留调用方的 replicaId，所以 new RGA(id).merge(shared) 之后，
        // 该副本新插入的 NodeId 都带自己的 ID —— 这是"副本加入已有文档"的标准姿势。
        RGA shared = new RGA("S").insert(0, 'a').insert(1, 'b');
        RGA a = new RGA("A").merge(shared).insert(1, 'X');
        RGA b = new RGA("B").merge(shared).insert(1, 'Y').delete(2);

        RGA aSynced = a.merge(b);
        RGA bSynced = b.merge(a);
        assertEquals("加入同一文档的两个副本应可安全合并", aSynced, bSynced);
        // 根下的 a 之后：Y(3,B) > X(3,A) > b(2,S，墓碑)
        assertEquals("aYX", aSynced.text());
    }

    @Test
    public void gCounterConverges() {
        for (long seed = 1; seed <= SEEDS; seed++) {
            final long[] expected = new long[1];
            Op<GCounter> op = new Op<GCounter>() {
                @Override
                public GCounter apply(GCounter state, Random rnd) {
                    long d = 1 + rnd.nextInt(3);
                    expected[0] += d;
                    return state.increment(d);
                }
            };
            List<GCounter> replicas = run("GCounter", new Supplier<GCounter>() {
                private int i = 0;

                @Override
                public GCounter get() {
                    return new GCounter("R" + (i++));
                }
            }, op, seed);
            assertEquals("GCounter(种子 " + seed + ") 总计数错误", expected[0], replicas.get(0).value());
        }
    }

    @Test
    public void pnCounterConverges() {
        for (long seed = 1; seed <= SEEDS; seed++) {
            final long[] expected = new long[1];
            Op<PNCounter> op = new Op<PNCounter>() {
                @Override
                public PNCounter apply(PNCounter state, Random rnd) {
                    long d = rnd.nextInt(3);
                    if (rnd.nextBoolean()) {
                        expected[0] += d;
                        return state.increment(d);
                    }
                    expected[0] -= d;
                    return state.decrement(d);
                }
            };
            List<PNCounter> replicas = run("PNCounter", new Supplier<PNCounter>() {
                private int i = 0;

                @Override
                public PNCounter get() {
                    return new PNCounter("R" + (i++));
                }
            }, op, seed);
            assertEquals("PNCounter(种子 " + seed + ") 净值错误", expected[0], replicas.get(0).value());
        }
    }

    @Test
    public void lwwRegisterConvergesToHighestTimestamp() {
        for (long seed = 1; seed <= SEEDS; seed++) {
            final long[] maxTimestamp = new long[] { Long.MIN_VALUE };
            Op<LWWRegister<Long>> op = new Op<LWWRegister<Long>>() {
                @Override
                public LWWRegister<Long> apply(LWWRegister<Long> state, Random rnd) {
                    // 时间戳故意大量碰撞，用于反复触发 replicaId tie-break 分支
                    long ts = state.isEmpty() ? 1 + rnd.nextInt(3) : state.timestamp() + rnd.nextInt(3);
                    if (ts > maxTimestamp[0]) {
                        maxTimestamp[0] = ts;
                    }
                    return state.set(rnd.nextLong(), ts, "R" + rnd.nextInt(REPLICAS));
                }
            };
            List<LWWRegister<Long>> replicas = run("LWWRegister",
                    () -> LWWRegister.<Long>empty(), op, seed);
            assertEquals("LWWRegister(种子 " + seed + ") 应保留最大时间戳的写",
                    maxTimestamp[0], replicas.get(0).timestamp());
        }
    }

    @Test
    public void mvRegisterConverges() {
        for (long seed = 1; seed <= SEEDS; seed++) {
            Op<MVRegister<Integer>> op = new Op<MVRegister<Integer>>() {
                @Override
                public MVRegister<Integer> apply(MVRegister<Integer> state, Random rnd) {
                    return state.assign(rnd.nextInt(1000), "R" + rnd.nextInt(REPLICAS));
                }
            };
            List<MVRegister<Integer>> replicas = run("MVRegister",
                    () -> MVRegister.<Integer>empty(), op, seed);
            assertTrue("MVRegister(种子 " + seed + ") 收敛后不应为空", !replicas.get(0).isEmpty());
        }
    }

    @Test
    public void orSetConverges() {
        for (long seed = 1; seed <= SEEDS; seed++) {
            Op<ORSet<String>> op = new Op<ORSet<String>>() {
                @Override
                public ORSet<String> apply(ORSet<String> state, Random rnd) {
                    String e = "e" + rnd.nextInt(6);
                    return rnd.nextInt(3) == 0 ? state.remove(e) : state.add(e);
                }
            };
            List<ORSet<String>> replicas = run("ORSet", new Supplier<ORSet<String>>() {
                private int i = 0;

                @Override
                public ORSet<String> get() {
                    return ORSet.empty("R" + (i++));
                }
            }, op, seed);
            // 至少有一个元素活下来（所有副本 add 过必然有存活 tag），否则说明 remove 语义写反了
            assertTrue("ORSet(种子 " + seed + ") 元素集合不应为空", replicas.get(0).size() > 0);
        }
    }

    @Test
    public void rgaConverges() {
        for (long seed = 1; seed <= SEEDS; seed++) {
            final int[] inserts = new int[1];
            final int[] deletes = new int[1];
            Op<RGA> op = new Op<RGA>() {
                @Override
                public RGA apply(RGA state, Random rnd) {
                    if (state.size() > 0 && rnd.nextInt(4) == 0) {
                        deletes[0]++;
                        return state.delete(rnd.nextInt(state.size()));
                    }
                    inserts[0]++;
                    return state.insert(rnd.nextInt(state.size() + 1), (char) ('a' + rnd.nextInt(26)));
                }
            };
            List<RGA> replicas = run("RGA", new Supplier<RGA>() {
                private int i = 0;

                @Override
                public RGA get() {
                    return new RGA("R" + (i++));
                }
            }, op, seed);

            // 每个 delete 恰好抹掉一个可见字符（墓碑不产生新字符），故可见长度 == 插入数 - 删除数
            assertEquals("RGA(种子 " + seed + ") 可见长度错误", inserts[0] - deletes[0], replicas.get(0).size());
            for (int i = 1; i < REPLICAS; i++) {
                assertEquals("RGA(种子 " + seed + ") 文本不一致", replicas.get(0).text(), replicas.get(i).text());
            }
        }
    }
}
