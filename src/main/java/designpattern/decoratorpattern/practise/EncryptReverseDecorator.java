package designpattern.decoratorpattern.practise;

public class EncryptReverseDecorator extends EncryptDecorator{


    public EncryptReverseDecorator(Encrypt encrypt) {
        super(encrypt);
    }
    public String encrypt(){

        super.encrypt();
        System.out.println("进行逆向加密。。。。。。");

        return null;

    }

}
