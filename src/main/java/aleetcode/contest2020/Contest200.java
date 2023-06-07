package aleetcode.contest2020;

import java.util.*;

public class Contest200 {
    public static void main(String[] args) {

//        int [][] grid={{0,0,1},{1,1,0},{1,0,0}};
//        new Contest200().minSwaps(grid);

        int[] arr={1,2,3,4,5};
        int k=100;
        new Contest200().getWinner(arr,k);


    }

    public int minSwaps(int[][] grid) {
        int n = grid.length, res=0;
        List<Integer> row = new LinkedList<>();
        for (int i=0; i<n; i++){
            int trailingZeroCnt=0;
            for (int j=n-1; j>-1 && grid[i][j]==0; j--) trailingZeroCnt++;
            row.add(trailingZeroCnt);
        }
        for (int curRowIdx=0, minTrailingZeros=n-1; curRowIdx<n; curRowIdx++, minTrailingZeros--){
            int satisfiedRowIdx =curRowIdx;
            while (satisfiedRowIdx <n && row.get(satisfiedRowIdx)<minTrailingZeros) satisfiedRowIdx++;
            if (satisfiedRowIdx ==n) return -1;
            int toRemove = row.remove(satisfiedRowIdx);
            row.add(curRowIdx, toRemove);
            //需要移动的次数 1要移动到3位置上 需要3-1=2次 3移动到3位置上需要3-3=0次
            res+=satisfiedRowIdx -curRowIdx;
        }
        return res;
    }

//    找出数组游戏的赢家
    //输入：arr = [2,1,3,5,4,6,7], k = 2
    //输出：5
    //本质上是找“部分数组上的最大值”
    public int getWinner(int[] arr, int k) {
        int n=arr.length;
        int my_k=0;
        int res;
        for(int i=1;i<n;){
            if(arr[0]>arr[i]){
                my_k++;
                i++;
            }else{
                //首元素易主
                my_k=1;
                arr[0]=arr[i];
                i++;
            }
            if(my_k==k) return arr[0];
        }
        return arr[0];
    }

    private static int getMax(int [] arr){
        int max=0;
        for (int i = 0; i < arr.length; i++) {
            max=Math.max(max,arr[i]);
        }
        return max;

    }
    //移动 数组左移 并且当前位置上的数放到最后
    private static void move(int []arr,int v){

        if(v<0||v>=arr.length)
            return;
        int tmp=arr[v];
        for(int i=v+1;i<arr.length;i++){
            arr[i-1]=arr[i];
        }
        arr[arr.length-1]=tmp;

    }

    //统计好三元组
    public int countGoodTriplets(int[] arr, int a, int b, int c) {

        int res=0;
        for (int i = 0; i < arr.length-2; i++) {
            for (int j = i+1; j < arr.length-1; j++) {
                //注意，这里进行剪枝操作！
                if(Math.abs(arr[i]-arr[j])<=a){
                    for (int k = j+1; k < arr.length; k++) {
                        if(Math.abs(arr[j]-arr[k])<=b&&Math.abs(arr[i]-arr[k])<=c)
                            res++;
                    }
                }
            }
        }
        return res;

    }
}
