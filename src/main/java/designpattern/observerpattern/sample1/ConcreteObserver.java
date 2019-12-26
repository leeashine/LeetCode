package designpattern.observerpattern.sample1;

public class ConcreteObserver implements Observer{
//    ConcreteSubject subject;
    @Override
    public void update() {
    }
//    在有些更加复杂的情况下，具体观察者类ConcreteObserver的update()方法在执行时需要使用到
//    具体目标类ConcreteSubject中的状态（属性），因此在ConcreteObserver与ConcreteSubject之间
//    有时候还存在关联或依赖关系，在ConcreteObserver中定义一个ConcreteSubject实例，通过该实
//    例获取存储在ConcreteSubject中的状态。如果ConcreteObserver的update()方法不需要使用到
//    ConcreteSubject中的状态属性，则可以对观察者模式的标准结构进行简化，在具体观察者
//    ConcreteObserver和具体目标ConcreteSubject之间无须维持对象引用。如果在具体层具有关联关
//    系，系统的扩展性将受到一定的影响，增加新的具体目标类有时候需要修改原有观察者的代
//    码，在一定程度上违反了“开闭原则”，但是如果原有观察者类无须关联新增的具体目标，则系
//    统扩展性不受影响。
}
