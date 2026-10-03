> **进度**：[← 目录](../README.md) ｜ 上一步：[第 7 步 收敛性证明 —— 用恶劣网络验证前 6 步](07-收敛性证明.md) ｜ 下一步：[第 9 步 墓碑 GC / 因果稳定 —— 让内存不无限膨胀](09-墓碑GC.md)
> **前置**：第 7 步　**产出**：`DeltaGCounter.java`、`DeltaORSet.java`　**验证**：`OK (29 tests)`

---

# 第 8 步：delta-state —— 只传增量，不传全量

**目标**：前面 7 步都是 **state-based**：每次同步把**整个状态**发给对方。1000 次插入的集合，同步一次要传 1000 个 tag。这一步改成只传**自上次确认以来新增的那一小块**。

**大白话**：关键在于——**增量本身也是同一个半格的元素**，所以它天然幂等、可交换、可结合。于是丢包重传、重复投递全都免费。

```
GCounter 的 delta = {本地副本: 当前累计值}     ← 绝对累计值，不是差值！这是幂等的关键
ORSet   的 delta = {本次新增的 tag} + {本次墓碑的 tag}   ← 都是集合，并入即可
```

**两条通道要分清**：

```
状态通道（state-based）:  传全量，简单但带宽随状态增长
增量通道（delta-state）:  传 pending() → 对方 apply() → 我方 ack()
                          丢包时不 ack，下一轮自然重传
```

## ⚠️ 必须理解的前提：增量通道需要一个"知道对方缺什么"的机制

这是本项目实测踩到的坑：**中转副本不会转发别人的增量**（它只发自己 `pending()` 里的本地贡献）。所以"随便点对点发一发"的拓扑下，信息只能走一跳，永远收不敛。

两种正确的做法（二选一）：

| 做法 | 说明 | 适用 |
|---|---|---|
| **广播 + 全员确认** | 把自己的增量广播给所有人，只有全员收到才 `ack()`；丢包就下一轮重发 | 全互联/小集群，本项目的测试用这个 |
| **交换因果上下文** | 同步前先交换 `version()`，算出"对方缺哪些 delta"再补发 | 树形/链式拓扑，Riak/Antidote 的做法 |

> 换句话说：**delta-state 省的是带宽，不是协议**。你要额外写一层"谁缺什么"的调度逻辑，CRDT 只保证那层逻辑写错时序时不会破坏数据。

## 你要写的 API

```java
public final class DeltaGCounter implements CvRDT<DeltaGCounter> {
    public DeltaGCounter(String replicaId);
    public DeltaGCounter increment();
    public DeltaGCounter increment(long delta);
    public long value();
    public Map<String,Long> state();
    public Delta pending();                    // 未确认送达的增量（peek，不改状态）
    public DeltaGCounter ack();                // 确认送达：清空缓冲
    public DeltaGCounter apply(Delta delta);   // 并入增量：逐 key max
    public DeltaGCounter merge(DeltaGCounter other);

    public static final class Delta {
        public static Delta empty();
        public Map<String,Long> counts();
        public Delta merge(Delta other);
        public boolean isEmpty();
    }
}

public final class DeltaORSet<E> implements CvRDT<DeltaORSet<E>> {
    public static <E> DeltaORSet<E> empty(String replicaId);
    public DeltaORSet<E> add(E element);
    public DeltaORSet<E> remove(E element);
    public boolean contains(E element);
    public Set<E> elements();
    public Delta<E> pending();                 // 注意：Delta 是泛型静态嵌套类
    public DeltaORSet<E> ack();
    public DeltaORSet<E> apply(Delta<E> delta);
    public DeltaORSet<E> merge(DeltaORSet<E> other);

    public static final class Delta<E> {
        public static <E> Delta<E> empty();
        public Map<E, Set<ORSet.Tag>> added();   // 本次新增的 tag
        public Set<ORSet.Tag> removed();         // 本次墓碑的 tag
        public Delta<E> merge(Delta<E> other);
        public boolean isEmpty();
    }
}
```

