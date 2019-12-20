package java8.chap6;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

public class PrimeNumbersCollector implements Collector<Integer, Map<Boolean, List<Integer>>,Map<Boolean, List<Integer>>> {
    /**
     * ��Լ����
     * @return ����һ���ڵ���ʱ�����ۼ����ĺ���
     */
    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        return ()-> new HashMap<Boolean, List<Integer>>(){{
            put(true,new ArrayList<Integer>());
            put(false,new ArrayList<Integer>());
        }};
    }

    /**
     * ���ﲻ�������������ۼ�����Map����Ϊtrue��false�����������ʼ���˶�Ӧ�Ŀ��б�
     * ���ռ������л�������ͷ������ֱ���ӵ�����ռ���������Ҫ�ķ�����accumulator����
     * Ϊ������������ռ�����Ԫ�ص��߼���������Ҳ��ʵ��ǰ���������Ż��Ĺؼ����������κ�һ��
     * �����У������Է����ռ����̵Ĳ��ֽ����Ҳ���ǰ��������ҵ����������ۼ�����
     * @return �ϲ������������
     */
    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (Map<Boolean,List<Integer>> acc,Integer candidate)->{
            acc.get( isPrime( acc.get(true),candidate) ).add(candidate);
        };
    }
//    ���ռ������й�����������ܣ�
//    Ҫ�ڲ����ռ�ʱ�����������ۼ����ϲ�����,�����ֻ��Ҫ�ϲ�����Map����
//    ���ڶ���Map�������ͷ������б��е��������ֺϲ�����һ��Map�Ķ�Ӧ�б��о�����
//    ʵ��������ռ����ǲ��ܲ���ʹ�õģ���Ϊ���㷨������˳��ġ�����ζ����Զ��
//    �������combiner����������԰�����ʵ�����գ����õ��������׳�һ��UnsupportedOperationException�쳣����Ϊ��������������������ǻ��Ǿ���ʵ������
    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (Map<Boolean, List<Integer>> map1,Map<Boolean, List<Integer>> map2)->{
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
            return map1;
        };
    }
//    �������������ʵ�ֶ��ܼ򵥡�ǰ��˵����accumulator���þ����ռ����Ľ�����ò���
//    ��һ��ת������ôfinisher�����ͷ���identity����
    @Override
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }
//    characteristics�᷵��һ�����ɱ��Characteristics���ϣ�������
//    ���ռ�������Ϊ���������ǹ������Ƿ���Բ��й�Լ���Լ�����ʹ����Щ�Ż�����ʾ��
//    Characteristics��һ������������Ŀ��ö�١�
//            ? UNORDERED������Լ�������������Ŀ�ı������ۻ�˳���Ӱ�졣
//            ? CONCURRENT����accumulator�������ԴӶ���߳�ͬʱ���ã��Ҹ��ռ������Բ��й�
//    Լ��������ռ���û�б�ΪUNORDERED����������������������Դʱ�ſ��Բ��й�Լ��
//            ? IDENTITY_FINISH���������������������صĺ�����һ����Ⱥ�������������������
//    ����£��ۼ������󽫻�ֱ��������Լ���̵����ս������Ҳ��ζ�ţ����ۼ���A���Ӽ�
//    ���ת��Ϊ���R�ǰ�ȫ�ġ�
    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(IDENTITY_FINISH);
    }

//    ����һ���������ڱ�����ƽ����ʱ����ֹͣ����
//    ��filter(p -> p <= candidateRoot)��ɸѡ��С�ڱ�����ƽ��������������filter
//    Ҫ�������������ܷ���ǡ���Ľ������������ͷ��������б��ǳ�������Ǹ������ˡ�����
//    ��������������ֻ�����������ڱ�����ƽ������ʱ��ͣ�����Ϳ����ˡ���ˣ����ǻᴴ��һ����
//    ΪtakeWhile�ķ���������һ�������б��һ��ν�ʣ����᷵��Ԫ������ν�ʵ��ǰ׺��
//    ���takeWhileʵ���Ǽ�ʱ�ġ���������£����ǻ���Ҫһ���ӳ���ֵ��
//    takeWhile�������Ϳ��Ժ�noneMatch�����ϲ������ҵ��ǣ�������ʵ�ֳ����˱��µķ�Χ��
//    ����Ҫ�˽�Stream API��ʵ�ֲ��С�
    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p){
        int i=0;
        for(A item:list){
            if(!p.test(item)){
                return list.subList(0,i);//����б��еĵ�ǰ��Ŀ�Ƿ�����ν�ʣ������㷵�ظ���Ŀ֮ǰ��ǰ׺���б�
            }
            i++;
        }
        return list;  //�б��е�������Ŀ������ν�ʣ���˷����б���
    }

//  ֻ�ò����ڱ�����ƽ����������ȥ������
    public static boolean isPrime(List<Integer> primes, int candidate){
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return takeWhile(primes, i -> i <= candidateRoot)
                .stream()
                .noneMatch(p -> candidate % p == 0);
    }

}
