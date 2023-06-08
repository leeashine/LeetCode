package aleetcode.contest2020;

import java.util.*;

public class Contest199 {

    public static void main(String[] args) {

        String codeleet = new Contest199().restoreString("codeleet", new int[]{4, 5, 6, 7, 0, 2, 1, 3});
        System.out.println(codeleet);

        System.out.println(9<<1);

        System.out.println(Integer.highestOneBit((255-1)<<1));

    }
    //        求大于等于一个整数的最小2次幂算法
    //2的30次方
    static final int MAXIMUM_CAPACITY = 1 << 30;
    //最高位后面全变1然后+1就是最小2次幂
    static final int tableSizeFor(int cap) {
        //对本身就是2的n次幂 减1进行处理
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }


    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

//    输入：s = "codeleet", indices = [4,5,6,7,0,2,1,3]
//    输出："leetcode"
    public String restoreString(String s, int[] indices) {

        String res="";
        Map<Integer,Character> map=new HashMap<>();
        for(int i=0;i<indices.length;i++){

            map.put(indices[i],s.charAt(i));

        }
        for (Character value : map.values()) {
            res+=value;
        }

        return res;


    }

}
