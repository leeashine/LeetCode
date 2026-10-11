# 第 8 步：只多写一次笔记，为什么要把全部历史再发一遍？

[← 目录](../README.md) ｜ 上一步：[第 7 步 收敛性验证](07-收敛性证明.md) ｜ 下一步：[第 9 步 墓碑 GC](09-墓碑GC.md)

**上一步**：计数、标签和正文在重复、换序同步后都能收敛。但每次同步都要发送全部历史。

**本课任务**：只发送本地尚未确认的变化；丢包后能重发同一份变化。

**完成后**：操作次数不因重发算重；先到的删除不会被旧添加带回；确认后待发内容清空，笔记状态不变。

**练习入口**：[DeltaGCounter 骨架](../learnning/DeltaGCounter.java)、[DeltaORSet 骨架](../learnning/DeltaORSet.java)，只有声明和 TODO。验收入口是 [DeltaLearningTest](../../../../test/java/crdt/learnning/DeltaLearningTest.java)。

**阅读约定**：本章固定三台设备：手机、电脑、备用机，简记为 **A（手机）**、**B（电脑）**、**C（备用机）**。设备身份固定，不能换用。本章只统计笔记的操作次数，不统计正文长度。后文用到四种记号：

- `{A=7}`：一张计数片段。含义：设备 A 的累计操作次数是 7。7 是绝对累计值，不是"加 7"。
- `A1`：一张票（代码中的 `ORSet.Tag`）。含义：设备 A 生成的第 1 张票。每次添加标签都生成一张新票，票是这次添加的凭证。
- `d1`、`d2`：按产生的先后给变化批次编号。
- **确认**：接收方应用片段后，告知发送方"已收到"。发送方随后调用 `ack()`。

所有方法都返回新对象。接住返回的新对象；旧对象作为历史快照保留，不要再改。

## 1. 笔记已经有很多标签，又多写了一次

**假设**

事情按这个顺序发生：

```text
① 手机保存着上千个标签，以及对应的删除记录
② 用户在手机上记了一次笔记操作，又添加了一个"工作"标签
③ 手机准备把这次变化同步给电脑
```

② 之后，变化只有两处：A 的操作次数加一，"工作"多一张票。其余标签和删除记录没有变化。

**问题**

电脑需要再次收到全部旧标签、全部设备的操作次数吗？

**解释**

1. 不需要。没有变化的部分再发一遍，合并结果与原来相同。
2. 只发发生变化的部分。这一小片状态叫 **delta**，本章简称为**片段**。
3. 片段是状态，不是要求接收方执行的命令。接收方把它并入已有状态。
4. 发送方需要记录哪些变化还没有被确认。这份记录叫**待发记录**。

先把完整状态拆开看：

- 计数的完整状态是"设备 → 累计次数"的映射。
- 标签的完整状态是"标签 → 存活票"的映射，外加一个墓碑集合。墓碑是已删除票的集合。

只取发生变化的分量或票，就得到一个片段。片段要能和已有完整状态合并。

因此两个练习类各自保存两份数据：

1. 计数类：复制状态 `counts`；发送进度 `acked`，含义是"上次确认时的本地累计值"。
2. 集合类：复制状态 `entries` 和 `tombstones`；待发记录 `unackedAdded`（待发添加票）和 `unackedRemoved`（待发删除票）。
3. 设备身份与发送进度不参与复制状态的相等判断。

这些字段都是快照。构造新对象时，复制需要修改的集合。不要直接修改旧对象持有的集合。返回给外部的集合也不能任意写入。

**本步实现**：先定义字段，再补骨架已有的构造器。公开计数构造器接设备身份，建立空 `counts` 与 `acked=0`。私有构造器接已有快照，保存身份、状态及确认进度。集合的空状态在后面引出 `empty` 时补全。

<details>
<summary>两个外层类的字段参考：为什么需要它们已在上文说明</summary>

