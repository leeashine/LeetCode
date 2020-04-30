package designpattern.observerpattern.practise;

import java.util.Observable;
import java.util.Observer;

public class GupiaoObserver implements Observer {

    private String name;
    public GupiaoObserver(String name){
        this.name=name;
    }
    public void sendMessage(Object arg) {
        System.out.println( this.name + "你好！");
        System.out.println("股票："+arg+"涨幅达到5%");
    }
    @Override
    public void update(Observable o, Object arg) {
        sendMessage(arg);
    }
}
