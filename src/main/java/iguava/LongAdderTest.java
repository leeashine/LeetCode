package iguava;

/**
 * AtomicInteger只有一个value，所有线程累加都要通过cas竞争value这一个变量，高并发下线程争用非常严重；
 * 高并发下N多线程同时去操作一个变量会造成大量线程CAS失败，然后处于自旋状态，导致严重浪费CPU资源，降低了并发性。
 * 而LongAdder则有两个值用于累加，一个是base，它的作用类似于AtomicInteger里面的value，在没有竞争的情况不会用到cells数组，它为null，这时使用base做累加，
 * 有了竞争后cells数组就上场了，第一次初始化长度为2，以后每次扩容都是变为原来的两倍，直到cells数组的长度大于等于当前服务器cpu的数量为止就不在扩容（想下为什么到超过cpu数量的时候就不再扩容）；每个线程会通过线程对cells[threadLocalRandomProbe%cells.length]位置的Cell对象中的value做累加，这样相当于将线程绑定到了cells中的某个cell对象上；
 * https://blog.csdn.net/jiangtianjiao/article/details/103844801/
 *
 * https://blog.csdn.net/zqz_zqz/article/details/70665941
 */
public class LongAdderTest {

    public static void main(String[] args) {



    }
}
