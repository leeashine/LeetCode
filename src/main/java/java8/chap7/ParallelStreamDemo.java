package java8.chap7;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreamDemo {

    public static void main(String[] args) {
        System.out.println("Sequential sum done in:" +
                measureSumPerf(ParallelStreamDemo::sequentialSum, 10_000_000) + " msecs");

//        �ô�ͳforѭ���ĵ����汾ִ������Ӧ�û��ܶ࣬��Ϊ����Ϊ�ײ㣬����Ҫ���ǲ���Ҫ��
//        ԭʼ�������κ�װ�������������������Ų�����������
        System.out.println("Iterative sum done in:" +
                measureSumPerf(ParallelStreamDemo::iterativeSum, 10_000_000) + " msecs");
//        ���а汾�����ԣ�
        System.out.println("Parallel sum done in: " +
                measureSumPerf(ParallelStreamDemo::parallelSum, 10_000_000) + " msecs" );
//        ʵ�������������⣺
//          ? iterate���ɵ���װ��Ķ��󣬱����������ֲ�����ͣ�
//          ? ���Ǻ��Ѱ�iterate�ֳɶ��������������ִ�С���Ϊÿ��Ӧ�����������Ҫ����ǰһ��Ӧ�õĽ��

//        LongStream.rangeClosed�ķ��������������iterate����������ŵ㡣
//          ? LongStream.rangeClosedֱ�Ӳ���ԭʼ���͵�long���֣�û��װ�����Ŀ�����
//          ? LongStream.rangeClosed���������ַ�Χ�������ײ��Ϊ������С�顣���磬��Χ1~20

        System.out.println("Parallel range sum done in:" +
                measureSumPerf(ParallelStreamDemo::parallelRangedSum, 10_000_000) +
                " msecs");
//        ʹ����ȷ�����ݽṹȻ��ʹ�䲢�й����ܹ���֤��ѵ����ܡ�
//        ���ڶ���ں�֮�� �ƶ����ݵĴ���Ҳ���ܱ������Ҫ�����Ժ���Ҫ��һ����Ҫ��֤���ں��в���ִ�й�����ʱ�� �����ں�֮�䴫�����ݵ�ʱ�䳤��




    }
//    Java 8����ԭʼ��������IntStream��
//    LongStream��DoubleStream�����������ֲ����������п��ܶ�Ӧ������Щ����
    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .parallel()
                .reduce(0L, Long::sum);
    }
    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(0L, Long::sum);
    }
    private static Long iterativeSum(Long n) {
        long result = 0;
        for (long i = 1L; i <= n; i++) {
            result += i;
        }
        return result;
    }

    public static long sequentialSum(long n){
        return Stream.iterate(1L,i->i+1)
                .limit(n)
                .reduce(0L,Long::sum);
    }

//    �������ڲ�ʹ����Ĭ�ϵ�ForkJoinPool��7.2�ڻ��һ��������֧/�ϲ���ܣ�����Ĭ�ϵ�
//    �߳�����������Ĵ��������������ֵ���� Runtime.getRuntime().availableProcessors()�õ��ġ�
    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel() //����ת��Ϊ������
                .reduce(0L, Long::sum);
    }

    public static long measureSumPerf(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result: " + sum);
            if (duration < fastest) fastest = duration;
        }
        return fastest;
    }


}
