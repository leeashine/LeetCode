package crdt;

/**
 * 可执行的 CRDT 讲解：跑一遍 {@code main} 就能看到"并发写 + 乱序合并"的真实结果。
 *
 * <p>它刻意不用任何网络库——副本就是普通对象，同步就是一次 {@code merge} 调用。
 * 这正是 CRDT 的价值所在：<b>同步逻辑被压缩成一个纯函数</b>，
 * 剩下的（序列化、传输、重试、持久化）才是工程问题。</p>
 */
public final class CrdtDemo {

    private CrdtDemo() {
    }

    public static void main(String[] args) {
        gCounter();
        orSet();
        registers();
        rga();
    }

    /** 只增计数器：不需要"协商"，merge 取逐副本最大值即可。 */
    private static void gCounter() {
        title("1. GCounter —— 并发计数不会丢");
        GCounter a = new GCounter("A").increment(3);
        GCounter b = new GCounter("B").increment(1);
        GCounter c = new GCounter("C").increment(5);
        System.out.println("  A 本地: " + a.state());
        System.out.println("  B 本地: " + b.state());
        System.out.println("  C 本地: " + c.state());
        GCounter merged = a.merge(b).merge(c);
        System.out.println("  merge 后: " + merged.state() + "  ->  value = " + merged.value() + " (3+1+5)");
        System.out.println("  再 merge 一次: " + merged.merge(c).equals(merged) + "  <- 幂等，重复投递无副作用");
    }

    /** 可增删集合：并发 add / remove 谁赢？ */
    private static void orSet() {
        title("2. ORSet —— 并发 add vs remove：add 胜");
        ORSet<String> a = ORSet.<String>empty("A").add("苹果").add("香蕉");
        ORSet<String> b = ORSet.<String>empty("B").add("苹果");

        // A 在还没看到 B 的写入前，删掉"苹果"
        ORSet<String> aAfterRemove = a.remove("苹果");
        System.out.println("  A(删苹果后): " + aAfterRemove.elements());
        System.out.println("  B(并发 add 苹果): " + b.elements());

        ORSet<String> aSynced = aAfterRemove.merge(b);
        ORSet<String> bSynced = b.merge(aAfterRemove);
        System.out.println("  A 同步后: " + aSynced.elements());
        System.out.println("  B 同步后: " + bSynced.elements());
        System.out.println("  两边一致: " + aSynced.equals(bSynced) + "  <- 苹果活着：A 的 remove 只能删掉它'看见过'的 tag");
    }

    /** 寄存器：同一个 key 被两个离线副本同时写，LWW 会丢一个，MV 两个都留。 */
    private static void registers() {
        title("3. LWWRegister vs MVRegister —— 并发写该丢还是该留？");
        LWWRegister<String> lwwA = LWWRegister.<String>empty().set("http://a", 1L, "A");
        LWWRegister<String> lwwB = LWWRegister.<String>empty().set("http://b", 1L, "B");
        System.out.println("  LWW merge 结果: '" + lwwA.merge(lwwB).value()
                + "'  <- 时间戳相同，靠 replicaId 定胜负，另一个值被静默丢弃");

        MVRegister<String> mvA = MVRegister.<String>empty().assign("http://a", "A");
        MVRegister<String> mvB = MVRegister.<String>empty().assign("http://b", "B");
        System.out.println("  MV  merge 结果: " + mvA.merge(mvB).values()
                + "  <- 两个并发值都保留，冲突交给上层决定");
    }

    /** 序列 CRDT：两个人在同一句话里并发插字，字符既不丢也不撕裂。 */
    private static void rga() {
        title("4. RGA —— 两人同时编辑同一句话");
        // 基线文档 "ab" 由副本 S 写下；A、B 通过 new RGA(id).merge(shared) 加入这份文档。
        // merge 保留的是"本地" replicaId，所以这行就是"副本加入已有文档"的标准写法。
        // 反面教材：直接拿同一个 RGA 对象派生两个副本会让两个副本共用 replicaId，
        // 生成相同的 NodeId，merge 时会抛 Conflicting node ID —— replicaId 必须全局唯一。
        RGA shared = append(new RGA("S"), "ab");
        RGA a = new RGA("A").merge(shared).insert(1, 'X');        // A 在 a 后面插 X
        RGA b = new RGA("B").merge(shared).insert(1, 'Y').delete(2); // B 并发插 Y，并删掉 b
        System.out.println("  shared: '" + shared.text() + "'");
        System.out.println("  A:      '" + a.text() + "'");
        System.out.println("  B:      '" + b.text() + "'");

        RGA aSynced = a.merge(b);
        RGA bSynced = b.merge(a);
        System.out.println("  A 同步后: '" + aSynced.text() + "'");
        System.out.println("  B 同步后: '" + bSynced.text() + "'");
        System.out.println("  两边一致: " + aSynced.equals(bSynced)
                + "  <- 同位置并发的 X/Y 按 (时间戳,replicaId) 得到同一个全序，b 的删除也保留");
    }

    private static RGA append(RGA rga, String text) {
        RGA result = rga;
        for (int i = 0; i < text.length(); i++) {
            result = result.insert(result.size(), text.charAt(i));
        }
        return result;
    }

    private static void title(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
