package java8;

/**
 * Created by LIZIXUAN560 on 2020/12/11.
 *
 * @author LIZIXUAN560
 */
public class ListNode extends BaseDO{
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
