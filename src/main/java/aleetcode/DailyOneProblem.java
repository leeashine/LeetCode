package aleetcode;

import linklist.LinkedReverse;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyOneProblem {
    int [][] dir={{0,1},{0,-1},{1,0},{-1,0}};
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

//        int [][]arr=new int [][]{{1,3},{2,6},{8,10},{15,18}};
////        merge(arr);
////
////
////        new DailyOneProblem().new Solution().isHappy(191);

//        int [] arr={0,0,0,0,0};
//        int k=0;
//        int res=new DailyOneProblem().subarraySum(arr,k);
//        System.out.println(res);
        int[][] matrix={{1,2,3,4},{5,6,7,8},{9,10,11,12}};
//        int[][] matrix={{1,2,3},{4,5,6},{7,8,9}};
        int []res=new DailyOneProblem().spiralOrder(matrix);
        System.out.println(Arrays.toString(res));

        new DailyOneProblem().sortedArrayToBST(new int[]{-10,-3,0,5,9});

    }

    //二分法查找该插入的位置
//    在一个有序数组中找第一个大于等于 target 的下标
    public int searchInsert(int[] nums, int target) {
        int n = nums.length;
        int left = 0, right = n - 1, ans = n;
        while (left <= right) {
            int mid = ((right - left) >> 1) + left;
            if (target <= nums[mid]) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }


    //给定两个数组，编写一个函数来计算它们的交集。
//    如果给定的数组已经排好序呢？你将如何优化你的算法？
//    如果 nums1 的大小比 nums2 小很多，哪种方法更优？
//    如果 nums2 的元素存储在磁盘上，磁盘内存是有限的，并且你不能一次加载所有的元素到内存中，你该怎么办？

    public int[] intersect(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return intersect(nums2, nums1);
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int num : nums1) {
            int count = map.getOrDefault(num, 0) + 1;
            map.put(num, count);
        }
        int[] intersection = new int[nums1.length];
        int index = 0;
        for (int num : nums2) {
            int count = map.getOrDefault(num, 0);
            if (count > 0) {
                intersection[index++] = num;
                count--;
                if (count > 0) {
                    map.put(num, count);
                } else {
                    map.remove(num);
                }
            }
        }
        return Arrays.copyOfRange(intersection, 0, index);
    }


//    dictionary = ["looked","just","like","her","brother"]
//    sentence = "jesslookedjustliketimherbrother"
//    恢复空格问题：把文章断开，要求未识别的字符最少，返回未识别的字符数。
    public int respace(String[] dictionary, String sentence) {

        int n = sentence.length();

        Trie root = new Trie();
        for (String word: dictionary) {
            root.insert(word);
        }

        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i <= n; ++i) {
            //没有找到的话我们可以复用 dp[i-1] 的状态再加上当前未被识别的第 i 个字符
            dp[i] = dp[i - 1] + 1;

            Trie curPos = root;
            for (int j = i; j >= 1; --j) {
                int t = sentence.charAt(j - 1) - 'a';
                if (curPos.next[t] == null) {
                    break;
                } else if (curPos.next[t].isEnd) {
                    dp[i] = Math.min(dp[i], dp[j - 1]);
                }
                if (dp[i] == 0) {
                    break;
                }
                curPos = curPos.next[t];
            }
        }
        return dp[n];

    }

    //将有序数组转化一颗高度平衡二叉搜索树
//    二叉搜索树的中序遍历是升序序列，题目给定的数组是按照升序排序的有序数组，因此可以确保数组是二叉搜索树的中序遍历序列。
    public TreeNode sortedArrayToBST(int[] nums) {
        return helper(nums, 0, nums.length - 1);
    }

    public TreeNode helper(int[] nums, int left, int right) {
        if (left > right) {
            return null;
        }

        // 总是选择中间位置左边的数字作为根节点
        int mid = (left + right) / 2;

        TreeNode root = new TreeNode(nums[mid]);
        root.left = helper(nums, left, mid - 1);
        root.right = helper(nums, mid + 1, right);
        return root;
    }


    //最接近的三数之和
    //排序+双指针
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        int best = 10000000;

        // 枚举 a
        for (int i = 0; i < n; ++i) {
            // 保证和上一次枚举的元素不相等
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            // 使用双指针枚举 b 和 c
            int j = i + 1, k = n - 1;
            while (j < k) {
                int sum = nums[i] + nums[j] + nums[k];
                // 如果和为 target 直接返回答案
                if (sum == target) {
                    return target;
                }
                // 根据差值的绝对值来更新答案
                if (Math.abs(sum - target) < Math.abs(best - target)) {
                    best = sum;
                }
                if (sum > target) {
                    // 如果和大于 target，移动 c 对应的指针
                    int k0 = k - 1;
                    // 移动到下一个不相等的元素
                    while (j < k0 && nums[k0] == nums[k]) {
                        --k0;
                    }
                    k = k0;
                } else {
                    // 如果和小于 target，移动 b 对应的指针
                    int j0 = j + 1;
                    // 移动到下一个不相等的元素
                    while (j0 < k && nums[j0] == nums[j]) {
                        ++j0;
                    }
                    j = j0;
                }
            }
        }
        return best;
    }


