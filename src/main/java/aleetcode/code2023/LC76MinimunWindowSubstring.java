package aleetcode.code2023;

import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 给定两个字符串 S 和 T ，求 S 中包含 T 所有字符的最短连续子字符串的长度，同时要求时间 复杂度不得超过 O(m+n)。
 * s and t consist of uppercase and lowercase English letters.
 * 我们可以用滑动窗口的思想解决这个问题。在滑动窗口类型的问题中都会有两个指针，一个用于「延伸」现有窗口的
 * �
 * r 指针，和一个用于「收缩」窗口的
 * �
 * l 指针。在任意时刻，只有一个指针运动，而另一个保持静止。我们在
 * �
 * s 上滑动窗口，通过移动
 * �
 * r 指针不断扩张窗口。当窗口包含
 * �
 * t 全部所需的字符后，如果能收缩，我们就收缩窗口直到得到最小窗口。
 */
public class LC76MinimunWindowSubstring {

    public static void main(String[] args) {

        String s = "ADOBECODEBANC";
        String t = "ABC";

        System.out.println(minWindow(s, t));
        System.out.println((int)('A'-'a'));
        System.out.println((int)('a'-'a'));
    }

    static Map<Character, Integer> ori = new HashMap<Character, Integer>();
    static Map<Character, Integer> cnt = new HashMap<Character, Integer>();

    public static String minWindow(String s, String t) {
        int tLen = t.length();
        for (int i = 0; i < tLen; i++) {
            char c = t.charAt(i);
            ori.put(c, ori.getOrDefault(c, 0) + 1);
        }
        int l = 0, r = -1;
        int len = Integer.MAX_VALUE, ansL = -1, ansR = -1;
        int sLen = s.length();
        while (r < sLen) {
            ++r;
            if (r < sLen && ori.containsKey(s.charAt(r))) {
                cnt.put(s.charAt(r), cnt.getOrDefault(s.charAt(r), 0) + 1);
            }
            while (check() && l <= r) {
                if (r - l + 1 < len) {
                    len = r - l + 1;
                    ansL = l;
                    ansR = l + len;
                }
                if (ori.containsKey(s.charAt(l))) {
                    cnt.put(s.charAt(l), cnt.getOrDefault(s.charAt(l), 0) - 1);
                }
                ++l;
            }
        }
        return ansL == -1 ? "" : s.substring(ansL, ansR);
    }

    /**
     * TODO 每次都检查元素出现的次数满不满足多余 可以优化
     * 我们可以维护一个额外的变量needCnt来记录所需元素的总数量，当我们碰到一个所需元素c，不仅need[c]的数量减少1，同时needCnt也要减少1，这样我们通过needCnt就可以知道是否满足条件，而无需遍历字典了。
     * @return
     */
    public static boolean check() {
        Iterator iter = ori.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Character key = (Character) entry.getKey();
            Integer val = (Integer) entry.getValue();
            if (cnt.getOrDefault(key, 0) < val) {
                return false;
            }
        }
        return true;
    }



}
