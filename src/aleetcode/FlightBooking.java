package aleetcode;

import java.util.Arrays;
/*���⣺һ��ָ��ĺ������������ָ��ֵ���кܶ�ָ�����������ÿ��λ�õ�ֵ��

˼·����ԭʼ�ķ������߶������������

��������ⲻ���ڲ�ѯ��ֻ�������ս����
�ǾͿ���ʹ�ñ�ǵķ���������Ŀ�ʼλ�ü���ָ��ֵ������������¸�λ�ü�ȥָ��ֵ��
Ȼ��������ۼӼ��ɡ�*/
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
