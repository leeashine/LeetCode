package designpattern.singletonpackage;

/**
 * 饿汉模式
 * 线程安全，比较常用，但容易产生垃圾，因为一开始就初始化
 * 在该内部类中定义了一个static类型的变量
 * instance，此时会首先初始化这个成员变量，由Java虚拟机来保证其线程安全性，确保该成员
 * 变量只能初始化一次。
 */
public class Singleton {
    private Singleton(){};
    private static Singleton singleton=new Singleton();
    public static Singleton getSingleton(){
        return singleton;
    }
}
