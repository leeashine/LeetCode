package aleetcode.baidu;

public class StringTest {
    public int a=10;
    public static void main(String[] args) {
        String s1="HelloWorld";
        String s2=new String("HelloWorld");
        String s3="Hello"+"World";
        String s4="Hello";
        String s5="World";
        String s6=s4+s5;
        System.out.println(s1==s2);//false
        System.out.println(s1==s3);//true
        System.out.println(s1==s6);//false
        System.out.println(s2==s6);//false
        System.out.println(s2==s2.intern());//false
        System.out.println(s1.intern()==s2.intern());//true
        System.out.println(s1==s2.intern());//true
        System.out.println(s1==s6.intern());//true
        System.out.println(s3==s6.intern());//true
    }
}
