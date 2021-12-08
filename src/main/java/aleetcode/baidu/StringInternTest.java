package aleetcode.baidu;

/**
 * string intern方法可以让new string()里的值放到字符串常量池里，减少空间占用
 */
public class StringInternTest {
    public static void main(String[] args) {
        String s1 = "Programming";
        String s2 = new String("Programming");
        String s3 = "Program";
        String s4 = "ming";
        String s5 = "Program" + "ming";
        String s6 = s3 + s4;
        System.out.println(s1 == s2); // false
        System.out.println(s1 == s5); //true
        System.out.println(s1 == s6); //flase
        System.out.println(s1 == s6.intern()); //true
        System.out.println(s2 == s2.intern()); //false

    }
}
