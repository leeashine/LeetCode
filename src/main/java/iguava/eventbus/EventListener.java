package iguava.eventbus;

import com.google.common.eventbus.Subscribe;

public class EventListener {

    @Subscribe
    public void listenInteger(Integer num) {
        System.out.println("Integer num: " + num);
    }

    @Subscribe
    public void listenLong(String num) {
        System.out.println("String num: " + num);
    }

}
