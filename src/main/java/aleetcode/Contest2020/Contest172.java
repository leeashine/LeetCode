package aleetcode.Contest2020;

import java.util.ArrayList;
import java.util.List;

public class Contest172 {

    public static void main(String[] args) {

        int n=new Contest172().maximum69Number(9996);
        new Contest172().printVertically("CONTEST IS COMING");
//        new Contest172().printVertically("HOW ARE YOU");

        TreeNode root=new TreeNode(1);
        TreeNode root2=new TreeNode(2);
        TreeNode root3=new TreeNode(3);
        TreeNode root4=new TreeNode(4);
        root.left=root2;
        root.right=root3;
        root.left.left=root2;
        root.right.left=root2;
        root.right.right=root4;

        new Contest172().removeLeafNodes(root,2);

    }
    public TreeNode removeLeafNodes(TreeNode root, int target) {
        if (root == null) {
            return null;
        }

        root.left = removeLeafNodes(root.left, target);
        root.right = removeLeafNodes(root.right, target);

        //后序遍历
        if (root.left == null && root.right == null && root.val == target) {
            return null;
        }

        return root;
    }


    public void dfs(TreeNode root){

        if (root == null)
            return;

        if (root.left != null) {
            dfs(root.left);

        }
        if (root.right != null) {
            dfs(root.right);

        }

    }

//    Input: s = "HOW ARE YOU"
//    Output: ["HAY","ORO","WEU"]
//    Explanation: Each word is printed vertically.
//         "HAY"
//         "ORO"
//         "WEU"
    public List<String> printVertically(String s) {

        List<String> list=new ArrayList<String>();

        String[] words = s.split(" ");
        int max=-1;
        for(String s1:words){
            max=Math.max(s1.length(),max);
        }
        List<String> ans = new ArrayList<>();
        for (int i = 0; i < max; ++i) {
            StringBuilder sb = new StringBuilder();
            for (String word : words)
                sb.append(i < word.length() ? word.charAt(i) : " ");
            while (sb.charAt(sb.length() - 1) == ' ')
                sb.deleteCharAt(sb.length() - 1); // remove trailing space.
            ans.add(sb.toString());
        }
        return ans;

    }
//    Input: num = 9669
//    Output: 9969
    public int maximum69Number (int num) {

        char[] chars = Integer.toString(num).toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '6') {
                chars[i] = '9';
                break;
            }
        }
        return Integer.parseInt(new String(chars));
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