//    给定一个列表 temperatures = [73, 74, 75, 71, 69, 72, 76, 73]，
//    你的输出应该是 [1, 1, 4, 2, 1, 1, 0, 0]
//    本题从最后一天倒推到第一天会简单很多
//    j += res[j]因为res[j]位置已经代表最近比这大的数相隔的位置 那第i个位置上想要找出比这大的最近位置至少也要加上res[j](j里面都比他小了其实)
    public int[] dailyTemperatures(int[] T) {
        int[] res = new int[T.length];
        res[T.length - 1] = 0;
        for (int i = T.length - 2; i >= 0; i--) {
            for (int j = i + 1; j < T.length; j += res[j]) {
                if (T[i] < T[j]) {
                    res[i] = j - i;
                    break;
                } else if (res[j] == 0) {
                    res[i] = 0;
                    break;
                }
            }
        }
        return res;
    }

//    输入：matrix = [[1,2,3],[4,5,6],[7,8,9]]
//    输出：[1,2,3,6,9,8,7,4,5]
//    螺旋数组
    public int[] spiralOrder(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new int[0];
        }
        int rows = matrix.length, columns = matrix[0].length;
        boolean[][] visited = new boolean[rows][columns];
        int total = rows * columns;
        int[] order = new int[total];
        int row = 0, column = 0;
//        顺时针顺序 右 下 左 上
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int directionIndex = 0;
        for (int i = 0; i < total; i++) {
            order[i] = matrix[row][column];
            visited[row][column] = true;
            int nextRow = row + directions[directionIndex][0], nextColumn = column + directions[directionIndex][1];
            if (nextRow < 0 || nextRow >= rows || nextColumn < 0 || nextColumn >= columns || visited[nextRow][nextColumn]) {
                directionIndex = (directionIndex + 1) % 4;
            }
            row += directions[directionIndex][0];
            column += directions[directionIndex][1];
        }
        return order;
    }


    //除自身以外数组的乘积
    public int[] productExceptSelf(int[] nums) {
        int[] res = new int[nums.length];
        int k = 1;
        for(int i = 0; i < res.length; i++){
            res[i] = k;
            k = k * nums[i]; // 此时数组存储的是除去当前元素左边的元素乘积
        }
        k = 1;
        for(int i = res.length - 1; i >= 0; i--){
            res[i] *= k; // k为该数右边的乘积。
            k *= nums[i]; // 此时数组等于左边的 * 该数右边的。
        }
        return res;
    }


    //    给定一个整数数组和一个整数 k，你需要找到该数组中和为 k 的连续的子数组的个数。
//    输入:nums = [1,1,1], k = 2
//    输出: 2 , [1,1] 与 [1,1] 为两种不同的情况。
//    前缀和进阶版
    public int subarraySum(int[] nums, int k) {
        int count = 0, pre = 0;
        HashMap < Integer, Integer > mp = new HashMap < > ();
        mp.put(0, 1);
        for (int i = 0; i < nums.length; i++) {
            pre += nums[i];
            if (mp.containsKey(pre - k))
                count += mp.get(pre - k);
            mp.put(pre, mp.getOrDefault(pre, 0) + 1);
        }
        return count;
    }

//    public int subarraySum(int[] nums, int k) {
//        int count = 0;
//        for (int start = 0; start < nums.length; ++start) {
//            int sum = 0;
//            for (int end = start; end >= 0; --end) {
//                sum += nums[end];
//                if (sum == k) {
//                    count++;
//                }
//            }
//        }
//        return count;
//    }


    //快乐数
