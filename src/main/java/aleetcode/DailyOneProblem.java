package aleetcode;

import java.util.*;

public class DailyOneProblem {

    public static void main(String[] args) {

//        int [] coins={2};
//        int amount=3;
//        int i = coinChange(coins, amount);
//        System.out.println(i);

//        int gcd = gcd(10, 8);
////        System.out.println(gcd);
//
//        int i = lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18});
//        System.out.println(i);

    }

    //    最长上升子序列的长度。
    public static int lengthOfLIS(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int maxans = 1;
        for (int i = 1; i < dp.length; i++) {
            int maxval = 0;
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    maxval = Math.max(maxval, dp[j]);
                }
            }
            dp[i] = maxval + 1;
            maxans = Math.max(maxans, dp[i]);
        }
        return maxans;
    }

    //多数元素
    public int majorityElement(int[] nums) {

        Map<Integer, Integer> map = new HashMap();
        for (int num : nums) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() > nums.length / 2) {
                return entry.getKey();
            }
        }
        return 0;

    }

    //字符串的最大公因子
//    输入：str1 = "ABCABC", str2 = "ABC"
//    输出："ABC"
    public String gcdOfStrings(String str1, String str2) {
        // 假设str1是N个x，str2是M个x，那么str1+str2肯定是等于str2+str1的。
        if (!(str1 + str2).equals(str2 + str1)) {
            return "";
        }
        // 辗转相除法求gcd。
        return str1.substring(0, gcd(str1.length(), str2.length()));
    }

    //求最大公共除数
    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }


    //以数数组分成和相等的三份（连续）
    public static boolean canThreePartsEqualSum(int[] A) {
        int[] prefixSum = new int[A.length];
        int sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i];
            prefixSum[i] = sum;
        }
        if (sum % 3 == 0) {
            int pre = sum / 3;
            int count = 0;
            for (int i = 0; i < prefixSum.length && count < 3; i++) {
                if (pre == prefixSum[i]) {
                    count++;
                    pre += sum / 3;
                }

            }
            return count == 3 ? true : false;

        } else {
            return false;
        }
    }

    //求二叉树的最大直径 两个结点之间最大距离
//    时间复杂度：O(N)O(N)，其中 NN 为二叉树的节点数，即遍历一棵二叉树的时间复杂度，每个结点只被访问一次。
//    空间复杂度：O(Height)O(Height)，其中 HeightHeight 为二叉树的高度。由于递归函数在递归过程中需要为每一层递归函数分配栈空间，所以这里需要额外的空间且该空间取决于递归的深度，而递归的深度显然为二叉树的高度，并且每次递归调用的函数里又只用了常数个变量，所以所需空间复杂度为 O(Height)O(Height) 。
    int res = 1;

    public int diameterOfBinaryTree(TreeNode root) {

        dfs(root);
        return res - 1;

    }

    public int dfs(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = 0;
        int right = 0;
        if (root.left != null) {
            left = dfs(root.left);// 左儿子为根的子树的深度
        }
        if (root.right != null) {
            right = dfs(root.right);// 右儿子为根的子树的深度
        }
        res = Math.max(left + right + 1, res);
        return Math.max(left, right) + 1;// 返回该节点为根的子树的深度
    }

    //买卖股票的最佳利润
    //求当前位置左边最小和最大值的差
    public int maxProfit(int[] prices) {
        int max = 0;
        int min = Integer.MAX_VALUE;
        int price;
        for (int i = 0; i < prices.length; i++) {
            price = prices[i];//2
            min = Math.min(min, price);
            if (price - min > max) {
                max = price - min;
            }
        }
        return max;
    }

    //找零钱问题 求凑成总金额所需的最少的硬币个数
    public static int coinChange(int[] coins, int amount) {
        int max = amount + 1;
        int[] dp = new int[amount + 1];// dp[j] 表示 j元钱的零钱的组合方式
        Arrays.fill(dp, max);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (coins[j] <= i) {
                    // 面值i的零钱 有2种选择  这个面值选或者不选 选 为什么不使用第n种货币呢，因为钱超了。
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    public int coinChange2(int[] coins, int amount) {
        if (amount < 1) return 0;
        return coinChange(coins, amount, new int[amount]);
    }

    private int coinChange(int[] coins, int rem, int[] count) {
        if (rem < 0) return -1;
        if (rem == 0) return 0;
        if (count[rem - 1] != 0) return count[rem - 1];
        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            int res = coinChange(coins, rem - coin, count);
            if (res >= 0 && res < min)
                min = 1 + res;
        }
        count[rem - 1] = (min == Integer.MAX_VALUE) ? -1 : min;
        return count[rem - 1];
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }


}
