package work.queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * 一个高性能的双缓冲异步消息队列实现。
 * 想象一下 **餐厅厨房的出餐台**。
 *
 * -----
 *
 * ### 传统的缓冲队列：只有一个出餐台
 *
 *   * 厨师（生产者）做好一道菜，就放到出餐台上。
 *   * 服务员（消费者）从这个出餐台把菜取走。
 *
 * **这里的问题是：**
 *
 *   * 如果服务员很忙，没来得及取餐，出餐台很快就**堆满了**。厨师就必须**停下来**，等台子空出来才能继续放菜。
 *   * 反过来，如果厨师做的慢，服务员就得在台子前**干等着**，没事可做。
 *
 * 他们俩共用这**一个**台子，很容易互相干扰和等待，就像在一条很窄的路上，两个人总得有一个人先让路，效率自然就低了。
 *
 * -----
 *
 * ### 双缓冲队列：有两个出餐台（A台 和 B台）
 *
 * 现在我们升级一下厨房，给厨师配了两个出餐台。
 *
 *   * 厨师（生产者）先专心在 **A出餐台** 上放菜，完全不用管服务员。
 *   * 当A台摆满后，厨师把整个 **A台直接“推给”服务员**，然后自己**立刻转身去空的B台**继续放新菜。
 *   * 服务员呢？他接手了整个A台，可以从容地把上面的菜全部取走，而**完全不会打扰到**正在B台忙活的厨师。
 *
 * 等厨师把B台也摆满了，服务员很可能也已经把A台清空了。这时他们再次“交换”台子，厨师用A台，服务员处理B台。
 *
 * -----
 *
 * ### 总结一下区别
 *
 *   * **传统队列** 就像是 **“单行道”**。生产者和消费者在同一条路上，你进我退，需要互相等待，容易“堵车”。
 *   * **双缓冲队列** 就像是 **“两条并行的流水线”**。一条线在生产，另一条线在出货，两者可以**同时进行**，互不干扰。
 *
 * 所以，双缓冲队列最大的好处就是**极大减少了生产者和消费者的互相等待**，让数据处理速度变得飞快，尤其是在需要处理海量数据的场景下，效果特别明显。
 * @param <T> 队列中消息的类型
 */
public class DoubleBufferedAsyncQueue<T> {

    // 后台缓冲区，用于生产者写入
    private Queue<T> writingBuffer;
    // 前台缓冲区，用于消费者读取
    private Queue<T> readingBuffer;

    // 用于保护缓冲区访问和交换的锁
    private final ReentrantLock lock;
    // 用于通知消费者缓冲区已准备好读取的条件变量
    private final Condition consumerCondition;

    // 消费者线程
    private final Thread consumerThread;
    // 队列运行状态的标志位
    private volatile boolean running = true;

    // 缓冲区的容量，当写入缓冲区达到此容量时会自动触发交换
    private final int capacity;

    /**
     * 构造函数。
     *
     * @param batchConsumer 当缓冲区满时，此回调函数将被调用以处理一批消息。
     * @param capacity      缓冲区容量，达到此容量将自动触发刷新（交换缓冲区）。
     */
    public DoubleBufferedAsyncQueue(Consumer<Queue<T>> batchConsumer, int capacity) {
        if (batchConsumer == null) {
            throw new IllegalArgumentException("Consumer callback cannot be null.");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }

        this.writingBuffer = new LinkedList<>();
        this.readingBuffer = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.consumerCondition = lock.newCondition();
        this.capacity = capacity;

        // 创建并启动消费者线程
        this.consumerThread = new Thread(() -> consumerLoop(batchConsumer), "db-queue-consumer");
        this.consumerThread.start();
    }

    /**
     * 消费者线程的主循环。
     * @param batchConsumer 消息处理回调
     */
    private void consumerLoop(Consumer<Queue<T>> batchConsumer) {
        while (running || !readingBuffer.isEmpty()) {
            lock.lock();
            try {
                // 如果读取缓冲区为空且队列仍在运行，则等待生产者填满并交换
                while (readingBuffer.isEmpty() && running) {
                    try {
                        consumerCondition.await();
                    } catch (InterruptedException e) {
                        // 如果在等待时被中断，重新设置中断状态并继续
                        Thread.currentThread().interrupt();
                    }
                }

                // 如果队列已停止且读取缓冲区也为空，则退出循环
                if (!running && readingBuffer.isEmpty()) {
                    break;
                }
                
                // 持有锁期间，将读取缓冲区的内容交给回调函数处理
                // 这样做是为了尽快释放锁，让生产者可以继续工作
                // 注意：回调函数将在消费者线程中执行
                batchConsumer.accept(readingBuffer);
                
                // 处理完后清空，为下一次交换做准备
                readingBuffer.clear();

            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 生产者调用的方法，用于向队列中添加消息。
     * 此方法是线程安全的。
     *
     * @param message 要添加的消息
     */
    public void produce(T message) {
        lock.lock();
        try {
            // 如果队列已经关闭，则拒绝新消息
            if (!running) {
                System.err.println("Queue is shut down. Message rejected: " + message);
                return;
            }
            writingBuffer.add(message);
            // 如果写入缓冲区已满，则触发一次刷新（交换）
            if (writingBuffer.size() >= capacity) {
                flush();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 手动触发刷新，将写入缓冲区的内容立即交给消费者。
     * 这在需要确保所有已生产消息都被尽快处理时很有用（例如，在程序关闭前）。
     * 此方法是线程安全的。
     */
    public void flush() {
        lock.lock();
        try {
            // 如果写入缓冲区没有内容，则无需操作
            if (writingBuffer.isEmpty()) {
                return;
            }
            // 交换缓冲区
            Queue<T> temp = writingBuffer;
            writingBuffer = readingBuffer;
            readingBuffer = temp;
            
            // 通知消费者线程有新的数据可以读取了
            consumerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 优雅地关闭队列。
     * 会等待所有已生产的消息被消费完毕。
     */
    public void shutdown() {
        System.out.println("Shutting down the queue...");
        lock.lock();
        try {
            running = false;
            // 执行最后一次刷新，确保写入缓冲区的所有剩余消息都被处理
            flush();
        } finally {
            lock.unlock();
        }

        try {
            // 等待消费者线程执行完毕并退出
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Queue has been shut down.");
    }

    /**
     * 主函数，用于演示队列的使用。
     */
    public static void main(String[] args) {
        // 定义一个消费者回调，它会打印收到的整批消息
        Consumer<Queue<String>> batchConsumer = (batch) -> {
            System.out.printf("--- Consuming batch of %d messages on thread: %s ---\n", 
                              batch.size(), Thread.currentThread().getName());
            batch.forEach(System.out::println);
            // 模拟耗时的处理操作
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        // 创建一个容量为10的双缓冲异步队列
        final DoubleBufferedAsyncQueue<String> queue = new DoubleBufferedAsyncQueue<>(batchConsumer, 10);
        
        // 使用线程池模拟多个生产者
        ExecutorService producerPool = Executors.newFixedThreadPool(3);
        
        System.out.println("Starting producers...");
        for (int i = 0; i < 3; i++) {
            final int producerId = i;
            producerPool.submit(() -> {
                for (int j = 0; j < 25; j++) {
                    String message = String.format("Message %d from Producer %d", j, producerId);
                    queue.produce(message);
                    try {
                        // 模拟生产间隔
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }

        // 等待生产者完成生产
        producerPool.shutdown();
        try {
            producerPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Producers finished. Shutting down the queue...");
        
        // 关闭队列，这将确保所有剩余的消息都被消费
        queue.shutdown();
    }
}