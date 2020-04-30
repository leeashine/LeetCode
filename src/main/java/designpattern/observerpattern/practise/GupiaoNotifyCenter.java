package designpattern.observerpattern.practise;

import java.util.Observable;

public class GupiaoNotifyCenter extends Observable {

    private String gupiaoName;

    public GupiaoNotifyCenter(String gupiaoName) {
        System.out.println(gupiaoName + "系统初始化成功！");
        System.out.println("----------------------------");
        this.gupiaoName = gupiaoName;
    }

    @Override
    public void notifyObservers(Object arg) {
        System.out.println("通知！");
        super.setChanged();
        super.notifyObservers(arg);
    }
}
