package aleetcode;

import util.TreeNode;

import java.util.*;

public class Practise {

    public static void main(String[] args) {
//        int [] arr={1,3,7,5,2};
//        new Practise().quickSort(arr);
//        System.out.println(Arrays.toString(arr));

//        Stack<Integer> stack=new Stack<>();
//        stack.push(1);
//        stack.push(5);
//        stack.push(6);
//        stack.push(4);
//        stack.push(2);
//
//        stackSort(stack);

//        7,  3
//        1 1 6
//        1 2 5
//        count(7,3);
//        int gcd = gcd(3, 6);
//        System.out.println(gcd);

//        ListNode listNode=new ListNode(1);
//        listNode.next=new ListNode(2);
//        listNode.next.next=new ListNode(3);
//
//        ListNode re= reverseList(listNode);

        TreeNode node=new TreeNode(1);
        node.left=new TreeNode(2);
        node.right=new TreeNode(3);
        node.left.left=new TreeNode(4);
        node.left.right=new TreeNode(5);
        node.right.left=new TreeNode(6);
        int i = new Practise().countNodes(node);
        System.out.println(i);

    }

    //解数独  优化 利用位运算压缩 0 1000 0001 表示0和8出现过
//    其中 i 个元素的值为 True，当且仅当数字 i+1出现过。例如我们用 line[2][3]=True 表示数字 4 在第 2 行已经出现过
//    https://leetcode-cn.com/problems/sudoku-solver/solution/jie-shu-du-by-leetcode-solution/
    private boolean[][] line = new boolean[9][9];
    private boolean[][] column = new boolean[9][9];
    private boolean[][][] block = new boolean[3][3][9];
    private boolean valid = false;
    private List<int[]> spaces = new ArrayList<int[]>();

    public void solveSudoku(char[][] board) {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (board[i][j] == '.') {
                    spaces.add(new int[]{i, j});
                } else {
                    int digit = board[i][j] - '0' - 1;
                    line[i][digit] = column[j][digit] = block[i / 3][j / 3][digit] = true;
                }
            }
        }

        dfs(board, 0);
    }

    public void dfs(char[][] board, int pos) {
        if (pos == spaces.size()) {
            valid = true;
            return;
        }

        int[] space = spaces.get(pos);
        int i = space[0], j = space[1];
        for (int digit = 0; digit < 9 && !valid; ++digit) {
            if (!line[i][digit] && !column[j][digit] && !block[i / 3][j / 3][digit]) {
                line[i][digit] = column[j][digit] = block[i / 3][j / 3][digit] = true;
                board[i][j] = (char) (digit + '0' + 1);
                dfs(board, pos + 1);
                line[i][digit] = column[j][digit] = block[i / 3][j / 3][digit] = false;
            }
        }
    }

    //统计素数质数的个数 Sieve of Eratosthenes
    int countPrimes(int n) {
        boolean[] isPrim = new boolean[n];
        Arrays.fill(isPrim, true);
        for (int i = 2; i * i < n; i++)
            if (isPrim[i])
                for (int j = i * i; j < n; j += i)
                    isPrim[j] = false;

        int count = 0;
        for (int i = 2; i < n; i++)
            if (isPrim[i]) count++;

        return count;
    }


    int sum=0;
    public int countNodes(TreeNode root) {

        dfs2(root);
        return sum;

    }
    public void dfs2(TreeNode root){

        if(root==null)
            return;
        sum++;
        if(root.left!=null){
            dfs2(root.left);
        }
        if(root.right!=null){
            dfs2(root.right);
        }

    }

    //青蛙跳台阶问题 1 2
    //dp[i] 代表跳到第i级台阶的跳法 跳1步 dp[i-1] 跳2步dp[i-2];
    //dp[0]=0 dp[1]=1 dp[2]=2 dp[3]=dp[1]+dp[2]
    public static int numWays(int n) {


        int []dp=new int[n+1];
        if(n==0)
            return 1;
        else if(n==1)
            return 1;
        else if(n==2)
            return 2;
        else {
            dp[0]=0;dp[1]=1;dp[2]=2;
            for (int i=3;i<=n;i++){
                dp[i]=dp[i-1]%1000000007+dp[i-2]%1000000007;
            }

        }
        return (dp[n]%1000000007);


    }

    public static ListNode reverseList(ListNode listNode){

        ListNode pre=null;
        ListNode tmp=null;
        while (listNode!=null){

            tmp=listNode.next;
            listNode.next=pre;
            pre=listNode;
            listNode=tmp;

        }
        return pre;

    }

    //二维数组的查找
    public boolean findNumberIn2DArray(int[][] matrix, int target) {

        if(matrix==null||matrix.length==0||matrix[0].length==0)
            return false;
        int rows=matrix.length;
        int cols=matrix[0].length;
        int row=0;
        int col=matrix[0].length-1;
        while(row<rows&&col>=0){
            if(matrix[row][col]==target)
                return true;
            else if(matrix[row][col]>target){
                col--;
            }else{
                row++;
            }
        }
        return false;

    }

    //树的深度 bfs
    public int maxDepth2(TreeNode root) {
        if(root == null) return 0;
        List<TreeNode> queue = new LinkedList<>(), tmp;
        queue.add(root);
        int res = 0;
        while(!queue.isEmpty()) {
            tmp = new LinkedList<>();
            for(TreeNode node : queue) {
                if(node.left != null) tmp.add(node.left);
                if(node.right != null) tmp.add(node.right);
            }
            queue = tmp;
            res++;
        }
        return res;
    }

    //树的深度 dfs
    public int getHight(TreeNode node){
        return dfs(node);
    }

    public int dfs(TreeNode node){
        if(node==null){
            return 0;
        }
        int ln=dfs(node.left);
        int rn=dfs(node.right);
        return Math.max(ln,rn)+1;
    }

    //())
    public int minAddToMakeValid(String S) {

        Stack stack=new Stack();
        char[] chars = S.toCharArray();
        int res=0;
        for(char c:chars){

            if(c=='('){
                stack.push('c');
            }else{
                if(stack.isEmpty())
                  res++;
                stack.pop();
            }
        }
        while (!stack.isEmpty()){
            stack.pop();
            res++;
        }
        return res;

    }

