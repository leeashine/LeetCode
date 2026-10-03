> **进度**：[← 目录](../README.md) ｜ 上一步：[第 4 步 `VersionVector` + `MVRegister` —— 并发写两个都留](04-版本向量与MV寄存器.md) ｜ 下一步：[第 6 步 `RGA` —— 协同文本编辑](06-RGA文本.md)
> **前置**：第 4 步　**产出**：`ORSet.java`　**验证**：`OK (13 tests)`

---

# 第 5 步：`ORSet` —— 多机同时增删集合

**目标**：多机并发地对同一个集合做 add / remove，收敛且语义合理。

**大白话**：集合的难点是"删除"——如果 A 删掉 x 的同时 B 加了 x，x 该不该在？

**解法：给每次 add 发一张唯一票（tag）**。

```
状态 = 每个元素挂着的 tag 集合  +  墓碑集合（被删掉的 tag）
add(x)    → 给 x 发一张新票 tag(A,1)
remove(x) → 把 x 当前所有票丢进墓碑
x 是否存在 = x 还有"没进墓碑的票"吗
```

**为什么"并发 add vs remove"是 add 赢**：`remove` 只能墓碑它**已经看见**的票。B 并发加的票 A 从没见过，自然删不掉。

```
A: add(苹果)  → 苹果:[票A1]
B: add(苹果)  → 苹果:[票B1]        ← 真并发
A: remove(苹果) → 墓碑{A1}          ← 只删得掉自己看见的票
同步后 → 苹果:[票B1] 且 票B1 不在墓碑 → 苹果还活着 ✅
```

## 你要写的 API

```java
public final class ORSet<E> implements CvRDT<ORSet<E>> {
    public static <E> ORSet<E> empty(String replicaId);
    public ORSet<E> add(E element);
    public ORSet<E> remove(E element);
    public boolean contains(E element);
    public Set<E> elements();
    public int size();
    public ORSet<E> merge(ORSet<E> other);

    public VersionVector version();     // 第 9 步：因果历史（GC 用）
    public int tombstoneCount();        // 第 9 步：观测墓碑数量（GC 用）
    public ORSet<E> gc(VersionVector stableFrontier);   // 第 9 步

    public static final class Tag {   // (replicaId, seq) 唯一票
        public Tag(String replicaId, long seq);
        public String replicaId();
        public long seq();
    }
}
```

## 硬性不变量

- [ ] **规范形式**：`entries` 的每个值集非空，且不含任何已在 `tombstones` 里的 tag（每次操作后都要维持）
- [ ] `remove` 只墓碑**当前观察到的** tag，不是"永久拉黑这个元素"
- [ ] `merge` = tag 并集 + 墓碑并集 → 再 prune
- [ ] `equals` 只比较 `(entries, tombstones)`，**不含 `replicaId` / `seq`**（本地身份和本地计数器不是复制状态）
- [ ] tag 的 `seq` 必须**单调递增**，保证同一副本产生的票永不重复

## 动手验证

```bash
JAR="$HOME/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:$HOME/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
cd /Users/lizixuan/Documents/IdeaProjects/LeetCode
rm -rf /tmp/crdt5 && mkdir -p /tmp/crdt5
javac -encoding UTF-8 -d /tmp/crdt5 -cp "$JAR" \
  src/main/java/crdt/CvRDT.java src/main/java/crdt/VersionVector.java \
  src/main/java/crdt/CausalStamp.java src/main/java/crdt/CausalStability.java \
  src/main/java/crdt/ORSet.java src/test/java/crdt/ORSetTest.java
java -cp "/tmp/crdt5:$JAR" org.junit.runner.JUnitCore crdt.ORSetTest
```

期望输出：

```
JUnit version 4.13.1
.............
OK (13 tests)
```

## 破坏性实验（实测结果）

让 `merge` **不再传播墓碑**（把 `tombstones` 的并集改成空集）：

```java
Set<Tag> joinedTombstones = new HashSet<Tag>(tombstones);   // 改坏它
Set<Tag> joinedTombstones = new HashSet<Tag>();             // 墓碑不再同步
```

实测：**5 个测试挂掉**——

```
✗ ORSetTest#concurrentAddSurvivesUnobservedRemove
✗ ORSetTest#threeReplicasConvergeAfterSeededOperationsAndMerges
✗ ORSetTest#versionVectorConvergesAfterMerge
✗ ORSetTest#mergeIsJoinWithEmptyIdentity
✗ LatticeLawsTest#orSetIsALattice
```

**墓碑记录了"删过"，但必须真的同步并剪枝才有用**：不同步墓碑，两个副本对"x 是否还存在"的答案就会永久分叉。

## 自检问答

1. 墓碑为什么不能马上删？（答：还没和所有副本同步过时，删掉墓碑会导致旧 tag 复活。必须等**因果稳定**——所有副本都见过这条墓碑——才能 GC，见第 9 步）
2. ORSet 的墓碑会无限增长吗？（答：会。这是它在真实系统里的头号内存问题，第 8、9 步专门处理它）

---

**下一步** → [第 6 步：`RGA` —— 协同文本编辑](06-RGA文本.md)
