package aleetcode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


//        massage(new int []{1,1});

//        int []deck=new int []{1,1};
//        hasGroupsSizeX(deck);

//        int getyinshu = getyinshu(2,2);
//        System.out.println(getyinshu);

//        new DailyOneProblem().numSteps("1101");
//        System.out.println(Integer.parseInt("1101",2));

//        String s = new DailyOneProblem().reverseWords("the sky is blue");
//        System.out.println(s);
//        String s="the sky is  blue";


    }

    //翻转字符串里的单词（双指针去除两端空格 统计单词） 对比liststring解析
    public String reverseWords(String s) {

        int left = 0, right = s.length() - 1;
        // 去掉字符串开头的空白字符
        while (left <= right && s.charAt(left) == ' ') ++left;

        // 去掉字符串末尾的空白字符
        while (left <= right && s.charAt(right) == ' ') --right;

        Deque<String> d = new ArrayDeque();
        StringBuilder word = new StringBuilder();

        while (left <= right) {
            char c = s.charAt(left);
            if ((word.length() != 0) && (c == ' ')) {
                // 将单词 push 到队列的头部
                d.offerFirst(word.toString());
                word.setLength(0);
            } else if (c != ' ') {
                word.append(c);
            }
            ++left;
        }
        d.offerFirst(word.toString());

        return String.join(" ", d);

    }

    //括号生成 n=2 ()() 或 (())
    public List<String> generateParenthesis(int n) {

        List<String> res=new ArrayList<>();
        if (n == 0) {
            return res;
        }
        //做减法
        dfs("",n,n,res);
        return res;

    }

    private void dfs(String s, int left, int right, List<String> res) {

        if (left == 0 && right == 0) {
            res.add(s);
            return;
        }
        //剪枝
        if (left > right) {
            return;
        }
        if (left > 0) {
            dfs(s + "(", left - 1, right, res);
        }

        if (right > 0) {
            dfs(s + ")", left, right - 1, res);
        }

    }

    //二进制数进位操作 +1 /2
    public int numSteps(String s) {
        int res = 0, carry = 0;
        for (int i = s.length() - 1; i > 0; --i) {
            ++res;
            if (s.charAt(i) - '0' + carry == 1) {
                carry = 1;
                ++res;
            }
        }
        return res + carry;
    }

    public List<Integer> minSubsequence(int[] nums) {
        List<Integer> res = new ArrayList<>();
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int sub_sum = 0, total_sum = 0;
        for (int n : nums) {
            pq.offer(n);
            total_sum += n;
        }
        while (sub_sum <= total_sum / 2) {
            res.add(pq.peek());
            sub_sum += pq.poll();
        }
        return res;
    }

    //旋转矩阵（不借助辅助数组）即空间复杂度为O(n)
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n / 2; ++i) {//旋转左上角4分之一就可以不重复 不遗漏
            for (int j = 0; j < (n + 1) / 2; ++j) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - j - 1][i];
                matrix[n - j - 1][i] = matrix[n - i - 1][n - j - 1];
                matrix[n - i - 1][n - j - 1] = matrix[j][n - i - 1];
                matrix[j][n - i - 1] = temp;
            }
        }

    }

    public int myAtoi(String str) {

        char []chars=str.toCharArray();
        for(int i=0;i<chars.length;i++){
            if(chars[i]==' ')
                i++;
            if(chars[i]!=' ')
                break;
        }

        return 0;

    }

    void gameOfLife(int[][] board) {
        int [] neighbors= {0, 1, -1};

        int rows = board.length;
        int cols = board[0].length;

        // 创建复制数组 copyBoard
        int [][]copyBoard=new int [rows][cols];

        // 从原数组复制一份到 copyBoard 中
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                copyBoard[row][col] = board[row][col];
            }
        }

        // 遍历面板每一个格子里的细胞
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                // 对于每一个细胞统计其八个相邻位置里的活细胞数量
                int liveNeighbors = 0;

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {

                        if (!(neighbors[i] == 0 && neighbors[j] == 0)) {
                            int r = (row + neighbors[i]);
                            int c = (col + neighbors[j]);

                            // 查看相邻的细胞是否是活细胞
                            if ((r < rows && r >= 0) && (c < cols && c >= 0) && (copyBoard[r][c] == 1)) {
                                liveNeighbors += 1;
                            }
                        }
                    }
                }

                // 规则 1 或规则 3
                if ((copyBoard[row][col] == 1) && (liveNeighbors < 2 || liveNeighbors > 3)) {
                    board[row][col] = 0;
                }
                // 规则 4
                if (copyBoard[row][col] == 0 && liveNeighbors == 3) {
                    board[row][col] = 1;
                }
            }
        }
    }


    public String replaceSpaces(String S, int length) {
        return S.substring(0, length).replaceAll(" ", "%20");
    }

    public int[] sortArray(int[] nums) {

        sort(nums,0,nums.length-1);

        return nums;
    }
    public void sort(int [] nums,int l,int r){

        if (l >= r) return;
        int p = partition(nums, l, r);
        sort(nums, l, p - 1);
        sort(nums, p + 1, r);

    }

    private int partition(int[] nums, int l, int r) {

        swap(nums,l,(int)(Math.random()*(r-l+1))+l);

        int v=nums[l];

        int i=l+1;
        int j=r;
        while(true){

            while(i<=r&&nums[i]<v) i++;
            while(j>=l+1&&nums[j]>v) j--;
            if(i>j) break;
            swap(nums,i,j);
            i++;
            j--;
        }
        swap(nums,l,j);
        return j;

    }
    public void swap(int []arr,int x,int y){
        int tmp=arr[x];
        arr[x]=arr[y];
        arr[y]=tmp;
    }


    public static boolean hasGroupsSizeX(int[] deck) {

        if(deck.length<=1)
            return false;
        int [] cnt=new int [10001];
        for(int i=0;i<deck.length;i++){
            cnt[deck[i]]++;
        }
        int X=cnt[deck[0]];

        for(int i=0;i<cnt.length-1;i++){
            if(cnt[i]>0)
            X=gcd(X,cnt[i]);
            if (X == 1) {
                return false;
            }

        }

        return true;
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
