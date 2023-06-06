package aleetcode.code2023;

/**
 * 给定一个整数数组，prices其中prices[i]是当天给定股票的价格。ith
 * 在每一天，您都可以决定买入和/或卖出股票。您在任何时候最多只能持有一股股票。但是，您可以买入然后在同一天立即卖出。
 * 找到并返回您可以获得的最大利润。
 */
public class LC122BestTimeToBuyAndSellStockII {

    public static void main(String[] args) {
//        int[] prices = {7, 1, 5, 3, 6, 4};
//        int i = maxProfit(prices);
//        System.out.println(i);
//        int[] prices = new int[]{1,2,3,4,5};
//        int i = maxProfit(prices);
//        System.out.println(i);
        int[] prices = {7, 6, 4, 3, 1};
        int i = maxProfit(prices);
        System.out.println(i);
    }

    public static int maxProfit(int[] prices) {
        if (prices.length == 0) {
            return 0;
        }
        int result = 0;
        int min = Integer.MAX_VALUE;
        int max = -1;
        // 当前-之前最小值=现在利润
        for (int i = 1; i < prices.length; i++) {
            //计算当前之前的最大值 最小值
            max = Math.max(max, prices[i-1]);
            min = Math.min(min, prices[i-1]);
            // 如果当前值比前一个值大，那说明可以算下利润了。当前最大利润=现在的价格-最低价格
            if (prices[i] >= prices[i - 1]) {
                result += prices[i] - min;
                min = prices[i];
            } else {
                // 如果没有前面一个大，说明要重新开始计算最大最小值了
                // 当前值就是最大最小值
                min = prices[i];
                max = prices[i];
            }
        }
        return result;
    }

    public static int maxProfit1(int[] prices) {
        if (prices.length == 0) {
            return 0;
        }
        int result = 0;
        int min = Integer.MAX_VALUE;
        // 当前-之前遍历过的最小值=当前利润<最大利润？最大利润：当前利润
        for (int i = 0; i < prices.length; i++) {
            min = Math.min(min, prices[i]);
            result = Math.max(prices[i] - min, result);
        }
        return result;
    }

}
