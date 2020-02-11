package aleetcode;

import java.util.*;

public class Contest175 {
    public static void main(String[] args) {

        System.out.println(3/2);

    }
//    Input: arr = [10,2,5,3]
//    Output: true
//    Explanation: N = 10 is the double of M = 5,that is, 10 = 2 * 5.
//    考虑几个0的情况
    public boolean checkIfExist(int[] arr) {

        Set set=new HashSet();
        int cn=0;
        for (int i : arr) {
            if(i!=0)
                set.add(i);
            else
                cn++;
        }
        for (int i : arr) {
            if(cn>=2)
                return true;
            if(set.contains(i*2))
                return true;
        }

        return false;

    }
//    Input: s = "bab", t = "aba"
//    Output: 1
//    Explanation: Replace the first 'a' in t with b, t = "bba" which is anagram of s.
//    计算需要调换几个字母成为和他相同的异位数 用一个数组 1.8流式编程 把数组转化为流进行操作
    public int minSteps(String s, String t) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) // count the occurrences of chars in s.
            ++count[c - 'a'];
        for (char c : t.toCharArray()) // compute the difference between s and t.
            --count[c - 'a'];
        return Arrays.stream(count).map(Math::abs).sum() / 2; // sum the absolute of difference and divide it by 2
    }

}
