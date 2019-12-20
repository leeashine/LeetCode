package java8.chap7;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class ForkJoinSumCalculator extends RecursiveTask<Long> {

    private final long[] numbers;
    private final int start;
    private final int end;

    private static final long THREADHOLD=10_000;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers,0,numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {

        int length=end-start;
        if(length<=THREADHOLD){
            return computeSequentially();
        }
        ForkJoinSumCalculator leftTask=new ForkJoinSumCalculator(numbers,start,start+length/2);
        leftTask.fork();//利用另一个ForkJoinPool线程异步执行新创建的子任务
        ForkJoinSumCalculator rightTask=new ForkJoinSumCalculator(numbers,start+length/2,end);
        Long rightResult=rightTask.compute();//同步执行第二个子任务，有可能允许进一步递归划分
        Long leftReuslt=leftTask.join();//读取第一个子任务的结果，如果尚未完成就等待
        return rightResult+leftReuslt;//把两个子任务结果组合
    }

    private Long computeSequentially() {

        long sum=0;
        for(int i=start;i<end;i++){
            sum+=numbers[i];
        }
        return sum;

    }

    public static long forkJoinSum(long n){
        long[] numbers= LongStream.rangeClosed(1,n).toArray();
        ForkJoinTask<Long> task=new ForkJoinSumCalculator(numbers);
        return new ForkJoinPool().invoke(task);
//        你创建了一个新的ForkJoinPool，并把任务传给它的调用方法 。在
//        ForkJoinPool中执行时，最后一个方法返回的值就是ForkJoinSumCalculator类定义的任务结果。
//        请注意在实际应用时，使用多个ForkJoinPool是没有什么意义的。
//         单例

    }


}
