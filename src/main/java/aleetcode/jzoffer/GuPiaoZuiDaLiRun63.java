package aleetcode.jzoffer;

/**
 * https://leetcode.cn/problems/gu-piao-de-zui-da-li-run-lcof/
 */
public class GuPiaoZuiDaLiRun63 {
    public static void main(String[] args) {

        int[] prices = {7, 1, 5, 3, 6, 4};
        int cost1 = new GuPiaoZuiDaLiRun63().maxProfit(prices);
        System.out.println(cost1);
        int[] prices2 = {7, 6, 4, 3, 1};
        int cost2 = new GuPiaoZuiDaLiRun63().maxProfit(prices2);
        System.out.println(cost2);

        int[] prices3 = {2, 1};
        int cost3 = new GuPiaoZuiDaLiRun63().maxProfit(prices3);
        System.out.println(cost3);


    }

    public int maxProfit(int[] prices) {
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (int price : prices) {
            if (price < min) {
                min = price;
            }
            //如果我今天卖出的价格比最高值还要高，那今天就是最高的
            if (price - min > max) {
                max = price - min;
            }
        }
        return max;
    }
}
