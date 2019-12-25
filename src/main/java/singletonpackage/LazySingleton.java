package singletonpackage;

/**
 * 懒汉模式 线程不安全
 * 双重检查锁定 注意 volatile关键字
 * 由于volatile关键字会屏蔽Java虚拟机所做的一
 * 些代码优化，可能会导致系统运行效率降低，因此即使使用双重检查锁定来实现单例模式也
 * 不是一种完美的实现方式。
 */
public class LazySingleton {
    private volatile static LazySingleton instance = null;

    private LazySingleton() {
    }

    public static LazySingleton getInstance() {
        //第一重判断
        if (instance == null) {
            //锁定代码块
            synchronized (LazySingleton.class) {
                //第二重判断
                if (instance == null) {
                    instance = new LazySingleton(); //创建单例实例
                }
            }
        }
        return instance;
    }

//    private LazySingleton() {
//        System.out.println("LazySingleton is create");
//    }
//
//    private static LazySingleton instance = null;
//
//    public static synchronized LazySingleton getInstance() {
//        if (instance == null) {
//            instance = new LazySingleton();
//        }
//        return instance;
//    }
}
