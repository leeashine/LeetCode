package aleetcode;

/**
 * 买卖股票的最佳时机
 * 1.5,-12.3,3.2,-5.5,23.2,3.2,-1.4,-12.2,34.2,5.4,-7.8,1.1,-4.9
 * 寻找一只股票最长的有效增长期。研究股票投资的人都想了解一只股票最长的有效增长期是哪一个时间段，即从哪一天买进到哪一天卖出，收益最大。
 * 上面这一组数字可以认为是一只股票每天的涨跌幅度（扣除大盘影响后）。
 * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/solution/121-mai-mai-gu-piao-de-zui-jia-shi-ji-by-leetcode-/
 */
public class MaiMaiGuPiaoDeZuiJiaShiJi {
    public static void main(String[] args) {

    }

    /**
     * 方法一 暴力破解法
     *
     * @param prices
     * @return
     */
    public int maxProfit1(int[] prices) {
        int maxprofit = 0;
        for (int i = 0; i < prices.length - 1; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                int profit = prices[j] - prices[i];
                if (profit > maxprofit) {
                    maxprofit = profit;
                }
            }
        }
        return maxprofit;
    }

    /**
     * 方法二：遍历法(动态规划)
     * <p>
     * 我们来假设自己来购买股票。随着时间的推移，每天我们都可以选择出售股票与否。那么，假设在第 i 天，如果我们要在今天卖股票，那么我们能赚多少钱呢？
     * <p>
     * 显然，如果我们真的在买卖股票，我们肯定会想：如果我是在历史最低点买的股票就好了！太好了，在题目中，我们只要用一个变量记录一个历史最低价格 minprice，我们就可以假设自己的股票是在那天买的。那么我们在第 i 天卖出股票能得到的利润就是 prices[i] - minprice。
     * <p>
     * 因此，我们只需要遍历价格数组一遍，记录历史最低点，然后在每一天考虑这么一个问题：如果我是在历史最低点买进的，那么我今天卖出能赚多少钱？当考虑完所有天数之时，我们就得到了最好的答案。
     * 方法二可以看做一种动态规划，只不过对空间复杂度进行了优化。
     * 前i日最大利润=max(前(i−1)日最大利润,第i日价格−前i日最低价格)
     * 初始状态： dp[0] = 0dp[0]=0 ，即首日利润为 00 ；
     * 返回值： dp[n - 1]dp[n−1] ，其中 nn 为 dpdp 列表长度。
     * @param prices
     * @return
     */
    public int maxProfit2(int[] prices) {
        int minprice = Integer.MAX_VALUE;
        int maxprofit = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < minprice) {
                //前i天的最低值
                minprice = prices[i];
            }
            //如果我今天卖出的利润要比之前高。那今天就是最高利润
            if (prices[i] - minprice > maxprofit) {
                maxprofit = prices[i] - minprice;
            }
        }
        return maxprofit;

    }

}
