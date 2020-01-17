package dp;
//动态规划经典问题 背包问题
//https://blog.csdn.net/chanmufeng/article/details/82955730
public class DP2 {
//    子问题的结果保存下来
    private static int[][] memo;
    /**
     * 解决背包问题的递归函数
     *
     * @param w        物品的重量数组
     * @param v        物品的价值数组
     * @param index    当前待选择的物品索引
     * @param capacity 当前背包有效容量
     * @return 最大价值
     */
    private static int solveKS(int[] w, int[] v, int index, int capacity) {
        //基准条件：如果索引无效或者容量不足，直接返回当前价值0
        if (index < 0 || capacity <= 0)
            return 0;
        //如果此子问题已经求解过，则直接返回上次求解的结果
        if (memo[index][capacity] != 0) {
            return memo[index][capacity];
        }
        //不放第index个物品所得价值
        int res = solveKS(w, v, index - 1, capacity);
        //放第index个物品所得价值（前提是：第index个物品可以放得下）
        if (w[index] <= capacity) {
            res = Math.max(res, v[index] + solveKS(w, v, index - 1, capacity - w[index]));
        }
        //添加子问题的解，便于下次直接使用
        memo[index][capacity] = res;
        return res;
    }

    public static int knapSack(int[] w, int[] v, int C) {
        int size = w.length;
        memo = new int[size][C + 1];
        return solveKS(w, v, size - 1, C);
    }
// --------------------------------------DP解法---------------------------------------------------
    public static int knapSack2(int[] w, int[] v, int C) {
        int size = w.length;
        if (size == 0) {
            return 0;
        }
        int[][] dp = new int[size][C + 1];
        //初始化第一行
        //仅考虑容量为C的背包放第0个物品的情况
        for (int i = 0; i <= C; i++) {
            dp[0][i] = w[0] <= i ? v[0] : 0;
        }
        //填充其他行和列
        for (int i = 1; i < size; i++) {
            for (int j = 0; j <= C; j++) {
                dp[i][j] = dp[i - 1][j];
                if (w[i] <= j) {
                    dp[i][j] = Math.max(dp[i][j], v[i] + dp[i - 1][j - w[i]]);
                }
            }
        }
        return dp[size - 1][C];
    }
//    空间优化
//    我们可以知道，当我们利用一维数组进行记忆化的时候，我们只需要使用到当前位置的值和该位置之前的值，举个例子
//    假设我们要计算F(i,4)F(i,4)F(i,4),我们需要用到的值为F(i−1,4)F(i-1,4)F(i−1,4)和F(i−1,4−w(i))F(i-1,4-w(i))F(i−1,4−w(i)),因此为了防止结果被覆盖，我们需要从后向前依次计算结果
    public static int knapSack3(int[] w, int[] v, int C) {
        int size = w.length;
        if (size == 0) {
            return 0;
        }

        int[] dp = new int[C + 1];
        //初始化第一行
        //仅考虑容量为C的背包放第0个物品的情况
        for (int i = 0; i <= C; i++) {
            dp[i] = w[0] <= i ? v[0] : 0;
        }

        for (int i = 1; i < size; i++) {
            for (int j = C; j >= w[i]; j--) {
                dp[j] = Math.max(dp[j], v[i] + dp[j - w[i]]);
            }
        }
        return dp[C];
    }

//    利用背包问题的思想解决问题
//    leetcode 416 Partition Equal Subset Sum
//    给定一个仅包含正整数的非空数组，确定该数组是否可以分成两部分，要求两部分的和相等
//    问题分析
//    该问题我们可以利用背包问题的思想进行求解。
//    假设给定元素个数为n的数组arr，数组元素的和为sum，对应于背包问题，
//    等价于有n个物品，每个物品的重量和价值均为为arr[i]，背包的限重为sum/2，求解背包中的物品最大价值为多少？

    private boolean knapSack(int[] nums,int sum){
        int size = nums.length;

        boolean[] dp = new boolean[sum + 1];
        for (int i = 0;i <= sum;i ++){
            dp[i] = i == nums[0];
        }

        for (int i = 1;i < size;i++){
            for (int j = sum;j >= nums[i];j--){
                dp[j] = dp[j] || dp[j-nums[i]];
            }
        }
        return dp[sum];
    }

    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int item : nums){
            sum += item;
        }

        //如果数组元素和不是2的倍数，直接返回false
        if (sum % 2 != 0)
            return false;

        return knapSack(nums,sum/2);
    }


    public static void main(String[] args){
        int[] w = {2,1,3,2};
        int[] v = {12,10,20,15};
        System.out.println(knapSack(w,v,5));
    }


}
