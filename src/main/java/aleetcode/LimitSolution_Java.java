package aleetcode;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Java内部也可以通过原子类计数器AtomicInteger、Semaphore信号量来做简单的限流。
 * 限流会导致用户在短时间内（这个时间段是毫秒级的）系统不可用，一般我们衡量系统处理能力的指标是每秒的QPS或者TPS，假设系统每秒的流量阈值是1000，理论上一秒内有第1001个请求进来时，那么这个请求就会被限流。
 */
public class LimitSolution_Java {
    // 限流的个数
    private int maxCount = 10;
    // 指定的时间内
    private long interval = 60;
    // 原子类计数器
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    // 起始时间
    private long startTime = System.currentTimeMillis();

    public boolean limit(int maxCount, int interval) {
        atomicInteger.addAndGet(1);
        if (atomicInteger.get() == 1) {
            startTime = System.currentTimeMillis();
            atomicInteger.addAndGet(1);
            return true;
        }
        // 超过了间隔时间，直接重新开始计数
        if (System.currentTimeMillis() - startTime > interval * 1000) {
            startTime = System.currentTimeMillis();
            atomicInteger.set(1);
            return true;
        }
        // 还在间隔时间内,check有没有超过限流的个数
        if (atomicInteger.get() > maxCount) {
            return false;
        }
        return true;
    }
}
