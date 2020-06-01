package aleetcode.Contest2020;

import java.util.*;

public class Contest189 {
    public static void main(String[] args) {

        int []startTime = {1,2,3};
        int []endTime = {3,2,7};
        int queryTime = 4;
//        new Contest189().busyStudent(startTime,endTime,queryTime);
        new Contest189().arrangeWords("Keep calm and code on");

    }
//    输入：text = "Keep calm and code on"
//    输出："On and keep calm code"
    public String arrangeWords(String text) {
        String res="";
        String[] words = text.split(" ");
        Map<Integer, List<String>> map=new HashMap();
        for(String word:words){
            int length=word.length();
            List list = map.getOrDefault(length, new ArrayList<>());
            list.add(word);
            map.put(length,list);
        }
        //map排序 先转换成List
        List<Map.Entry<Integer, List<String>>> teamRankList = new ArrayList<>(map.entrySet());
        Collections.sort(teamRankList, Comparator.comparingInt(Map.Entry::getKey));
        for(Map.Entry<Integer, List<String>> entry:teamRankList){
            List<String> entryValue = entry.getValue();
            for(String s:entryValue){
                res+=s+" ";
            }
        }
        res=res.toLowerCase();
        char c = res.charAt(0);
        char cc= (char) (c+('A'-'a'));
        res=cc+res.substring(1);
        return res.trim();
    }
//    输入：startTime = [1,2,3], endTime = [3,2,7], queryTime = 4
//    输出：1
    public int busyStudent(int[] startTime, int[] endTime, int queryTime) {

        int res=0;
        for(int i=0;i<startTime.length;i++){
            if(startTime[i]<=queryTime){
                if(endTime[i]>=queryTime)
                    res ++;
            }
        }
//        System.out.println(res);
        return res;

    }
}
