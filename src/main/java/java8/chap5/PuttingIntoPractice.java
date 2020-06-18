package java8.chap5;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class PuttingIntoPractice{
    public static void main(String ...args){    
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
        
        
        // Query 1: Find all transactions from year 2011 and sort them by value (small to high).
        List<Transaction> tr2011 = transactions.stream()
                                               .filter(transaction -> transaction.getYear() == 2011)
                                               .sorted(comparing(Transaction::getValue))
                                               .collect(toList());
        System.out.println(tr2011);
        
        // Query 2: What are all the unique cities where the traders work?
        List<String> cities = 
            transactions.stream()
                        .map(transaction -> transaction.getTrader().getCity())
                        .distinct()
                        .collect(toList());
        System.out.println(cities);

        // Query 3: Find all traders from Cambridge and sort them by name.
        
        List<Trader> traders = 
            transactions.stream()
                        .map(Transaction::getTrader)
                        .filter(trader -> trader.getCity().equals("Cambridge"))
                        .distinct()
                        .sorted(comparing(Trader::getName))
                        .collect(toList());
        System.out.println(traders);
        
        
        // Query 4: Return a string of all traders’ names sorted alphabetically.
        
        String traderStr = 
            transactions.stream()
                        .map(transaction -> transaction.getTrader().getName())
                        .distinct()
                        .sorted()
                        .reduce("", (n1, n2) -> n1 + n2);
        System.out.println(traderStr);
        
        // Query 5: Are there any trader based in Milan?
        
        boolean milanBased =
            transactions.stream()
                        .anyMatch(transaction -> transaction.getTrader()
                                                            .getCity()
                                                            .equals("Milan")
                                 );
        System.out.println(milanBased);
        
        
        // Query 6: Update all transactions so that the traders from Milan are set to Cambridge.
        transactions.stream()
                    .map(Transaction::getTrader)
                    .filter(trader -> trader.getCity().equals("Milan"))
                    .forEach(trader -> trader.setCity("Cambridge"));
        System.out.println(transactions);
        
        
        // Query 7: What's the highest value in all the transactions?
        int highestValue = 
            transactions.stream()
                        .map(Transaction::getValue)
                        .reduce(0, Integer::max);
        System.out.println(highestValue);



    }

    /**
     * @param areaCode
     */
    private final Transaction findAreaOrg(String areaCode,List<Transaction> lists)
    {
        Stream<Transaction> stream = null;
        try
        {
            stream = lists.stream();
            Optional<Transaction> op = stream.filter(new Predicate<Transaction>() {
                @Override
                public boolean test(Transaction biz) {
                    try {
                        int value = biz.getValue();
                        return areaCode.equals(value+"");
                    } catch (Exception e) {
//                        logger.warn(e.getMessage(),e);
                        return false;
                    }
                }
            }).findFirst();
            Transaction resultBiz = op.isPresent()?op.get():null;
            return resultBiz;
        }
        finally
        {
            try{if(stream != null) stream.close();}catch(Exception ex) {}
        }
    }

    /**
     * 	根据渠道代码进行代码转换，如无法转换，则使用OtherWish
     * @param codeNo
     * @param channelItemNo
     */
//    public String getChannelCodeMapItem(String codeNo,String channelItemNo)
//    {
//        MerchantCodeMapItem returnItem = null;
//        Stream<MerchantCodeMapItem> stream = null;
//        try
//        {
//            List<MerchantCodeMapItem> codeList = channelCodeMap.get(codeNo);
//            if(codeList != null)
//            {
//                stream = codeList.stream();
//                List<MerchantCodeMapItem> lists = stream
//                        .filter((item) -> StringUtils.equals(channelItemNo, item.getChannelItemNo()))
//                        .collect(Collectors.toList());
//                if(lists.size()==0)
//                {
//                    if(channelCodeOtherWish.containsKey(codeNo))
//                        return channelCodeOtherWish.get(codeNo).getItemNo();
//                    else
//                        return null;
//                }
//                else
//                {
//                    return lists.get(0).getItemNo();
//                }
//            }
//            return null;
//        }
//        finally
//        {
//            if(stream != null) stream.close();
//        }
//    }
}