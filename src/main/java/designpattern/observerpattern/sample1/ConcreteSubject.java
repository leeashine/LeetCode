package designpattern.observerpattern.sample1;
//具体目标类ConcreteSubject是实现了抽象目标类Subject的一个具体子类，其典型代码如下所
//抽象观察者角色一般定义为一个接口，通常只声明一个update()方法，为不同观察者的更新
//        （响应）行为定义相同的接口，这个方法在其子类中实现，不同的观察者具有不同的响应方
//        法。抽象观察者Observer典型代码如下所示：
public class ConcreteSubject extends Subject {

    //实现通知方法
    public void notify1() {
        //遍历观察者集合，调用每一个观察者的响应方法
        for (Object obs : observers) {
            ((Observer) obs).update();
        }
    }
}
