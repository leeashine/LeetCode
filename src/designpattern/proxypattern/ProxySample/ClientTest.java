package designpattern.proxypattern.ProxySample;

import java.io.Console;

public class ClientTest {
//    在代理类ProxySearcher中实现对真实主题类的
//    权限控制和引用计数，如果需要在访问真实主题时增加新的访问控制机制和新功能，只需增
//    加一个新的代理类，再修改配置文件，在客户端代码中使用新增代理类即可，源代码无须修
//    改，符合开闭原则
    public static void main(String[] args) {
        //读取配置文件
//        String proxy = ConfigurationManager.AppSettings["proxy"];
//        //反射生成对象，针对抽象编程，客户端无须分辨真实主题类和代理类
//        Searcher searcher;
//        searcher = (Searcher)Assembly.Load("ProxySample").CreateInstance(proxy);
//        String result = searcher.DoSearch("杨过", "玉女心经");
//        System.out.println(result);

    }

}
