package aleetcode;

import java.util.Arrays;

public class demo1 {

	public static void main(String[] args) {
		
		

	}

	public static int method(int [][] books,int shelf_width){
		int n=books.length;
		int []dp=new int[n+1];
		Arrays.fill(dp, 0);
		
		for(int i=1;i<=n;i++){
			
			dp[i]=dp[i-1]+books[i-1][1];
			int max_height=books[i-1][1];
			int width=books[i-1][0];
			
			for(int j=i-1;j>0;j--){
				
				width+=books[j-1][0];
				if(width>shelf_width)
					break;//ÓÅ»¯
				if(books[j-1][1]>max_height)
					max_height=books[j-1][1];
				dp[i]=Math.min(dp[i], dp[j-1]+max_height);
			
				
			}
			
		}
		return dp[n];
	}
	
}
