package designpattern.decoratorpattern.practise;

public class EncryptDecorator extends Encrypt{

    private Encrypt encrypt;

    public EncryptDecorator() {
    }

    public EncryptDecorator(Encrypt encrypt) {
        this.encrypt = encrypt;
    }

    @Override
    String encrypt() {
        return encrypt.encrypt();
    }
}
