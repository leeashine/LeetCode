package linklist;

public class LinkedReverse {

    //单链表翻转例题 contest167 getDecimalValue问题 1—>0->1  5
    public static void main(String[] args) {
        Node node = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        node.next = node2;
        node2.next = node3;
        node3.next = node4;

        reverseDG2(node);
    }

    /**
     * 函数定义: 翻转链表并返回翻转后的头节点
     * <p>
     * 1->2->3->4->null   ==>  1-> 2<-3<-4
     * |
     * null
     *
     * @param head
     * @return
     */
    public static Node reverseDG(Node head) {
        if (head.next == null) {
            return head;
        }
        Node last = reverseDG(head.next);
        head.next.next = head;
        head.next = null;
        return last;
    }

    /**
     * 对于递归问题，如果不好debug(线上试题模式),
     * 技巧：打印方便调试(每次在开头和返回处打印相关信息)
     */
    public static Node reverseDG2(Node head) {
        printIndent(count++);
        System.out.printf("head: %s\n ", head);
        if (head.next == null) {
            printIndent(--count);
            System.out.print(" return \n");
            return head;
        }
        Node last = reverseDG2(head.next);
        head.next.next = head;
        head.next = null;
        printIndent(--count);
        System.out.printf("return last: %s\n", last);
        return last;
    }

    static int count = 0;

    /**
     * 输入n,打印n个tab缩进
     */
    static void printIndent(int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("  ");
        }
    }

    public static Node reverse(Node head) {

        Node prev = null;
        Node curr = head;
        while (curr != null) {
            Node nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;

    }

    public static class Node {

        public int value;
        public Node next;

        public Node(int data) {
            this.value = data;
        }

        @Override
        public String toString() {
            return this.value + "->" + (this.next == null ? "NULL" : this.next.toString());
        }
    }
}
