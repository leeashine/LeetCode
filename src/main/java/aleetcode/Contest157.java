package aleetcode;

import java.util.HashMap;

public class Contest157 {

	public static void main(String[] args) {
		
		int [] arr={1,5,7,8,5,3,4,2,1};
		int difference=-2;
		int count=longestSubsequence(arr, difference);
		System.out.println(count);

	}
	
	private static final int[] d = {0, 1, 0, -1, 0};
    public int getMaximumGold(int[][] grid) {
        int ans = 0;
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[0].length; ++j) {
            	//每一个点都进行dfs
                ans = Math.max(ans, dfs(grid, i, j, 0));
            }
        }
        return ans;
    }
    private int dfs(int[][] g, int i, int j, int sum) {
        if(i<0||j<0||i>g.length||j>g[0].length||g[i][j]<0||g[i][j]>1000)
        	return sum;
        
        sum+=g[i][j];
        g[i][j]+=1000;
        
        int mx=0;
        for(int k=0;k<d.length;++k){
        	
        	mx=Math.max(mx, dfs(g,i+d[k],j+d[k+1],sum));
        	
        }
        
        g[i][j]-=1000; //还原数值
        return mx;
        
    }
    
    
	
	//arr = [1,5,7,8,5,3,4,2,1], difference = -2  //倒推法！
	public static int longestSubsequence2(int[] arr, int difference) {
		HashMap<Integer, Integer> dp = new HashMap<>();
		int longest = 0;
		for(int i=0; i<arr.length; i++) {
			dp.put(arr[i], dp.getOrDefault(arr[i] - difference, 0) + 1);
			longest = Math.max(longest, dp.get(arr[i]));
		}
		return longest;
	}
	
	
	public static int longestSubsequence(int[] arr, int difference) {

		HashMap<Integer, Integer> map=new HashMap<Integer, Integer>();
		int count=0;
		for(int i:arr){
			
			map.put(i, map.getOrDefault(i-difference, 0)+1);
			count=Math.max(count, map.get(i));
			
		}
		return count;
	}

}