```java
// DeltaGCounter 内
private final String replicaId;
private final Map<String, Long> counts;
private final long acked;

// DeltaORSet<E> 内
private final String replicaId;
private final long seq;
private final Map<E, Set<ORSet.Tag>> entries;
private final Set<ORSet.Tag> tombstones;
private final Set<ORSet.Tag> unackedAdded;
private final Set<ORSet.Tag> unackedRemoved;
```

</details>

## 2. A 已记过 5 次，又记了 2 次

**假设**

事情按这个顺序发生：

```text
① 手机之前记过 5 次操作，电脑已经收到并确认
② 手机又记了 2 次操作，本地累计达到 7 次
③ 手机准备发送片段；网络可能把同一片段投递两次
```

③ 是关键：同一片段可能到达不止一次。片段内容必须让重发不改变结果。

**问题**

应该发"加 2"、`{A=2}`，还是 `{A=7}`？

**解释**

发 `{A=7}`。把三种写法各算一遍。合并规则：逐设备取最大值。

| 方案 | 片段内容与合并方式 | B 第一次收到（已知 A=5） | 重发再次收到 | 结果 |
| --- | --- | --- | --- | --- |
| 一 | "加 2"，执行加法 | 5 + 2 = 7 | 7 + 2 = 9 | 重发多算 2 次 |
| 二 | `{A=2}`，取最大值 | max(5, 2) = 5 | max(5, 2) = 5 | 停在 5，丢失 2 次操作 |
| 三 | `{A=7}`，取最大值 | max(5, 7) = 7 | max(7, 7) = 7 | 正确，重发不改变结果 |

逐步推理：

1. 方案一执行加法。重发会把同一次变化算两次。
2. 方案二发算术差。B 已有 5，2 不更大，B 停在 5。
3. 方案三发绝对累计值。第一次合并到 7，重发仍是 7。
4. 结论：片段省的是其他设备的分量，不是把累计值换成算术差。

现在才能引出本地操作方法：

- `increment(long delta)`：接非负增加量，返回本地分量增加后的新计数器。
- `increment()`：无参数，表示增加一次。
- `value()`：返回所有设备分量之和。
- `state()`：返回分量表，用于观察为什么结果是 7。

先补这些 TODO：复制 `counts`，只更新 `replicaId` 那一栏。负数拒绝，0 不改变计数。单栏更新和求和都检查 long 溢出。这部分复用第 2 章的累计计数思路。同步时不要调用 `increment`。

## 3. 发出去的消息丢了，下一轮还要取出来

**假设**

接着第 2 节。事情按这个顺序发生：

```text
① 手机取出待发片段 {A=7}，交给网络
② 网络丢包，电脑没有收到
③ 手机没有收到确认；手机本轮没有新操作
④ 下一轮，手机再次取出待发内容，准备重发
```

③ 是关键：没有确认，就不能认为 {A=7} 已送达。

**问题**

取出待发内容时如果同时清空待发记录，第 ④ 步还能发什么？

**解释**

1. 什么也发不出。{A=7} 已不在待发记录中，丢包后无法重发。
2. 取出待发内容的方法必须只读。
3. 这个方法叫 `pending()`。无参数，返回待发片段。不修改计数器，也不推进确认进度。
4. 连续调用 `pending()`，两次得到的内容相同。

计数片段用 `Delta` 表示，保存 `Map<String,Long>`：

- `counts()`：读片段内容。
- `empty()`：表示没有变化。
- `isEmpty()`：判断本轮是否需要发送业务负载。

取出规则：先判断本地累计值是否超过 `acked`。超过就发这一栏的绝对累计值；没超过就返回空片段。用具体值算一遍：

```text
情况一：counts = {A=7}，acked = 5
  判断：本地累计值 7 > 已确认值 5
  → 片段只放本地这一栏：{A=7}

情况二：counts = {A=7}，acked = 7
  判断：7 ≤ 7
  → 返回空片段 Delta.empty()

情况三：counts = {A=7, B=3}，acked = 5
  B=3 是远端分量，已经在 counts 里
  远端分量不进入本地待发内容
  → 片段仍是 {A=7}
```

