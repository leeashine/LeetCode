package dp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;

//https://leetcode.com/contest/weekly-contest-171/problems/minimum-distance-to-type-a-word-using-two-fingers/
public class DP3 {

    public static void main(String[] args) {

        new DP3().minimumDistance("CAKE");
        JSONObject extInfo = new JSONObject();
        extInfo.put("creditAmt", "16000");
        System.out.println(extInfo.toJSONString());




    }
    //状态转移方程
//    dp[i][l][r] 表示在输入了字符串 word 的第 i 个字母后，左手的位置为 l，右手的位置为 r，达到该状态的最小移动距离。这里的位置为指向的字母编号
//    并且由于字母输入的顺序是固定的，每一个字母都可以看成一个阶段，字母不断输入的过程即是阶段的递增，例如第一个字母为第一个阶段，第二个字母为第二个阶段，后面以此类推。
//    状态如何进行转移。假设字符串为 CAKE，并且此时阶段为 1，即当前考虑字母是 A。
//    在这个阶段下，左右指会存在一种现象，要么左指为 A ，要么右指为 A，此时才能输入字母 A。
//    对于左指为 A，表示我们通过移动左指来到达这个阶段，而右指是没有移动的。
//    总结来说，这个阶段下，左指会变成 A，右指不变。因此，我们需要遍历上一个阶段左指和右指的所有情况，
//    并且转移到下一个阶段时，只移动左指（dp[1][A][R] = Math.min(dp[1][A][R], dp[0][L][R] + move(L, A))）。
//    注意观察，如果上一个阶段右指为 R，此时这个阶段右指也必须保持不变，同样为 R
//    移动右指同理
    public int minimumDistance(String word) {
        // 初始化
        int[][][] dp = new int[301][26][26];
        for (int i = 1; i <= 300; i++) {
            for (int j = 0; j < 26; j++) {
                Arrays.fill(dp[i][j], Integer.MAX_VALUE);
            }
        }
        int ans = Integer.MAX_VALUE;
        char[] ca = word.toCharArray();
        // 遍历每个字母
        for (int i = 1; i <= word.length(); i++) {
            int v = ca[i - 1] - 'A';
            // 遍历上一个阶段左指键位
            for (int l = 0; l < 26; l++) {
                // 遍历上一个阶段右指键位
                for (int r = 0; r < 26; r++) {
                    // 判断上一个阶段的状态是否存在
                    if (dp[i - 1][l][r] != Integer.MAX_VALUE) {
                        // 移动左指
                        dp[i][v][r] = Math.min(dp[i][v][r], dp[i - 1][l][r] + help(l, v));
                        // 移动右指
                        dp[i][l][v] = Math.min(dp[i][l][v], dp[i - 1][l][r] + help(r, v));
                    }
                    if (i == word.length()) {
                        ans = Math.min(ans, dp[i][v][r]);
                        ans = Math.min(ans, dp[i][l][v]);
                    }
                }
            }
        }
        return ans;
    }
    //优化空间时间
    public int minimumDistance2(String word) {
        // 初始化
        int len = word.length();
        int ans = Integer.MAX_VALUE;
        char[] ca = word.toCharArray();
        // 第一个字母的初始值为 0，从第二个字母开始考虑。
        int[][] dp = new int[2][26];
        Arrays.fill(dp[1], Integer.MAX_VALUE);

        // 遍历每个字母
        for (int i = 2; i <= word.length(); i++) {
            int v = ca[i - 1] - 'A';
            // 遍历上一个阶段键位
            for (int j = 0; j < 26; j++) {
                if (dp[i % 2][j] == Integer.MAX_VALUE) {
                    continue;
                }
                int preV = ca[i - 2] - 'A';
                //第一个指头移动          上一个位置到v距离
                dp[(i + 1) % 2][j] = Math.min(dp[(i + 1) % 2][j], dp[i % 2][j] + help(preV, v));
                //第二个指头移动          只要计算+ j到v距离
                dp[(i + 1) % 2][preV] = Math.min(dp[(i + 1) % 2][preV], dp[i % 2][j] + help(j, v));
                if (i == word.length()) {
                    ans = Math.min(ans, dp[(i + 1) % 2][j]);
                    ans = Math.min(ans, dp[(i + 1) % 2][preV]);
                }
            }
            Arrays.fill(dp[i % 2], Integer.MAX_VALUE);//每次重新初始化
        }
        return ans;
    }
    // 计算距离
    public int help(int a, int b) {
        int x = a / 6, y = a % 6;
        int x2 = b / 6, y2 = b % 6;
        return (int)(Math.abs(x - x2)) + (int)(Math.abs(y - y2));
    }


//    int dp[][][] = new int [27][27][301];
//    int cost(int from, int to) {
//        if (from == 26) return 0;
//        return Math.abs(from / 6 - to / 6) + Math.abs(from % 6 - to % 6);
//    }
//
//    public int minimumDistance(String word) {
//        return helper(word,0,26,26);
//    }
//    int helper(String word, int pos , int left , int right ) {
//        if (pos >= word.length()) return 0;
//        if (dp[left][right][pos] == 0) {
//            int to = word.charAt(pos) - 'A';
//            dp[left][right][pos] = Math.min(cost(left, to) + helper(word, pos + 1, to, right),
//                    cost(right, to) + helper(word, pos + 1, left, to)) + 1;
//        }
//        return dp[left][right][pos] - 1;
//    }
//
//    public int minimumDistance2(String word) {
//        int dp[] = new int[26], res = 0, save = 0, n = word.length();
//        for (int i = 0; i < n - 1; ++i) {
//            int b = word.charAt(i) - 'A', c = word.charAt(i + 1) - 'A';
//            for (int a = 0; a < 26; ++a)
//                dp[b] = Math.max(dp[b], dp[a] + d(b, c) - d(a, c));
//            save = Math.max(save, dp[b]);
//            res += d(b, c);
//        }
//        return res - save;
//
//    }
//
//    private int d(int a, int b) {
//        return Math.abs(a / 6 - b / 6) + Math.abs(a % 6 - b % 6);
//    }

}
