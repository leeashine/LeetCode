package designpattern.proxypattern.part1;

public class Proxy extends Subject{

    private RealSubject realSubject=new RealSubject();

    public void PreRequest(){

    }

    public void PostRequest(){

    }

    @Override
    public void Request() {
        PreRequest();
        realSubject.Request(); //������ʵ�������ķ���
        PostRequest();
    }
}
