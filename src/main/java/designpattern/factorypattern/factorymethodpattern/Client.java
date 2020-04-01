package designpattern.factorypattern.factorymethodpattern;

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
