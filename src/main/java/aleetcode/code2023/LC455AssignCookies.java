package aleetcode.code2023;

import java.util.Arrays;

public class LC455AssignCookies {

    public static void main(String[] args) {
        int[] g = {10, 9, 8, 7};
        int[] s = {5, 5, 6, 7, 8};
        int contentChildren = findContentChildren(g, s);
        System.out.println(contentChildren);
    }


    /**
     * Each child i has a greed factor g[i], which is the minimum size of a cookie that the child will be content with; and each cookie j has a size s[j].
     * If s[j] >= g[i], we can assign the cookie j to the child i, and the child i will be content. Your goal is to maximize the number of your content children and output the maximum number.
     *
     * @param g
     * @param s
     * @return
     */
    public static int findContentChildren(int[] g, int[] s) {

        // 先从小达到排序
        Arrays.sort(g);
        Arrays.sort(s);
        // 比较两个数组，如果s[cookies]>=g[child] cookies++
        int cookies = 0;
        int child = 0;
        while (child < g.length && cookies < s.length) {
            if (s[cookies] >= g[child]) {
                child++;
            }
            cookies++;
        }
        return child;

    }
}
