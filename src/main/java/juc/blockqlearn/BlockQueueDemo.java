package juc.blockqlearn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自己实现一个阻塞队列
 * 消费者/生产者模式
 * 1.put/take方法
 * 2.一个队列、2个condition
 */
public class BlockQueueDemo<E> implements IBlockQueue<E> {

    /**
     * 不空 为了take
     */
    private final Condition notEmpty;

    /**
     * 不满 为了put
     */
    private final Condition notFull;

    /**
     * 元素
     */
    private final List<E> list;

    /**
     * 锁
     *
     * @param e
     */
    private final ReentrantLock lock;

    /**
     * 最大容量
     */
    private int capacity;


    public BlockQueueDemo(int capacity) {
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
        this.capacity = capacity;
        this.list = new ArrayList<>(capacity);
    }

    @Override
    public void put(E e) {
        lock.lock();
        try {
            while (capacity == list.size()) {
                notFull.await();
            }
            list.add(e);
            capacity++;
            notEmpty.signal();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    @Override
    public E take() throws InterruptedException {
        lock.lock();
        E e = null;
        try {
            while (capacity == 0) {
                notEmpty.await();
            }
            capacity--;
            notFull.signal();
            e = list.remove(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
        return e;
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return list.size();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockQueueDemo<Integer> queue = new BlockQueueDemo<>(10);
        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    queue.put(i);
                    System.out.println("生产了: " + i + "，队列大小: " + queue.size());
                    Thread.sleep(100); // 模拟生产耗时
                }
            } catch (InterruptedException e) {
                System.out.println("生产者被中断！");
                e.printStackTrace();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    Integer value = queue.take();
                    System.out.println("消费了: " + value + "，队列大小: " + queue.size());
                    Thread.sleep(150); // 模拟消费耗时
                }
            } catch (InterruptedException e) {
                System.out.println("消费者被中断！");
                e.printStackTrace();
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
        System.out.println("所有任务完成！");



    }


}
