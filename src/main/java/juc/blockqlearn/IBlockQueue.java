package juc.blockqlearn;

public interface IBlockQueue<E> {

    /**
     * 放入队列中，必要时等待空间可用
     * @param e
     */
    void put(E e);

    /**
     * 从队列头部取出元素，必要时等待，直到元素可用
     * @return
     * @throws InterruptedException
     */
    E take() throws InterruptedException;

    int size();
}
