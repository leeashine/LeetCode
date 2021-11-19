package thinkinjava.reflect;

import java.lang.reflect.Proxy;

/**
 * 动态代理
 *  1.类加载器
 *  2.代理实现的接口列表
 *  3.以及InvocationHandler接口地一个实现
 */
public class SimpleDynamicProxy {
    public static void consumer(Interface iface) {
        iface.doSomething();
        iface.somethingElse("bonobo");
    }

    public static void main(String[] args) {
        RealObject real = new RealObject();
        consumer(real);
        Interface proxy = (Interface) Proxy.newProxyInstance(
                Interface.class.getClassLoader(),
                new Class[]{Interface.class},
                new DynamicProxyHandler(real)
        );
        consumer(proxy);
    }
}
