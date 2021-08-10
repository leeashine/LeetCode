package aleetcode.dfs;

/**
 * Created by LIZIXUAN560 on 2020/10/30.
 * 岛屿的周长
 * https://leetcode-cn.com/problems/island-perimeter/
 * @author LIZIXUAN560
 */
public class IsLandPerimeter {

//    输入:
//            [[0,1,0,0],
//            [1,1,1,0],
//            [0,1,0,0],
//            [1,1,0,0]]
//
//    输出: 16
//
//    解释: 它的周长是下面图片中的 16 个黄色的边：
    public int islandPerimeter(int[][] grid) {

        for(int r=0;r<grid.length;r++){
            for(int c=0;c<grid[0].length;c++){
                if(grid[r][c]==1){
                    //只有一个岛屿
                    return dfs(grid,r,c);
                }
            }
        }
        return 0;

    }

    public int dfs(int [][]grid,int r,int c){

        //岛屿到边界
        if(r<0||r>=grid.length||c<0||c>=grid[0].length){
            return 1;
        }
        //岛屿到海
        if(grid[r][c]==0){
            return 1;
        }
        if(grid[r][c]!=1){
            return 0;
        }
        //遍历标记
        grid[r][c]=2;
        return dfs(grid,r-1,c)
        + dfs(grid,r+1,c)
        + dfs(grid,r,c+1)
        + dfs(grid,r,c-1);

    }

    public static void main(String[] args) {

    }
}
