package aleetcode;

import java.util.*;
import java.util.stream.Collectors;

public class Contest178 {
    public static void main(String[] args) {

        int [] nums=new int []{8,1,2,2,3};
        int[] ints = smallerNumbersThanCurrent(nums);
        System.out.println(Arrays.toString(ints));

        String [] votes=new String[]{"ABC","ACB","ABC","ACB","ACB"};
        String s = rankTeams(votes);
        System.out.println(s);

        ListNode head=new ListNode(1);
        head.next=new ListNode(4);
        head.next.next=new ListNode(2);
        head.next.next.next=new ListNode(6);
        head.next.next.next.next=new ListNode(8);

        TreeNode root=new TreeNode(1);
        root.left=new TreeNode(4);
        root.left.right=new TreeNode(2);
        root.left.right.left=new TreeNode(1);
        root.right=new TreeNode(4);
        root.right.left=new TreeNode(2);
        root.right.left.left=new TreeNode(6);
        root.right.left.right=new TreeNode(8);
        root.right.left.right.left=new TreeNode(1);
        root.right.left.right.right=new TreeNode(3);


        isSubPath(head,root);

    }

    public int minCost(int[][] grid) {

        int m=grid.length;
        int n=grid[0].length;
        if(grid==null||m<1||n<1)
            return -1;
        //下 上 又 左
        int [][] dir={{0,0},{0,1},{0,-1},{1,0},{-1,0}};
        Deque deque=new LinkedList();
        deque.addFirst(new Node(0,0,0));

        boolean[][] visit = new boolean[m][n];
        while (!deque.isEmpty()){

            //当前位置
            Node poll=(Node)deque.pollFirst();

            int x=poll.x;
            int y=poll.y;
            int cost=poll.cost;

            if(x==m-1&&y==n-1)
                return cost;
            visit[x][y]=true;

            for(int i=1;i<=4;i++){
                //下一个位置
                int nx=x+dir[i][0];
                int ny=y+dir[i][1];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || visit[nx][ny]) continue;
                if(grid[x][y]==i){//方向相同
                    deque.addFirst(new Node(nx,ny,cost));
                }else{//变方向
                    deque.addLast(new Node(nx,ny,cost+1));
                }
            }
        }
        return -1;
    }

    //BFS+DFS dp 最优解
    class Solution {
        int[][] DIR = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        public int minCost(int[][] grid) {
            int m = grid.length, n = grid[0].length, cost = 0;
            int[][] dp = new int[m][n];
            for (int i = 0; i < m; i++) Arrays.fill(dp[i], Integer.MAX_VALUE);
            Queue<int[]> bfs = new LinkedList<>();
            dfs(grid, 0, 0, dp, cost, bfs);
            while (!bfs.isEmpty()) {
                cost++;
                for (int size = bfs.size(); size > 0; size--) {
                    int[] top = bfs.poll();
                    int r = top[0], c = top[1];
                    for (int i = 0; i < 4; i++) dfs(grid, r + DIR[i][0], c + DIR[i][1], dp, cost, bfs);
                }
            }
            return dp[m - 1][n - 1];
        }

        void dfs(int[][] grid, int r, int c, int[][] dp, int cost, Queue<int[]> bfs) {
            int m = grid.length, n = grid[0].length;
            if (r < 0 || r >= m || c < 0 || c >= n || dp[r][c] != Integer.MAX_VALUE) return;
            dp[r][c] = cost;
            bfs.offer(new int[]{r, c}); // add to try to change direction later
            int nextDir = grid[r][c] - 1;
            dfs(grid, r + DIR[nextDir][0], c + DIR[nextDir][1], dp, cost, bfs);
        }
    }

    //0-1BFS
    public int minCost2(int[][] grid) {
        int m, n;
        if (grid == null || (m = grid.length) < 1 || (n = grid[0].length) < 1) {
            return -1;
        }

        int[] dx = {0, 0, 0, 1, -1};
        int[] dy = {0, 1, -1, 0, 0};

        Deque<Node> deq = new LinkedList<>();
        deq.addFirst(new Node(0, 0, 0));

        boolean[][] visit = new boolean[m][n];

        while (!deq.isEmpty()) {
            Node node = deq.pollFirst();
            int x = node.x;
            int y = node.y;
            int cost = node.cost;
            if (x == m-1 && y == n-1)
                return cost;

            visit[x][y] = true;

            for (int i=1; i<=4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || visit[nx][ny]) continue;
                if (grid[x][y] == i) {
                    deq.addFirst(new Node(nx, ny, cost));
                } else {
                    deq.addLast(new Node(nx, ny, cost+1));
                }
            }
        }

        return -1;
    }

    class Node {
        int x;
        int y;
        int cost;
        Node(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }
    }


    //搜索二叉树中子树
    public static boolean isSubPath(ListNode head, TreeNode root) {
        if (head == null) return true;
        if (root == null) return false;
        //先判断当前的节点，如果不对，再看左子树和右子树
        return dfs(head, root) || isSubPath(head, root.left) || isSubPath(head, root.right);
    }

    private static boolean dfs(ListNode head, TreeNode root) {
        // 链表已经全部匹配完，匹配成功
        if (head == null) return true;
        // 二叉树访问到了空节点，匹配失败
        if (root == null) return false;
        // 当前匹配的二叉树上节点的值与链表节点的值不相等，匹配失败
        if(head.val == root.val) return false;
        return (dfs(head.next, root.left) || dfs(head.next, root.right));
    }

    public static String rankTeams(String[] votes) {
        //key是参赛团队，value是该团队每个排位获得的票数
        Map<Character, int[]> teamRankMap = new HashMap<>();

        for (String vote : votes) {
            for (int i = 0; i < vote.length(); i++) {
                int[] rankArr = teamRankMap.getOrDefault(vote.charAt(i), new int[26]);
                rankArr[i]++;
                teamRankMap.put(vote.charAt(i), rankArr);
            }
        }

        //map排序 先转换成List
        List<Map.Entry<Character, int[]>> teamRankList = new ArrayList<>(teamRankMap.entrySet());
        Collections.sort(teamRankList, (team1, team2) -> {
            int[] ranks1 = team1.getValue();
            int[] ranks2 = team2.getValue();
            //根据投票排序
            for (int i = 0; i < 26; i++) {
                if (ranks1[i] != ranks2[i]) {
                    return ranks2[i] - ranks1[i];
                }
            }
            //字母顺序排序
            return team1.getKey() - team2.getKey();
        });

        //转换为字符串输出
        return teamRankList.stream().map(entry -> String.valueOf(entry.getKey())).collect(Collectors.joining());
    }

    public static int[] smallerNumbersThanCurrent(int[] nums) {
        int[] count = new int[101];

        //由于题目数比较小0 <= nums[i] <= 100 可以用这种办法对数组进行排序 并进行统计次数
        for(int i=0;i<nums.length;i++)
            count[nums[i]]++;

        int sum=0;//前缀和
        for(int i=0;i<count.length;i++){
            int curr = count[i];
            count[i] = sum;//更新数组第I位置之前的前缀和 这样只用原数组就能 不用多开辟一个数组空间
            sum+=curr;
        }

        for(int i=0;i<nums.length;i++)
            nums[i] = count[nums[i]];

        return nums;
    }


    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
