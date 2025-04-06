package aleetcode.dp;

/**
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 *
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * @author: Lee
 * @create: 2025/02/28 10:12
 **/
public class DP_pa_lou_ti {

    public static void main(String[] args) {
        DP_pa_lou_ti dp = new DP_pa_lou_ti();
        dp.climbStairs(2);

    }

    /**
     * 1.定义一个 dp[i],表示需要n阶达楼顶的总共方法数
     * 2.状态转移方程 dp[i] = dp[i-1]+dp[i-2]
     * dp[0]=0
     * dp[1]=1
     * dp[2]=2
     * @param n
     * @return
     */
    public int climbStairs(int n) {
        if (n == 0) {
            return 1;
        }
        if (n == 1) {
            return 1;
        }

        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;
//        dp[2] = 2;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }




}
