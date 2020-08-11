package juc.print1A2B3C;

import java.util.concurrent.locks.LockSupport;
// 解决假死
public class lock_support {
    static Thread t1 = null, t2 = null;

    public static void main(String[] args) throws Exception {
        char[] aI = "1234567".toCharArray();
        char[] aC = "ABCDEFG".toCharArray();

        t1 = new Thread(() -> {

            for(char c : aI) {
                System.out.print(c);
                LockSupport.unpark(t2); //叫醒T2
                LockSupport.park(); //T1阻塞
            }
            LockSupport.unpark(t2);

        }, "t1");

        t2 = new Thread(() -> {

            for(char c : aC) {
                System.out.print(c);
                LockSupport.unpark(t1); //叫醒t1
                LockSupport.park(); //t2阻塞
            }
            LockSupport.unpark(t1);
        }, "t2");

        t1.start();
        Thread.sleep(100);
        t2.start();
    }
}
