package java8.chap11;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * 使用Future中提供的方法完成这样
 * 的操作又是另外一回事。这也是我们需要更具描述能力的特性的原因，比如下面这些。
 *  将两个异步计算合并为一个——这两个异步计算之间相互独立，同时第二个又依赖于第
 * 一个的结果。
 *  等待Future集合中的所有任务都完成。
 *  仅等待Future集合中最快结束的任务完成（有可能因为它们试图通过不同的方式计算同
 * 一个值），并返回它的结果。
 *  通过编程方式完成一个Future任务的执行（即以手工设定异步操作结果的方式）。
 *  应对Future的完成事件（即当Future的完成事件发生时会收到通知，并能使用Future
 * 计算的结果进行下一步的操作，不只是简单地阻塞等待操作的结果）。
 */
public class FutureDemo {

    public static void main(String[] args) {

        ExecutorService executor =  new ThreadPoolExecutor(
                5,
                20,
                0,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                new ThreadFactoryBuilder().setNameFormat("retryClient-pool-%d").build(),
                new ThreadPoolExecutor.DiscardPolicy());

        //以异步的方式在新的线程中执行耗时的操作
        Future<Double> future = executor.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return doSomeLongComutation();
            }
            private Double doSomeLongComutation() {
                return 0.0;
            }

        });
        //异步操作进行的同时，你可以做其他的事情
        doSomethingElse();

        try {
            //可以调用它的get方法去获取操作的结果。
            // 如果操作已经完成，该方法会立刻返回操作的结果，否则它会阻塞你的线程，直到操作完成，返回相应的结果。
            //获取异步操作的结果，如果最终被阻塞，无法得到结果，那么在最多等待一秒后退出
            Double result = future.get(1, TimeUnit.SECONDS);
        }catch (ExecutionException e){
            //计算抛出一个异常
        }catch (InterruptedException e){
            //当前线程在等待过程中被中断
        }catch (TimeoutException e){
            //在Future对象完成之前超过已过期
        }


    }

    private static void doSomethingElse() {
    }

}
