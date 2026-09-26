package boss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 按 Boss 分片的单写者扣血服务。
 *
 * <p>同一 Boss 的所有命令固定进入同一个 Lane。每个 Lane 由一条固定线程按 Tick
 * 批量消费有界 Mailbox，Boss 状态只由所属 Lane 读写。</p>
 *
 * <pre>
 * 多个业务线程 --offer--&gt; Lane Mailbox --每个 Tick drainTo--&gt; Lane 唯一线程
 *                                                            |
 *                                                            +-- 修改 BossState
 * </pre>
 *
 * <p>这里的 Lane 不是线程池：Lane 数量在服务创建时固定，并且每个 Lane 从始至终
 * 只有一条消费线程。多 Boss 可以共享一个 Lane，但同一 Boss 永远不会跨 Lane。</p>
 */
public final class BossDamageService implements AutoCloseable {

    /** 默认每 10ms 处理一批命令，兼顾延迟和批处理收益。 */
    private static final long DEFAULT_TICK_MILLIS = 10;

    /** 限制单 Tick 工作量，避免一次积压让战斗循环长时间无法进入下一 Tick。 */
    private static final int DEFAULT_MAX_COMMANDS_PER_TICK = 1_024;

    private final BattleLane[] lanes;

    public BossDamageService(int laneCount, int queueCapacity) {
        this(laneCount, queueCapacity, DEFAULT_TICK_MILLIS, DEFAULT_MAX_COMMANDS_PER_TICK);
    }

    /**
     * @param laneCount 每个 Lane 对应一条固定线程，通常按可用 CPU 和战斗负载配置
     * @param queueCapacity 每个 Lane 的 Mailbox 容量，满载后新命令立即失败
     * @param tickMillis Lane 批量消费 Mailbox 的时间间隔
     * @param maxCommandsPerTick 单个 Lane 每 Tick 最多处理的命令数
     */
    public BossDamageService(int laneCount, int queueCapacity, long tickMillis,
                             int maxCommandsPerTick) {
        if (laneCount <= 0) {
            throw new IllegalArgumentException("laneCount must be positive");
        }
        if (queueCapacity <= 0) {
            throw new IllegalArgumentException("queueCapacity must be positive");
        }
        if (tickMillis <= 0) {
            throw new IllegalArgumentException("tickMillis must be positive");
        }
        if (maxCommandsPerTick <= 0) {
            throw new IllegalArgumentException("maxCommandsPerTick must be positive");
        }

        lanes = new BattleLane[laneCount];
        for (int laneIndex = 0; laneIndex < laneCount; laneIndex++) {
            lanes[laneIndex] = new BattleLane(
                    laneIndex,
                    queueCapacity,
                    tickMillis,
                    maxCommandsPerTick
            );
        }
    }

    public CompletableFuture<Void> registerBoss(String bossId, long maxHealth) {
        requireText(bossId, "bossId");
        if (maxHealth <= 0) {
            throw new IllegalArgumentException("maxHealth must be positive");
        }

        RegisterBossCommand command = new RegisterBossCommand(bossId, maxHealth);
        return laneFor(bossId).submit(command);
    }

    public CompletableFuture<DamageResult> damage(String bossId, long requestedDamage) {
        requireText(bossId, "bossId");
        if (requestedDamage <= 0) {
            throw new IllegalArgumentException("requestedDamage must be positive");
        }

        DamageCommand command = new DamageCommand(bossId, requestedDamage);
        return laneFor(bossId).submit(command);
    }

    public CompletableFuture<BossSnapshot> snapshot(String bossId) {
        requireText(bossId, "bossId");
        SnapshotCommand command = new SnapshotCommand(bossId);
        return laneFor(bossId).submit(command);
    }

    @Override
    public void close() {
        // 两阶段关闭：先让所有 Lane 同时停止接收，再分别等待已入队命令处理完。
        // 如果逐个 Lane 关闭并等待，后面的 Lane 会在等待期间继续接收请求。
        for (BattleLane lane : lanes) {
            lane.shutdown();
        }
        for (BattleLane lane : lanes) {
            lane.awaitTermination();
        }
    }

