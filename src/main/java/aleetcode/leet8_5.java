package aleetcode;

public class leet8_5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int [][] grid = {{1,1,1},{1,0,1},{1,1,1}};
		System.out.println(largest1BorderedSquare(grid));
		
	}

	public static  int largest1BorderedSquare(int[][] grid) {

		int n=grid.length;
		int m=grid[0].length;
		int [][]left=new int [n][m];
		int [][]up=new int[n][m];
		int res=0;
		for(int i=0;i<n;i++)
			for(int j=0;j<m;j++){
				if(i==0) up[i][j]=(grid[i][j]==1?1:0); 
				else up[i][j]=(grid[i][j]==1?up[i-1][j]+1:0);
				if(j==0) left[i][j]=(grid[i][j]==1?1:0);
				else left[i][j]=(grid[i][j]==1?left[i][j-1]+1:0);
				
				for(int k=1;k<=Math.min(i, j)+1;k++){
					
					if(up[i][j] <k) continue;
					if(up[i][j-k+1]<k) continue;
					if(left[i][j]<k) continue;
					if(left[i-k+1][j]<k) continue;
					res=Math.max(res, k*k);
				}
			}
		return res;
		
		
	}
}
