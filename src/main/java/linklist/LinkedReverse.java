package linklist;

public class LinkedReverse {

    //单链表翻转例题 contest167 getDecimalValue问题 1—>0->1  5
    public static void main(String[] args) {
        Node node=new Node(1);
        Node node2=new Node(2);
        Node node3=new Node(3);
        Node node4=new Node(4);
        node.next=node2;
        node2.next=node3;
        node3.next=node4;

        System.out.println(reverse(node));
    }

    public static Node reverse(Node head){

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

    public static class Node{

        public int value;
        public Node next;

        public Node(int data){
            this.value=data;
        }

        @Override
        public String toString() {
            return this.value + "->" + (this.next == null ? "NULL" : this.next.toString());
        }
    }
}