<details>
<summary>计数 pending 的参考方法体：先自行写，再展开</summary>

```java
long local = countOf(replicaId);
if (local <= acked) return Delta.empty();
Map<String, Long> pending = new TreeMap<>();
pending.put(replicaId, local);
return new Delta(pending);
```

`countOf(id)` 是内部读取分量的辅助方法，缺失时返回 0。片段构造器保存只读快照。

</details>

## 4. B 收到同一片段两次，又收到较旧片段

**假设**

电脑已知 A=5。之后事情按这个顺序发生：

```text
① 电脑收到片段 {A=7}
② 网络重发，电脑再次收到 {A=7}
③ 一张更早发出的旧片段 {A=6} 现在才到达
```

③ 中的 {A=6} 是手机累计 6 次时取出的片段。它发出得早，到达得晚。

**问题**

接收方法要返回什么？是否需要记录"这是第几次收到消息"？

**解释**

不需要记录次数。逐步演算三次合并，规则是逐设备取最大值：

| 步骤 | 收到的片段 | 合并前电脑的 A 分量 | 逐栏计算 | 合并后 |
| --- | --- | --- | --- | --- |
| ① | {A=7} | 5 | max(5, 7) | 7 |
| ② | {A=7} | 7 | max(7, 7) | 7 |
| ③ | {A=6} | 7 | max(7, 6) | 7 |

逐步推理：

1. 三次合并后，A 分量始终是 7。
2. 重复投递和乱序的旧片段都不改变结果。
3. 累计值本身足以识别重叠信息，不靠消息次数去重。

由此引出 `apply(Delta delta)`：输入网络送来的片段，输出合并后的新计数器。它只逐设备取最大值，不执行加法，也不清自己的待发记录。

还有两个需求：

1. 网络可能把多张片段合成一批再投递。用 `Delta.merge(other)` 合批。
2. 偶尔仍要用完整状态同步。用计数器的 `merge(other)`。

三个方法共用同一个逐分量 max 规则。输入大小不同，不另造一套计数语义。

<details>
<summary>计数 apply、merge 与片段合批的实现思路</summary>

```java
// apply 的方法体
Objects.requireNonNull(delta, "delta");
return new DeltaGCounter(replicaId, join(counts, delta.counts), acked);

// merge 的方法体
Objects.requireNonNull(other, "other");
return new DeltaGCounter(replicaId, join(counts, other.counts), acked);
```

`join(left,right)` 复制左表，遍历右表，仅在右值大于已有值时更新。`Delta.merge` 返回 `new Delta(join(counts, other.counts))`。片段也满足幂等、交换、结合三律。

</details>

## 5. 电脑先收到了删除，后收到标签添加

**假设**

事情按这个顺序发生：

```text
① 手机添加"工作"标签，生成票 A1
② 手机发出添加片段："工作" → 票 {A1}
③ 用户删除"工作"，A1 进入手机的墓碑
④ 手机发出删除片段：删除票 {A1}
⑤ 网络换序：电脑先收到④的删除片段，后收到②的添加片段
```

⑤ 是关键：删除片段到达时，电脑还没有"工作"标签。

**问题**

删除到达时电脑没有"工作"，能直接忽略这张删除票吗？

**解释**

1. 不能。
2. 忽略它之后，②的旧添加片段到达，"工作"会重新出现。
3. 先保存删除的证据：把 A1 记入墓碑。
4. 旧添加到达时，墓碑给出剔除 A1 的理由。
5. 删除只针对已观察到的票。没见过的并发新票不受影响。

逐步演算电脑的合并过程：

| 步骤 | 电脑收到 | 合并后 entries | 合并后墓碑 | "工作"可见 |
| --- | --- | --- | --- | --- |
| ⑤a | 删除片段 removed={A1} | 无 | {A1} | 否 |
| ⑤b | 添加片段 "工作"→{A1} | 先并入"工作"→{A1}，再剔除墓碑中的 A1，该元素退出映射 | {A1} | 否 |

