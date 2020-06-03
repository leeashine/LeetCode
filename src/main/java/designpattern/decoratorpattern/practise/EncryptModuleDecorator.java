package designpattern.decoratorpattern.practise;

public class EncryptModuleDecorator extends EncryptDecorator{

    public EncryptModuleDecorator() {
    }

    public EncryptModuleDecorator(Encrypt encrypt) {
        super(encrypt);
    }
    public String encrypt() {
        super.encrypt();
        System.out.println("进行取模加密。。。。。。。");
        return null;
    }
//    如果我想只调用取模加密 那就单独定义一个方法 供调用
    public String moduleEncrypt() {
        System.out.println("单独进行取模加密。。。。。。。");
        return null;
    }
}
