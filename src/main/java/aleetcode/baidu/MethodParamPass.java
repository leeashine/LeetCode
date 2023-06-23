package aleetcode.baidu;

public class MethodParamPass {

    public static void swap(int a, int b) {
        int temp = a;
        a = b;
        b = temp;
    }

    public static void main(String[] args) {
        int a = 1;
        int b = 2;
        System.out.format("Before a:%s,b:%s\n", a, b);
        swap(a, b);
        System.out.format("After a:%s,b:%s", a, b);
    }

}
