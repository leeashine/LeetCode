# 第 6 步：笔记正文，两边都在 ab 中间插字

**上一步**：上章让笔记标签支持离线添加、取消和重加，靠每次添加的身份保存历史。

**本课任务**：手机 A 和电脑 B 同时编辑正文 `ab`，在中间插入 X、Y，并删除原来的 b。

**完成后**：把界面的下标转换成稳定字符身份，写出可同步的插入、删除和文本读取。

**练习入口**：[learnning/RGA.java](../learnning/RGA.java) 和 [RGALearningTest.java](../../../../test/java/crdt/learnning/RGALearningTest.java)，按正文顺序填 TODO。

[← 目录](../README.md) ｜ 上一步：[OR-Set 标签](05-ORSet.md) ｜ 下一步：[收敛性验证](07-收敛性证明.md)

## 1. 下标为什么不能直接拿来同步？

**假设**：两边正文最初都是 `ab`。A 在下标 1 插入 X，B 在下标 0 插入 Y。

```text
A：ab → aXb
B：ab → Yab
```

**问题**：把 A 的“在下标 1 插 X”直接在 B 上执行，会得到什么？

**解释**：会得到 `YXab`，X 跑到 a 前面。下标只在当时的快照里有意义；B 插字后，相同数字已经指向别的位置。

界面仍然可以使用下标，但状态要记“在原来那个 a 后面插 X”。给每个字符一个稳定 ID，即使正文里有两个 a，它们也能区分。

本章处理 Java 的 `char`：一个 UTF-16 代码单元。一个 emoji 可能占两个代码单元，本实现没有按用户看到的完整字符分段，也没有富文本、光标、选区或撤销。

## 2. 先定义字符身份与锚点

**假设**：初始 `ab` 由设备 S 写出，a 编号 1，b 编号 2；A、B 都收到这份状态。

```text
a.id = (1, S)，a.parentId = null
b.id = (2, S)，b.parentId = a.id
```

**问题**：A、B 同时产生第 3 个节点，怎样避免身份相同？又怎样保留“插在 a 后”这个位置？

**解释**：身份由逻辑编号和设备 ID 一起组成。A 发 `(3,A)`，B 发 `(3,B)`。每个字符节点再记录前驱锚点 `parentId` 和字符 `value`。

先补 `NodeId(long timestamp, String replicaId)`：输入编号、设备 ID，返回不可变身份。`timestamp` 是逻辑编号，不是墙上时钟。它的比较规则是先比编号，相同再按设备 ID 的字符串顺序比较：

```java
int byTimestamp = Long.compare(timestamp, other.timestamp);
return byTimestamp != 0 ? byTimestamp : replicaId.compareTo(other.replicaId);
```

`equals` 比较这两项，`hashCode` 使用这两项，否则相同身份无法在 `Map` 中去重。

再补 `Node(NodeId id, NodeId parentId, char value)`：输入本节点身份、锚点、字符，返回不可变节点。`parentId=null` 表示挂在虚拟根下；不是存一个可见字符。节点相等必须比较三项：相同身份但不同锚点或字符意味着冲突，不能当成重复消息。

接着在骨架里定义状态字段：

```java
String replicaId;             // 本设备后续写入使用的身份
Map<NodeId, Node> nodes;       // 所有字符节点，包括已删除的
Set<NodeId> tombstones;        // 本章只保存已删除字符 ID
```

本章直接用 Set 保存“这个 ID 已删除”，填写骨架的三参数内部构造器。第 9 章再把墓碑升级为带删除见证的 Map，并填写四参数构造器。insert/delete/merge 的基础流程不调用 CausalStamp，先让正文编辑能独立完成。

`new RGA(replicaId)` 输入本设备身份，返回空正文状态；`replicaId()` 无输入，直接返回这个身份，方便检查后续写入归属。写入方法都返回新实例；修改前复制集合，避免改变旧快照。

## 3. A 在中间输入 X：实现 insert

**假设**：A 当前看见 `ab`，用户要求 `insert(1, 'X')`，结果应是 `aXb`。

**问题**：下标 1 应转换成哪个锚点？插在开头时怎么办？

**解释**：插入位置的左邻居是 a，所以新节点挂在 a 后面。下标 0 没有左邻居，挂到虚拟根；末尾位置的左邻居就是最后一个可见字符。

`insert(int index, char c)` 接收当前可见正文的位置和字符，返回插入后的新状态。允许 `index` 从 0 到当前长度，其他位置抛 `IndexOutOfBoundsException`。

把 TODO 拆成三段：

