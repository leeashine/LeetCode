package aleetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class L100Problem {

    public static void main(String[] args) {
        System.out.println('t'+'i');
        System.out.println('h'+'u');

        System.out.println((char)(1+'a'));

        String[] strs = new String[]{"ddddddddddg","dgggggggggg"};
        L100Problem s = new L100Problem();
//        List<List<String>> lists = s.groupAnagrams(strs);
        List<List<String>> lists2 = s.groupAnagrams2(strs);
//        System.out.println(lists);
        System.out.println(lists2);
    }

    /**
     * 49. 字母异位词分组
     * 中等
     * 给你一个字符串数组，请你将 字母异位词 组合在一起。可以按任意顺序返回结果列表。
     *
     * 字母异位词 是由重新排列源单词的所有字母得到的一个新单词。
     *
     *
     *
     * 示例 1:
     *
     * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
     * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
     * 示例 2:
     *
     * 输入: strs = [""]
     * 输出: [[""]]
     * 示例 3:
     *
     * 输入: strs = ["a"]
     * 输出: [["a"]]
     *
     *
     * 提示：
     *
     * 1 <= strs.length <= 104
     * 0 <= strs[i].length <= 100
     * strs[i] 仅包含小写字母
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams(String[] strs) {

        Map<String, List<String>> map = new HashMap<>();
        // 1.标记字母是否一样(排序 重复) 一样的就分到一组 不一样的分到新的一组
        for (String str : strs) {
            int[] arr = new int[26];
            // 排序 重复字母累加
            for (char c : str.toCharArray()) {
                arr[c - 'a'] += 1;
            }
            StringBuilder key = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
                while (arr[i] > 0) {
                    key.append((char) (i + 'a'));
                    arr[i]--;
                }
            }
            List<String> stringList = map.getOrDefault(key.toString(), new ArrayList<>());
            stringList.add(str);
            map.put(key.toString(), stringList);
        }
        return new ArrayList<List<String>>(map.values());

    }

    public List<List<String>> groupAnagrams2(String[] strs) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (String str : strs) {
            int[] counts = new int[26];
            int length = str.length();
            for (int i = 0; i < length; i++) {
                counts[str.charAt(i) - 'a']++;
            }
            // 将每个出现次数大于 0 的字母和出现次数按顺序拼接成字符串，作为哈希表的键
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 26; i++) {
                if (counts[i] != 0) {
                    sb.append((char) ('a' + i));
                    sb.append(counts[i]);
                }
            }
            String key = sb.toString();
            List<String> list = map.getOrDefault(key, new ArrayList<String>());
            list.add(str);
            map.put(key, list);
        }
        return new ArrayList<List<String>>(map.values());
    }

}
