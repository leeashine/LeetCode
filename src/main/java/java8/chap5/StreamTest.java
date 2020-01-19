package java8.chap5;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

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
//            (1) 找出2011年发生的所有交易，并按交易额排序（从低到高）。
        List<Transaction> tr2011=
                transactions.stream()
                        .filter( transaction -> transaction.getYear()==2011)
                        .sorted(comparing(Transaction::getValue))
                        .collect(Collectors.toList());
//            (2) 交易员都在哪些不同的城市工作过？
        Set<String> cities=
                transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .collect(Collectors.toSet());
//                .distinct()
//                .collect(toList());

//            (3) 查找所有来自于剑桥的交易员，并按姓名排序。
        List<Trader> traders=
                transactions.stream()
                .map(Transaction::getTrader)
                        .filter(trader -> trader.getCity().equalsIgnoreCase("Cambridge"))
                        .distinct()
                        .sorted(comparing(Trader::getName))
                .collect(Collectors.toList());

//            (4) 返回所有交易员的姓名字符串，按字母顺序排序。
        String traderStr=
                transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct().sorted()
                .collect(Collectors.joining());
//                .reduce("",(n1,n2)-> n1 + n2);
//                .reduce("",(n1,n2)->n1+n2);

//            (5) 有没有交易员是在米兰工作的？
        boolean milanBased=
                transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals("Milan"));

//            (6) 打印生活在剑桥的交易员的所有交易额。
        transactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals("Cambridge"))
                .map(Transaction::getValue)
                .forEach(System.out::println);
//            (7) 所有交易中，最高的交易额是多少？
        Optional<Integer> highestValue =
                transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);//reduce 规约

//            (8) 找到交易额最小的交易。
        Optional<Transaction> smallestTransaction =
                transactions.stream()   //通过反复比较每个交 易的交易额，找出最小的交易
                .reduce((t1,t2)->t1.getValue()<t2.getValue()?t1:t2);

    }

}
