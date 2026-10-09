package crdt.learnning;

/** 第 02 章练习骨架，按文档推导补全 TODO。 */
public final class PNCounter implements CvRDT<PNCounter> {
    // TODO 第 02 章：建议字段：positive、negative（GCounter），分别保存增加与撤销的历史。

    public PNCounter(String replicaId) {
        // TODO 第 02 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 02 章：PNCounter");
    }

    private PNCounter(GCounter positive, GCounter negative) {
        // TODO 第 02 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 02 章：PNCounter");
    }

    public PNCounter increment() {
        // TODO 第 02 章：记录本设备的一次或多次操作，返回新状态。
        throw new UnsupportedOperationException("TODO 第 02 章：increment");
    }

    public PNCounter increment(long delta) {
        // TODO 第 02 章：记录本设备的一次或多次操作，返回新状态。
        throw new UnsupportedOperationException("TODO 第 02 章：increment");
    }

    public PNCounter decrement() {
        // TODO 第 02 章：记录本设备的撤销次数，返回新状态。
        throw new UnsupportedOperationException("TODO 第 02 章：decrement");
    }

    public PNCounter decrement(long delta) {
        // TODO 第 02 章：记录本设备的撤销次数，返回新状态。
        throw new UnsupportedOperationException("TODO 第 02 章：decrement");
    }

    public long value() {
        // TODO 第 02 章：读取当前可见结果。
        throw new UnsupportedOperationException("TODO 第 02 章：value");
    }

    @Override
    public PNCounter merge(PNCounter other) {
        // TODO 第 02 章：合并双方状态，满足三条合并定律且不改动输入。
        throw new UnsupportedOperationException("TODO 第 02 章：merge");
    }

    @Override
    public boolean equals(Object other) {
        // TODO 第 02 章：按类型契约比较状态内容。
        throw new UnsupportedOperationException("TODO 第 02 章：equals");
    }

    @Override
    public int hashCode() {
        // TODO 第 02 章：与 equals 使用相同的状态内容。
        throw new UnsupportedOperationException("TODO 第 02 章：hashCode");
    }

    @Override
    public String toString() {
        // TODO 第 02 章：提供便于观察的状态描述。
        throw new UnsupportedOperationException("TODO 第 02 章：toString");
    }
}
