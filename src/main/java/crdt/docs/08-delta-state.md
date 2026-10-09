# 第 8 步：只多写一次笔记，为什么要把全部历史再发一遍？

[← 目录](../README.md) ｜ 上一步：[第 7 步 收敛性验证](07-收敛性证明.md) ｜ 下一步：[第 9 步 墓碑 GC](09-墓碑GC.md)

上章已经能让离线笔记的计数、标签和正文在重复、换序同步后收敛。本章给应用增加一种能力：**把本地尚未确认的变化单独发送，丢包后还能重发。**

你将补全 [DeltaGCounter 骨架](../learnning/DeltaGCounter.java) 和 [DeltaORSet 骨架](../learnning/DeltaORSet.java)。验收入口是 [DeltaLearningTest](../../../../test/java/crdt/learnning/DeltaLearningTest.java)。完成后能看见：操作次数不因重发算重，删除先到的标签不会被旧添加带回来，确认后待发内容变空而笔记状态不变。

这里只统计笔记的操作次数，不统计正文长度；A、B、C 是不同设备的固定身份。返回的新对象要接住，旧对象作为历史快照保留。

## 1. 笔记已经有很多标签，又多写了一次

**假设**：手机保存着上千个标签和删除记录，这次只记下一次笔记操作，另加一个“工作”标签。

**问题**：电脑需要再次收到全部旧标签、全部设备的操作次数吗？

**解释**：不需要重复发没变化的部分，但收到的小片段仍要能和已有状态合并。

先把完整状态拆开看：计数是“设备 → 累计次数”；标签是“标签 → 添加票”，外加删除票。只取发生变化的分量或票，就得到一小片可以合并的状态。这个片段叫 **delta**，不是一句要求接收方执行的命令。

因此先在两个练习类中区分“笔记的复制状态”和“本地待发记录”。计数类保留 `counts`，另存上次确认的本地累计值 `acked`；集合类保留 `entries/tombstones`，另存 `unackedAdded/unackedRemoved`。设备身份与发送进度不参与复制状态的相等判断。

这些字段都是快照。构造新对象时复制需要修改的集合，不直接修改旧对象持有的集合；返回给外部的集合也不能任意写入。

**本步实现**：先定义字段，再补骨架已有的构造器。公开计数构造器接设备身份，建立空 `counts` 与 `acked=0`；私有构造器接已有快照，保存身份、状态及确认进度。集合的空状态在后面引出 `empty` 时补全。

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

**假设**：B 已经知道 A 累计 5 次。A 又操作 2 次，准备发送消息，网络可能重发。

**问题**：应该发“加 2”、`{A=2}`，还是 `{A=7}`？

**解释**：发 `{A=7}`。B 继续逐设备取最大值：第一次得到 7，再收一次仍是 7。

发“加 2”并执行加法，重发会重复计数；发 `{A=2}` 再取最大值，B 会停在 5。delta 省的是其他设备的分量，不是把累计值换成算术差。

现在才能引出本地操作方法：`increment(long delta)` 接非负增加量，返回本地分量增加后的新计数器；无参数 `increment()` 表示增加一次。`value()` 返回所有设备分量之和，`state()` 返回分量表，方便观察为什么结果是 7。

先补这些 TODO：复制 `counts`，只更新 `replicaId` 那栏；负数拒绝，0 不改变计数；单栏更新和求和都检查 long 溢出。这个部分复用第 2 章的累计计数思路，不能在同步时调用 `increment`。

## 3. 发出去的消息丢了，下一轮还要取出来

**假设**：A 取到 `{A=7}`，交给网络后丢包；A 此时没有新操作。

**问题**：取待发内容时如果顺手清空，下一轮还能发什么？

**解释**：什么也发不出。因此需要一个只读方法 `pending()`：无参数，返回待发片段，不修改计数器，也不推进确认进度。

计数片段 `Delta` 保存 `Map<String,Long>`。它的 `counts()` 用于读内容，`empty()` 表示没变化，`isEmpty()` 用于决定本轮是否需要业务负载。连续调用 `pending()` 应得到相同内容。

先判断本地累计值是否超过 `acked`。超过就发这一栏的绝对累计值；没超过就返回空片段。远端分量已经在 `counts` 里，也不应因此全部进入本地待发内容。

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

**假设**：B 先后收到 `{A=7}`、`{A=7}`、`{A=6}`。

**问题**：接收方法要返回什么？是否需要记录“这是第几次收到消息”？

**解释**：B 的 A 分量始终是 7，不需要靠消息次数去重。累计状态本身足以识别重叠信息。

由此引出 `apply(Delta delta)`：输入网络送来的片段，输出合并后的新计数器。它只逐设备取最大值，不执行加法，也不清自己的待发记录。

还有两个需求：网络可能把片段合为一批，调用 `Delta.merge(other)`；偶尔仍要用完整状态同步，调用计数器的 `merge(other)`。三个方法共用同一个逐分量 max 规则，输入大小不同，不另造一套计数语义。

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

**假设**：A 添加“工作”得到票 A1，发过添加片段；随后删除 A1。B 因网络换序，先收到删除片段，再收到旧添加片段。

**问题**：删除到达时 B 没有“工作”，能直接忽略它吗？

**解释**：不能。先保存 A1 已删除的证据，旧添加到达时才有理由剔除它。这里删除的仍是已观察到的添加票，没见过的并发新票仍然存活。

