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
        leftTask.fork();//������һ��ForkJoinPool�߳��첽ִ���´�����������
        ForkJoinSumCalculator rightTask=new ForkJoinSumCalculator(numbers,start+length/2,end);
        Long rightResult=rightTask.compute();//ͬ��ִ�еڶ����������п��������һ���ݹ黮��
        Long leftReuslt=leftTask.join();//��ȡ��һ��������Ľ���������δ��ɾ͵ȴ�
        return rightResult+leftReuslt;//�����������������
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
//        �㴴����һ���µ�ForkJoinPool���������񴫸����ĵ��÷��� ����
//        ForkJoinPool��ִ��ʱ�����һ���������ص�ֵ����ForkJoinSumCalculator�ඨ�����������
//        ��ע����ʵ��Ӧ��ʱ��ʹ�ö��ForkJoinPool��û��ʲô����ġ�
//         ����

    }


}
