package aleetcode.code2023;

import java.util.Arrays;
import java.util.Comparator;

public class LC435NonOverlappingIntervals {

    public static void main(String[] args) {
        int [][] intervals = {{1,2},{2,3},{3,4},{1,3}};
//        Arrays.sort(intervals, Comparator.comparingInt(v -> v[1]));
//        System.out.println(Arrays.toString(intervals));
        int i = eraseOverlapIntervals(intervals);
        System.out.println(i);

    }

    public static int eraseOverlapIntervals(int[][] intervals) {
        int result = 0;
        // 排序
        // [1,2] [2,3] [3,4] [2,4]
        // 当发现区间的值
        Arrays.sort(intervals, Comparator.comparingInt(v -> v[1]));
        int pre = intervals[0][1];
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < pre) {
                result++;
            } else {
                pre = intervals[i][1];
            }
        }
        return result;
    }

}