所以标签片段需要两部分：`added` 是元素到新增票集合的映射，`removed` 是新增删除票集合。`DeltaORSet.Delta<String>` 自己带元素类型；它是静态嵌套类，不写成 `DeltaORSet<String>.Delta`。

先补本地操作。`add(element)` 接标签，返回多一张唯一票的新集合：递增本地 `seq`，把票加入 `entries`，同时加入 `unackedAdded`。每次添加都有新票，即使界面已经显示该标签。

`remove(element)` 接标签，返回删除当前已知票后的新集合：从 `entries` 移除该元素，把这些票加入 `tombstones` 和 `unackedRemoved`，并从 `unackedAdded` 移除它们。不存在的元素不产生新的删除证据。

**本步实现**：先补 `empty(replicaId)` 的空状态，再补 `add/remove`；`contains(element)` 和 `elements()` 只从存活票映射读界面结果。身份为空、票序号耗尽时应拒绝继续生成票，不得复用旧票。

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

**假设**：A 仍有许多旧标签，但本轮只有“工作”的一张新票和“临时”的删除票。

**问题**：`pending()` 怎样挑出这两部分？B 的 `apply()` 又怎样处理？

**解释**：A 只挑 `unackedAdded` 指定的存活票，再附上 `unackedRemoved`；B 则先合并删除证据与添加票，最后清掉被删除的票。

`pending()` 输入为空，输出 `Delta<E>`，也不消费缓冲。遍历 `entries` 时对每个标签取“当前票 ∩ 待发添加票”，空集合不写进片段。

`apply(Delta<E>)` 输入片段，输出新标签集合。添加票按元素取并集，删除票取并集，再从每个元素剔除墓碑票；没有存活票的元素退出映射。保留接收方身份和本地待发记录。

`Delta<E>.merge(other)` 只是合并传输片段：两部分各自取并集，**不在片段内部剔除添加票**。规范化存活状态是 `apply` 的责任，否则合批规则就和直接分别投递不同了。

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

`apply` 与完整状态 `merge` 共用上述“并集后剔除”流程。合并遇到属于自身身份的票时，更新本地序号高水位，避免随后生成相同票。完整方法参考 [DeltaORSet 原参考类](../DeltaORSet.java)，不要让练习直接调用答案类。

</details>

## 7. 已经确认收到，为什么还一直待发？

**假设**：B 已应用本轮片段并确认，A 没有后续修改。

**问题**：怎样停止重发，又保留笔记的次数和标签？

**解释**：需要单独的 `ack()`。无参数，返回待发记录已清理的新对象，复制状态不变。这个方法不查询网络；调用者必须先取得相应确认。

计数器把 `acked` 推进到当前本地累计值；标签集合清空两个待发票集合。`ack` 后同一对象分支的 `pending().isEmpty()` 为 true，但 `value()`、`elements()` 和墓碑仍保留。

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

**假设**：A 发出 d1，B 确认，C 仍离线。等确认期间，A 又产生 d2。

**问题**：收到 B 对 d1 的确认，就对当前 A 调用 `ack()`，会漏什么？

**解释**：C 会缺 d1；当前无参数 `ack()` 还可能把没有确认的 d2 一起清掉。方法不知道接收者和批次，不能替外部协议做这个判断。

本章采用容易核对的教学约束：固定成员；一轮先暂停写入，每个来源把待发内容直接发给所有其他成员，全部应用并确认后再 ack，再开始下一轮。这个约束使当前全局缓冲有明确的确认范围。

若要持续异步写入，需要按对端、批次或水位确认的发送协议。保留旧不可变对象不等于已经解决此问题：旧对象 ack 后，新对象上的待发记录不会自动得到正确的部分清理。

## 9. 想让 B 替 A 把变化传给 C

**假设**：B `apply` 了 A 的片段，随后只把 B 的 `pending()` 发给 C。

**问题**：C 能靠这条路径拿到 A 的变化吗？

**解释**：当前实现不能。`apply` 只更新复制状态，不把远端片段加入本地发送缓冲，所以本章要求每个来源直接广播。

继续扩展时可以保存待转发片段、按对端进度补发，或周期性全量同步；只传进度表而不保留缺失内容，补不回变化。本章不实现这些协议。

无新修改时业务片段可以为空，确认和保活仍可能有流量。标签墓碑仍会积累；`DeltaORSet` 没有 `version/gc`，第 9 章增强的是普通 `ORSet` 与 `RGA`，不是清空这章的发送缓冲。

## 10. 验收本章能力

补完骨架后，应能解释三个可见结果：累计 5 次确认后再增加 2 次，片段是 `{A=7}`；删除先到、旧添加后到，标签仍不可见；ack 前可反复读待发内容，ack 后只清发送记录。

测试类：[DeltaLearningTest](../../../../test/java/crdt/learnning/DeltaLearningTest.java)。在项目根目录运行：

```bash
cd /Users/lizixuan/Documents/IdeaProjects/LeetCode
JAR="$HOME/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:$HOME/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
CRDT_BUILD=$(mktemp -d /tmp/crdt-learnning.XXXXXX)
javac -encoding UTF-8 -cp "$JAR" -d "$CRDT_BUILD" src/main/java/crdt/learnning/*.java src/test/java/crdt/learnning/*.java
java -cp "$CRDT_BUILD:$JAR" org.junit.runner.JUnitCore crdt.learnning.DeltaLearningTest
```

骨架中的 TODO 尚未补全时测试失败是正常起点；补齐后本章测试应通过。这验证的是计数、标签与教学同步场景，不代表测过真实网络流量或完整异步确认协议。
