package designpattern.decoratorpattern.practise;

public class Client {
    public static void main(String[] args) {
//        透明模式 抽象构建定义 抽象装饰类定义 可以对一个已装饰过的对象进行多次装饰，得到更为复杂、功能更为强大的对象
//        Encrypt encrypt1,encrypt2,encrypt3;
//        encrypt1=new ShiftEncrypt();
//        encrypt2=new EncryptReverseDecorator(encrypt1);
//        encrypt3=new EncryptModuleDecorator(encrypt2);
//        encrypt3.encrypt();

//        半透明模式 抽象构件定义 具体装饰类定义
//        可以单独使用一种加密方式
//        Encrypt encrypt1=new CommonEncrypt();//抽象构件定义
        EncryptModuleDecorator moduleDecorator=new EncryptModuleDecorator();//具体装饰类定义
        moduleDecorator.moduleEncrypt();
    }
}
