package crdt.learnning;

import java.util.List;

/** 第 09 章练习骨架，按文档推导补全 TODO。 */
public final class CausalStability {
    // TODO 第 09 章：定义稳定前沿工具的边界，本类型无需保存副本状态字段。

    private CausalStability() {
        // TODO 第 09 章：定义所需状态并构造不可变实例。
        throw new UnsupportedOperationException("TODO 第 09 章：CausalStability");
    }

    public static VersionVector frontier(List<VersionVector> observed) {
        // TODO 第 09 章：计算所有存活设备共同确认的操作范围。
        throw new UnsupportedOperationException("TODO 第 09 章：frontier");
    }

    public static boolean covers(VersionVector frontier, CausalStamp stamp) {
        // TODO 第 09 章：判断共同确认的范围是否包含指定删除操作。
        throw new UnsupportedOperationException("TODO 第 09 章：covers");
    }

}