所以标签片段需要两部分：

- `added`：元素到新增票集合的映射。
- `removed`：删除票的集合。

片段类型写作 `DeltaORSet.Delta<String>`。它自己带元素类型；它是静态嵌套类，不写成 `DeltaORSet<String>.Delta`。

先补本地操作：

- `add(element)`：接标签，返回多一张唯一票的新集合。递增本地 `seq`，把票加入 `entries`，同时加入 `unackedAdded`。每次添加都有新票，即使界面已经显示该标签。
- `remove(element)`：接标签，返回删除当前已知票后的新集合。从 `entries` 移除该元素，把这些票加入 `tombstones` 和 `unackedRemoved`，并从 `unackedAdded` 移除它们。不存在的元素不产生新的删除证据。

**本步实现**：先补 `empty(replicaId)` 的空状态，再补 `add/remove`。`contains(element)` 和 `elements()` 只从存活票映射读界面结果。身份为空、票序号耗尽时应拒绝继续生成票，不得复用旧票。

<details>
<summary>添加与删除需要更新哪些数据</summary>

```text
add("工作")：
  新票 = Tag(replicaId, seq + 1)
  entries["工作"] = 原有票 ∪ 新票
  unackedAdded = 原待发添加票 ∪ 新票
  其他状态保持原快照

remove("工作")：
  tags = entries["工作"]；不存在就返回原状态
  entries 去掉 "工作"
  tombstones = 原墓碑 ∪ tags
  unackedAdded = 原待发添加票 - tags
  unackedRemoved = 原待发删除票 ∪ tags
```

删除不增加添加票序号。同一批还未确认时，先添加再删除可以只待发删除票：对方可能沿先前的一次发送见过添加。

</details>

## 6. 标签片段怎样取出，怎样并入？

**假设**

事情按这个顺序发生：

```text
① 手机之前添加过标签"临时"，票是 A2，电脑已确认
② 本轮手机添加"工作"，生成票 A3；A3 进入待发添加票
③ 本轮手机删除"临时"；A2 进入墓碑，也进入待发删除票
④ 手机取出本轮片段，发给电脑
⑤ 电脑应用这张片段
```

① 中的"临时"代表手机上的旧标签：它们的票早已确认，本轮没有变化。

**问题**

`pending()` 怎样挑出这两部分？电脑的 `apply()` 又怎样处理？

**解释**

`pending()` 的取出规则：只挑 `unackedAdded` 指定的存活票，再附上 `unackedRemoved`。输入为空，输出 `Delta<E>`，不消费待发记录。遍历 `entries` 时，对每个标签取"当前票 ∩ 待发添加票"，空集合不写进片段。用具体值算一遍：

```text
手机当前状态：
  entries = { 工作→{A3}, …其余旧标签（票已确认）… }
  unackedAdded = {A3}
  unackedRemoved = {A2}

逐个标签计算交集：
  工作：{A3} ∩ {A3} = {A3}    → 写入片段
  旧标签：当前票 ∩ {A3} = ∅   → 不写入片段

片段 = added{ 工作→{A3} }, removed{A2}
```

`apply(Delta<E>)` 的并入规则：添加票按元素取并集，删除票取并集，再从每个元素剔除墓碑票。没有存活票的元素退出映射。保留接收方身份和本地待发记录。电脑端算一遍：

```text
电脑合并前：entries = { 临时→{A2} }，墓碑 = {}
① 删除票并入墓碑：{} ∪ {A2} = {A2}
② 添加票按元素并入：工作→{A3}；临时→{A2} 不变
③ 逐元素剔除墓碑票：
   工作：{A3} 与墓碑无交集 → 保留
   临时：{A2} 全部在墓碑中 → 该元素退出映射
合并后：entries = { 工作→{A3} }，墓碑 = {A2}
```

