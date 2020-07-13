package designpattern.iteratorpattern;

//产品
public class ConcreteIterator implements Iterator{
    private ConcreteAggregate objects; //维持一个对具体聚合对象的引用，以便于访问存储在聚合对象中的数据
    private int cursor; //定义一个游标，用于记录当前访问位置
    public ConcreteIterator(ConcreteAggregate objects) {
        this.objects=objects;
    }

    @Override
    public void first() {

    }

    @Override
    public void next() {

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object currentItem() {
        return null;
    }
}
