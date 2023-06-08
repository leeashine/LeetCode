package aleetcode.code2023;

import java.util.Arrays;

public class LC135Candy {


    public static void main(String[] args) {

        // 111
        // 212
        int[] ratings = {1, 0, 2};
        int candy = candy(ratings);
        System.out.println(candy);

        ratings = new int[]{1, 2, 2};
        candy = candy(ratings);
        System.out.println(candy);

        ratings = new int[]{1, 3, 2, 2, 1};
        // 1 1 1 1 1
        // 1 2 1 1 1
        // 1 2 1 2 1
        candy = candy(ratings);
        System.out.println(candy);

    }

    /**
     * 一群孩子站成一排，每一个孩子有自己的评分。现在需要给这些孩子发糖果，规则是如果一 个孩子的评分比自己身旁的一个孩子要高，那么这个孩子就必须得到比身旁孩子更多的糖果;所 有孩子至少要有一个糖果。求解最少需要多少个糖果。
     * 1 <= n <= 2 * 104
     * Input: [1,0,2]    1,2,3,4,5
     * Output: 5
     * 1.所有孩子的糖果初始化1
     * 2.从左往右遍历，如果当前孩子的评分比左边孩子的高。右边孩子的糖果数=左边孩子糖果+1
     * 3.从右往左遍历，如果当前孩子的评分比右边孩子的高。左边孩子的糖果数=右边孩子糖果+1
     *
     * @param ratings
     * @return
     */
    public static int candy(int[] ratings) {
        if (ratings.length == 1) {
            return 1;
        }
        int[] result = new int[ratings.length];
        Arrays.fill(result, 1);
        int sum = 0;
        for (int i = 1; i < ratings.length; i++) {
            // 从左往右遍历 当前>左边
            if (ratings[i] > ratings[i - 1]) {
                result[i] = result[i - 1] + 1;
            }
        }
        // 1 2 1 1 1
        // 1 2 1 2 1
        for (int j = ratings.length - 1; j > 0; j--) {

            // 看又往左遍历 当前<左边
            if (ratings[j] < ratings[j - 1]) {
                result[j - 1] = Math.max(result[j - 1], result[j] + 1);
            }

        }
        System.out.println(Arrays.toString(result));
        for (int i : result) {
            sum += i;
        }

        return sum;

    }

    public static int candy2(int[] ratings) {
        if (ratings.length == 1) {
            return 1;
        }
        int[] arr = new int[ratings.length];
        int sum = 0;
        Arrays.fill(arr, 1);

        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                arr[i] = arr[i - 1] + 1;
            }
        }

        // 1 0 2
        // 1 1 2
        for (int i = ratings.length - 1; i > 0; i--) {
            if (ratings[i] < ratings[i - 1]) {
                arr[i - 1] = Math.max(arr[i - 1], arr[i] + 1);
            }
        }

        return sum;
    }
}