`Delta<E>.merge(other)` 只合并传输片段：两部分各自取并集，不在片段内部剔除添加票。规范化存活状态是 `apply` 的责任。在片段内部剔除会使合批结果与分别投递不同。

<details>
<summary>标签 pending 的参考方法体</summary>

```java
Map<E, Set<ORSet.Tag>> added = new HashMap<>();
for (Map.Entry<E, Set<ORSet.Tag>> entry : entries.entrySet()) {
    Set<ORSet.Tag> tags = new HashSet<>(entry.getValue());
    tags.retainAll(unackedAdded);
    if (!tags.isEmpty()) {
        added.put(entry.getKey(), Collections.unmodifiableSet(tags));
    }
}
if (added.isEmpty() && unackedRemoved.isEmpty()) return Delta.empty();
return new Delta<E>(Collections.unmodifiableMap(added), unackedRemoved);
```

`apply` 与完整状态 `merge` 共用上述"并集后剔除"流程。合并遇到属于自身身份的票时，更新本地序号高水位，避免随后生成相同票。完整方法参考 [DeltaORSet 原参考类](../DeltaORSet.java)，不要让练习直接调用答案类。

</details>

## 7. 已经确认收到，为什么还一直待发？

**假设**

事情按这个顺序发生：

```text
① 电脑应用了本轮片段 {A=7}，把确认送回手机
② 手机收到确认
③ 手机没有后续修改
```

② 之后，{A=7} 不需要再重发。③ 说明也没有新变化要发。但手机的 `acked` 仍是 5，`pending()` 仍会取出 {A=7}。

**问题**

怎样停止重发，又保留笔记的次数和标签？

**解释**

1. 需要单独的 `ack()`。无参数，返回待发记录已清理的新对象。
2. 复制状态不变。次数、标签和墓碑都保留。
3. `ack()` 不查询网络。调用者必须先取得确认，再调用 `ack()`。

计数器把 `acked` 推进到当前本地累计值。用具体值算一遍：

```text
ack 前：counts = {A=7}，acked = 5
  判断 7 > 5 → pending() = {A=7}
ack 后：counts = {A=7}，acked = 7
  判断 7 ≤ 7 → pending() = 空片段
value() 在 ack 前后都是 7
```

标签集合清空两个待发票集合：

```text
ack 前：unackedAdded = {A3}，unackedRemoved = {A2}
ack 后：unackedAdded = {}，unackedRemoved = {}
entries、tombstones 前后相同
```

<details>
<summary>ack 的参考方法体</summary>

```java
// DeltaGCounter.ack()
return new DeltaGCounter(replicaId, counts, countOf(replicaId));

// DeltaORSet.ack()
return new DeltaORSet<E>(replicaId, seq, entries, tombstones,
        Collections.<ORSet.Tag>emptySet(), Collections.<ORSet.Tag>emptySet());
```

原参考：[DeltaGCounter](../DeltaGCounter.java)、[DeltaORSet](../DeltaORSet.java)。相等判断只比较复制状态，因此不能拿 `equals` 判断两个对象是否都已确认。

为了让同一消息重发前后的结果可以比较，计数器与计数片段的 `equals/hashCode` 都只用 `counts`；标签集合用 `entries/tombstones`，标签片段用 `added/removed`。`toString` 显示这些字段便于观察；它不参与合并判断。

</details>

## 8. B 确认了，C 没收到；A 还继续写了

**假设**

事情按这个顺序发生：

```text
① 手机取出片段 d1 = {A=7}，发给电脑和备用机
② 电脑应用了 d1，送回确认
③ 备用机离线，没有收到 d1
④ 等待确认期间，手机又记了 1 次操作，本地累计达到 8
```

④ 之后，手机上有两批变化：d1 = {A=7}，电脑已确认、备用机未确认；新变化 d2 对应片段 {A=8}，没有任何确认。{A=8} 是绝对累计值，内容覆盖 d1。

