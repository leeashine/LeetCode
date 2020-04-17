package aleetcode.greedyalgorithm;

import org.omg.CORBA.MARSHAL;

/**
 * 给定一个非负整数数组，你最初位于数组的第一个位置。
 *
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 *
 * 判断你是否能够到达最后一个位置。
 *
 * 使用贪心算法
 */
public class DkipGame {
    public static void main(String[] args) {
        int [] nums={2,3,1,1,4};
        canJum(nums);
    }
    public static boolean canJum(int[] nums) {
        int n = nums.length;
        int max=0;
        for(int i=0;i<n;i++){

            if(i<=max){
                //当前位置i肯定能达到
                max=Math.max(max,i+nums[i]);
                if(max>=n-1)
                    return true;

            }

        }
        return false;
    }


}
