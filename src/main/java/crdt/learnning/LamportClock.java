package crdt.learnning;

/** 第 03 章练习骨架，按文档推导补全 TODO。 */
public final class LamportClock {
    // TODO 第 03 章：建议字段：time（long），保存当前逻辑时间。

    private LamportClock(long time) {
        // TODO 第 03 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 03 章：LamportClock");
    }

    public static LamportClock start() {
        // TODO 第 03 章：创建尚未发生事件的时钟。
        throw new UnsupportedOperationException("TODO 第 03 章：start");
    }

    public static LamportClock at(long time) {
        // TODO 第 03 章：创建指定时间的合法时钟。
        throw new UnsupportedOperationException("TODO 第 03 章：at");
    }

    public long time() {
        // TODO 第 03 章：读取当前逻辑时间。
        throw new UnsupportedOperationException("TODO 第 03 章：time");
    }

    public LamportClock tick() {
        // TODO 第 03 章：为本地下一次事件推进逻辑时间。
        throw new UnsupportedOperationException("TODO 第 03 章：tick");
    }

    public LamportClock observe(long remoteTime) {
        // TODO 第 03 章：接收远端时间，为后续本地事件准备时钟。
        throw new UnsupportedOperationException("TODO 第 03 章：observe");
    }

    @Override
    public boolean equals(Object other) {
        // TODO 第 03 章：按类型契约比较状态内容。
        throw new UnsupportedOperationException("TODO 第 03 章：equals");
    }

    @Override
    public int hashCode() {
        // TODO 第 03 章：与 equals 使用相同的状态内容。
        throw new UnsupportedOperationException("TODO 第 03 章：hashCode");
    }

    @Override
    public String toString() {
        // TODO 第 03 章：提供便于观察的状态描述。
        throw new UnsupportedOperationException("TODO 第 03 章：toString");
    }
}
