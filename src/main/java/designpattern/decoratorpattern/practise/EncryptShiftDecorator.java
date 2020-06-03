package designpattern.decoratorpattern.practise;

public class EncryptShiftDecorator extends EncryptDecorator{


    public EncryptShiftDecorator(Encrypt encrypt) {
        super(encrypt);
    }
    public String encrypt(){
        System.out.println("进行移位加密。。。");
        return null;
    }

}
