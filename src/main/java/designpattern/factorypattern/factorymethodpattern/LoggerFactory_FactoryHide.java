package designpattern.factorypattern.factorymethodpattern;
//改为抽象类
abstract class LoggerFactory_FactoryHide {
    //在工厂类中直接调用日志记录器类的业务方法writeLog()
    public void writeLog() {
        Logger logger = this.createLogger();
        logger.writeLog();
    }
    public abstract Logger createLogger();
}
