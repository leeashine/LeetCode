package aleetcode.dp;

import java.util.Arrays;

/**
 * 完全平方数
 * 给你一个整数 n ，返回 和为 n 的完全平方数的最少数量 。
 * 完全平方数 是一个整数，其值等于另一个整数的平方；换句话说，其值等于一个整数自乘的积。例如，1、4、9 和 16 都是完全平方数，而 3 和 11 不是。
 * 示例 1：
 * 输入：n = 12
 * 输出：3
 * 解释：12 = 4 + 4 + 4
 * 示例 2：
 * 输入：n = 13
 * 输出：2
 * 解释：13 = 4 + 9
 * 提示：
 * 1 <= n <= 104
 * @author: Lee
 * @create: 2025/04/09 13:27
 **/
public class DP_04_perfect_squares {

    public static void main(String[] args) {
        DP_04_perfect_squares squares = new DP_04_perfect_squares();
        squares.numSquares(8);
    }

    /**
     * 这道题和dp有啥关系？
     * 和凑钱有点像 类似？
     * 先不管 定义方程，然后列一下
     * dp[i] : 和为n的完全平方数的最少数量
     * dp[1]=1
     * dp[2]=2
     * dp[3]=1+1+1 =3
     * dp[4]= 2*2 =1
     * dp[5] = 2*2 +1 =2
     * dp[6] = 2*2 +1 +1 = dp[5]+1 =3
     * dp[7] = 2*2 + 1 +1 +1 =dp[6]+1 =4
     * dp[8]= 2*2 + 2*2 =2
     * dp[9] = 1
     * 好像并不会超过4？
     * dp[i] 怎么定义？
     * 那这个状态转移方程可以表示为：
     * dp[i] = min(dp[i], dp[i - j²] + 1) ，其中j² ≤i。
     * 需要组成数n，如果我选择了一个平方数x，那么剩下的问题就是如何组成n-x，并且这个子问题的最优解加上当前的x（即1个）就是总的数量。
     * @param n
     * @return
     */
    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j*j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }
}