**问题**

收到电脑对 d1 的确认，就对当前手机调用 `ack()`，会漏什么？

**解释**

1. 备用机会缺 d1。`ack()` 之后 d1 不在待发记录中，手机不会再发它。
2. d2 会被一起清掉。无参数 `ack()` 把确认进度推进到当前累计值，不区分批次。
3. `ack()` 不知道接收者和批次，不能替外部协议做这个判断。

用具体值算一遍：

```text
ack 前：counts = {A=8}，acked = 5
  d1 = {A=7}：电脑已确认，备用机未确认
  d2 对应 {A=8}：无人确认
调用 ack()：acked 从 5 推进到 8
  判断 8 ≤ 8 → pending() 变空
  后果一：备用机永远收不到 d1 的 7 次操作
  后果二：d2 的第 8 次操作被当成已确认，同样发不出去
```

本章采用容易核对的教学约束：

1. 成员固定。
2. 一轮先暂停写入。每个来源把待发内容直接发给所有其他成员。
3. 全部成员应用并确认后，才调用 `ack()`。
4. 然后开始下一轮。

这个约束使待发记录有明确的确认范围。

若要持续异步写入，需要按对端、批次或水位确认的发送协议。保留旧不可变对象不等于解决了这个问题：旧对象 ack 后，新对象上的待发记录不会自动得到正确的部分清理。

## 9. 想让 B 替 A 把变化传给 C

**假设**

事情按这个顺序发生：

```text
① 手机把片段 {A=7} 发给电脑，电脑应用了它
② 手机没有把片段发给备用机
③ 电脑取出自己的 pending()，发给备用机
```

③ 是这条转发路径的关键：备用机只能收到电脑待发记录里的内容。

**问题**

备用机能靠这条路径拿到手机的变化吗？

**解释**

1. 不能。
2. `apply` 只更新复制状态，不把远端片段加入本地待发记录。
3. 电脑的待发记录里没有手机的变化。
4. 因此本章要求每个来源把片段直接发给所有成员。

用具体值算一遍：

```text
电脑应用 {A=7} 后：counts = {A=7}，其中 A 是远端分量
电脑取出 pending()：
  本地分量 B = 0，已确认值 acked = 0
  判断 0 ≤ 0 → 返回空片段
备用机收到空片段，得不到 {A=7}
```

继续扩展时可以保存待转发片段、按对端进度补发，或周期性全量同步。只传进度表而不保留缺失内容，补不回变化。本章不实现这些协议。

无新修改时业务片段可以为空，确认和保活仍可能有流量。标签墓碑仍会积累。`DeltaORSet` 没有 `version/gc`；第 9 章增强的是普通 `ORSet` 与 `RGA`，不是清空这章的待发记录。

## 10. 验收本章能力

补完骨架后，应能解释三个可见结果：

1. 累计 5 次确认后再增加 2 次，片段是 `{A=7}`，重发不改变结果。
2. 删除先到、旧添加后到，标签仍不可见。
3. ack 前可反复读待发内容，ack 后只清待发记录，复制状态不变。

测试类：[DeltaLearningTest](../../../../test/java/crdt/learnning/DeltaLearningTest.java)。在项目根目录运行：

```bash
cd /Users/lizixuan/Documents/IdeaProjects/LeetCode
JAR="$HOME/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:$HOME/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
CRDT_BUILD=$(mktemp -d /tmp/crdt-learnning.XXXXXX)
javac -encoding UTF-8 -cp "$JAR" -d "$CRDT_BUILD" src/main/java/crdt/learnning/*.java src/test/java/crdt/learnning/*.java
java -cp "$CRDT_BUILD:$JAR" org.junit.runner.JUnitCore crdt.learnning.DeltaLearningTest
```

骨架中的 TODO 尚未补全时，测试失败是正常起点。补齐后本章测试应通过。这验证的是计数、标签与教学同步场景，不代表测过真实网络流量或完整异步确认协议。
