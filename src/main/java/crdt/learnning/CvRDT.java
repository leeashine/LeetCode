package crdt.learnning;

/** 第 01 章练习骨架，按文档推导补全 TODO。 */
public interface CvRDT<T extends CvRDT<T>> {

    // TODO 第 01 章：约定不可变状态的合并接口，并在实现类检验三条定律。
    T merge(T other);
}
