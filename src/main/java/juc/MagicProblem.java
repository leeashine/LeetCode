package juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lizixuan
 */
public class MagicProblem {

    private static final AtomicInteger num = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {

        long start = System.currentTimeMillis();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                for (int i= 0; i < 1000000000; i++) {
                    num.addAndGet(1);
                    //safePoint
                }
                //safePoint
//                HotSpot虚拟机为了避免安全点过多带来过重的负担，对循环还有一项优化措施，认为循环次数较少的话，执行时间应该也不会太长，所以使用int类型或范围更小的数据类型作为索引值的循环默认是不会被放置安全点的。这种循环被称为可数循环（Counted Loop），相对应地，使用long或者范围更大的数据类型作为索引值的循环就被称为不可数循环（Uncounted Loop），将会被放置安全点。
                //在stackoverflow上有人提到过一个问题，由于BigInteger的pow执行时JVM没有插入safepoint,导致大量运算时线程一直无法进入safepoint，而GC线程也在等待这个Java线程进入safepoint才能开始GC，结果JVM就Freezen了。
                //How to get Java stacks when JVM can't reach a safepoint https://stackoverflow.com/questions/20134769/how-to-get-java-stacks-when-jvm-cant-reach-a-safepoint
            }
        };
        new Thread(runnable).start();
        new Thread(runnable).start();
        Thread.sleep(1000);

        System.out.println("cost: " + (System.currentTimeMillis() - start));
        System.out.println(num.get());

    }


}
