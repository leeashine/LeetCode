package juc;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * list线程不安全
 * Vector、ArrayList在迭代的时候如果同时对其进行修改就会抛出java.util.ConcurrentModificationException
 * ConcurrentModificationException
 * CopyOnWriteArrayList线程安全
 */
public class ListTest {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> list = Lists.newArrayList(88, 236, 2918, 26, 666);
        list.add(88);
        list.add(8);
        list.add(889);
        new Thread(()->{
            Collections.sort(list);
        }).start();

        new Thread(()->{
            Collections.sort(list);
        }).start();

        System.out.println(list);
        Thread.sleep(300);
        System.out.println(list);
    }
}
