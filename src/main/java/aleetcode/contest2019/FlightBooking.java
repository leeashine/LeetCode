package aleetcode.contest2019;

import java.util.Arrays;
/*题意：一条指令的含义是区间加上指定值，有很多指令，求最终区间每个位置的值。

思路：最原始的方法是线段树区间操作。

但是这道题不存在查询，只是求最终结果。
那就可以使用标记的方法，区间的开始位置加上指定值，区间结束的下个位置减去指定值。
然后从左到右累加即可。*/
public class FlightBooking {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int [][] bookings={{1,2,10},{2,3,20},{2,5,25}};
		int n=5;
		
		System.out.println(Arrays.toString(corpFlightBookings(bookings, n)));

	}
	
	public static int[] corpFlightBookings(int[][] bookings, int n) {
		int[] res = new int[n];
        for (int[] b : bookings) {
            res[b[0] - 1] += b[2];
            if (b[1] < n) res[b[1]] -= b[2];
        }
        for (int i = 1; i < n; ++i)
            res[i] += res[i - 1];
        return res;
    }
	
	public static int[] corpFlightBookings2(int[][] bookings, int n) {

		int [] sum=new int [n];
		
		for(int i=0;i<bookings.length;i++){
			
			int k=bookings[i][1]-bookings[i][0];
			
			sum[bookings[i][0]-1]+=bookings[i][2];
			if(k>=1)
			sum[bookings[i][1]-1]+=bookings[i][2];
			while(k>1){
				sum[bookings[i][0]-1+k-1]+=bookings[i][2];
				k--;
			}
				
			
		}
		
		return sum;
		
		
	}

}
