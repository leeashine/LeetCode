package aleetcode;

import com.google.common.collect.Lists;

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

//        int[] height = new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7};
//        int i = problem.maxArea(height);
//        System.out.println(i);

//        int[] nums = new int[]{0, 0, 0, 0};
//        problem.threeSum(nums);

//        String str = "pwwkew";
//        int i = problem.lengthOfLongestSubstring(str);
//        System.out.println(i);

//        String s = "cbaebabacd";
//        String p = "abc";
//        List<Integer> anagrams = problem.findAnagrams(s, p);
//        System.out.println(anagrams);

//        int[] nums = {1, 1, 1};
//        int k = 2;
//        int i = problem.subarraySum(nums, k);
//        System.out.println(i);

//        int[] nums = {1,3,-1,-3,5,3,6,7};
//        int[] ints = problem.maxSlidingWindow2(nums, 3);
//        System.out.println(Arrays.toString(ints));

//        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
//        int[][] merge = problem.merge(intervals);
//        System.out.println(merge);

//        int[] nums = {1, 2, 3, 4, 5, 6, 7};
//        int k = 3;
//        problem.rotate(nums, k);
//        System.out.println(Arrays.toString(nums));

//        int [] nums = {5,4,-1,7,8};
//        problem.maxSubArray(nums);

        int [] nums = {1,2,3,4};
        problem.productExceptSelf(nums);
    }

    /**
     * 给你一个整数数组 nums，返回 数组 answer ，其中 answer[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积 。
     *
     * 题目数据 保证 数组 nums之中任意元素的全部前缀元素和后缀的乘积都在  32 位 整数范围内。
     *
     * 请 不要使用除法，且在 O(n) 时间复杂度内完成此题。
     * @param nums
     * @return
     */
    public int[] productExceptSelf(int[] nums) {

        int[] result = new int[nums.length];
//        int[] leftArray = new int[nums.length];
//        int[] rightArray = new int[nums.length];
        // 前缀乘积 * 后缀乘积 ， 有点类似前缀和
        // nums = [1,2,3,4]  result:[24,12,8,6]
        // left  1  1  2  6
        // right 24 12 4  1
//        int left = 1;
        result[0] = 1;
        for (int i = 1; i < nums.length; i++) {
//            left = left * nums[i - 1];
            result[i] = result[i - 1] * nums[i - 1];
        }
        int right = 1;
//        rightArray[nums.length - 1] = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            right = right * nums[i + 1];
//            rightArray[i] = right;
            result[i] = result[i] * right;
        }
        //leftArray[i] 其实可以直接赋值给result[i] 并且可以直接在right便利的时候就可以计算出不需要再遍历一遍
