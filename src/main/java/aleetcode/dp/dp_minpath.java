package aleetcode.dp;

/**
 * @author: Lee
 * @create: 2025/04/09 14:55
 **/
public class dp_minpath {

    public static void main(String[] args) {
        dp_minpath dp = new dp_minpath();
//        int[][] grid = new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};
        int[][] grid = new int[][]{{1,2,3},{4,5,6}};
        dp.minPathSum(grid);
    }

    /**
     * 给定一个包含非负整数的 m x n 网格 grid ，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
     * 说明：每次只能向下或者向右移动一步。
     * grid = [[1,3,1],[1,5,1],[4,2,1]]
     * dp[i][j] 代表走到第i行第j列这个路径上的数字总和最小的值
     * dp[0][0] = grid[0][0] = 1
     * dp[0][1] = dp[0][1-1]+grid[0][1]= 1+3=4
     * dp[0][2] = min(dp[0][2-1]+grid[0][2] , dp[0-1][2]+grid[0][2] )
     * dp[1][0] = dp[0][0]+grid[1][0] =2
     * dp[1][1] = min(dp[1][0]+grid[1][1] , dp[0][1]+grid[1][1]) =  min(4+5,2+5)
     * dp[i][j] = min(dp[i-1][j] +grid[i][j],dp[i][j-1]+grid[i][j]
     *
     * @param grid
     * @return
     */
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = grid[0][0];
//        dp[0][1] = dp[0][0] + grid[0][1];
//        dp[1][0] = dp[0][0] + grid[1][0];
        for (int i = 1; i < n; i++) {
            dp[0][i] = dp[0][i - 1] + grid[0][i];
        }
        for (int j = 1; j < m; j++) {
            dp[j][0] = dp[j - 1][0] + grid[j][0];
        }
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i-1][j] + grid[i][j], dp[i][j-1] + grid[i][j]);
            }
        }
        return dp[m-1][n-1];
    }

}
