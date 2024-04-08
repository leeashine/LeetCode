package aleetcode;

import java.util.*;

public class L100Problem {

    public static void main(String[] args) {

        L100Problem problem = new L100Problem();

//        String[] strs = new String[]{"ddddddddddg","dgggggggggg"};
////        List<List<String>> lists = s.groupAnagrams(strs);
//        List<List<String>> lists2 = s.groupAnagrams2(strs);
//        System.out.println(lists2);

//        int[] nums = new int[]{3,3};
//        int[] ints = problem.twoSum2(nums, 6);
//        System.out.println(Arrays.toString(ints));


//        int[] nums = new int[]{0, 1, 0, 3, 12};
//        problem.moveZeroes2(nums);
//        System.out.println(Arrays.toString(nums));

        int[] height = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7};
        int i = problem.maxArea(height);
        System.out.println(i);
    }


    /**
     * 双指针2
     * 11. 盛最多水的容器
     * 给定一个长度为 n 的整数数组 height 。有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i]) 。
     * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
     * 返回容器可以储存的最大水量。
     * 说明：你不能倾斜容器。
     * 输入：[1,8,6,2,5,4,8,3,7]
     * 输出：49
     * 解释：图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
     * 示例 2：
     * 输入：height = [1,1]
     * 输出：1
     *
     * 思路：
     * 使用两个指针 left 和 right，分别指向数组的起始位置和结束位置。
     * 初始时，容器的宽度为 n-1，即两个指针之间的距离。
     * 计算当前容器能容纳的水的容量，取决于两个指针指向的高度较小的那个，以及它们之间的距离。
     * 移动指针的规则是，每次移动高度较小的指针，因为移动较小的指针可能会有机会找到更高的柱子，从而使容器的容量增大。
     * 继续迭代，直到两个指针相遇为止。在迭代过程中，不断更新最大的容量值。
     * 最终返回最大的容量值作为结果。
     */
    public int maxArea(int[] height) {

        int left = 0, right = height.length - 1;
        int n = height.length;
        int max = 0;
        while (left < right) {

            int a = height[left];
            int b = height[right];
            max = Math.max(max, (right - left) * Math.min(a, b));
            if (a <= b) {
                left++;
            } else {
                right--;
            }
        }

        return max;

    }

    /**
     * 双指针1
     * 283. 移动零
     * 简单
     * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     * <p>
     * 示例 1:
     * 输入: nums = [0,1,0,3,12]
     * 输出: [1,3,12,0,0]
     * 示例 2:
     * 输入: nums = [0]
     * 输出: [0]
     * 请注意 ，必须在不复制数组的情况下原地对数组进行操作。
     * 进阶：你能尽量减少完成的操作次数吗？
     */
    public void moveZeroes(int[] nums) {

        for (int i = 0; i < nums.length - 1; i++) {
            int n = nums[i];
            int j = i + 1;
            while (j < nums.length) {
                // 当遇到第一个不等于0的数，交换位置
                if (nums[j] != 0 && n == 0) {
                    nums[i] = nums[j];
                    nums[j] = n;
                    break;
                }
                j++;
            }
        }


    }


    /**
     * 使用双指针，左指针指向当前已经处理好的序列的尾部，右指针指向待处理序列的头部。
     * <p>
     * 右指针不断向右移动，每次右指针指向非零数，则将左右指针对应的数交换，同时左指针右移。
     * <p>
     * 注意到以下性质：
     * <p>
     * 左指针左边均为非零数；
     * <p>
     * 右指针左边直到左指针处均为零。
     * <p>
     * 因此每次交换，都是将左指针的零与右指针的非零数交换，且非零数的相对顺序并未改变。
     * <p>
     * 作者：力扣官方题解
     * 链接：https://leetcode.cn/problems/move-zeroes/solutions/489622/yi-dong-ling-by-leetcode-solution/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param nums
     */
    public void moveZeroes2(int[] nums) {
        int left = 0, right = 0;
        int n = nums.length;
        while (right < n) {
            if (nums[right] != 0) {
                int t = nums[left];
                nums[left] = nums[right];
                nums[right] = t;
                ++left;
            }
            ++right;
        }
    }


    /**
     * 哈希1
     * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那两个整数，并返回它们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
     * 你可以按任意顺序返回答案。
     * <p>
     * 同一个位置不能重复
     * <p>
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
     * 哈希2
     * 128. 最长连续序列
     * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
     * <p>
     * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
     * <p>
     * 示例 1：
     * 输入：nums = [100,4,200,1,3,2]
     * 输出：4
     * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
     * 示例 2：
     * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
     * 输出：9
     *
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
     * 哈希3
     * 49. 字母异位词分组
     * 中等
     * 给你一个字符串数组，请你将 字母异位词 组合在一起。可以按任意顺序返回结果列表。
     * <p>
     * 字母异位词 是由重新排列源单词的所有字母得到的一个新单词。
     * <p>
     * <p>
     * <p>
     * 示例 1:
     * <p>
     * 输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
     * 输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
     * 示例 2:
     * <p>
     * 输入: strs = [""]
     * 输出: [[""]]
     * 示例 3:
     * <p>
     * 输入: strs = ["a"]
     * 输出: [["a"]]
     * <p>
     * <p>
     * 提示：
     * <p>
     * 1 <= strs.length <= 104
     * 0 <= strs[i].length <= 100
     * strs[i] 仅包含小写字母
     *
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
