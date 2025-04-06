package aleetcode.code2023;

import aleetcode.Practise;
import com.alibaba.fastjson.JSON;

/**
 * 给定一个链表，如果有环路，找出环路的开始点。
 * https://leetcode.com/problems/linked-list-cycle-ii/
 * 思路：快慢指针，快指针每次前进2步，慢指针每次前进1步。当第一次相遇就代表有环，把fast移到头，然后重新走步长1直到相遇的节点即为环路的开始点
 */
public class LC142LinkedListcycleII {

    public static void main(String[] args) {

        ListNode node = new ListNode(3);
        ListNode node1 = new ListNode(2);
        ListNode node2 = new ListNode(0);
        ListNode node3 = new ListNode(-4);
        node.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node1;
        ListNode listNode = detectCycle(node);
        System.out.println(JSON.toJSONString(listNode));

    }

    public static ListNode detectCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        // 判断是否存在环路
        while (fast != null && fast.next != null) {

            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow) {
                break;
            }
        }
        if (fast == null || fast.next == null) {
            return null;
        }
        // 如果存在，查找环路节点
        fast = head;
        while (fast != slow) {
            fast = fast.next;
            slow = slow.next;
        }
        return fast;
    }

    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int x) {
            val = x;
            next = null;
        }
    }

}
