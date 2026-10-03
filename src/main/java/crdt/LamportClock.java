package crdt;

/**
 * 不可变 Lamport 逻辑时钟：以非负整数记录本地事件顺序，收到远端时间后取最大值，
 * 再通过 {@link #tick()} 为后续事件分配更大的时间戳。它不是 CvRDT：这里没有
 * 用于副本状态收敛的 join；{@link #observe(long)} 只推进本地的因果时钟。
 *
 * <p>局限：Lamport 时间戳只保证 {@code a → b ⇒ ts(a) < ts(b)}，反过来不成立。
 * 按时间戳给并发事件排序会“武断地”排出先后，LWW 因而可能静默丢掉并发写。
 * {@code long} 耗尽时也无法再 tick。</p>
 */
public final class LamportClock {
    private static final LamportClock START = new LamportClock(0);

    private final long time;

    private LamportClock(long time) {
        this.time = time;
    }

    public static LamportClock start() {
        return START;
    }

    public static LamportClock at(long time) {
        if (time < 0) {
            throw new IllegalArgumentException("Lamport time must be nonnegative");
        }
        return time == 0 ? START : new LamportClock(time);
    }

    public long time() {
        return time;
    }

    public LamportClock tick() {
        if (time == Long.MAX_VALUE) {
            throw new IllegalStateException("Lamport time overflow");
        }
        return new LamportClock(time + 1);
    }

    public LamportClock observe(long remoteTime) {
        return new LamportClock(Math.max(time, remoteTime));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LamportClock)) {
            return false;
        }
        return time == ((LamportClock) other).time;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(time);
    }

    @Override
    public String toString() {
        return "LamportClock{time=" + time + '}';
    }
}
