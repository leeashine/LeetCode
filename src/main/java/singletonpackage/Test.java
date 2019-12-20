package singletonpackage;

public class Test {
    public static void main(String[] args) {

        StaticSingleton instance1 = StaticSingleton.getInstance();
        StaticSingleton instance2 = StaticSingleton.getInstance();
        System.out.println(instance1==instance2);

        Singleton singleton=Singleton.getSingleton();
        Singleton singleton2=Singleton.getSingleton();
        System.out.println(singleton==singleton2);

    }
}