//        for (int i = 0; i < nums.length; i++) {
//            result[i] = result[i] * rightArray[i];
//        }


        return result;
    }

    /**
     * （动态规划）给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
     * 子数组是数组中的一个连续部分。
     * @param nums
     * @return
     */
    public int maxSubArray(int[] nums) {
//        if (nums.length == 1) {
//            return nums[0];
//        }
//        int max = 0;
//        int min = Integer.MAX_VALUE;
//        int[] sumPrefix = new int[nums.length + 1];
//        sumPrefix[0] = nums[0];
//        for (int i = 1; i < nums.length; i++) {
//            sumPrefix[i] = nums[i] + sumPrefix[i - 1];
//        }
//
//        // 5,4,-1,7,8
//        // 5 9 8 15 23
//        for (int prefix : sumPrefix) {
//            max = Math.max(max, prefix);
//            min = Math.min(min, prefix);
//        }
//        return max - min;
        // 动态规划 dp[i] = max(dp[i-1]+nums[i],nums[i])
        // 只和前一个有关，可以用一个变量，从而让空间复杂度降为O(1) 2.2次遍历，可以在第一次遍历时直接更新最大值。
//        int[] dp = new int[nums.length];
//        dp[0] = nums[0];
//        for (int i = 1; i < nums.length; i++) {
//            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
//        }
//        int max = Integer.MIN_VALUE;
//        for (int i = 0; i < nums.length; i++) {
//            max = Math.max(max, dp[i]);
//        }
//        return max;

        // pre 代表以当前元素结尾的最大子数组和
        int pre = nums[0];
        // max 代表全局的最大子数组和
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            // 当前元素结尾的最大子数组和 = max(当前+当前元素，当前元素)
            pre = Math.max(pre + nums[i], nums[i]);
            max = Math.max(max, pre);
        }
        return max;

    }

    /**
     * 给定一个整数数组 nums，将数组中的元素向右轮转 k 个位置，其中 k 是非负数。
     * // 0+3=3 1+3=4 2+3=5 3+3=6 (4+3)%7=0 (5+3)%7=1   (7+3)%7=2
     * @param nums
     * @param k
     */
    public void rotate(int[] nums, int k) {
        int length = nums.length;
        int[] oldNums = Arrays.copyOf(nums, length);
        for (int i = 0; i < length; i++) {
            int newIndex = (i + k) % length;
            nums[newIndex] = oldNums[i];
        }
    }

    /**
     * 合并区间
     * 以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [starti, endi] 。请你合并所有重叠的区间，并返回 一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间 。
     * 示例 1：
     *
     * 输入：intervals = [[1,3],[2,6],[8,10],[15,18]]
     * 输出：[[1,6],[8,10],[15,18]]
     * 解释：区间 [1,3] 和 [2,6] 重叠, 将它们合并为 [1,6].
     * 示例 2：
     *
     * 输入：intervals = [[1,4],[4,5]]
     * 输出：[[1,5]]
     * 解释：区间 [1,4] 和 [4,5] 可被视为重叠区间。
     * @param intervals
     * @return
     */
    public static int[][] merge(int[][] intervals) {

        // 1.排序 按第一个数字从小到大排序
        Arrays.sort(intervals, Comparator.comparingInt(o -> o[0]));
        List<int[]> merged = new ArrayList<>();
        for (int i = 0; i < intervals.length; i++) {
            // 如果为空 添加
            if (merged.isEmpty()) {
                merged.add(intervals[i]);
            }
//            检查当前区间是否与上一个区间有重叠。只要比较当前区间和上一个区间
            // 上一个
            int[] arr = merged.get(merged.size() - 1);
            // 当前
            if (intervals[i][0] <= arr[1]) {
                arr[1] = Math.max(arr[1], intervals[i][1]);
            } else {
                // 没有重叠 直接添加
                merged.add(intervals[i]);
            }
        }
        return merged.toArray(new int[merged.size()][]);
    }

    /**
     * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
     * 返回 滑动窗口中的最大值 。
     * 分析：本题难点在如何动态维护滑动窗口的最大值
     * 堆 大根堆实时维护一系列元素的最大值
     * 时间复杂度 nlogk
     * @param nums
     * @param k
     * @return
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        // 最大堆：存储二元祖(num,index) 表示元素 num在数组中的下标为 index
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>(new Comparator<int[]>() {
            public int compare(int[] pair1, int[] pair2) {
                return pair1[0] != pair2[0] ? pair2[0] - pair1[0] : pair2[1] - pair1[1];
            }
        });
        // 插入时间复杂度logk 插入k个 就是klogk
        for (int i = 0; i < k; ++i) {
            pq.offer(new int[]{nums[i], i});
        }
        int[] ans = new int[n - k + 1];
        ans[0] = pq.peek()[0];
        for (int i = k; i < n; ++i) {
            pq.offer(new int[]{nums[i], i});
            // 不断地移除堆顶的元素，直到其确实出现在滑动窗口中
            while (pq.peek()[1] <= i - k) {
                pq.poll();
            }
            ans[i - k + 1] = pq.peek()[0];
        }
        return ans;
    }

    /**
     * 这种单调性的双端队列一般称作「单调队列」
     * @param nums
     * @param k
     * @return
     */
    public int[] maxSlidingWindow2(int[] nums, int k) {
        int n = nums.length;
        // 队列插入的时间复杂度是n
        Deque<Integer> deque = new LinkedList<Integer>();
        for (int i = 0; i < k; ++i) {
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            deque.offerLast(i);
        }

        int[] ans = new int[n - k + 1];
        ans[0] = nums[deque.peekFirst()];
        for (int i = k; i < n; ++i) {
            // 当当前元素>=队列末尾元素 弹出队列。因为要维护k里最大值
            while (!deque.isEmpty() && nums[i] >= nums[deque.peekLast()]) {
                deque.pollLast();
            }
            // 添加当前元素到队列中
            deque.offerLast(i);
            // 当最大元素不在此窗口里了，移除 保留窗口内元素
            while (deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            // 更新结果
            ans[i - k + 1] = nums[deque.peekFirst()];
        }
        return ans;
    }

    /**
     * 560. 和为 K 的子数组
     * 给你一个整数数组 nums 和一个整数 k ，请你统计并返回 该数组中和为 k 的子数组的个数 。
     * 子数组是数组中元素的连续非空序列。
     * 示例 1：
     *
     * 输入：nums = [1,1,1], k = 2
     * 输出：2
     * 示例 2：
     *
     * 输入：nums = [1,2,3], k = 3
     * 输出：2
     *
     * 方法一的瓶颈在于对每个 i，我们需要枚举所有的 j 来判断是否符合条件，这一步是否可以优化呢？答案是可以的。
     * 由条件得 pre[i]-pre[j-1]=k 移向得pre[j-1]=pre[i]-k(pre代表前缀和数组)
     * @return
     */
    public int subarraySum(int[] nums, int k) {

        int result = 0;
        // 子数组+和  =》 前缀和
        int sum = 0;
//        int[] prefixSum = new int[nums.length];
        Map<Integer, Integer> prefixSumCount = new HashMap<>();
        // 初始化 前缀和为0的 出现1次
        prefixSumCount.put(0, 1);
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            // 1 2 3
//            prefixSum[i] = sum;
            if (prefixSumCount.containsKey(sum - k)) {
                result += prefixSumCount.get(sum - k);
            }

            // 出现次数更新到Map中
            prefixSumCount.put(sum, prefixSumCount.getOrDefault(sum, 0) + 1);
        }


        return result;
    }

    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        if (s == null || p == null || s.length() < p.length()) {
            return result;
        }

        // 创建字符计数数组
        int[] pCount = new int[26];
        int[] sCount = new int[26];

        // 初始化 p 的字符计数
        for (char c : p.toCharArray()) {
            pCount[c - 'a']++;
        }

        // p是固定窗口 所以只要窗口一直在移动对比即可
        int windowSize = p.length();
        int sLength = s.length();

        // 初始化窗口的字符计数
        for (int i = 0; i < windowSize; i++) {
            sCount[s.charAt(i) - 'a']++;
        }

        // 比较初始窗口 第一个窗口直接比
        if (matches(pCount, sCount)) {
            result.add(0);
        }

        // 滑动窗口
        for (int i = windowSize; i < sLength; i++) {
            // 增加新字符到窗口
            sCount[s.charAt(i) - 'a']++;
            // 移除窗口中最左边的字符
            sCount[s.charAt(i - windowSize) - 'a']--;
            // 比较当前窗口
            if (matches(pCount, sCount)) {
                result.add(i - windowSize + 1);
            }
        }

        return result;
    }

    // 比较两个字符计数数组是否相同
    private boolean matches(int[] pCount, int[] sCount) {
        for (int i = 0; i < 26; i++) {
            if (pCount[i] != sCount[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。
     * 异位词 指由相同字母重排列形成的字符串（包括相同的字符串）。
     * 示例 1:
     * 输入: s = "cbaebabacd", p = "abc"
     * 输出: [0,6]
     * 解释:
     * 起始索引等于 0 的子串是 "cba", 它是 "abc" 的异位词。
     * 起始索引等于 6 的子串是 "bac", 它是 "abc" 的异位词。
     *  示例 2:
     * 输入: s = "abab", p = "ab"
     * 输出: [0,1,2]
     * 解释:
     * 起始索引等于 0 的子串是 "ab", 它是 "ab" 的异位词。
     * 起始索引等于 1 的子串是 "ba", 它是 "ab" 的异位词。
     * 起始索引等于 2 的子串是 "ab", 它是 "ab" 的异位词。
     * 提示:
     * 1 <= s.length, p.length <= 3 * 104
     * s 和 p 仅包含小写字母
     * @param s
     * @param p
     * @return
     */
    public List<Integer> findAnagrams2(String s, String p) {

        Map<Character, Integer> set = new HashMap<>();
        for (char c : p.toCharArray()) {
            set.put(c, set.getOrDefault(c, 0) + 1);
        }
        List<Integer> result = new ArrayList<>();
        char[] charArray = s.toCharArray();
        // 动态窗口 一定点 一动点 i是定点 j是动点
        for (int i = 0; i < charArray.length; i++) {
            int j = i;
            // 如果包含 说明动点可以动 更新值
            while (j < charArray.length && set.getOrDefault(charArray[j], 0) > 0) {
                set.put(charArray[j], set.get(charArray[j]) - 1);
                j++;
            }

            // 遍历set看有没有都用完
            boolean flag = false;
            for (Map.Entry<Character, Integer> entry : set.entrySet()) {
                Integer value = entry.getValue();
                if (value != 0) {
                    flag = true;
                    break;
                }
            }

            // 如果都遍历完 发现都有，则这个动点就是所要的起始位置
            if (!flag) {
                result.add(i);
            }
            // 进入下一次寻找。同时初始化
            set.clear();
            for (char c : p.toCharArray()) {
                set.put(c, set.getOrDefault(c, 0) + 1);
            }


        }


        return result;
    }

    /**
     * 无重复字符的最长字串
     * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长字串的长度
     * 滑动窗口
     * 先定义一个定点，然后动点一直在扩大直到遇到重复为止。当前就代表这个定点开始最长的无重复子串
     * 然后这个定点再移动，以此类推最后就能找到最大值
     * 复杂度分析
     * 时间复杂度：O(n)，其中 n 是字符串的长度。每个字符最多被访问两次（一次被添加到哈希集，一次被移除）。
     * 空间复杂度：O(min(m, n))，其中 m 是字符集的大小，n 是字符串的长度。哈希集最多包含 m 或 n 个字符。
     * @param s
     * @return
     */
    public int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        int maxLength = 0;
        int start = 0, end = 0;
        while (end < s.length()) {
            // 右边没访问过，继续扩大，找最大值
            if (!set.contains(s.charAt(end))) {
                set.add(s.charAt(end));
                end++;
                maxLength = Math.max(maxLength, end - start);
            } else {
                // 右边访问过，代表这次结束了，动点该移动进入下次计算了。
                set.remove(s.charAt(start));
                start++;
            }
        }

        return maxLength;
    }

    /**
     * 15. 三数之和
     * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。请
     * 你返回所有和为 0 且不重复的三元组。
     * 注意：答案中不可以包含重复的三元组。
     * <p>
     * 示例 1：
     * 输入：nums = [-1,0,1,2,-1,-4]
     * 输出：[[-1,-1,2],[-1,0,1]]
     * 解释：
     * nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0 。
     * nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0 。
     * nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0 。
     * 不同的三元组是 [-1,0,1] 和 [-1,-1,2] 。
     * 注意，输出的顺序和三元组的顺序并不重要。
     * 示例 2：
     * 输入：nums = [0,1,1]
     * 输出：[]
     * 解释：唯一可能的三元组和不为 0 。
     * 示例 3：
     * 输入：nums = [0,0,0]
     * 输出：[[0,0,0]]
     * 解释：唯一可能的三元组和为 0 。
     * <p>
     * <p>
     * 提示：
     * 3 <= nums.length <= 3000
     * -105 <= nums[i] <= 105
     */
    public List<List<Integer>> threeSum(int[] nums) {
        // 1.排序，处理重复数据并且使得后续查找更加高效！！！
//        2.使用三个指针：外层指针 i 用于遍历数组，内层的双指针 left 和 right 用于在剩余数组中查找满足条件的两个元素。
//        3.在遍历过程中，对于每个元素 nums[i]，使用双指针 left 和 right 在 i+1 到数组末尾之间寻找两个元素，使得它们的和等于 -nums[i]。
//        4.如果找到了满足条件的两个元素，将它们和 nums[i] 组成一个满足条件的三元组，并将其加入结果集中。
//        5.为了避免重复，需要跳过相邻的相同元素 ？？？？？？？
//        6.最终返回结果集中的所有不重复的三元组。
        List<List<Integer>> result = new ArrayList();
        // 排序，为了后面处理重复数据和后续查找更加高效！
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            // 优化1:如果当前元素大于0，后续元素之和必然大于0，直接结束循环
            if (nums[i] > 0) {
                break;
            }
            // 跳过相邻的相同元素！！！
            if (i > 0 && nums[i] == nums[i - 1])
                continue;
            int target = -nums[i];
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                int sum = nums[left] + nums[right];
                if (sum == target) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    //移动指针的规则：1.每次移动？如果相等 继续便利 如果<target  2.如果相同元素移动(跳过)
                    // 跳过相邻的相同元素！！！
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    left++;
                    right--;
                } else if (sum < target) {
                    //如果sum<target 那么为了使得和更接近目标值，我们可以尝试将 left 指针向右移动，因为右边的元素更大，可能可以使得和变大。
                    left++;
                } else {
                    //同理
                    right--;
                }
            }
        }
        return result;
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
     * <p>
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