```text
visible = 按确定顺序读出的可见节点
校验 0 <= index <= visible.size()
parentId = index == 0 ? null : visible[index - 1].id

maximum = 当前 nodes 中最大的 timestamp，空状态取 0
id = NodeId(maximum + 1, replicaId)

复制 nodes，加入 Node(id, parentId, c)
返回新状态，沿用墓碑
```

a、b 的编号分别是 1、2，所以 X 的身份是 `(3,A)`。取**所有现存节点**的最大编号，包括已删除节点，不能只看可见正文。合并后再插字也会接在已知最大编号之后。

这里用到了“按确定顺序读可见节点”的辅助方法。先按上面确定插入输入和数据，再实现下一节的读取，最后回来连通 `insert`。

## 4. 把节点读成正文：实现 visibleNodes、text、size

**假设**：A、B 都在 `ab` 中间插字。X、Y 以及原来的 b 都挂在 a 下：

```text
虚拟根
└─ a (1,S)
   ├─ b (2,S)
   ├─ X (3,A)
   └─ Y (3,B)
```

**问题**：这些节点谁先读？只按全部节点的编号排序够不够？

**解释**：先看本地：若先读编号 2 的 b，再读新 X，就会变成 `abX`，不符合中间插入。我们约定同一锚点下的新节点先读，即兄弟按 ID 降序访问。

编号同为 3 时，B 的字符串顺序大于 A，所以 Y 在 X 前。这个规则只用于裁决并发顺序，不声称 Y 在真实世界更晚写入。结果应是 `aYXb`。

还不能把所有节点直接排序。如果 A 输入一段 `X1`，结构是 a → X → 1；B 输入 `Y2`，结构是 a → Y → 2。要先读某个节点，再读完整个后代分支，然后才读下一个兄弟，得到 `aY2X1b`。这种读法叫前序遍历。

为复用读取规则，在类内实现辅助方法 `visibleNodes()`，以及负责压栈的 `pushChildren`。`visibleNodes()` 无输入，返回按这个规则排序、且排除墓碑的节点列表。列表保留身份信息，供插入、删除定位；`text()` 无输入，依次拼接这些节点的 `value`，返回字符串；`size()` 返回列表长度。

先建立“锚点 → 子节点列表”，再排序、遍历：

```text
children = 按 node.parentId 分组所有节点
每份兄弟列表按 NodeId 升序排序
把虚拟根的子节点依次压入栈
while 栈不为空:
    node = 出栈
    如果 node.id 不在墓碑里，将 node 加进 visible
    无论是否已删除，都把 node 的子节点依次压入栈
```

**升序排序不等于升序访问**。参考实现的 `pushChildren` 从列表开头依次 `push`；栈后进先出。对 `[b,X,Y]` 依次压栈，先 `pop` 的是 Y，再是 X，最后是 b。子节点又压到栈顶，所以会先读完整个分支。

把这个顺序写在纸上再填 `pushChildren`。如果反过来压栈，两个副本可能仍然输出相同字符串，但本地“中间插入”的语义已经错了。

## 5. B 删除原来的 b：实现 delete

**假设**：A 当前看见 `aXb`，B 当前看见 `aYb`。B 执行 `delete(2)`，删除自己看见的 b。

**问题**：同步后 A 怎样确定删的是原 b？能否直接移除 b 节点？

**解释**：本地先把下标 2 转成 b 的 ID `(2,S)`，保存针对这个 ID 的删除。后续同步的是删除身份，不是在 A 的新正文里重新执行下标删除。

`delete(int index)` 接收当前可见正文的下标，返回增加墓碑的新状态。允许 `0 <= index < size()`；空正文或越界删除应抛 `IndexOutOfBoundsException`。

核心算法：

```text
visible = visibleNodes()
校验下标
id = visible[index].id
复制墓碑，记下 id 已删除
返回新状态，保留 nodes
```

参考实现还给墓碑记录删除见证；先理解“以 ID 为目标、保留节点”这条主线，第 9 章再补见证的含义。

再假设正文结构是 a → b → c，用户删 b，期望看到 `ac`。若把 b 从 `nodes` 删掉，从 a 就无法走到 c；所以 b 留作墓碑锚点。前面读取时只跳过它的输出，仍然把它的子节点压栈，c 才能留下。

## 6. 联网合并正文：实现 merge

**假设**：两边都有初始 a、b。A 有新增 X；B 有新增 Y，并已给 b 记墓碑。

**问题**：同一个 a 会收到两份，应该重复显示吗？一个 ID 若对应两个不同字符又怎么办？

