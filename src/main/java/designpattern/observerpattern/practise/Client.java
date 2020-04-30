package designpattern.observerpattern.practise;

import designpattern.observerpattern.sample2.PlayerObserver;

import java.util.Observable;
import java.util.Observer;

public class Client {
    public static void main(String[] args) {
        Double priceOld=1d,priceNew=1.5d;
        Observable gupiao=new GupiaoNotifyCenter("万达股票");
        Observer observer1,observer2,observer3;
        observer1 = new GupiaoObserver("张三");
        observer2 = new GupiaoObserver("李四");
        observer3 = new GupiaoObserver("王五");
        gupiao.addObserver(observer1);
        gupiao.addObserver(observer2);
        gupiao.addObserver(observer3);
//        notifyObservers会自动调用update方法
        if((priceOld*1.5)<=priceNew)
            gupiao.notifyObservers("万达股票");

    }
}
