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
