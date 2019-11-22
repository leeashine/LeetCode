package java8.chap6;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

public class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>,Map<Boolean, List<Integer>>> {
    /**
     * 规约过程
     * @return 返回一个在调用时创建累加器的函数
     */
    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        return ()-> new HashMap<Boolean, List<Integer>>(){{
            put(true,new ArrayList<Integer>());
            put(false,new ArrayList<Integer>());
        }};
    }

    /**
     * 这里不但创建了用作累加器的Map，还为true和false两个键下面初始化了对应的空列表。
     * 在收集过程中会把质数和非质数分别添加到这里。收集器中最重要的方法是accumulator，因
     * 为它定义了如何收集流中元素的逻辑。这里它也是实现前面所讲的优化的关键。现在在任何一次
     * 迭代中，都可以访问收集过程的部分结果，也就是包含迄今找到的质数的累加器：
     * @return 合并两个结果容器
     */
    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (Map<Boolean,List<Integer>> acc,Integer candidate)->{
            acc.get( isPrime( acc.get(true),candidate) ).add(candidate);
        };
    }
//    让收集器并行工作（如果可能）
//    要在并行收集时把两个部分累加器合并起来,这里，它只需要合并两个Map，即
//    将第二个Map中质数和非质数列表中的所有数字合并到第一个Map的对应列表中就行了
//    实际上这个收集器是不能并行使用的，因为该算法本身是顺序的。这意味着永远都
//    不会调用combiner方法，你可以把它的实现留空（更好的做法是抛出一个UnsupportedOperationException异常）。为了让这个例子完整，我们还是决定实现它。
    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (Map<Boolean, List<Integer>> map1,Map<Boolean, List<Integer>> map2)->{
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
            return map1;
        };
    }
//    最后两个方法的实现都很简单。前面说过，accumulator正好就是收集器的结果，用不着
//    进一步转换，那么finisher方法就返回identity函数
    @Override
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }
//    characteristics会返回一个不可变的Characteristics集合，它定义
//    了收集器的行为——尤其是关于流是否可以并行归约，以及可以使用哪些优化的提示。
//    Characteristics是一个包含三个项目的枚举。
//            ? UNORDERED——归约结果不受流中项目的遍历和累积顺序的影响。
//            ? CONCURRENT——accumulator函数可以从多个线程同时调用，且该收集器可以并行归
//    约流。如果收集器没有标为UNORDERED，那它仅在用于无序数据源时才可以并行归约。
//            ? IDENTITY_FINISH——这表明完成器方法返回的函数是一个恒等函数，可以跳过。这种
//    情况下，累加器对象将会直接用作归约过程的最终结果。这也意味着，将累加器A不加检
//    查地转换为结果R是安全的。
    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(IDENTITY_FINISH);
    }

//    在下一个质数大于被测数平方根时立即停止测试
//    用filter(p -> p <= candidateRoot)来筛选出小于被测数平方根的质数。但filter
//    要处理整个流才能返回恰当的结果。如果质数和非质数的列表都非常大，这就是个问题了。你用
//    不着这样做；你只需在质数大于被测数平方根的时候停下来就可以了。因此，我们会创建一个名
//    为takeWhile的方法，给定一个排序列表和一个谓词，它会返回元素满足谓词的最长前缀：
//    这个takeWhile实现是即时的。理想情况下，我们会想要一个延迟求值的
//    takeWhile，这样就可以和noneMatch操作合并。不幸的是，这样的实现超出了本章的范围，
//    你需要了解Stream API的实现才行。
    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p){
        int i=0;
        for(A item:list){
            if(!p.test(item)){
                return list.subList(0,i);//检查列表中的当前项目是否满足谓词，不满足返回该项目之前的前缀子列表
            }
            i++;
        }
        return list;  //列表中的所有项目都满足谓词，因此返回列表本身
    }

//  只用不大于被测数平方根的质数去测试了
    public static boolean isPrime(List<Integer> primes, int candidate){
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return takeWhile(primes, i -> i <= candidateRoot)
                .stream()
                .noneMatch(p -> candidate % p == 0);
    }

}