**解释**：`merge(RGA other)` 输入对方状态，返回合并后的新状态。空参数应拒绝；后续本地写入继续用接收者的 `replicaId`。

先复制本地节点，遍历对方节点：

```text
ID 不存在：加入整个节点
ID 已存在，节点三项相同：重复消息，保留一份
ID 已存在，但字符或锚点不同：抛异常，身份发生冲突
```

然后合并墓碑，任何一方已经删除的 ID 都继续删除。本章墓碑直接取 Set 并集。第 9 章升级为 Map 后，同 ID 的多个删除见证再统一成确定选择。

不需要在合并时生成最终字符串，也不重算锚点。合并完成后让 `text()` 按相同遍历规则读取，便得到 `aYX`：X、Y 都保留，原 b 不输出。

合法输入必须保证锚点指向已知节点或虚拟根，结构无环。参考实现检查相同 ID 的内容冲突，但没有完整验证任意外部导入的缺失锚点或环；本章通过构造、插入和合并合法状态维持这些条件。

## 7. 同样的正文等于同样的状态吗：实现 equals

**假设**：两个状态当前都显示 `ac`，其中一个还保存被删的 b 作为 c 的锚点。

**问题**：只比较 `text()` 能验证同步完成吗？

**解释**：不能。字符身份、锚点和删除记录决定后续插入与合并，不能只比较当前显示。

`equals(Object other)` 接收任意对象，返回它是否为 RGA 且 `nodes`、`tombstones` 都相等。`hashCode()` 使用同样两份数据。基础阶段墓碑是 Set，按删除 ID 比较；第 9 章升级为 Map 后，见证也参与参考实现的比较。本地副本 ID 和进度不参与。

调试字符身份时，`NodeId.toString()` 无输入，返回编号和设备 ID 的描述；调试当前页面时，`RGA.toString()` 返回 `text()`。这些输出只便于观察，不代替状态比较。

因此测试既要比较合并后的状态，也要检查具体正文。两个状态一起读成错误的 `abXY`，仍可能相等，但没有完成“在 ab 中间插字”的任务。

## 8. 跑通一篇正文的完整操作

**假设**：设备 S 写好 `ab`，A、B 各用自己的身份加入。每次编辑都保存返回的新状态。

**问题**：每行之后分别能看到哪些字符？合并是否保留两次输入并准确删除 b？

```java
RGA shared = new RGA("S").insert(0, 'a').insert(1, 'b');
RGA a = new RGA("A").merge(shared).insert(1, 'X'); // aXb
RGA b = new RGA("B").merge(shared).insert(1, 'Y'); // aYb
RGA joined = a.merge(b);                           // aYXb
b = b.delete(2);                                  // aY
joined = a.merge(b);                              // aYX
```

**解释**：初始字符拥有相同 ID，新字符各用 A、B 身份，删除命中原 b。新设备应使用 `new RGA(myId).merge(sharedState)`；不能拿同一个写入身份的状态分叉后各自插不同字符，否则可能生成相同 ID。

标记为第 9 章的 `version()`、`tombstoneCount()`、`gc()` 暂时留到那里；本章测试不依赖这些方法。完成核心 TODO 后，对照 [完整参考 RGA.java](../RGA.java) 核对结构、排序与压栈，特别是墓碑节点仍遍历后代这一行。

字符 ID 的 `timestamp` 与 `version` 不是同一个编号：前者由现存节点最大值推出，后者记录本设备插入和删除的修改进度。墓碑回收留到 [第 9 章](09-墓碑GC.md)。当前参考实现没有保留跨 GC、重启的完整编号高水位；GC 删除最大节点后再次插入可能复用旧 ID，这个边界尚未解决。

## 验证你的实现

[RGALearningTest.java](../../../../test/java/crdt/learnning/RGALearningTest.java) 检查开头、中间、末尾插入，并发插字、按身份删除、墓碑后代与合并结果。未填完 TODO 时预期失败；补完后应通过。

在仓库根目录运行：

```bash
JAR="$HOME/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:$HOME/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar"
CRDT_BUILD=$(mktemp -d /tmp/crdt-learning.XXXXXX)
javac -encoding UTF-8 -cp "$JAR" -d "$CRDT_BUILD" src/main/java/crdt/learnning/*.java src/test/java/crdt/learnning/*.java
java -cp "$CRDT_BUILD:$JAR" org.junit.runner.JUnitCore crdt.learnning.RGALearningTest
```

**下一步** → [检查“大家相同”和“业务结果正确”](07-收敛性证明.md)
