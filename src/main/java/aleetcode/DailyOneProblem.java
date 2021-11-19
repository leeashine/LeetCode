package aleetcode;

import aleetcode.util.TreeNode;

import java.util.*;

public class DailyOneProblem {

    public static void main(String[] args) {

//        String[] words = {"abcw", "baz", "foo", "bar", "xtfn", "abcdef"};
//
//        int i = new DailyOneProblem().maxProduct(words);
//        System.out.println(i);


    }

    int sum = 0;

    public int findTilt(TreeNode root) {
        dfs(root);
        return sum;
    }

    private int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int sumleft = dfs(root.left);
        int sumright = dfs(root.right);
        //root节点时
        sum += Math.abs(sumleft - sumright);
        //子节点时
        return sumright + sumleft + root.val;
    }

    public int maxProduct(String[] words) {
        int max = 0;
        for (int i = 0; i < words.length - 1; i++) {
            for (int j = i + 1; j < words.length; j++) {
                if (!contain(words[i], words[j])) {
                    max = Math.max(max, words[i].length() * words[j].length());
                }
            }
        }
        return max;

    }

    //26[] 去重
    public boolean contain(String s1, String s2) {
        int[] arr1 = new int[27];
        int[] arr2 = new int[27];
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        for (char c1 : chars1) {
            if (arr1[c1 - 'a'] == 0) {
                arr1[c1 - 'a'] = 1;
            }
        }

        for (char c1 : chars2) {
            if (arr2[c1 - 'a'] == 0) {
                arr2[c1 - 'a'] = 1;
            }
        }

        for (int i = 0; i < 27; i++) {
            if (arr1[i] * arr2[i] > 0) {
                return true;
            }
        }
        return false;
    }


}
