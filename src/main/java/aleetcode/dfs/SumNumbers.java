package aleetcode.dfs;

import util.TreeNode;

/**
 * Created by LIZIXUAN560 on 2020/10/30.
 * 求根到叶子节点数字之和
 * https://leetcode-cn.com/problems/sum-root-to-leaf-numbers/
 * @author LIZIXUAN560
 */
public class SumNumbers {
//    输入: [1,2,3]
//            1
//            / \
//            2   3
//    输出: 25
//    解释:
//    从根到叶子节点路径 1->2 代表数字 12.
//    从根到叶子节点路径 1->3 代表数字 13.
//    因此，数字总和 = 12 + 13 = 25.
    public int sumNumbers(TreeNode root) {
        return helper(root, 0);
    }

    public int helper(TreeNode root, int i){
        if (root == null) {
            return 0;
        }
        int temp = i * 10 + root.val;
        if (root.left == null && root.right == null) {
            return temp;
        }
        return helper(root.left, temp) + helper(root.right, temp);
    }
}
