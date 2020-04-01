package designpattern.factorypattern.factorymethodpattern;

//优点：当需要添加一个日志实现方法 只需要添加一个类实现LoggerFactory即可，不需要对原有代码进行修改
//缺点：系统 中类的个数将成对增加，
public class Client {
    public static void main(String[] args) {
        LoggerFactory factory;
        Logger logger;
        factory = new FileLoggerFactory(); //可引入配置文件实现
        logger = factory.createLogger();
        logger.writeLog();

//        可以将工厂方法隐藏，在工厂类中将直接调用产品类的业务方法
//        LoggerFactory_FactoryHide loggerFactory=new FileLoggerFactory();
//        loggerFactory.writeLog();
    }
}
