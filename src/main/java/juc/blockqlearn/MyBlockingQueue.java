package juc.blockqlearn;

import java.util.LinkedList;

/**
 * synchronized + wait/notify 版本
 * @param <E>
 */
class MyBlockingQueue<E> {
        private final LinkedList<E> queue = new LinkedList<>();
        private final int capacity;

        public MyBlockingQueue(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void put(E e) throws InterruptedException {
            while (queue.size() == capacity) {
                wait(); // 队列满了，等
            }
            queue.addLast(e);
            notifyAll(); // 有新元素了，通知等待的消费者
        }

        public synchronized E take() throws InterruptedException {
            while (queue.isEmpty()) {
                wait(); // 队列空了，等
            }
            E e = queue.removeFirst();
            notifyAll(); // 有空间了，通知等待的生产者
            return e;
        }
    }