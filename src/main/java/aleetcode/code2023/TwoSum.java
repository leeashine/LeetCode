package aleetcode.code2023;

public class TwoSum {

    public static void main(String[] args) {
        System.out.println(0^0);
        System.out.println(-5&-3);
        System.out.println(-2&-3);
//        System.out.println(calculateSum(Integer.MIN_VALUE, Integer.MIN_VALUE));
        System.out.println(calculateSum2(Integer.MIN_VALUE, Integer.MIN_VALUE));
    }

    public static int calculateSum(int x, int y) {
        int r = x + y;
        // HD 2-12 Overflow iff both arguments have the opposite sign of the result
        if (((x ^ r) & (y ^ r)) < 0) {
            throw new ArithmeticException("integer overflow");
        }
        return r;
    }

    public static int calculateSum2(int a, int b) {
        if ((b > 0 && a > Integer.MAX_VALUE - b) || (b < 0 && a < Integer.MIN_VALUE - b)) {
            throw new ArithmeticException("integer overflow2");
        }
        return a + b;
    }

}
