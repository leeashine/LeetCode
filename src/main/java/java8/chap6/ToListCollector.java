package java8.chap6;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

    /**
     *
     * @return 建立新的结果容器
     */
    @Override
    public Supplier<List<T>> supplier() {
        return () -> new ArrayList<T>();
    }

    /**
     *
     * @return 将元素添加到结果容器
     */
    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return (list, item) -> list.add(item);
    }

    /**
     *在遍历完流后，finisher方法必须返回在累积过程的最后要调用的一个函数，以便将累加
     * 器对象转换为整个集合操作的最终结果。通常，就像ToListCollector的情况一样，累加器对
     * 象恰好符合预期的最终结果，因此无需进行转换。所以finisher方法只需返回identity函数：
     * @return 对结果容器应用最终转换
     */
    @Override
    public Function<List<T>, List<T>> finisher() {
        return i -> i;
    }

    /**
     * combiner方法会返回一个供归约操作使用的函数，它定义了对
     * 流的各个子部分进行并行处理时，各个子部分归约所得的累加器要如何合并。对于toList而言，
     * 这个方法的实现非常简单，只要把从流的第二个部分收集到的项目列表加到遍历第一部分时得到
     * 的列表后面就行了：
     * 有了这第四个方法，就可以对流进行并行归约了。它会用到Java 7中引入的分支/合并框架和
     * Spliterator抽象
     * @return    合并两个结果容器
     */
    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
    }
}
