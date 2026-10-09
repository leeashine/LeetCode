package crdt.learnning;

import java.util.Map;
import java.util.SortedMap;

/** 第 04 章练习骨架，按文档推导补全 TODO。 */
public final class VersionVector {
    // TODO 第 04 章第 4 节：定义按设备保存修改次数的 counters（SortedMap<String, Long>）字段。

    private VersionVector(SortedMap<String, Long> counters) {
        // TODO 第 04 章第 4 节：保存次数表，保持旧记录不可变。
        throw new UnsupportedOperationException("TODO 第 04 章第 4 节：构造记录");
    }

    public static VersionVector empty() {
        // TODO 第 04 章第 4 节：没有任何修改时的起点。
        throw new UnsupportedOperationException("TODO 第 04 章第 4 节：empty");
    }

    public static VersionVector of(Map<String, Long> counters) {
        // TODO 第 09 章：从次数表构造合法只读记录，供稳定前沿使用。
        throw new UnsupportedOperationException("TODO 第 09 章：of");
    }

    public long get(String replicaId) {
        // TODO 第 04 章第 4 节：读取设备次数，未出现的设备视为 0。
        throw new UnsupportedOperationException("TODO 第 04 章第 4 节：get");
    }

    public VersionVector increment(String replicaId) {
        // TODO 第 04 章第 4 节：只推进本设备的次数，返回新记录。
        throw new UnsupportedOperationException("TODO 第 04 章第 4 节：increment");
    }

    public VersionVector merge(VersionVector other) {
        // TODO 第 04 章第 4 节：汇总双方次数表，不改变输入。
        throw new UnsupportedOperationException("TODO 第 04 章第 4 节：merge");
    }

    public boolean greaterOrEqual(VersionVector other) {
        // TODO 第 04 章第 5 节：判断每栏是否都不落后。
        throw new UnsupportedOperationException("TODO 第 04 章第 5 节：greaterOrEqual");
    }

    public boolean dominates(VersionVector other) {
        // TODO 第 04 章第 5 节：判断是否包含对方且确实多出修改。
        throw new UnsupportedOperationException("TODO 第 04 章第 5 节：dominates");
    }

    public boolean isConcurrent(VersionVector other) {
        // TODO 第 04 章第 5 节：判断是否各有对方不知道的修改。
        throw new UnsupportedOperationException("TODO 第 04 章第 5 节：isConcurrent");
    }

    @Override
    public boolean equals(Object obj) {
        // TODO 第 04 章第 4 节：按记录内容比较。
        throw new UnsupportedOperationException("TODO 第 04 章第 4 节：equals");
    }

    @Override
    public int hashCode() {
        // TODO 第 04 章第 4 节：与 equals 使用相同的记录内容。
        throw new UnsupportedOperationException("TODO 第 04 章第 4 节：hashCode");
    }

    public Map<String, Long> asMap() {
        // TODO 第 04 章第 4 节：提供只读查看。
        throw new UnsupportedOperationException("TODO 第 04 章第 4 节：asMap");
    }
}
