package aleetcode.staticdemo;

public class StaticClassDemo {
    static {
        System.out.println("静态代码块随着类的加载而执行，它会在类初始化的时候执行一次，执行完成便销毁，它仅能初始化类变量，即static修饰的数据成员。");
    }
    public void say(){
        System.out.println("调用普通方法");
    }
    public static void main(String[] args) {

        StaticClassDemo staticClassDemo = new StaticClassDemo();
        staticClassDemo.say();
        staticClassDemo.say();
        staticClassDemo.say();

        StaticClassDemo staticClassDemo2 = new StaticClassDemo();
        System.out.println("-------------------");
        staticClassDemo2.say();
        staticClassDemo2.say();
    }
}
