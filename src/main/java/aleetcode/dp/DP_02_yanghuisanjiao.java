package aleetcode.dp;

import java.util.ArrayList;
import java.util.List;

/**
 * 杨辉三角
 * 给定一个非负整数 numRows，生成「杨辉三角」的前 numRows 行。
 * <p>
 * 在「杨辉三角」中，每个数是它左上方和右上方的数的和。
 *
 * @author: Lee
 * @create: 2025/02/28 15:49
 **/
public class DP_02_yanghuisanjiao {

    public static void main(String[] args) {
        DP_02_yanghuisanjiao dp = new DP_02_yanghuisanjiao();
        List<List<Integer>> generate = dp.generate(3);
        System.out.println(generate);
    }

    /**
     * 为什么想到用dp？看着像是重叠子问题
     *          1
     *         1 1
     *        1 2 1
     *       1 3 3 1
     * @param numRows
     * @return
     */
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();
//        int[][] arr = new int[numRows][numRows];
//        arr[0][0]=1;
//        if (numRows == 1) {
//            result.add(new ArrayList<>(Arrays.asList(1)));
//            return result;
//        }
//        arr[1][0] = 1;
//        arr[1][1] = 1;
//        result.add(Arrays.asList(1));
//        result.add(Arrays.asList(1, 1));

        for (int i = 0; i < numRows; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                //a[3][1] = a[2][0] + a[2][1]
                //a[3][2] = a[2][1] + a[2][2]
                //a[3][3] = a[2][2] + a[2][3]
                if (j == 0) {
                    list.add(1);
                } else if (j == i) {
                    list.add(1);
                } else {
                    list.add(result.get(i - 1).get(j - 1) + result.get(i - 1).get(j));
                }
            }
            result.add(list);
        }

        return result;
    }


}
