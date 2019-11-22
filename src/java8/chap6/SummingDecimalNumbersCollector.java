package java8.chap6;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

public class SummingDecimalNumbersCollector implements Collector<Txt, Map<Date, BigDecimal>,Map<Date, BigDecimal>> {

    @Override
    public Supplier<Map<Date, BigDecimal>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<Date, BigDecimal>, Txt> accumulator() {
        return (Map<Date,BigDecimal> acc,Txt date)->{
            if(acc.containsKey(date.getDate()))
                acc.put(date.getDate(),date.getMoney().add(acc.get(date.getDate())));
            else
                acc.put(date.getDate(),date.getMoney());

        };
    }

    @Override
    public BinaryOperator<Map<Date, BigDecimal>> combiner() {
        return null;
    }

    @Override
    public Function<Map<Date, BigDecimal>, Map<Date, BigDecimal>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(IDENTITY_FINISH);
    }


    public static void main(String[] args) {

        List<Txt> list = new ArrayList<Txt>();

        Txt t1 = new Txt();
        t1.setDate(new Date("08/01/2019"));
        t1.setMoney(new BigDecimal("200.2").setScale(6));

        Txt t2 = new Txt();
        t2.setDate(new Date("08/01/2019"));
        t2.setMoney(new BigDecimal("300.2").setScale(6));

        Txt t3 = new Txt();
        t3.setDate(new Date("08/02/2019"));
        t3.setMoney(new BigDecimal("10.2").setScale(6));

        list.add(t1);
        list.add(t2);
        list.add(t3);

//        System.out.println(new BigDecimal("10.2").setScale(6));

//        Map<Date,Double> map = list.stream().collect(Collectors.groupingBy(Txt::getDate,Collectors.summingDouble(Txt::getMoney))));

        //根据日期统计消费金额
        Map<Date,BigDecimal> map = list.stream().collect(new SummingDecimalNumbersCollector());

        System.out.println(map);


    }

    public static List<Txt> countData(List<Txt> list) {
        String name = list.get(0).getName();
        String shopName = list.get(0).getShopName();
        //根据日期统计消费金额
        Map<Date,BigDecimal> map = list.stream().collect(new SummingDecimalNumbersCollector());
        return null;
    }
}
