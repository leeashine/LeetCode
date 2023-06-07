package aleetcode.staticdemo.demo1;

public class Father {
    static {
        System.out.println("父类中的静态代码块");
    }

    public Father() {
        System.out.println("父类中的构造函数");
    }
    {
        System.out.println("父类中的非静态代码块");
    }

    public void say() {
        System.out.println("father.say...");
    }

    public static void main(String[] args) {
        System.out.println("父类中的main方法");
        new Father();
        new Father();
    }
}
