package crdt.learnning;

/** 第 09 章练习骨架，按文档推导补全 TODO。 */
public final class CausalStamp implements Comparable<CausalStamp> {
    // TODO 第 09 章：建议字段：replicaId（String）、seq（long），标识删除操作的因果坐标。

    public CausalStamp(String replicaId, long seq) {
        // TODO 第 09 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 09 章：CausalStamp");
    }

    public String replicaId() {
        // TODO 第 09 章：读取本地写入身份。
        throw new UnsupportedOperationException("TODO 第 09 章：replicaId");
    }

    public long seq() {
        // TODO 第 09 章：读取该设备操作的序号。
        throw new UnsupportedOperationException("TODO 第 09 章：seq");
    }

    @Override
    public int compareTo(CausalStamp other) {
        // TODO 第 09 章：为标识定义稳定且一致的比较顺序。
        throw new UnsupportedOperationException("TODO 第 09 章：compareTo");
    }

    public static CausalStamp min(CausalStamp left, CausalStamp right) {
        // TODO 第 09 章：选择双方确定一致的因果标记。
        throw new UnsupportedOperationException("TODO 第 09 章：min");
    }

    @Override
    public boolean equals(Object other) {
        // TODO 第 09 章：按类型契约比较状态内容。
        throw new UnsupportedOperationException("TODO 第 09 章：equals");
    }

    @Override
    public int hashCode() {
        // TODO 第 09 章：与 equals 使用相同的状态内容。
        throw new UnsupportedOperationException("TODO 第 09 章：hashCode");
    }

    @Override
    public String toString() {
        // TODO 第 09 章：提供便于观察的状态描述。
        throw new UnsupportedOperationException("TODO 第 09 章：toString");
    }
}
