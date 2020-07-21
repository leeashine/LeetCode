package linklist;

/**
 * 判断一个链表是否有环
 * 快慢指针法 一个走一步 一个走两步 最终能相遇就是环
 */
public class IsCycle {

    public static void main(String[] args) {

        ListNode node=new ListNode(1);
        ListNode node2=new ListNode(2);
        ListNode node3=new ListNode(3);
        ListNode node4=new ListNode(4);
        node.next=node2;
        node2.next=node3;
        node3.next=node4;
        node4.next=node;
        boolean b = new IsCycle().hasCycle(node);
        System.out.println(b);

    }

    /**
     * 判断一个链表是否有环
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while(fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
            if(fast == slow){
                return true;
            }
        }
        return false;

    }

    /**
     * 寻找带环链表的环入口
     * @param head
     * @return
     */
    public ListNode detectCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while(fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
            if(fast == slow){
                break;
            }
        }
        if(fast == null || fast.next == null){
            return null;
        }

        slow = head;
        while(fast != slow){
            slow = slow.next;
            fast = fast.next;
        }
        return slow;

    }

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

}
