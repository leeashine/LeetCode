package designpattern.iteratorpattern;

//工厂 创建对象
public class ConcreteAggregate implements Aggregate{
    @Override
    public Iterator createIterator() {
        return new ConcreteIterator(this);
    }
}
