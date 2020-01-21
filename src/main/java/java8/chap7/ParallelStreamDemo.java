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

//        用传统for循环的迭代版本执行起来应该会快很多，因为它更为底层，更重要的是不需要对
//        原始类型做任何装箱或拆箱操作。如果你试着测量它的性能
        System.out.println("Iterative sum done in:" +
                measureSumPerf(ParallelStreamDemo::iterativeSum, 10_000_000) + " msecs");
//        并行版本做测试：
        System.out.println("Parallel sum done in: " +
                measureSumPerf(ParallelStreamDemo::parallelSum, 10_000_000) + " msecs" );
//        实际上有两个问题：
//          ? iterate生成的是装箱的对象，必须拆箱成数字才能求和；
//          ? 我们很难把iterate分成多个独立块来并行执行。因为每次应用这个函数都要依赖前一次应用的结果

//        LongStream.rangeClosed的方法。这个方法与iterate相比有两个优点。
//          ? LongStream.rangeClosed直接产生原始类型的long数字，没有装箱拆箱的开销。
//          ? LongStream.rangeClosed会生成数字范围，很容易拆分为独立的小块。例如，范围1~20

        System.out.println("Parallel range sum done in:" +
                measureSumPerf(ParallelStreamDemo::parallelRangedSum, 10_000_000) +
                " msecs");
//        使用正确的数据结构然后使其并行工作能够保证最佳的性能。
//        但在多个内核之间 移动数据的代价也可能比你想的要大，所以很重要的一点是要保证在内核中并行执行工作的时间 比在内核之间传输数据的时间长。




    }
//    Java 8中有原始类型流（IntStream、
//    LongStream、DoubleStream）来避免这种操作，但凡有可能都应该用这些流。
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

//    并行流内部使用了默认的ForkJoinPool（7.2节会进一步讲到分支/合并框架），它默认的
//    线程数量就是你的处理器数量，这个值是由 Runtime.getRuntime().availableProcessors()得到的。
    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel() //将流转换为并行流
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
