package java8.lambda;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LIZIXUAN560 on 2020/11/19.
 *
 * @author LIZIXUAN560
 */
public class AnyMatchTest {
    public static void main(String[] args) {

        List<String>list= Arrays.asList("164722510506","164724430001","164725070600","164727040006","164728100709");
        String s = "164722510506,164724430001,164725070600,164728100709";
        List<String>list22= Arrays.asList(s.split(","));
        List<String>list2=new ArrayList<>();
        System.out.println(isSendMsg(list,"111 "));
        System.out.println(isSendMsg(list,"164725070600"));
        System.out.println(isSendMsg(list2,"164725070600"));
        list22.forEach(System.out::println);

        System.out.println("************************");
        Lists.newArrayList(s.split(",")).forEach(System.out::println);
    }
    public static boolean isSendMsg(List<String> list,String subBizType){
        return list.stream().anyMatch((sub)->sub.equals(subBizType));
    }
}
