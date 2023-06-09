package aleetcode.code2023;

import java.util.Arrays;

/**
 * 在一个增序的整数数组里找到两个数，使它们的和为给定值。已知有且只有一对解。
 * 2 <= numbers.length <= 3 * 104
 *
 * @author lizixuan
 * @link https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/
 */
public class LC167TwoSumIIInputArrayIsSorted {

    public static void main(String[] args) {
        int[] numbers = {2, 7, 11, 15};
        int[] ints = twoSum(numbers, 9);
        System.out.println(Arrays.toString(ints));

//        int[] numbers = {2, 3, 4, 6};
//        int[] ints = twoSum(numbers, 6);
//        System.out.println(Arrays.toString(ints));

//        int[] numbers = {-1, 0};
//        int[] ints = twoSum(numbers, -1);
//        System.out.println(Arrays.toString(ints));
    }

    public static int[] twoSum(int[] numbers, int target) {
        // 双指针 当和<target继续右遍历。=target停止遍历 >target结束当前轮次进入下一轮次遍历
        int i = 0;
        int j = numbers.length - 1;
        while (i < j) {
            int sum = numbers[i] + numbers[j];
            if (sum == target) {
                break;
            }
            if (sum < target) {
                i++;
            }
            if (sum > target) {
                j--;
            }
        }
        return new int[]{i+1,j+1};
    }

    /**
     * 这其实没有真正用到双指针的精髓，因为它还是会有重复的路要走
     * @param numbers
     * @param target
     * @return
     */
    public static int[] twoSum2(int[] numbers, int target) {
        // 双指针 当和<target继续右遍历。=target停止遍历 >target结束当前轮次进入下一轮次遍历
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == target) {
                    return new int[]{i + 1, j + 1};
                }
                if (numbers[i] + numbers[j] > target) {
                    break;
                }
            }
        }
        return new int[2];
    }
}
