package aleetcode.staticdemo.demo1;

public class Son extends Father{
    static {
        System.out.println("子类中的静态代码块");
    }

    public Son() {
        System.out.println("子类中的构造函数");
    }
    {
        System.out.println("子类中的非静态代码块");
    }

    @Override
    public void say() {
        System.out.println("son.say...");
    }

    public static void main(String[] args) {
        System.out.println("子类中的main方法");
        Father son = new Son();
        son.say();

    }
}
