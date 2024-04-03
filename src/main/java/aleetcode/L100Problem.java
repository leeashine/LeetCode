package aleetcode;

import java.util.*;

public class L100Problem {

    public static void main(String[] args) {

        L100Problem problem = new L100Problem();

//        String[] strs = new String[]{"ddddddddddg","dgggggggggg"};
////        List<List<String>> lists = s.groupAnagrams(strs);
//        List<List<String>> lists2 = s.groupAnagrams2(strs);
//        System.out.println(lists2);

        int[] nums = new int[]{3,3};
        int[] ints = problem.twoSum2(nums, 6);
        System.out.println(Arrays.toString(ints));
    }


    /**
     * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那两个整数，并返回它们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
     * 你可以按任意顺序返回答案。
     *
     * 同一个位置不能重复
     *
     * 示例 1：
     * 输入：nums = [2,7,11,15], target = 9
     * 输出：[0,1]
     * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
     * 示例 2：
     * 输入：nums = [3,2,4], target = 6
     * 输出：[1,2]
     * 示例 3：
     * 输入：nums = [3,3], target = 6
     * 输出：[0,1]
     *
     */
    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            List<Integer> poList = map.getOrDefault(num, new ArrayList<>());
            poList.add(i);
            map.put(num, poList);
        }
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            List<Integer> left = map.get(target - num);
            if (left != null && !left.isEmpty()) {
                result[0] = i;
                for (Integer j : left) {
                    if (j != i) {
                        result[1] = j;
                        // 2层循环 需要跳过
                        return result;
                    }
                }
            }
        }

        return result;
    }

    public int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int rest = target - nums[i];
            if (map.containsKey(rest)) {
                return new int[]{map.get(rest), i};
            }
            map.put(nums[i], i);
        }
        return null;
    }

    /**
     * 128. 最长连续序列
     *     给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
     *
     *     请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
     *
     *     示例 1：
     *     输入：nums = [100,4,200,1,3,2]
     *     输出：4
     *     解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
     *     示例 2：
     *     输入：nums = [0,3,7,2,5,8,4,6,0,1]
     *     输出：9
     * @param nums
     * @return
     */
    public int longestConsecutive(int[] nums) {

        // 利用set去重!
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int result = 0;
        for (int num : set) {
            // 倒过来看 如果num-1不在集合中，说明他断掉了，他只最多可能是子序列的起点，这时候找即可
            if (!set.contains(num - 1)) {
                int curNum = num;
                int curLen = 1;
                // 向后寻找连续的数字
                while (set.contains(curNum + 1)) {
                    curLen++;
                    curNum++;
                }
                // 更新最长连续序列长度
                result = Math.max(result, curLen);
            }
        }

        return result;
    }

    public int longestConsecutive2(int[] nums) {
        Set<Integer> num_set = new HashSet<Integer>();
        for (int num : nums) {
            num_set.add(num);
        }

        int longestStreak = 0;
        for (int num : num_set) {
            if (!num_set.contains(num - 1)) {
                int curNum = num;
                int curStreak = 1;
                while (num_set.contains(curNum + 1)) {
                    curStreak++;
                    curNum++;
                }
                longestStreak = Math.max(longestStreak, curStreak);
            }
        }

        return longestStreak;
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
