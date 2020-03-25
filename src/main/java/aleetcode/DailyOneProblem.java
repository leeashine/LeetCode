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

//        lengthOfLIS2(new int[]{10,9,2,5,3,7,21,18});


//        String s = compressString("abbccd");
//        System.out.println(s);


        massage(new int []{1,1});

    }

    //按摩师
    //dp0[]代表当前位置不按摩的最优解
    //dp1[]代表当前位置按摩的最优解，也就是dp0位置的左边最优解
//    定义 dp[i][0] 表示考虑前 i 个预约，第 i 个预约不接的最长预约时间，
//    dp[i][1] 表示考虑前 i 个预约，第 i 个预约接的最长预约时间。
//    首先考虑 dp[i][0] 的转移方程，由于这个状态下第 i 个预约是不接的，所以第 i-1 个预约接或不接都可以，
//    故可以从 dp[i-1][0] 和 dp[i-1][1] 两个状态转移过来，转移方程即为：dp[i][0]=Max(dp[i-1][0],dp[i-1][1])
//    考虑dp[i][1]的转移方程，这个状态下第i个预约是接的，所以第i-1个是不接的,所以转移方程为：dp[i][1]=dp[i-1][0]+nums[i]
//    答案即为 max(dp[n][0],dp[n][1])
//    再回来看转移方程，我们发现计算 dp[i][0/1]时，只与前一个状态 dp[i-1][0/1] 有关，所以我们可以不用开数组，只用两个变量 dp_0和dp_1分别存储dp[i-1][0]和dp[i-1][1]的答案，然后去转移更新答案即可。
    public static int massage(int[] nums) {

        if(nums.length==0)
            return 0;
        int dp0=0;
        int dp1=nums[0];
        for(int i=1;i<nums.length;i++){
            int tdp0=Math.max(dp0,dp1);
            int tdp1=dp0+nums[i];

            dp0=tdp0;//更新状态
            dp1=tdp1;

        }

        return Math.max(dp0,dp1);

    }

    //求最小的K个数
    public int[] getLeastNumbers(int[] arr, int k) {

        int [] res=new int [k];

        Arrays.sort(arr);
        for(int i=0;i<k;i++){

            res[i]=arr[i];

        }


        return res;


    }

    //最长回文串
    public int longestPalindrome(String s) {

        Map<Character,Integer> map=new HashMap();
        int res=0;

        for(char c:s.toCharArray()){
            map.put(c,map.getOrDefault(c,0)+1);
        }
        for(Map.Entry<Character,Integer> entry:map.entrySet()){

            int value=entry.getValue();
            res+=value/2*2;//ans 一直是偶数
            if(value%2==1&&res%2==0){//碰到第一个基数的时候 +1
                res++;
            }

        }
        return res;

    }

    //拼写单词
    public int countCharacters(String[] words, String chars) {

        int [] count=count(chars);
        int res=0;
        //bt cat hat tree
        for(String s:words){

            int [] word_count=count(s);
            if(contains(count,word_count)){
                res+=s.length();
            }
        }
        return res;

    }

    private boolean contains(int[] count, int[] word_count) {
        for(int i=0;i<26;i++){
            if(count[i]<word_count[i])
                return false;
        }
        return true;
    }

    // 统计 26 个字母出现的次数
    int[] count(String word) {
        int[] counter = new int[26];
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            counter[c-'a']++;
        }
        return counter;
    }





    //字符串压缩
    public static String compressString(String S) {
        if(S.length()==0)
            return S;
        String ans="";
        char[] chars = S.toCharArray();
        char c=chars[0];
        int cnt=1;
        for(int i=1;i<S.length();++i){

            if(c==chars[i]) cnt++;
            else{
                ans+=c+""+cnt;
                c=chars[i];
                cnt=1;
            }

        }
        ans+=c+""+cnt;

        return ans.length()>S.length()?S:ans;

    }

    //岛屿最大面积
    int [][] dir={{0,1},{0,-1},{1,0},{-1,0}};
    int x,y;
    public int maxAreaOfIsland(int[][] grid) {
        int res=0;

        for(int i=0;i<grid.length;i++)
            for(int j=0;j<grid[0].length;j++)
                res=Math.max(res,dfs(grid,i,j));
        return res;

    }

    public int dfs(int[][] grid, int a, int b) {
        if(a<0||a>=grid.length||b<0||b>=grid[0].length|| grid[a][b]==0)
            return 0;
        grid[a][b]=0;//遍历标志
        int ans=1;
        for(int i=0;i<4;i++){
            x=a+dir[i][0];
            y=b+dir[i][1];
            ans+=dfs(grid,x,y);

        }
        return ans;
    }

    //    最长上升子序列的长度。 O(n2)
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
            dp[i] = maxval + 1;//状态转移方程
            maxans = Math.max(maxans, dp[i]);
        }
        return maxans;
    }

    //    最长上升子序列的长度。 O(nlogn)
    public static int lengthOfLIS2(int[] nums) {
        int[] tails = new int[nums.length];
        int res = 0;
        for(int num : nums) {
            int i = 0, j = res;
            while(i < j) {//二分法
                int m = (i + j) / 2;
                if(tails[m] < num) i = m + 1;
                else j = m;
            }
            tails[i] = num;
            if(res == j) res++;
        }
        return res;
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
