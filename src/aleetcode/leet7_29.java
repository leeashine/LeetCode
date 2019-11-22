package aleetcode;

public class leet7_29 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		tribonacci(2);
		
	}

	public static int tribonacci(int n) {
        if (n < 2) return n;
        int a = 0, b = 1, c = 1, d;
        while (n-- > 2) {
            d = a + b + c;
            a = b;
            b = c;
            c = d;
        }
        return c;
    }
	
}
