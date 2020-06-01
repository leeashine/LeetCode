package aleetcode;

import java.util.*;

public class Contest191 {
    public static void main(String[] args) {
//        new Contest191().maxArea(8,3,new int []{5,7,3,4,6},new int[]{2});

//        int res=new Contest191().minReorder(6,new int[][]{{0,1},{1,3},{2,3},{4,0},{4,5}});
//        int res=new Contest191().minReorder(5,new int[][]{{1,0},{1,2},{3,2},{3,4}});
        int res=new Contest191().minReorder(3,new int[][]{{1,0},{2,0}});
        System.out.println(res);
    }

//    重新规划路线问题
//    输入：n = 6, connections = [[0,1],[1,3],[2,3],[4,0],[4,5]]
//    输出：3
//    思路：用DFS解决连通性问题，找到有几条联通，如果方向相反就+1
//
    public int minReorder2(int n, int[][] connections) {
        boolean[] set = new boolean[n+1];
        ArrayDeque<int[]> qq = new ArrayDeque<>();
        int ans = 0;
        for(int[] a:connections){
            if(a[1]==0){
                set[a[0]] = true;
                n--;
            }
            else
                qq.add(a);
        }
        set[0] = true;
        while(n>1){
            int[] cur = qq.pop();
            if(set[cur[1]]){
                set[cur[0]]=true;
                n--;
                continue;
            }
            if(set[cur[0]]){
                set[cur[1]]=true;
                n--;
                ans++;
            }
            else
                qq.add(cur);
        }
        return ans;
    }

    int res=0;
    public int minReorder(int n, int[][] connections) {
        if(n<(connections.length+1))
            return 0;
        int []visited=new int[connections.length+1];
        visited[0]=1;
        Stack<Integer> stack= new Stack();
        for (int i = 0; i < connections.length; i++) {
            int from=connections[i][0];
            int to=connections[i][1];
            if(from==0&&visited[to]==0){
                stack.push(to);
                visited[to]=1;
                res++;
            }
            else if (to==0&&visited[from]==0){
                stack.push(from);
                visited[from]=1;
            }
        }
        while(!stack.empty()){
            int dest=stack.pop();
            dfs(dest,connections,visited);
        }

        return res;

    }

    public void dfs(int n,int [][]arr,int [] visited){
        visited[n]=1;
        for (int i = 0; i < arr.length; i++) {
            int from=arr[i][0];
            int to=arr[i][1];
            if(visited[from]==1&&visited[to]==1)
                continue;
            if(from==n){
                res++;
                dfs(to,arr,visited);
            }else if(to==n){
                dfs(from,arr,visited);
            }
        }

    }

//   防止整形溢出 先定义long 结果取模 再转成int
    public int maxArea(int h, int w, int[] horizontalCuts, int[] verticalCuts) {

        long hMax=0;
        long wMax=0;
        int mod = 1000000007;
        Arrays.sort(horizontalCuts);
        Arrays.sort(verticalCuts);
        hMax=Math.max(horizontalCuts[0],h-horizontalCuts[horizontalCuts.length-1]);
        wMax=Math.max(verticalCuts[0],w-verticalCuts[verticalCuts.length-1]);
        for (int i = 1; i < horizontalCuts.length; i++) {
                int j=i-1;
                hMax=Math.max(hMax,horizontalCuts[i]-horizontalCuts[j]);

        }
        for (int i = 1; i < verticalCuts.length; i++) {
                int j = i - 1;
                wMax = Math.max(wMax, verticalCuts[i] - verticalCuts[j]);

        }


        return (int)((hMax * wMax) % mod);

    }
//    输入：nums = [3,4,5,2]
//    输出：12
    public int maxProduct(int[] nums) {

        int max=0;
        for(int i=0;i<nums.length-1;i++)
            for(int j=i+1;j<nums.length;j++){

                max=Math.max(max,(nums[i]-1)*(nums[j]-1));

            }
        return max;


    }
}
