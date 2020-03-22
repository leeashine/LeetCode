package aleetcode;

import java.util.Arrays;

public class Contest181 {

    public static void main(String[] args) {

        createTargetArray(new int []{4,2,4,3,2},new int[]{0,0,1,3,1});

//        int yinshu = getYinshu(7);
//        System.out.println(yinshu);

    }
    int[][] dir = new int[][]{{0,0},{3,4,5,6}, {5, 6}, {1, 0}, {-1, 0}};
    public boolean hasValidPath(int[][] grid) {

        int [][] vivisted=new int[grid.length][grid[0].length];



        return false;

    }
    public boolean dfs(int [][] grid,int x,int y){
        if(x<0 || x>=grid.length || y<0 || y>=grid[0].length) return false;
        if(grid[x][y]==-1) return true;

        grid[x][y]=-1;

        boolean res=true;
        for(int [] d:dir){
            res=res & (dfs(grid,x+d[0],y+d[1]));
        }
        return res;
    }


    public int sumFourDivisors(int[] nums) {

        int sum=0;
        for(int num:nums){
            sum+=getYinshu(num);
        }
        return sum;

    }

    private static int getYinshu(int a) {
        int cnt=0;
        int sum=0;
        for (int i = 1; i <= a; i++) {
            if (a%i== 0) {
                if(cnt>4)
                    return 0;
                sum+=i;
                cnt++;
            }
        }

        return cnt==4?sum:0;

    }

    public static int[] createTargetArray(int[] nums, int[] index) {

        int []res=new int[nums.length];
        int i=0;
        for(int idx:index){

            //当前位置上右移k个位置（不循环）
            int k=i-idx;
            int tmp=i;
            for (int j=k;j>0;j--){
                res[tmp]=res[tmp-1];
                tmp--;
            }
            res[idx]=nums[i];
            i++;
        }
        return res;

    }


}
