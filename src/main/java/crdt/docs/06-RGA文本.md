> **进度**：[← 目录](../README.md) ｜ 上一步：[第 5 步 `ORSet` —— 多机同时增删集合](05-ORSet.md) ｜ 下一步：[第 7 步 收敛性证明 —— 用恶劣网络验证前 6 步](07-收敛性证明.md)
> **前置**：第 4 步　**产出**：`RGA.java`　**验证**：`OK (16 tests)`

---

# 第 6 步：`RGA` —— 协同文本编辑

**目标**：两个人同时在同一句话里插字、删字，最后两人的文本必须完全一致，且**字符既不丢也不撕裂**。

**大白话**：文本不能像集合那样只存"有哪些字符"——顺序也是数据。RGA 的思路是**给每个字符一个"父指针"**，把它挂在前一个字符后面，形成一棵树；再规定一套**确定的遍历顺序**，就能从树上唯一地还原出文本。

```
插入 "ab"：      root → a → b
A 在 a 后插 X：  root → a → b
                        ↘ X
B 在 a 后插 Y：  root → a → b
                        ↘ Y

同步后：a 的子节点按 (时间戳, replicaId) 降序 → Y, X, b
遍历结果：a Y X b  →  "aYXb"
两边算出来完全一样 ✅
```

**关键规则（写错这一步就全废）**：

1. 每个字符是一个节点，节点 ID = `(时间戳, replicaId)`
2. 从虚拟根出发，做**深度优先前序**遍历
3. 同一个父节点下的多个子节点，按节点 ID **降序**排列（时间戳更大 = 更晚插入 = 排在左边）
4. 删除 = 打墓碑，**不真删节点**（否则它的子节点会变成孤儿）

## 你要写的 API

```java
public final class RGA implements CvRDT<RGA> {
    public RGA(String replicaId);
    public RGA insert(int index, char c);   // index ∈ [0, size()]
    public RGA delete(int index);           // index ∈ [0, size())
    public String text();
    public int size();
    public RGA merge(RGA other);

    public VersionVector version();         // 第 9 步
    public int tombstoneCount();            // 第 9 步
    public RGA gc(VersionVector stableFrontier);  // 第 9 步

    public static final class NodeId implements Comparable<NodeId> {  // (timestamp, replicaId)
        public final long timestamp;
        public final String replicaId;
    }
    public static final class Node {
        public final NodeId id;
        public final NodeId parentId;   // 根节点为 null
        public final char value;
    }
}
```

## 硬性不变量

- [ ] **不存本地时钟字段**：新节点的时间戳 = `状态中所有节点时间戳的最大值 + 1`。这样 merge 之后时钟自动跟上，不需要额外同步
- [ ] 子节点排序：`NodeId` **降序**（时间戳优先，再比 replicaId）
- [ ] `text()` / `size()` 跳过墓碑；但**遍历时必须穿过墓碑**（墓碑的后代还要保持原位）
- [ ] `merge` = 节点按 id 并集 + 墓碑并集；**同一 id 必须携带完全相同的 `(value, parentId)`**，否则抛异常（这是"重复投递"的健全性检查）
- [ ] `equals` 只比较 `(nodes, tombstones)`，不含 `replicaId`
- [ ] **副本加入已有文档的写法**：`new RGA(myId).merge(sharedState)`。`merge` 保留调用方的 `replicaId`，所以之后新插入的节点都带本地 ID

> ⚠️ **踩坑记录（本项目真实发生过）**：如果直接拿同一个 `RGA` 对象派生出两个"副本"，两个副本会共用 `replicaId`，生成相同 `NodeId`，`merge` 时抛 `Conflicting node ID`。**`replicaId` 必须全局唯一**——这是部署约束，不是代码 bug。

## 动手验证

```bash
JAR="$HOME/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:$HOME/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
cd /Users/lizixuan/Documents/IdeaProjects/LeetCode
rm -rf /tmp/crdt6 && mkdir -p /tmp/crdt6
javac -encoding UTF-8 -d /tmp/crdt6 -cp "$JAR" \
  src/main/java/crdt/CvRDT.java src/main/java/crdt/VersionVector.java \
  src/main/java/crdt/CausalStamp.java src/main/java/crdt/CausalStability.java \
  src/main/java/crdt/RGA.java src/test/java/crdt/RGATest.java
java -cp "/tmp/crdt6:$JAR" org.junit.runner.JUnitCore crdt.RGATest
```

期望输出：

```
JUnit version 4.13.1
................
OK (16 tests)
```

## 破坏性实验（本手册最有价值的一个，实测结果）

把子节点排序从**降序**改成**升序**：

```java
Collections.sort(siblings, (left, right) -> left.id.compareTo(right.id));   // 改坏它
Collections.sort(siblings, (left, right) -> right.id.compareTo(left.id));   // 反过来
```

实测：**6 个测试挂掉**，但请看仔细——

```
✗ RGATest#duplicateAndOutOfOrderDeliveryIsIdempotentAndCommutative
✗ RGATest#concurrentInsertsKeepBothCharactersAndTheirSubtrees
✗ RGATest#localEditsAtBeginningMiddleAndEnd
✗ RGATest#concurrentDeleteAndInsertPreserveTombstonedDescendants
✗ LatticeLawsTest#rgaIsALattice
✗ ConvergenceTest#rgaForkedReplicasConverge
```

**`ConvergenceTest#rgaConverges` 依然通过！** 那是跑 25 个随机种子、5 副本、带丢包/重复/乱序的纯收敛测试，它照样全绿；挂掉的是**断言了具体文本顺序**的那些测试（比如 `rgaForkedReplicasConverge` 断言合并结果必须是 `"aYX"`）。

**这一下你就理解了 CRDT 领域最重要的一句话：收敛 ≠ 正确。** 收敛只保证"大家一样"，至于"一样成什么样"是否尊重用户意图，是设计者要单独保证、单独测试的。

## 自检问答

1. 为什么删除不能真删节点？（答：节点是别人的父指针目标，删了会让后代字符丢失或位置错乱。第 9 步会看到这个约束如何限制墓碑回收）
2. RGA 每个字符一个节点，代价是什么？（答：内存开销大；真实产品（Yjs、Automerge）会用 run-length 压缩 + 富文本属性表）

---

**下一步** → [第 7 步：收敛性证明 —— 用恶劣网络验证前 6 步](07-收敛性证明.md)
