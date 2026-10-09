package crdt.learnning;

import java.util.Map;

/** 第 02 章练习骨架，按文档推导补全 TODO。 */
public final class GCounter implements CvRDT<GCounter> {
    // TODO 第 02 章：建议字段：replicaId（String）、counts（Map<String, Long>），区分本地身份与复制状态。

    public GCounter(String replicaId) {
        // TODO 第 02 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 02 章：GCounter");
    }

    private GCounter(String replicaId, Map<String, Long> counts) {
        // TODO 第 02 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 02 章：GCounter");
    }

    public String replicaId() {
        // TODO 第 02 章：读取本地写入身份。
        throw new UnsupportedOperationException("TODO 第 02 章：replicaId");
    }

    public GCounter increment() {
        // TODO 第 02 章：记录本设备的一次或多次操作，返回新状态。
        throw new UnsupportedOperationException("TODO 第 02 章：increment");
    }

    public GCounter increment(long delta) {
        // TODO 第 02 章：记录本设备的一次或多次操作，返回新状态。
        throw new UnsupportedOperationException("TODO 第 02 章：increment");
    }

    public long value() {
        // TODO 第 02 章：读取当前可见结果。
        throw new UnsupportedOperationException("TODO 第 02 章：value");
    }

    public long countOf(String replicaId) {
        // TODO 第 02 章：读取指定设备的累计次数，缺失时视为零。
        throw new UnsupportedOperationException("TODO 第 02 章：countOf");
    }

    @Override
    public GCounter merge(GCounter other) {
        // TODO 第 02 章：合并双方状态，满足三条合并定律且不改动输入。
        throw new UnsupportedOperationException("TODO 第 02 章：merge");
    }

    public Map<String, Long> state() {
        // TODO 第 02 章：提供复制状态的只读快照。
        throw new UnsupportedOperationException("TODO 第 02 章：state");
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
