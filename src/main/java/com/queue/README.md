# Disruptor + Redis 队列（《亿级流量网站架构核心技术》§15.9，pp.303–311）

复现书中第 15 章“队列术”末尾的通用 EventQueue 框架：Redis list 做可靠队列，
Disruptor 做 JVM 内高吞吐分发。

## 队列模型（p311 命名约定）

| 队列 | Redis key | 作用 |
| --- | --- | --- |
| 等待队列 | `queueName` | 生产者 `enqueueToLeft` LPUSH（`<10000` 条时排重） |
| 本地处理队列 | `queueName_processing_queue_<本机IP>` | `next()` 用 RPOPLPUSH 挪入，成功 `success()` LREM 移除；崩溃后需人工/定时任务把僵死任务挪回等待队列 |
| 失败队列 | `queueName_failed_queue` | `fail()` 重试 `processingErrorRetryCount` 次仍失败的任务 |
| 备份队列 | `queueName_bak_queue_<小时>` | 每小时一个（24 个/天）镜像队列，超 `maxBakSize` FIFO 裁剪，用于重构/数据回滚 |

## 任务流

```
生产者 ──enqueueToLeft LPUSH──▶ 等待队列
EventPublishThread（每队列一个） ──RPOPLPUSH──▶ 本地处理队列
本地处理队列 ──publishEvent──▶ RingBuffer
WorkerPool（threadPoolSize 个 WorkHandler） ──按 eventType 路由──▶ EventHandler
成功 ──queue.success(key)──▶ LREM 处理队列
失败 ──queue.fail(key)──▶ 重试 N 次 ──▶ 失败队列
```
书中为每个 EventQueue 启动一个 EventPublishThread（一个发布线程对应一种事件类型），并指出可以优化为全局只有一个发布线程轮询所有队列（书中原话“可以优化为只有一个”）。

## 类映射（书 → 本包）

| 书中 | 本实现 |
| --- | --- |
| Event / DefaultEventFactory | `com.queue.Event` / `com.queue.DefaultEventFactory` |
| EventQueue（RedisTemplate + Lua） | `com.queue.EventQueue`（`QueueRedis` 抽象 + `EventQueueScript`） |
| Spring Data Redis RedisTemplate | `com.queue.JedisQueueRedis`（Jedis，`eval` 传 KEYS 与书一致）；无 Redis 时用 `com.queue.InMemoryQueueRedis`（同语义 Java 实现） |
| EventPublishThread | `com.queue.EventPublishThread`（daemon 化，其余同书） |
| EventWorker（init/stop 生命周期） | `com.queue.EventWorker`（3.4.2 下沿用书中 Executor 构造器 + `handleExceptionsWith`，已加 @SuppressWarnings） |
| EventHandler / ProductEventHandler | `com.queue.EventHandler` / `com.task.handler.ProductEventHandler`（内存 Map 模拟线上异构数据集群） |
| PauseUtils | `com.queue.PauseUtils` |
| Spring XML（p311） | `src/main/resources/disruptor-redis-queue.xml`（仅说明用，仓库无 spring-context） |

## Disruptor 组件（书中 p304 术语表）

- **RingBuffer**：Disruptor 组件，环形队列，定长数组存储并预填充事件对象（不像链表每次增删节点要创建/回收，减少 GC）；数组长度 2^N 可用位运算取模；整体无锁设计减少竞争；缓存行填充解决 CPU 伪共享。
- **WorkPool**：Disruptor 组件，存放 WorkProcessor 的池子；Disruptor 把任务处理器放入 WorkPool 后通过 Executor 并发启动每个 WorkProcessor。
- **WorkProcessor**：Disruptor 组件，从 RingBuffer 消费事件/任务并交给 WorkHandler 处理。
- **WorkHandler**：Disruptor 组件，处理任务的工作者；本实现按事件类型（`eventType=队列名`）委托给对应的 EventHandler。

任务可以由其他系统直接写入或经 MQ 推入 Redis 等待队列（书中提到峰值时 Redis 等待队列有 2 亿多个任务）。


## 书中注意事项（务必阅读）

- **示例不完善**：排重（仅队列 `<10000` 条时生效）、任务调度、可靠性都有优化空间。
- **Redis 有丢任务风险**（如 AOF 每秒刷盘窗口、主从切换），重要业务需对账。
- **备份队列按小时轮转**，用于重构/数据回滚，不是持久存储。
- RPOPLPUSH 成功但 JVM 崩溃 → 任务僵死在本机处理队列，需人工/定时任务回收。
- Disruptor 3.2.1 → 3.4.2 适配：Executor 构造器与 `handleExceptionsWith` 已 deprecated 但仍可用；
  新代码建议 ThreadFactory 构造器 + `setDefaultExceptionHandler`。

## 测试

全部使用 `InMemoryQueueRedis`，无需真实 Redis，确定性（CountDownLatch，无 sleep 断言）：

```
mvn -Dtest='com.queue.*Test' test
```

（注意：仓库 `src/main/java` 中存在与本次无关的历史编译错误，需先修复或用 javac 单独编译本包，见测试类注释。）