//    「快乐数」定义为：对于一个正整数，每一次将该数替换为它每个位置上的数字的平方和，然后重复这个过程直到这个数变为 1，也可能是 无限循环 但始终变不到 1。如果 可以变为  1，那么这个数就是快乐数。
//
//    如果 n 是快乐数就返回 True ；不是，则返回 False 。
//    意识到我们实际有个链表，那么这个问题就可以转换为检测一个链表是否有环。因此我们在这里可以使用弗洛伊德循环查找算法。这个算法是两个奔跑选手，一个跑的快，一个跑得慢。在龟兔赛跑的寓言中，跑的快的称为 “乌龟”，跑得快的称为 “兔子”。
//    方法：使用“快慢指针”思想找出循环：“快指针”每次走两步，“慢指针”每次走一步，
//    当二者相等时，即为一个循环周期。此时，判断是不是因为1引起的循环，是的话就是快乐数，否则不是快乐数。
//    注意：此题不建议用集合记录每次的计算结果来判断是否进入循环，因为这个集合可能大到无法存储；另外，也不建议使用递归，同理，如果递归层次较深，会直接导致调用栈崩溃。不要因为这个题目给出的整数是int型而投机取巧。
//
//    为啥一定不会出现死循环，因为int类型最大值为为‭‭2 147 483 647‬‬， 所以平方和最大的数是1 999 999 999，平方和为1 + 81*9 = 724。任何数的平方和都在1到724之间，724次循环之内一定有重复的
//    链表找环 是一个很经典的问题了，使用快慢指针可破解。
    public class Solution {
        public int squareSum(int n) {
            int sum = 0;
            while(n > 0){
                int digit = n % 10;
                sum += digit * digit;
                n /= 10;
            }
            return sum;
        }

        public boolean isHappy(int n) {
            int slow = n, fast = squareSum(n);
            while (fast!=1&&slow != fast){
                slow = squareSum(slow);
                fast = squareSum(squareSum(fast));
            }
            return fast == 1;
        }
    }




//    输入: nums = [4,5,6,7,0,1,2], target = 0
//    输出: 4
    public int search(int[] nums, int target) {

        int lo = 0, hi = nums.length - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
//            (nums[0] > target) ^ (nums[0] > nums[mid]) ^ (target > nums[mid]) 3个条件有一个为真 逗向后规约
            if ((nums[0] > target) ^ (nums[0] > nums[mid]) ^ (target > nums[mid]))
                lo = mid + 1;
            else
                hi = mid;
        }
        return lo == hi && nums[lo] == target ? lo : -1;

    }

    //合并区间（双指针）
//    merge最右端点>左端点合并
//  {1,3},{2,6},{8,10}合并成 {1,10}
    public static int[][] merge(int[][] arr) {
        if(arr == null || arr.length<=1)
            return arr;
        List<int[]> list = new ArrayList<>();
        Arrays.sort(arr, (o1, o2) -> o1[0]-o2[0]);
        int i=0;
        int n = arr.length;
        while(i<n){
            int left = arr[i][0];
            int right = arr[i][1];
            while(i<n-1 && right>=arr[i+1][0]){
                right = Math.max(right,arr[i+1][1]);
                i++;
            }
            list.add(new int[] {left,right});
            i++;
        }
        return list.toArray(new int[list.size()][2]);
    }

    //01矩阵 BFS
    public int[][] updateMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return null;
        int m = matrix.length, n = matrix[0].length;
        int[][] res = new int[m][n];//结果集
        boolean[][] visited = new boolean[m][n];//记录已经计算过的位置
        Queue<int[]> queue = new LinkedList<>();//广搜队列
        //遍历，将等于0的位置计入结果集并入队
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    res[i][j] = 0;
                    visited[i][j] = true;
                    queue.offer(new int[]{i, j});
                }
            }
        }
        //四个方向广搜
        int[][] direction = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};//上下左右
        while (!queue.isEmpty()) {
            int[] poll = queue.poll();
            int i = poll[0], j = poll[1];
            //四个方向上找 1
            for (int k = 0; k < 4; k++) {
                int di = i + direction[k][0], dj = j + direction[k][1];
                //没有计算过的地方一定是 1
                if (di >= 0 && di < m && dj >= 0 && dj < n && !visited[di][dj]) {
                    res[di][dj] = res[i][j] + 1;
                    visited[di][dj] = true;
                    queue.offer(new int[]{di, dj});
                }
            }
        }
        return res;
    }

    //两数相加（链表） 注意进位
    public ListNode addTwoNumbers(ListNode l1,ListNode l2) {

        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();

        while(l1 != null) {
            s1.push(l1.val);
            l1 = l1.next;
        }
        while(l2 != null) {
            s2.push(l2.val);
            l2 = l2.next;
        }

        ListNode res = null;
        int c = 0;
        //节点链接成链表 和反转链表对比 带进位的加
        while(!s1.isEmpty() || !s2.isEmpty() || c > 0) {
            int sum = (s1.isEmpty() ? 0 : s1.pop()) +
                    (s2.isEmpty() ? 0 : s2.pop()) + c;
            ListNode n = new ListNode(sum % 10);//
            c = sum / 10;//下一次进位值
            n.next = res;
            res = n;
        }
        return res;

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

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


}
