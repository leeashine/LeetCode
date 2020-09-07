package aleetcode.dp;

/**
 * 64. 最小路径和
 * 给定一个包含非负整数的 m x n 网格，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * 说明：每次只能向下或者向右移动一步。
 * 示例: 输入:
 * [
 *   [1,3,1],                   1 4 5               1 4 5
 *   [1,5,1],         ----->    2           ----->  2 7 6
 *   [4,2,1]                    6                   6
 * ]
 * 输出: 7
 * 解释: 因为路径 1→3→1→1→1 的总和最小。
 *      画网格
 */
public class MinimumPathSum {

    public int minPathSum(int[][] grid) {

        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int[][]dp=new int[grid.length][grid[0].length];

        dp[0][0]=grid[0][0];
        for(int i=1;i<grid.length;i++){
            dp[i][0]=dp[i-1][0]+grid[i][0];
        }
        for(int i=1;i<grid[0].length;i++){
            dp[0][i]=dp[0][i-1]+grid[0][i];
        }
        for(int i=1;i<grid.length;i++){
            for(int j=1;j<grid[0].length;j++){

                dp[i][j]=grid[i][j]+Math.min(dp[i-1][j],dp[i][j-1]);

            }
        }
        return dp[grid.length-1][grid[0].length-1];

    }
}