> 小陷阱：`DeltaORSet.Delta` 是**泛型静态嵌套类**，类型必须写成 `DeltaORSet.Delta<String>`。写成 `DeltaORSet<String>.Delta` 会编译报错"无法从参数化的类型中选择静态类"（本项目真实踩过）。

## 硬性不变量

- [ ] **`pending()` 必须幂等**：不 `ack` 时连续调用返回相等结果，且不改变状态（否则重传会出错）
- [ ] **`pending()` 返回的是绝对累计值，不是差值**（差值不幂等，见下面的破坏性实验）
- [ ] **`ack()` 只清本地发送缓冲**，不动被复制的状态
- [ ] `equals` 只比较**被复制的状态**，不比 `replicaId`、不比发送缓冲水位
- [ ] `Delta` 自身也是半格元素：`Delta.merge` 必须幂等 / 交换 / 结合
- [ ] `apply(delta)` 不能碰发送缓冲（否则对方的状态会污染我的重传队列）

## 动手验证

```bash
JAR="$HOME/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:$HOME/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
cd /Users/lizixuan/Documents/IdeaProjects/LeetCode
rm -rf /tmp/crdt8 && mkdir -p /tmp/crdt8
javac -encoding UTF-8 -d /tmp/crdt8 -cp "$JAR" src/main/java/crdt/*.java \
  src/test/java/crdt/DeltaGCounterTest.java src/test/java/crdt/DeltaORSetTest.java \
  src/test/java/crdt/DeltaSyncTest.java
java -cp "/tmp/crdt8:$JAR" org.junit.runner.JUnitCore \
  crdt.DeltaGCounterTest crdt.DeltaORSetTest crdt.DeltaSyncTest
```

期望输出：

```
JUnit version 4.13.1
.............................
OK (29 tests)
```

## 关键断言（这是 delta 的全部价值）

```java
// 1000 次本地自增，未确认增量仍然只有 1 个条目
assertEquals(1, counter.pending().counts().size());

// 稳态零传输：没有新操作时，同步 100 轮一个字节都不用发
assertTrue(acked.pending().isEmpty());
```

## 破坏性实验（实测结果 + 一个反直觉发现）

把 `pending()` 改成返回**差值**（`counts[local] - acked`）而不是绝对累计值：

```java
pending.put(replicaId, local);            // 改坏它
pending.put(replicaId, local - acked);    // 差值
```

实测：**3 个测试挂掉**，全在 `DeltaGCounterTest`——

```
✗ DeltaGCounterTest#pendingPeeksAtAbsoluteCountUntilAcknowledged
✗ DeltaGCounterTest#receivingNewerLocalComponentDoesNotAdvanceAcknowledgementWatermark
✗ DeltaGCounterTest#mergingFullStateKeepsLocalAcknowledgementWatermark
```

**反直觉的地方**：`DeltaSyncTest` 的 5 个测试**全部通过**！为什么？因为那个仿真把 60 次本地操作全部做完才开始同步，此时 `acked` 还是 0，"差值"恰好等于"绝对值"，bug 被掩盖了。

**这是本手册第二个最重要的教训：只在特定时序下暴露的 bug 最危险。** 如果你的测试只覆盖"先写完再同步"这一种时序，你就漏掉了"写一半、同步、再写"这种真实到不能再真实的场景。**测试要覆盖时序，不只是覆盖功能。**

## 自检问答

1. delta-state 比 state-based 省了什么？（答：带宽从"随状态规模增长"变成"随操作频率增长"；稳态下为 0）
2. delta-state 比 op-based CRDT（只传操作）好在哪？（答：op-based 依赖可靠有序投递；delta 是状态片段，仍然幂等，能容忍丢包重复乱序）

---

**下一步** → [第 9 步：墓碑 GC / 因果稳定 —— 让内存不无限膨胀](09-墓碑GC.md)
