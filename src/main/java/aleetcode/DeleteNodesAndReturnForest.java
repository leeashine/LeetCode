package aleetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aleetcode.util.TreeNode;


public class DeleteNodesAndReturnForest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TreeNode root1=new TreeNode(1);
		TreeNode root2=new TreeNode(2);
		TreeNode root3=new TreeNode(3);
		TreeNode root4=new TreeNode(4);
		TreeNode root5=new TreeNode(5);
		TreeNode root6=new TreeNode(6);
		TreeNode root7=new TreeNode(7);
		root1.left=root2;
		root1.right=root3;
		root2.left=root4;
		root2.right=root5;
		root3.left=root6;
		root3.right=root7;
		
		int []to_delete=new int[] {3,5};
		delNodes(root1, to_delete);
	}

	
	static Set<Integer> to_delete_set;
	static List<TreeNode> res = new ArrayList<>();

	public static List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
		res = new ArrayList<>();
		to_delete_set = new HashSet<>();
		for (int i : to_delete)
			to_delete_set.add(i);
		helper(root, true);
		return res;
	}

	private static TreeNode helper(TreeNode node, boolean is_root) {
		if (node == null)
			return null;
		boolean deleted = to_delete_set.contains(node.val);
		if (is_root && !deleted)
			res.add(node);
		node.left = helper(node.left, deleted);
		node.right = helper(node.right, deleted);
		return deleted ? null : node;
	}

}
