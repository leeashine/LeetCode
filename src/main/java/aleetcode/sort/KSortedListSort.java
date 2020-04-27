package aleetcode.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class KSortedListSort {
    public static void main(String[] args) {
//        [[1,4,5],[1,3,4],[2,6]]
//        ListNode node1=new ListNode(1);
//        ListNode node2=new ListNode(4);
//        ListNode node3=new ListNode(5);
//        node1.next=node2;
//        node2.next=node3;
//        ListNode node11=new ListNode(1);
//        ListNode node4=new ListNode(3);
//        ListNode node41=new ListNode(4);
//        ListNode node5=new ListNode(2);
//        ListNode node6=new ListNode(6);
//        node11.next=node4;
//        node4.next=node41;
//        node5.next=node6;
//        ListNode [] lists={node1,node11,node5};
//        new KSortedListSort().mergeKLists(lists);


    }
    public ListNode mergeKLists(ListNode[] lists) {

        if(lists==null || lists.length==0) {
            return null;
        }
        //创建一个堆，并设置元素的排序方式
        PriorityQueue<ListNode> queue = new PriorityQueue(new Comparator<ListNode>() {
            public int compare(ListNode o1, ListNode o2) {
                return (o1.val - o2.val);
            }
        });
        //由于leetcode的listnode构造函数需要传val,所以初始化节点传一个-1,最后节点返回next
        ListNode dummy = new ListNode(-1);
        ListNode cur = dummy;//cur代表现在遍历到的最后的节点
        //这里跟上一版不一样，不再是一股脑全部放到堆中
        //而是只把k个链表的第一个节点放入到堆中
        for(int i=0;i<lists.length;i++) {
            ListNode head = lists[i];
            if(head!=null) {
                queue.offer(head);
            }
        }
        //之后不断从堆中取出节点，如果这个节点还有下一个节点，
        //就将下个节点也放入堆中
        while(queue.size()>0) {
            //链接结点
            ListNode node = queue.poll();
            cur.next = node;
            cur = cur.next;

            if(node.next!=null) {
                queue.offer(node.next);
            }
        }
        cur.next = null;
        return dummy.next;

    }
    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }
}
