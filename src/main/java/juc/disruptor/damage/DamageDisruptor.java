package juc.disruptor.damage;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DamageDisruptor {
    private final Disruptor<DamageEvent> disruptor;
    private final RingBuffer<DamageEvent> ringBuffer;

    // 测试
    public static void main(String[] args) throws InterruptedException {
        Character character = new Character(1000);
        DamageDisruptor damageDisruptor = new DamageDisruptor(character);

        // 创建多个线程发起扣血请求
        Runnable task = () -> {
            for (int i = 0; i < 500; i++) {
                damageDisruptor.publishDamage(1);
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        // 给 Disruptor 一些时间处理事件
        Thread.sleep(100);

        System.out.println("Final Health: " + character.getHealth());

        // 关闭 Disruptor
        damageDisruptor.shutdown();
    }

    public DamageDisruptor(Character character) {
        // 创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();

        // 创建工厂
        DamageEventFactory factory = new DamageEventFactory();

        // 设置缓冲区大小，必须是2的幂次方
        int bufferSize = 1024;

        // 创建 Disruptor
//        指定生产者类型为多生产者模式，适用于多个线程发布事件的情况。
//        BlockingWaitStrategy 是一种等待策略，适用于对延迟要求不高的场景。根据需求，可以选择其他等待策略，如 BusySpinWaitStrategy。
        disruptor = new Disruptor<>(factory, bufferSize, executor, ProducerType.MULTI, new BlockingWaitStrategy());

        // 连接事件处理器
        disruptor.handleEventsWith(new DamageEventHandler(character));

        // 启动 Disruptor
        disruptor.start();

        // 获取 RingBuffer
        ringBuffer = disruptor.getRingBuffer();
    }

    /**
     * 发布扣血事件
     */
    public void publishDamage(int damage) {
        long sequence = ringBuffer.next(); // Grab the next sequence
        try {
            DamageEvent event = ringBuffer.get(sequence); // Get the entry in the Disruptor
            event.setDamage(damage); // Fill with data
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    // 关闭 Disruptor
    public void shutdown() {
        disruptor.shutdown();
    }
}