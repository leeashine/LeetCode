package juc;

public class InterruptExample {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            // 感知到中断，终止
            while (!Thread.currentThread().isInterrupted()) {
                // 执行任务
                // ...

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // 捕获 InterruptedException 异常
                    // 可以选择继续执行或退出循环
                    System.out.println("中断异常发生");
                    // 捕获 InterruptedException 异常，但不重新设置中断状态。任务将继续执行，无法正确地停止
                    Thread.currentThread().interrupt(); // 重新设置中断状态
                }
            }
        });

        // guava的com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(java.util.concurrent.Future<V>)方法源码
        // 和上面的类似，只不过一个是通过break结束循环。第二种方式通return

        thread.start();

        // 等待一段时间后中断线程
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println("感知中断");
            e.printStackTrace();
        }

        thread.interrupt(); // 中断线程

        // 等待线程执行完毕
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}