> **进度**：[← 目录](../README.md) ｜ 上一步：[第 2 步 `GCounter` / `PNCounter` —— 并发计数不丢](02-计数器.md) ｜ 下一步：[第 4 步 `VersionVector` + `MVRegister` —— 并发写两个都留](04-版本向量与MV寄存器.md)
> **前置**：第 1 步　**产出**：`LamportClock.java`、`LWWRegister.java`　**验证**：`OK (6 tests)`

---

# 第 3 步：`LamportClock` + `LWWRegister` —— 谁最后写的谁赢

**目标**：多机同时改同一个值（比如用户昵称），约定"最后写的赢"。

**大白话**：既然要分先后，就得有时间戳。但**不能用机器时钟**（时钟会漂移、会回拨）。用**逻辑时钟**：每发生一个本地事件就 +1，收到别人的时间戳就取 max+1。

```
副本A: 写昵称 = "小明"   (时间戳 1)
副本B: 写昵称 = "小红"   (时间戳 1)   ← 真并发，时间戳撞车了
merge → 时间戳相同，比 replicaId 字典序，大的赢
```

**关键认知**：Lamport 时钟只保证 `a 因果先于 b ⇒ ts(a) < ts(b)`，**反过来不成立**——时间戳大不代表因果更晚，可能只是并发。所以 LWW 会**静默丢掉并发写**。这是它的特性，不是 bug；下一步的 MVRegister 就是来修这个的。

## 你要写的 API

```java
public final class LamportClock {                       // 不可变，注意：它不是 CRDT
    public static LamportClock start();                 // time == 0
    public static LamportClock at(long time);
    public long time();
    public LamportClock tick();                         // 本地事件：time + 1
    public LamportClock observe(long remoteTime);       // 收到远端：max(time, remoteTime)
}

public final class LWWRegister<T> implements CvRDT<LWWRegister<T>> {
    public static <T> LWWRegister<T> empty();           // 半格底元素
    public LWWRegister<T> set(T value, long timestamp, String replicaId);
    public T value();
    public long timestamp();
    public String replicaId();
    public boolean isEmpty();
    public LWWRegister<T> merge(LWWRegister<T> other);
}
```

## 硬性不变量

- [ ] **底元素（bottom）**：`empty()` 必须返回 `(timestamp = Long.MIN_VALUE, replicaId = "", value = null)`，且必须是 merge 的**单位元**：`x.merge(empty()) == x`
- [ ] **全序规则**：先比 `timestamp`，大者胜；相同则比 `replicaId` 的字符串自然序，大者胜。**这条 tie-break 不能省**——省了交换律立刻挂
- [ ] `set` 不允许 `timestamp == Long.MIN_VALUE`（那是底元素专用）和空的 `replicaId`
- [ ] `equals` 比较三元组 `(timestamp, replicaId, value)`

> **为什么需要"底元素"？** 一个新副本还没写过任何值时，它的状态必须能与任何状态 merge 且不改变对方。这就是半格理论里的"最小元"，工程上对应"空副本加入集群"。

## 动手验证

```bash
JAR="$HOME/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:$HOME/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
cd /Users/lizixuan/Documents/IdeaProjects/LeetCode
rm -rf /tmp/crdt3 && mkdir -p /tmp/crdt3
javac -encoding UTF-8 -d /tmp/crdt3 -cp "$JAR" \
  src/main/java/crdt/CvRDT.java src/main/java/crdt/LamportClock.java \
  src/main/java/crdt/LWWRegister.java src/test/java/crdt/LWWRegisterTest.java
java -cp "/tmp/crdt3:$JAR" org.junit.runner.JUnitCore crdt.LWWRegisterTest
```

期望输出：

```
JUnit version 4.13.1
......
OK (6 tests)
```

## 破坏性实验（实测结果）

把 `merge` 里的 tie-break 去掉（时间戳相等时直接返回 `this`）：

```java
return replicaId.compareTo(other.replicaId) >= 0 ? this : other;  // 删掉这行
return this;                                                      // 变成"相等就保留自己"
```

实测：**4 个测试挂掉**——

```
✗ LWWRegisterTest#bottomIsIdentityAndJoinLawsHold
✗ LWWRegisterTest#replicaIdBreaksConcurrentTimestampTies
✗ LatticeLawsTest#lwwRegisterIsALattice
✗ ConvergenceTest#lwwRegisterConvergesToHighestTimestamp
```

> 📌 **本项目真实踩坑记录**：这个实验第一版跑的时候，`LatticeLawsTest#lwwRegisterIsALattice` **居然没挂**。原因是我最初写的通用定律检查只验了 `(a,b)` 一对的交换律，而时间戳撞车发生在 `(b,c)` 这一对上——测试漏了个洞。后来把交换律补成"三对全验"，这个破坏性实验才如实暴露出来。
> **教训：验证半格性质时，"随便挑一对验交换律"是不够的。** 边界往往藏在特定的那一对里。

## 自检问答

1. 为什么 `LamportClock` 不实现 `CvRDT`？（答：它是本地的时间工具，不是被复制的状态；它自己 merge 没有意义）
2. LWW 适合什么场景？（答：昵称、主题色、开关这类"最后设置生效"的字段；不适合购物车、收藏夹这类"不能丢数据"的场景）

---

**下一步** → [第 4 步：`VersionVector` + `MVRegister` —— 并发写两个都留](04-版本向量与MV寄存器.md)
