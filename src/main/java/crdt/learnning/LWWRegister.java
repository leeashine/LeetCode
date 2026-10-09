package crdt.learnning;

/** 第 03 章练习骨架，按文档推导补全 TODO。 */
public final class LWWRegister<T> implements CvRDT<LWWRegister<T>> {
    // TODO 第 03 章：建议字段：value（T）、timestamp（long）、replicaId（String），绑定标题与写入标记。

    private LWWRegister(T value, long timestamp, String replicaId) {
        // TODO 第 03 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 03 章：LWWRegister");
    }

    @SuppressWarnings("unchecked")
    public static <T> LWWRegister<T> empty() {
        // TODO 第 03 章：创建没有任何记录的起点。
        throw new UnsupportedOperationException("TODO 第 03 章：empty");
    }

    public LWWRegister<T> set(T value, long timestamp, String replicaId) {
        // TODO 第 03 章：保存标题及其写入身份、时间。
        throw new UnsupportedOperationException("TODO 第 03 章：set");
    }

    public T value() {
        // TODO 第 03 章：读取当前可见结果。
        throw new UnsupportedOperationException("TODO 第 03 章：value");
    }

    public long timestamp() {
        // TODO 第 03 章：读取写入的时间标记。
        throw new UnsupportedOperationException("TODO 第 03 章：timestamp");
    }

    public String replicaId() {
        // TODO 第 03 章：读取本地写入身份。
        throw new UnsupportedOperationException("TODO 第 03 章：replicaId");
    }

    public boolean isEmpty() {
        // TODO 第 03 章：判断是否没有记录。
        throw new UnsupportedOperationException("TODO 第 03 章：isEmpty");
    }

    @Override
    public LWWRegister<T> merge(LWWRegister<T> other) {
        // TODO 第 03 章：合并双方状态，满足三条合并定律且不改动输入。
        throw new UnsupportedOperationException("TODO 第 03 章：merge");
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
