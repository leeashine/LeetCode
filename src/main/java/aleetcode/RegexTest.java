package aleetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {
        String s="{id:123,title:提交作业,courseID:huangt-test},{beginTime:1398873600000,endTime:111111}";
        String pattern="\\{(.*?)\\}";
        Pattern r=Pattern.compile(pattern);
        Matcher m=r.matcher(s);
        while (m.find( )) {
            System.out.println("Found value: " + m.group(1) );
        }

    }
}
