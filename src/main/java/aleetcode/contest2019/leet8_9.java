package aleetcode.contest2019;

import util.TreeNode;

public class leet8_9 {

	
	int []sz;
	int res;
	int x;
	boolean method(TreeNode root,int n,int x){
		sz=new int [n+1];
		dfs(root);
		res=n-sz[x];
		this.x=x;
		update(root);
		
		return res>n/2;
	}
	
	void dfs(TreeNode root){
		
		if(root==null) return;
		sz[root.val]=1;
		if(root.left!=null) {
			dfs(root.left);
			sz[root.val]+=sz[root.left.val];
		}
		if(root.right!=null){
			dfs(root.right);
			sz[root.val]+=sz[root.right.val];
		}
		
	}
	
	void update(TreeNode root){
		if(root==null) return;
		if(root.val==x){//�ҵ�root.val==x���Ǹ��㣬�ҳ��������������ڵ�
			
			if(root.left!=null){
				res=Math.max(res, sz[root.left.val]);
			}
			if(root.right!=null){
				res=Math.max(res, sz[root.right.val]);
			}
			
		}
		update(root.left);
		update(root.right);
		
	}
	
}
