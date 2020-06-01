package aleetcode.contest2019;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contest167 {

    public static void main(String[] args) {
        ListNode node=new ListNode(1);
        ListNode node1=new ListNode(0);
        ListNode node2=new ListNode(1);
        node.next=node1;
        node1.next=node2;
        int n=getDecimalValue(node);
        System.out.println(n);


        sequentialDigits(1000,13000);

        int [][] mat={{1,1,3,2,4,3,2},{1,1,3,2,4,3,2},{1,1,3,2,4,3,2}};
        int threshold=4;
        int i = maxSideLength(mat, threshold);
        System.out.println(i);

//        int cn=4;
//        int res=1;
//        int next=1;
//        while(--cn>0){
//            int n1=res*10+ ++next;
//            if (n1 > res) {
//                res = n1;
//            }else{
//                break;
//            }
//            System.out.println(res);
//        }

    }

    //DP prefixsum
    public static int maxSideLength(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] sum = new int[m + 1][n + 1];

        int res = 0;
        int len = 1; // square side length

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                sum[i][j] = sum[i-1][j] + sum[i][j-1] - sum[i-1][j-1] + mat[i-1][j-1];

                if (i >= len && j >= len && sum[i][j] - sum[i-len][j] - sum[i][j-len] + sum[i-len][j-len] <= threshold)
                    res = len++;
            }
        }

        return res;
    }

//    Input: low = 1000, high = 13000
//    Output: [1234,2345,3456,4567,5678,6789,12345]
    public static List<Integer> sequentialDigits(int low, int high) {

        List<Integer> res = new ArrayList<>();
        for (int digit = 1; digit < 9; ++digit) {
            int next = digit, n = next;
            while (n <= high && next < 10) {
                if (n >= low) {
                    res.add(n);
                }
                int n1 = n * 10 + ++next;
                if (n1 > n) {
                    n = n1;
                }else { // Integer overflow.
                    break;
                }
            }
        }
        Collections.sort(res);
        return res;

    }
    //head = [1,0,1]   5
    public static int getDecimalValue(ListNode head) {

        int res=1;
        int sum=0;
        ListNode node= reverse(head);
        while (node!=null){
//            System.out.println(res);
            sum+=res*node.val;
//            System.out.println(sum);
            node=node.next;
            res*=2;
        }

        return sum;

    }

    public static ListNode reverse(ListNode head){
        ListNode pre=null;
        ListNode next=null;
        while (head!=null){

            next=head.next;
            head.next=pre;
            pre=head;
            head=next;

        }
        return pre;
    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
