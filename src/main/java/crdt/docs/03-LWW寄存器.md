# 第 3 步：两台设备改标题，怎样约定一个赢家

[← 操作统计](02-计数器.md) ｜ [目录](../README.md) ｜ [下一章：保留独立标题修改 →](04-版本向量与MV寄存器.md)

**上一步**：不同来源的计数可以同时保留，再汇总。

**本课任务**：这次标题只留一个值，规定两边一致的选取规则。

**完成后**：旧标题晚到，不覆盖带更大编号的新标题；同编号时两边仍选同一个值。

**练习入口**：[LamportClock](../learnning/LamportClock.java)、[LWWRegister](../learnning/LWWRegister.java)。

## 1. 最后收到的消息不同

**假设**

A 保存标题“周末计划”，B 保存“出差计划”。A 最后收到出差，B 最后收到周末。

**问题**

谁的消息后到就显示谁，最终会一样吗？

**解释**

不会。我们给修改附一个可比较的顺序标记，让接收次序不决定结果。

本课接受“独立修改也按规则只留一个”的产品选择。下一课改成不丢独立修改，这两种选择不要混为同一种业务要求。

## 2. A 已收到编号 5，接下来再保存

**假设**

A 本地计数是 1，已收到 B 编号 5 的标题，再保存一次。若继续用 2，新标题还赢不过已知状态。

**问题**

怎样产生一个比已经看到的顺序标记更大的编号？需要哪些时钟方法？

**解释**

在本地保存时加一，产生 tick；收到远端编号时先跟上它，产生 observe。需要一个初始 0，产生 start；显示或传入标题时需要读数，产生 time。

定义一个 time 字段及构造器，随后填写这些方法：observe 取 max，tick 再加一。本仓库 observe 自己不加一，组合 `clock.observe(remote).tick()` 才给下一次修改更大编号。

at 只是方便从已知读数构造时钟，equals/hashCode 比 time；它们不是额外的同步规则。

## 3. 两边第一次写都编号 1

**假设**

A 的修改是 `(1,A,周末计划)`，B 是 `(1,B,出差计划)`。

**问题**

数字平局时保留自己，为何会分歧？怎样补一条裁决规则？

**解释**

A 留周末、B 留出差。再比较 ID，约定自然序较大者赢，于是两边都选 B。

因此 LWWRegister 保存 value、timestamp 和这次写的 replicaId。这里 ID 标识获胜写，不是接收者身份，应参与相等判断。

数字与 ID 合起来标识一次写；同一顺序标记不能对应两个不同值，这由调用方保证。编号用于裁决，不等于并发操作的真实时间先后。

## 4. 把“保存一个标题”变成方法

**假设**

调用方已拿到本次编号，想把标题与编号、来源绑定，稍后再合并另一台设备的记录。

**问题**

时钟与标题容器各负责什么？为什么 set 与 merge 不合在一起？

**解释**

set(value,timestamp,id) 保存一次本地新写；merge(other) 比较已存在的两次写。set 不自动生成编号，也不自动 observe，调用方先操作时钟再传入顺序标记。

填构造器，绑定三个字段；set 校验保留时间戳和 ID，再返回新状态。merge 先比较 timestamp，再比较 ID，返回获胜状态。

<details>
<summary>合并只按共同顺序标记选取</summary>

```java
if (timestamp > other.timestamp) {
    return this;
}
if (timestamp < other.timestamp) {
    return other;
}
return replicaId.compareTo(other.replicaId) >= 0 ? this : other;
```

</details>

先在片段前拒绝 null 输入。value/timestamp/replicaId 三个读取方法，分别满足显示标题和为下一次写推进时钟的需要。

## 5. 空设备第一次加入

**假设**

新设备尚未写任何标题，合并它不应该抹掉已有的“周末计划”。null 也可能是一次合法赋值。

**问题**

如何表示“从没写过”，而不让它赢过真实修改？

**解释**

empty 保存保留的最低顺序标记 `(Long.MIN_VALUE,"",null)`；普通 set 禁用最低时间戳。isEmpty 判断保留顺序标记，不能只看 value 是否为 null。

最后补 equals/hashCode，比较完整三元组。这份空状态与任何合法写合并，合法写都赢。toString 只把时钟读数或标题三元组显示出来，方便观察，不决定排序。

## 本课任务顺序

先给时钟定义 time，再完成 start/time/observe/tick；随后定义标题三元组，完成 empty/set/merge；最后补读取方法与状态相等。

完成后观察这段目标用法。代码中的 A/B 是两台设备：

```java
LamportClock clock = LamportClock.start().tick();
LWWRegister<String> a = LWWRegister.<String>empty().set("周末计划", clock.time(), "A");
LWWRegister<String> b = LWWRegister.<String>empty().set("出差计划", 1, "B");
a = a.merge(b);  // 同编号时 B 赢
clock = clock.observe(a.timestamp()).tick();
a = a.set("新的计划", clock.time(), "A");  // 本次顺序标记更大
System.out.println(b.merge(a).value());  // 新的计划
```

代价是另一份独立修改会被丢弃。物理时钟也能作为排序顺序标记，但漂移和回拨影响业务时间语义；本课用逻辑计数不等于恢复了真实先后。

## 运行本章练习

测试类：[RegisterLearningTest](../../../../test/java/crdt/learnning/RegisterLearningTest.java)。补齐本章和前置章节的 TODO 后运行：

```bash
cd /Users/lizixuan/Documents/IdeaProjects/LeetCode
JAR="$HOME/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:$HOME/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
CRDT_BUILD=$(mktemp -d /tmp/crdt-learnning.XXXXXX)
javac -encoding UTF-8 -cp "$JAR" -d "$CRDT_BUILD" src/main/java/crdt/learnning/*.java src/test/java/crdt/learnning/*.java
java -cp "$CRDT_BUILD:$JAR" org.junit.runner.JUnitCore crdt.learnning.RegisterLearningTest
```

## 本课回看

去掉平局 ID 裁决，两边为什么不同？A 收到编号 9 后，下次写需要怎样操作时钟？能够解释这个取舍，就进入保留两份标题的方案。

参考：[LamportClock](../LamportClock.java)、[LWWRegister](../LWWRegister.java)。

**下一步** → [04 保留独立标题修改](04-版本向量与MV寄存器.md)
