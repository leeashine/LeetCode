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

    public static Node reverse(Node node){

        Node pre=null;
        Node temp=null;
        while(node!=null){
            temp=node.next;//把下一个节点赋值给临时节点
            node.next=pre;//连接新的一个位置
            pre=node;//把node 和 pre对调
            node=temp;//此时这个节点就是反转链表里的下一个节点

        }
        return pre;

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
