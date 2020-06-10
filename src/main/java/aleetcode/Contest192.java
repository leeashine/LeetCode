package aleetcode;

import java.util.Arrays;

public class Contest192 {
    public static void main(String[] args) {
//        int [] nums={2,5,1,3,4,7};
//        int n=3;
//        int[] shuffle = new Contest192().shuffle(nums, n);
//        System.out.println(Arrays.toString(shuffle));
        int [] arr={1,2,3,4,5};
        int k=2;
        int[] strongest = new Contest192().getStrongest(arr, k);
        System.out.println(Arrays.toString(strongest));

    }

//    输入：houses = [0,0,0,0,0], cost = [[1,10],[10,1],[10,1],[1,10],[5,1]], m = 5, n = 2, target = 3
//    输出：9
//    解释：房子涂色方案为 [1,2,2,1,1]
//    此方案包含 target = 3 个街区，分别是 [{1}, {2,2}, {1,1}]。
//    涂色的总花费为 (1 + 1 + 1 + 1 + 5) = 9。

    public int minCost(int[] houses, int[][] cost, int m, int n, int target) {
        // dp[i][j][k] 涂完前i个房子，目前有j个街区，且第i个房子的颜色为k的所有方案 cost最小的。
        // 房子从0开始，街区从1开始，颜色从1开始
        final int INF = (int)Math.pow(10, 8);
        // System.out.println(INF);
        int[][][] dp = new int [m+1][target+1][n+1];
        for(int i = 0; i < m + 1; i++){
            for(int j = 0; j < target + 1; j++){
                for(int k = 0; k < n + 1; k++){
                    dp[i][j][k] = INF;
                }
            }
        }
        // 初始化第0个房子
        // 第0个房子已经涂色
        if(houses[0] > 0){
            dp[0][1][houses[0]] = 0;
        }else{
            // 第0个房子没有涂色，初始化cost
            for(int i = 1; i <= n; i++){
                dp[0][1][i] = cost[0][i - 1];
            }
        }
        // 状态转移时涂色完第i个房子。
        for(int i = 1; i < m; i++){
            // 最多target个街区
            for(int j = 1; j <= target; j++){
                // 分成第i个房子是否涂色
                if(houses[i] > 0){
                    int temp = houses[i];
                    for(int k = 1; k <= n; k++){
                        // 分成第i个房子和第i-1的房子
                        // 如果两个房子颜色相同，那么街区数就相同
                        // 如果两个房子颜色不同，那么第i个房子就独自成一个街区
                        if(temp == k){
                            dp[i][j][temp] = Math.min(dp[i][j][temp], dp[i - 1][j][k]);
                        }else{
                            dp[i][j][temp] = Math.min(dp[i][j][temp], dp[i - 1][j - 1][k]);
                        }
                    }
                }else{
                    for(int k = 1; k <= n; k++){
                        for(int s = 1; s <= n; s++){
                            if(k == s){
                                dp[i][j][k] = Math.min(dp[i][j][k], dp[i - 1][j][s] + cost[i][k - 1]);
                            }else{
                                dp[i][j][k] = Math.min(dp[i][j][k], dp[i - 1][j - 1][s] + cost[i][k - 1]);
                            }
                        }
                    }

                }
            }
        }
        int res = INF;
        for(int i = 1; i <= n; i++){
            res = Math.min(res, dp[m-1][target][i]);
            // System.out.println(res);
        }
        if(res == INF) return -1;
        return res;


    }


//    输入：arr = [1,2,3,4,5], k = 2
//    输出：[5,1]
//    解释：中位数为 3，按从强到弱顺序排序后，数组变为 [5,1,4,2,3]。最强的两个元素是 [5, 1]。[1, 5] 也是正确答案。
//    注意，尽管 |5 - 3| == |1 - 3| ，但是 5 比 1 更强，因为 5 > 1 。
//    双指针题
    public int[] getStrongest(int[] arr, int k) {
        if (arr.length <= 1 || k >= arr.length) {
            return arr;
        }

        Arrays.sort(arr);
        int mid = arr[(arr.length - 1) / 2];
        int l = 0;
        int r = arr.length - 1;
        int[] ans = new int[k];
        int idx = 0;
        while (idx < k) {
            if (Math.abs(arr[r] - mid) >= Math.abs(arr[l] - mid)) {
                ans[idx++] = arr[r];
                r--;
            } else {
                ans[idx++] = arr[l];
                l++;
            }

        }
        return ans;

    }



//                 0 1 2 3 4 5
//    输入：nums = [2,5,1,3,4,7], n = 3
//    输出：[2,3,5,4,1,7]
    public int[] shuffle(int[] nums, int n) {
        int length=nums.length;
        int []res=new int[length];
        int offset=length/2;
        int j=0;
        for (int i = 0; i < length&&j<length; i++) {
            res[j++]=nums[i];
            res[j++]=nums[i+offset];
        }

        return res;

    }
}
