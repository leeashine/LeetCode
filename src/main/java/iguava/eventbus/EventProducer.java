package iguava.eventbus;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executors;


public class EventProducer {


    public static void main(String[] args) {
        //异步
        EventBus eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
        eventBus.register(new EventListener());

        /**
         * 在高并发的环境下使用AsyncEventBus时，发送事件可能会出现异常，因为它使用的线程池，当线程池的线程不够用时，会拒绝接收任务，就会执行线程池的拒绝策略，
         * 如果需要关注是否提交事件成功，就需要将线程池的拒绝策略设为抛出异常，并且try-catch来捕获异常。
         */
        try {
            eventBus.post(1);
        } catch (Exception e) {

        }
        eventBus.post(2);

        eventBus.post("3");

    }

}
