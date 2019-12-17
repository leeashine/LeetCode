package designpattern.proxypattern.part1;
//抽象主题类声明了真实主题类和代理类的公共方法，它可以是接口、抽象类或具体类，客户
//端针对抽象主题类编程，一致性地对待真实主题和代理主题
public abstract class Subject {
    public abstract void Request();
}
