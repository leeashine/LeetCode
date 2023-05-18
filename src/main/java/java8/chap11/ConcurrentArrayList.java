package java8.chap11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 写多读少的线程安全的list
 * TODO 不知道性能如何？
 * @author: Lee
 * @create: 2023/05/07 10:01
 **/
public class ConcurrentArrayList<E> extends ArrayList<E> {
    private ReentrantLock lock = new ReentrantLock();

    public ConcurrentArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public boolean add(E e) {
        boolean var2;
        try {
            this.lock.lock();
            super.add(e);
            var2 = true;
        } finally {
            this.lock.unlock();
        }

        return var2;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean var2;
        try {
            this.lock.lock();
            super.addAll(c);
            var2 = true;
        } finally {
            this.lock.unlock();
        }

        return var2;
    }
}

