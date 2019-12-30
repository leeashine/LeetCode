package aleetcode;

import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Contest169 {

    public static void main(String[] args) {
        int [] n=sumZero(5);

        TreeNode root1=new TreeNode(1);
        TreeNode root12=new TreeNode(1);
        TreeNode root13=new TreeNode(8);
//        root1.left=root12;
        root1.right=root13;
        TreeNode root2=new TreeNode(8);
        TreeNode root22=new TreeNode(1);
        root2.left=root22;
//        root2.right=new TreeNode(3);
        List<Integer> allElements = getAllElements(root1, root2);
//        System.out.println(allElements.toString());

        int [] arr={4,2,3,0,3,1,2};
        int start=5;
        new Contest169().canReach(arr,5);


    }
//    Input: arr = [4,2,3,0,3,1,2], start = 5
//    Output: true
//    Explanation:
//    All possible ways to reach at index 3 with value 0 are:
//    index 5 -> index 4 -> index 1 -> index 3
//    index 5 -> index 6 -> index 4 -> index 1 -> index 3
    public boolean canReach(int[] arr, int st) {
        if (st >= 0 && st < arr.length && arr[st] < arr.length) {
            int jump = arr[st];
            arr[st] += arr.length;//遍历过的标记
            return jump == 0 || canReach(arr, st + jump) || canReach(arr, st - jump);
        }
        return false;
    }


    public static List<Integer> getAllElements(TreeNode root1, TreeNode root2) {

        List<Integer> list=new ArrayList<>();

        preOrderTraveral(list,root1);
        preOrderTraveral(list,root2);

        Collections.sort(list);
        return  list;

    }
    public static void preOrderTraveral(List<Integer> list,TreeNode node){
        if(node == null){
            return;
        }
        list.add(node.val);
        preOrderTraveral(list,node.left);
        preOrderTraveral(list,node.right);
    }

    public static int[] sumZero(int n) {

        int [] res=new int[n];
        int k=0;//数
        int index=n-1;//位置
        if(n%2==0){
            k=n/2;
            while(index>-1){

                if(k==0){
                    k--;
                }
                res[index]=k;
                index--;
                k--;
            }
        }else{
            k=(n-1)/2;
            while(index>-1){
                res[index]=k;
                index--;
                k--;
            }
        }
        return res;
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
