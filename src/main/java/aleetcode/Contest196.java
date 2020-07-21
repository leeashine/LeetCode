package aleetcode;

import java.util.Arrays;
import java.util.Stack;

public class Contest196 {

    public static void main(String[] args) {

//        int lastMoment = new Contest196().getLastMoment(4, new int[]{4,3}, new int[]{0,1});
//        System.out.println(lastMoment);

        int []A={1,0,1,1,1,0};
        int i = new Contest196().countOneRow(A);
        System.out.println(i);


    }

    public int numSubmat(int[][] mat) {
        int n = mat.length;
        int m = mat[0].length;
        int[][] left = new int[n + 1][m + 1];
        int res = 0;
        for (int i = 1; i <= n; i++) {
            int now = 0;
            for (int j = 1; j <= m; j++) {
                if (mat[i - 1][j - 1] == 1) now++;
                else now = 0;
                left[i][j] = now;
            }
        }
        //首先按列枚举
        for (int j = 1; j <= m; j++) {
            int to_sum = 0;//每个顶点(i,j)为矩阵右下角的矩阵个数，动态更新。
            //栈里元素int[]=<当前的栈顶元素的向左能过延伸的最长长度，被push进栈的元素中，大于栈顶元素的个数>
            Stack<int[]> stack = new Stack<>();
            for (int i = 1; i <= n; i++) {
                int cnt = 0;//计算当前要被push进栈的left[i][j]之前，栈中有多少元素大于left[i][j]
                while (!stack.isEmpty() && stack.peek()[0] > left[i][j]) {
                    //把栈中永不会参与到(i,j)为右下角元素的构建那些矩阵单元去掉（参考PPT的动态）
                    //去掉的过程相当于对栈中元素进行了修剪对齐（使之单调）（见ppT动图）
                    to_sum -= (stack.peek()[1] + 1) * (stack.peek()[0] - left[i][j]);
                    cnt +=stack.peek()[1]+1; // 大于当前left[i][j] 的栈中元素个数 计数
                    stack.pop();
                }
                to_sum+=left[i][j];
                res+=to_sum;
                stack.push(new int[]{left[i][j],cnt});
            }
        }
        return res;

    }

    private int countOneRow(int[] A) {

        int res = 0, length = 0;
        for (int i = 0; i < A.length; ++i) {
            length = (A[i] == 0 ? 0 : length + 1);
            res += length;
        }
        return res;
    }

    public int getLastMoment(int n, int[] left, int[] right) {
        int max = -1;
        for(int i = 0; i < left.length;i++){
            max = Math.max(max,left[i]);
        }
        for(int i = 0; i < right.length;i++){
            max = Math.max(max,n-right[i]);
        }
        return max;
    }


    public boolean canMakeArithmeticProgression(int[] arr) {

        Arrays.sort(arr);
        int dif=arr[1]-arr[0];
        for(int i=1;i<arr.length-1;i++){

            int j=i+1 ;
            int cur=arr[j]-arr[i];
            if(cur!=dif)
                return false;

        }
        return true;

    }
}
