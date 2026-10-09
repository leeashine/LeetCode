package crdt.learnning;

import java.util.Map;

/** 第 08 章练习骨架，按文档推导补全 TODO。 */
public final class DeltaGCounter implements CvRDT<DeltaGCounter> {
    // TODO 第 08 章：建议字段：replicaId（String）、counts（Map<String, Long>）、acked（long），区分复制状态与发送确认记录。

    public DeltaGCounter(String replicaId) {
        // TODO 第 08 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 08 章：DeltaGCounter");
    }

    private DeltaGCounter(String replicaId, Map<String, Long> counts, long acked) {
        // TODO 第 08 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 08 章：DeltaGCounter");
    }

    public String replicaId() {
        // TODO 第 08 章：读取本地写入身份。
        throw new UnsupportedOperationException("TODO 第 08 章：replicaId");
    }

    public DeltaGCounter increment() {
        // TODO 第 08 章：记录本设备的一次或多次操作，返回新状态。
        throw new UnsupportedOperationException("TODO 第 08 章：increment");
    }

    public DeltaGCounter increment(long delta) {
        // TODO 第 08 章：记录本设备的一次或多次操作，返回新状态。
        throw new UnsupportedOperationException("TODO 第 08 章：increment");
    }

    public long value() {
        // TODO 第 08 章：读取当前可见结果。
        throw new UnsupportedOperationException("TODO 第 08 章：value");
    }

    public long countOf(String replicaId) {
        // TODO 第 08 章：读取指定设备的累计次数，缺失时视为零。
        throw new UnsupportedOperationException("TODO 第 08 章：countOf");
    }

    public Map<String, Long> state() {
        // TODO 第 08 章：提供复制状态的只读快照。
        throw new UnsupportedOperationException("TODO 第 08 章：state");
    }

    public Delta pending() {
        // TODO 第 08 章：读取尚未确认送达的增量，不清除发送记录。
        throw new UnsupportedOperationException("TODO 第 08 章：pending");
    }

    public DeltaGCounter ack() {
        // TODO 第 08 章：确认当前待发内容，保留复制状态及后续写入能力。
        throw new UnsupportedOperationException("TODO 第 08 章：ack");
    }

    public DeltaGCounter apply(Delta delta) {
        // TODO 第 08 章：接收增量，允许重复和乱序投递。
        throw new UnsupportedOperationException("TODO 第 08 章：apply");
    }

    @Override
    public DeltaGCounter merge(DeltaGCounter other) {
        // TODO 第 08 章：合并双方状态，满足三条合并定律且不改动输入。
        throw new UnsupportedOperationException("TODO 第 08 章：merge");
    }

    @Override
    public boolean equals(Object other) {
        // TODO 第 08 章：按类型契约比较状态内容。
        throw new UnsupportedOperationException("TODO 第 08 章：equals");
    }

    @Override
    public int hashCode() {
        // TODO 第 08 章：与 equals 使用相同的状态内容。
        throw new UnsupportedOperationException("TODO 第 08 章：hashCode");
    }

    @Override
    public String toString() {
        // TODO 第 08 章：提供便于观察的状态描述。
        throw new UnsupportedOperationException("TODO 第 08 章：toString");
    }

    public static final class Delta {
        // TODO 第 08 章：建议字段：counts（Map<String, Long>），保存待传输的累计次数。

        private Delta(Map<String, Long> counts) {
            // TODO 第 08 章：定义所需状态并构造不可变实例。
            throw new UnsupportedOperationException("TODO 第 08 章：Delta");
        }

        public static Delta empty() {
            // TODO 第 08 章：创建没有任何记录的起点。
            throw new UnsupportedOperationException("TODO 第 08 章：empty");
        }

        public Map<String, Long> counts() {
            // TODO 第 08 章：提供增量中的设备累计次数只读表。
            throw new UnsupportedOperationException("TODO 第 08 章：counts");
        }

        public Delta merge(Delta other) {
            // TODO 第 08 章：合并双方状态，满足三条合并定律且不改动输入。
            throw new UnsupportedOperationException("TODO 第 08 章：merge");
        }

        public boolean isEmpty() {
            // TODO 第 08 章：判断是否没有记录。
            throw new UnsupportedOperationException("TODO 第 08 章：isEmpty");
        }

        @Override
        public boolean equals(Object other) {
            // TODO 第 08 章：按类型契约比较状态内容。
            throw new UnsupportedOperationException("TODO 第 08 章：equals");
        }

        @Override
        public int hashCode() {
            // TODO 第 08 章：与 equals 使用相同的状态内容。
            throw new UnsupportedOperationException("TODO 第 08 章：hashCode");
        }

        @Override
        public String toString() {
            // TODO 第 08 章：提供便于观察的状态描述。
            throw new UnsupportedOperationException("TODO 第 08 章：toString");
        }
    }
}
