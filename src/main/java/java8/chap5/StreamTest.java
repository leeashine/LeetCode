package java8.chap5;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class StreamTest {

    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario","Milan");
        Trader alan = new Trader("Alan","Cambridge");
        Trader brian = new Trader("Brian","Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
//            (1) �ҳ�2011�귢�������н��ף��������׶����򣨴ӵ͵��ߣ���
        List<Transaction> tr2011=
                transactions.stream()
                        .filter(transaction -> transaction.getYear()==2011)
                        .sorted(Comparator.comparing(Transaction::getValue))
                        .collect(toList());
//            (2) ����Ա������Щ��ͬ�ĳ��й�������
        Set<String> cities=
                transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .collect(toSet());
//                .distinct()
//                .collect(toList());

//            (3) �������������ڽ��ŵĽ���Ա��������������
        List<Trader> traders=
                transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> trader.getCity().equals("Cambridge"))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .collect(toList());

//            (4) �������н���Ա�������ַ���������ĸ˳������
        String traderStr=
                transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .collect(Collectors.joining());
//                .reduce("",(n1,n2)->n1+n2);

//            (5) ��û�н���Ա�������������ģ�
        boolean milanBased=
                transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));

//            (6) ��ӡ�����ڽ��ŵĽ���Ա�����н��׶
        transactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .forEach(System.out::println);
//            (7) ���н����У���ߵĽ��׶��Ƕ��٣�
        Optional<Integer> highestValue =
                transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);//reduce ��Լ

//            (8) �ҵ����׶���С�Ľ��ס�
        Optional<Transaction> smallestTransaction =
                transactions.stream()   //ͨ�������Ƚ�ÿ���� �׵Ľ��׶�ҳ���С�Ľ���
                .reduce((t1,t2)->t1.getValue()<t2.getValue()?t1:t2);

    }

}
