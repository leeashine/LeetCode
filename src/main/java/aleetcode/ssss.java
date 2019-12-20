package aleetcode;

import java.math.BigDecimal;
import java.util.Scanner;

public class ssss {

	public static void main(String[] args) {
		

//		int [] A={1};
//		System.out.println(smallestRangeI(A, 0));
		
		String ss="1.0E+7";
		String news=new BigDecimal(ss).setScale(2).toString();
		System.out.println(news);
		
	}
	
	public static int  smallestRangeI(int[] A, int K) {
        int min = A[0];
        int max = A[0];
        for (int x : A) {
            min = x < min ? x : min;
            max = x > max ? x : max;
        }
        return max - min <= 2 * K ? 0 : max - min - 2 * K;
    }


}
