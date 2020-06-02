package aleetcode.contest2020;

import util.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contest180 {

    public static void main(String[] args) {

        int [][] matrix={{36376,85652,21002,4510},{68246,64237,42962,9974},{32768,97721,47338,5841},{55103,18179,79062,46542}};
        luckyNumbers(matrix);


    }
    public int sumFourDivisors(int[] nums) {
        int ans = 0;
        for (int num: nums) {
            // factor_cnt: 因数的个数
            // factor_sum: 因数的和
            int factor_cnt = 0, factor_sum = 0;
            for (int i = 1; i * i <= num; ++i) {
                if (num % i == 0) {
                    ++factor_cnt;
                    factor_sum += i;
                    if (i * i != num) {   // 判断 i 和 num/i 是否相等，若不相等才能将 num/i 看成新的因数
                        ++factor_cnt;
                        factor_sum += num / i;
                    }
                }
            }
            if (factor_cnt == 4) {
                ans += factor_sum;
            }
        }
        return ans;
    }


    //二叉树转化为平衡二叉树
    ArrayList<TreeNode> orderList = null;
    TreeNode[] orderNode = null;
    public TreeNode balanceBST(TreeNode root) {
        orderList = new ArrayList<>();

        // 思路：先找出所有节点，然后中分取数
        // 先找所有节点，中序遍历，查出有序的集合
        doSelect(root);

        if (orderList.size() < 3) return root; // 个数小于3就没必要了

        orderNode = orderList.toArray(new TreeNode[orderList.size()]); // 构建成数组，以便于取区间的中位数

        return doBuild(0, orderNode.length - 1); // 构建新的树
    }

    private TreeNode doBuild(int left, int right){
        if (left > right) return null;

        int curRootIndex = (left + right) / 2;
        TreeNode curRoot = orderNode[curRootIndex];
        curRoot.left = doBuild(left, curRootIndex - 1);
        curRoot.right = doBuild(curRootIndex + 1, right);
        return curRoot;
    }

    // 中序遍历获取数组链表
    private void doSelect(TreeNode curRoot){
        if (curRoot == null) return;

        doSelect(curRoot.left);
        orderList.add(curRoot);
        doSelect(curRoot.right);
    }


    //找出这个幸运值
    //在同一行的所有元素中最小
    //在同一列的所有元素中最大
    public static List<Integer> luckyNumbers (int[][] matrix) {

        List<Integer> list=new ArrayList<>();

        int m=matrix.length;//行
        int min;
        int max;
        Map<Integer,Integer> map=new HashMap();
        int n=0;

        for (int i = 0; i < m; i++) {
            min=Integer.MAX_VALUE;
            n=matrix[i].length;//列
            //找同一行的最小值
            for (int j = 0; j < n; j++) {
                if(matrix[i][j]<min){
                    min=matrix[i][j];
                    map.put(i,j);
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            max=Integer.MIN_VALUE;
            int x=entry.getKey();
            int y=entry.getValue();
            if(matrix[x][y]>max){
                max=matrix[x][y];
            }
            for(int j=0;j<m;j++){
                if(matrix[j][y]>max){
                    max=matrix[j][y];
                }
            }
            if(max==matrix[x][y])
                list.add(max);
        }

        return list;
    }


}
