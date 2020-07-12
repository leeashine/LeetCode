package aleetcode.dp;


public class DP_shizi_boyi {

    public static void main(String[] args) {

    }

    /* 返回游戏最后先手和后手的得分之差 */
    public boolean stoneGame2(int[] piles) {
        int n = piles.length;
        // 初始化 dp 数组
        Pair[][] dp = new Pair[n][n];
        for (int i = 0; i < n; i++)
            for (int j = i; j < n; j++)
                dp[i][j] = new Pair(0, 0);
        // 填入 base case
        for (int i = 0; i < n; i++) {
            dp[i][i].fir = piles[i];
            dp[i][i].sec = 0;
        }
        // 斜着遍历数组
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = l + i - 1;
                // 先手选择最左边或最右边的分数
                int left = piles[i] + dp[i+1][j].sec;
                int right = piles[j] + dp[i][j-1].sec;
                // 套用状态转移方程
                if (left > right) {
                    dp[i][j].fir = left;
                    dp[i][j].sec = dp[i+1][j].fir;
                } else {
                    dp[i][j].fir = right;
                    dp[i][j].sec = dp[i][j-1].fir;
                }
            }
        }
        Pair res = dp[0][n-1];
        return (res.fir - res.sec)>0;
    }


    private class Pair {

        int fir;
        int sec;

        public Pair(int fir, int sec) {
            this.fir = fir;
            this.sec = sec;
        }
    }

    //先手 后手
    public boolean stoneGame(int[] piles) {
        int n=piles.length;
        int [][]dp=new int[n][n];
        //初始化dp[i][i]
        for(int i=0;i<n;i++)
            dp[i][i]=piles[i];

        //注意这里的两个循环
        //第一个循环dis代表间隔距离，比如说dis=1时，配合接下来i的循环，
        //会不断得到相邻2个石头堆的最优选择策略，比如说{1,2,3,4时}，会
        //得到{1,2}、{2、3}、{3、4}的最优选择策略；当dis=2时，会得到相
        //邻3个石头堆的最优选择策略，得到{1,2,3}、{2、3、4}。
        for(int dis=1;dis<n;dis++)
            for(int i=0;i<n-dis;i++)//i仍然表示起始位置
                dp[i][i+dis]=Math.max(piles[i]-dp[i+1][i+dis],piles[i+dis]-dp[i][i+dis-1]);
        return dp[0][n-1]>0;
    }
}
