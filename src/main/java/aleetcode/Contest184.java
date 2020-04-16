package aleetcode;

import java.util.ArrayList;
import java.util.List;

public class Contest184 {
    public static void main(String[] args) {

//        String x=  "&amp; is an HTML entity but &ambassador; is not.&quot;".replaceAll("&quot;","\\\"");
//        System.out.println(x);

        String [] words={"leetcoder","leetcode","od","hamlet","am"};
        stringMatching(words);

    }
    //数组中的字符串匹配 双指针遍历 注意去重
    public static List<String> stringMatching(String[] words) {

        List list=new ArrayList();
        for(int i=0;i<words.length-1;i++)
            for(int j=i+1;j<words.length;j++){
                    if(words[i].contains(words[j])||words[j].contains(words[i])){
                        String sb=words[i].length()<words[j].length()?words[i]:words[j];
                        //注意去重
                        if(!list.contains(sb))
                         list.add(sb);
                    }

            }
        return list;

    }

    public String entityParser(String text) {

        return  text.replaceAll("&quot;","\\\"").replaceAll("&apos;","'").replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("&frasl;","/").replaceAll("&amp;","&");

    }
}