//    public int rand10() {
//        // 首先得到一个数
//        int num = (rand7() - 1) * 7 + rand7();
//        // 只要它还大于40，那你就给我不断生成吧
//        while (num > 40)
//            num = (rand7() - 1) * 7 + rand7();
//        // 返回结果，+1是为了解决 40%10为0的情况
//        return 1 + num % 10;
//
//    }


    public void jiexi(String str){

        StringBuffer sb = new StringBuffer();
        int len = 0;
        int quotaNum = 0;
        for (int i = 0; i < str.length(); i++){
            //去除双引号
            if (str.charAt(i) == '\"'){
                quotaNum++;
                continue;
            }
            if (str.charAt(i) != ' '){
                sb.append(str.charAt(i));
            } else if (quotaNum % 2 == 0){
                //遇到新单词
                sb.append('\n');
                len++;
            }else {
                //引号里的空格
                sb.append(' ');
            }
        }
        System.out.println(len+1);
        System.out.println(sb.toString());

    }

    //堆翻转 堆倒置
    void stackReverse(Stack<Integer> stack){

        if(stack.empty()){
            return ;
        }
        int last=removeLast(stack);
        stackReverse(stack);//递归操作
        stack.push(last);

    }

    int removeLast(Stack<Integer> stack){

        int value=stack.pop();
        if(stack.isEmpty()){
            return value;
        }else{
            int last=removeLast(stack);
            stack.push(value);
            return last;
        }

    }

    //二进制数1的个数
    int numOfBit1(int a)
    {
        int cnt = 0;
        while(a != 0)
        {
            a &= a - 1;    //将最右边的1置为0；正负都可计算，负数是按照补码计算的，最后的符号位也被统计
            ++cnt;
            // a|=a-1;//统计的是0的个数
        }
        return cnt;
    }

    //最小公倍数就是两数之积/最大公约数
    //最大公约数
    public static int gcd(int a,int b){
        return b == 0 ? a : gcd(b, a % b);
    }

    //M个苹果放入N个盘子里的分法种数
    public static int countDp(int m,int n){

        int [][]dp=new int[m+1][n+1];
        for(int i=0;i<=m;i++){
            dp[i][1]=1;
            dp[i][0]=1;
        }
        for(int j=0;j<=n;j++){
            dp[1][j]=1;
            dp[0][j]=1;
        }
        //i个苹果 放到j个盘子里
        for(int i=2;i<=m;i++){
            for (int j=2;j<=n;j++){
                if(i>=j){
                    //如果每个盘里都有苹果
                    //       每个盘子都有苹果 + 有一个盘子没有苹果
                    dp[i][j]=dp[i-j][j]+dp[i][j-1];
                }else{
                    //注定有盘子空着 因为无关顺序 所以i个苹果放到i个盘子里
                    dp[i][j]=dp[i][j-1];
                }
            }
        }
        return dp[m][n];

    }

    //M个盘子放n个苹果题
    public static int count(int m,int n)
    {
        if(m<0||n<=0)
            return 0;
        //细分到苹果数为一或盘子数为一的情况返回一
        if(m==1||n==1||m==0)
            return 1;
        //将此事件无线细分
        return count(m,n-1)+count(m-n,n);
    }

    //567123
    //旋转数组 找到他的最小那个数的位置
    public int sort(int [] arr){

        int l=0;
        int r=arr.length-1;
        while(l<r){
            int mid=l+(r-l)/2;
            if(arr[mid]>arr[r]){
                l=mid+1;
            }else if(arr[mid]<arr[r]){
                r=mid;
            }else{
                //如果右半边数字都相同 需要一次次比较
                r--;
            }

        }
        return arr[l];
    }

    public static void stackSort(Stack<Integer> stack){
        Stack<Integer> tmp=new Stack<>();
        while(!stack.empty()){
            int cur=stack.pop();
            while(!tmp.empty()&&tmp.peek()>cur){
                stack.push(tmp.pop());
            }
            tmp.push(cur);
        }
        while(!tmp.empty()){
            stack.push(tmp.pop());
        }
    }


    public void quickSort(int [] arr){

        quickSort(arr,0,arr.length-1);

    }
    public void quickSort(int[] arr,int l,int r){

        if(l>=r)
            return;

        int p=partition(arr,l,r);

        quickSort(arr,l,p-1);
        quickSort(arr,p+1,r);

    }

    private int partition(int[] arr, int l, int r) {

        swap(arr,l,(int)(Math.random()*(r-l+1))+l);
        int v=arr[l];
        int i=l+1;
        int j=r;
        while(true){

            while(i<=r&&arr[i]<v) i++;
            while(j>=l+1&&arr[j]>v) j--;
            if(i>j) break;
            swap(arr,i,j);
            i++;
            j--;
        }
        swap(arr,l,j);

        return j;

    }
    private void swap(int []arr,int i,int j){

        int tmp=arr[i];
        arr[i]=arr[j];
        arr[j]=tmp;

    }
    public static class
    ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
