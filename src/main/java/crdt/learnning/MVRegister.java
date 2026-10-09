package crdt.learnning;

import java.util.Set;

/** 第 04 章练习骨架，按文档推导补全 TODO。 */
public final class MVRegister<T> implements CvRDT<MVRegister<T>> {
    // TODO 第 04 章第 6 节：定义 entries（Set<Entry<T>>），保存候选内容及其写出时的记录。

    private MVRegister(Set<Entry<T>> entries) {
        // TODO 第 04 章第 6 节：保存候选集合，保持旧状态不可变。
        throw new UnsupportedOperationException("TODO 第 04 章第 6 节：构造容器");
    }

    private static final class Entry<T> {
        // TODO 第 04 章第 6 节：定义 vv（VersionVector）与 value（T），将记录与内容绑定。

        private Entry(VersionVector vv, T value) {
            // TODO 第 04 章第 6 节：将记录与内容绑定。
            throw new UnsupportedOperationException("TODO 第 04 章第 6 节：构造 Entry");
        }

        @Override
        public boolean equals(Object obj) {
            // TODO 第 04 章第 6 节：比较记录与内容。
            throw new UnsupportedOperationException("TODO 第 04 章第 6 节：Entry.equals");
        }

        @Override
        public int hashCode() {
            // TODO 第 04 章第 6 节：与 Entry.equals 使用相同信息。
            throw new UnsupportedOperationException("TODO 第 04 章第 6 节：Entry.hashCode");
        }
    }

    public static <T> MVRegister<T> empty() {
        // TODO 第 04 章第 7 节：尚未写入任何内容的容器。
        throw new UnsupportedOperationException("TODO 第 04 章第 7 节：empty");
    }

    public VersionVector version() {
        // TODO 第 04 章第 7 节：汇总当前已知的修改经历。
        throw new UnsupportedOperationException("TODO 第 04 章第 7 节：version");
    }

    public MVRegister<T> assign(T value, String replicaId) {
        // TODO 第 04 章第 7 节：在当前已知经历上保存一次新内容。
        throw new UnsupportedOperationException("TODO 第 04 章第 7 节：assign");
    }

    @Override
    public MVRegister<T> merge(MVRegister<T> other) {
        // TODO 第 04 章第 8 节：保留独立修改，剔除已经被替换的内容。
        throw new UnsupportedOperationException("TODO 第 04 章第 8 节：merge");
    }

    public Set<T> values() {
        // TODO 第 04 章第 8 节：提取只读的可见内容。
        throw new UnsupportedOperationException("TODO 第 04 章第 8 节：values");
    }

    public int size() {
        // TODO 第 04 章第 8 节：查询可见不同值的数量。
        throw new UnsupportedOperationException("TODO 第 04 章第 8 节：size");
    }

    public boolean isEmpty() {
        // TODO 第 04 章第 8 节：判断是否没有候选内容。
        throw new UnsupportedOperationException("TODO 第 04 章第 8 节：isEmpty");
    }

    @Override
    public boolean equals(Object obj) {
        // TODO 第 04 章第 8 节：按候选记录比较完整状态。
        throw new UnsupportedOperationException("TODO 第 04 章第 8 节：equals");
    }

    @Override
    public int hashCode() {
        // TODO 第 04 章第 8 节：与 equals 使用相同的状态信息。
        throw new UnsupportedOperationException("TODO 第 04 章第 8 节：hashCode");
    }
}
