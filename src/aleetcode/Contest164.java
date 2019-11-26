package aleetcode;

public class Contest164 {

    public static void main(String[] args) {

        int[][] points = {{1, 1}, {3, 4}, {-1, 0}};

        int cnt = minTimeToVisitAllPoints(points);
        System.out.println(cnt);
        int[][] grid = {{1, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
//		int [][] grid={{1,0},{1,1}};
        System.out.println(countServers(grid));

    }

    //œ»º”∫Ûºı
    public static int countServers(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        int cnt = 0;
        int[] rowCount = new int[grid.length];
        int[] colCount = new int[grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    rowCount[i]++;
                    colCount[j]++;
                    cnt++;
                }
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    if (rowCount[i] == 1 && colCount[j] == 1) {
                        cnt--;
                    }
                }
            }
        }


        return cnt;
    }


    public static int minTimeToVisitAllPoints(int[][] points) {
        int ans = 0;
        for (int i = 1; i < points.length; ++i) {
            int[] cur = points[i], prev = points[i - 1];
            ans += Math.max(Math.abs(cur[0] - prev[0]), Math.abs(cur[1] - prev[1]));
        }
        return ans;
    }

}