    private BattleLane laneFor(String bossId) {
        // 稳定分片保证同一 Boss 的所有命令始终由同一条 Lane 线程顺序执行。
        return lanes[Math.floorMod(bossId.hashCode(), lanes.length)];
    }

    private static void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
    }

    private static final class BattleLane {

        // 线程封闭状态：只能在本 Lane 的 thread 中访问，因此扣血不需要锁或 CAS。
        private final Map<String, BossState> bosses = new HashMap<String, BossState>();

        // 多个业务线程负责 offer，唯一 Lane 线程负责 drain。
        // 有界队列用于背压，避免流量洪峰无限占用堆内存。
        private final ArrayBlockingQueue<BossCommand<?>> mailbox;

        // 每 Tick 复用同一个列表，避免为每批命令重复创建集合。
        private final List<BossCommand<?>> batch;

        // 只控制接收和关闭生命周期，不参与 Boss 血量计算。
        private final AtomicBoolean accepting = new AtomicBoolean(true);
        private final long tickNanos;
        private final int maxCommandsPerTick;

        // Lane 自己持有固定线程，不使用 Executor 或线程池。
        private final Thread thread;

        private BattleLane(final int laneIndex, int queueCapacity, long tickMillis,
                           int maxCommandsPerTick) {
            this.mailbox = new ArrayBlockingQueue<BossCommand<?>>(queueCapacity);
            this.batch = new ArrayList<BossCommand<?>>(Math.min(queueCapacity, maxCommandsPerTick));
            this.tickNanos = TimeUnit.MILLISECONDS.toNanos(tickMillis);
            this.maxCommandsPerTick = maxCommandsPerTick;
            this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    runLoop();
                }
            }, "boss-battle-lane-" + laneIndex);
            this.thread.setDaemon(true);
            // 线程启动后会等待第一个 Tick；命令提交不会直接执行战斗逻辑。
            this.thread.start();
        }

        private <T> CompletableFuture<T> submit(BossCommand<T> command) {
            if (!accepting.get()) {
                command.reject("battle lane is stopping");
                return command.future();
            }
            // offer 不阻塞业务线程。Mailbox 已满时立即返回失败的 Future。
            if (!mailbox.offer(command)) {
                command.reject("battle lane mailbox is full");
                return command.future();
            }

            // 处理“检查 accepting 后恰好发生 shutdown”的竞态。
            if (!accepting.get() && mailbox.remove(command)) {
                command.reject("battle lane is stopping");
            }
            return command.future();
        }

        private void runLoop() {
            // 使用绝对的下一 Tick 时间，避免简单 sleep(tickMillis) 把处理耗时
            // 不断叠加到 Tick 周期中。
            long nextTick = System.nanoTime() + tickNanos;
            try {
                while (accepting.get() || !mailbox.isEmpty()) {
                    if (accepting.get()) {
                        waitUntil(nextTick);
                    }

                    // 正常运行时每 Tick 处理一批；关闭后跳过等待，尽快排空 Mailbox。
                    drainAndExecute();

                    nextTick += tickNanos;
                    long now = System.nanoTime();
                    // 如果处理耗时已经落后一个完整 Tick，直接重置基准时间，
                    // 防止连续无等待追帧形成“死亡螺旋”。
                    if (now - nextTick >= tickNanos) {
                        nextTick = now + tickNanos;
                    }
                }
            } finally {
                rejectRemainingCommands();
            }
        }

        private void waitUntil(long deadlineNanos) {
            while (accepting.get()) {
                long remainingNanos = deadlineNanos - System.nanoTime();
                if (remainingNanos <= 0) {
                    return;
                }
                // parkNanos 可以被 shutdown 的 interrupt 立即唤醒；Thread.sleep
                // 不方便表达基于 nanoTime 的剩余等待时间。
                LockSupport.parkNanos(this, remainingNanos);
                Thread.interrupted();
            }
        }

        private void drainAndExecute() {
            batch.clear();
            // drainTo 一次加锁批量取出命令，比消费者逐条 poll 更适合 Tick 批处理。
            // 最大条数同时限定单 Tick 的处理预算，剩余命令留到后续 Tick。
            mailbox.drainTo(batch, maxCommandsPerTick);
            try {
                for (int index = 0; index < batch.size(); index++) {
                    try {
                        batch.get(index).executeOn(this);
                    } catch (Error error) {
                        rejectUnexecutedBatch(index + 1);
                        throw error;
                    }
                }
            } finally {
                batch.clear();
            }
        }

        private void rejectUnexecutedBatch(int fromIndex) {
            for (int index = fromIndex; index < batch.size(); index++) {
                batch.get(index).reject("battle lane stopped before executing command");
            }
        }

        private void registerBoss(String bossId, long maxHealth) {
            if (bosses.containsKey(bossId)) {
                throw new IllegalStateException("boss already registered: " + bossId);
            }
            bosses.put(bossId, new BossState(maxHealth));
        }

        private DamageResult damage(String bossId, long requestedDamage) {
            BossState boss = boss(bossId);
            long appliedDamage = Math.min(boss.currentHealth, requestedDamage);
            boss.currentHealth -= appliedDamage;
            boolean killed = appliedDamage > 0 && boss.currentHealth == 0;
            return new DamageResult(appliedDamage, boss.currentHealth, killed);
        }

        private BossSnapshot snapshot(String bossId) {
            return new BossSnapshot(boss(bossId).currentHealth);
        }

        private BossState boss(String bossId) {
            BossState boss = bosses.get(bossId);
            if (boss == null) {
                throw new IllegalStateException("boss not registered: " + bossId);
            }
            return boss;
        }

        private void shutdown() {
            if (accepting.getAndSet(false)) {
                thread.interrupt();
            }
        }

        private void awaitTermination() {
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        private void rejectRemainingCommands() {
            BossCommand<?> command;
            while ((command = mailbox.poll()) != null) {
                command.reject("battle lane stopped before executing command");
            }
        }
    }

    /**
     * Mailbox 中传递的显式战斗命令。
     *
     * <p>业务线程只负责创建并投递命令；execute 只会在目标 Lane 的唯一线程中调用。
     * Future 用来把实际伤害、剩余血量或异常异步返回给提交方。</p>
     */
    private abstract static class BossCommand<T> {

        private final CompletableFuture<T> future = new CompletableFuture<T>();

        private CompletableFuture<T> future() {
            return future;
        }

        private void executeOn(BattleLane lane) {
            try {
                future.complete(execute(lane));
            } catch (RuntimeException ex) {
                future.completeExceptionally(ex);
            } catch (Error error) {
                future.completeExceptionally(error);
                throw error;
            }
        }

        private void reject(String message) {
            future.completeExceptionally(new RejectedExecutionException(message));
        }

        protected abstract T execute(BattleLane lane);
    }

    private static final class RegisterBossCommand extends BossCommand<Void> {

        private final String bossId;
        private final long maxHealth;

        private RegisterBossCommand(String bossId, long maxHealth) {
            this.bossId = bossId;
            this.maxHealth = maxHealth;
        }

        @Override
        protected Void execute(BattleLane lane) {
            lane.registerBoss(bossId, maxHealth);
            return null;
        }
    }

    private static final class DamageCommand extends BossCommand<DamageResult> {

        private final String bossId;
        private final long requestedDamage;

        private DamageCommand(String bossId, long requestedDamage) {
            this.bossId = bossId;
            this.requestedDamage = requestedDamage;
        }

        @Override
        protected DamageResult execute(BattleLane lane) {
            return lane.damage(bossId, requestedDamage);
        }
    }

    private static final class SnapshotCommand extends BossCommand<BossSnapshot> {

        private final String bossId;

        private SnapshotCommand(String bossId) {
            this.bossId = bossId;
        }

        @Override
        protected BossSnapshot execute(BattleLane lane) {
            return lane.snapshot(bossId);
        }
    }

    private static final class BossState {

        // 普通 long 足够：该字段从不被多个线程同时读写。
        private long currentHealth;

        private BossState(long maxHealth) {
            this.currentHealth = maxHealth;
        }
    }
}
