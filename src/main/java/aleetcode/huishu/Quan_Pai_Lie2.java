package aleetcode.huishu;

import java.util.*;

//给定一个可包含重复数字的序列，返回所有不重复的全排列。
//https://leetcode-cn.com/problems/permutations-ii/solution/quan-pai-lie-ii-by-leetcode-solution/
public class Quan_Pai_Lie2 {

    public static void main(String[] args) {
        int []nums={1,1,2};
        List<List<Integer>> lists = new Quan_Pai_Lie2().permuteUnique(nums);
        System.out.println(lists.toString());
    }

    boolean[] vis;

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        List<Integer> perm = new ArrayList<Integer>();
        vis = new boolean[nums.length];
        Arrays.sort(nums);
        backtrack(nums, ans, 0, perm);
        return ans;
    }

    public void backtrack(int[] nums, List<List<Integer>> ans, int idx, List<Integer> perm) {
        if (idx == nums.length) {
            ans.add(new ArrayList<Integer>(perm));
            return;
        }
        for (int i = 0; i < nums.length; ++i) {
//            要解决重复问题，我们只要设定一个规则，保证在填第 idx 个数的时候重复数字只会被填入一次即可。
//            而在本题解中，我们选择对原数组排序，保证相同的数字都相邻，然后每次填入的数一定是这个数所在
//            重复数集合中「从左往右第一个未被填过的数字」，即如下的判断条件：
            if (vis[i] || (i > 0 && nums[i] == nums[i - 1] && !vis[i - 1])) {
                continue;
            }
            perm.add(nums[i]);
            vis[i] = true;
            backtrack(nums, ans, idx + 1, perm);
            vis[i] = false;
            perm.remove(idx);
        }
    }

}
