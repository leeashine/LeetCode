package util;

import java.util.Map;

public class LRU3<k,v>{

	//����
    private int capacity;
    //��ǰ�ж��ٽڵ��ͳ��
    private int count;
    //����ڵ�
    private Map<k, Node<k, v>> nodeMap;
    private Node<k, v> head;
    private Node<k, v> tail;

    public LRU3(int capacity) {
        if(capacity<1){
        	throw new IllegalArgumentException(String.valueOf(capacity));
        }
        //�ڱ�ģʽ
        Node headNode=new Node(null, null);
        Node tailNode=new Node(null, null);
        headNode.next=tailNode;
        tailNode.pre=headNode;
        this.head=headNode;
        this.tail=tailNode;
    	
    }

    public void put(k key, v value) {

        Node<k, v> node = nodeMap.get(key);
        if(node==null){

            if(count>capacity){
                //�Ƴ�һ���ڵ�
                removeNode();
            }
            node=new Node<>(key,value);
            //��ӽڵ�
            addToHead(node);
        }else{
            //�ƶ���ͷ���
            addToHead(node);
        }

        return;
    }

    public Node<k, v> get(k key) {

        Node<k, v> node = nodeMap.get(key);
        if (node != null) {
            //�ƶ���ͷ�ڵ�
            moveNodeToHead(node);
        }
        return node;

    }

    //�Ƴ��ڵ㣨β����
    private void removeNode() {

        Node node=tail.pre;
        removeFromList(node);
        nodeMap.remove(node);
        count--;

    }


    private void addNode(Node<k, v> node) {
        //��ӵ�ͷ��
        addToHead(node);
        nodeMap.put(node.key, node);
        count++;
    }
    //��ӽڵ㵽ͷ��
    private void addToHead(Node<k, v> node) {

        //������ͷ�ڵ�
        Node headNode=head.next;
        headNode.pre=node;
        node.next=headNode;
        node.pre=head;
        head.next=node;

    }
    //�����������Ƴ�
    private void removeFromList(Node<k, v> node) {

        Node pre=node.pre;
        Node next=node.next;
        pre.next=next;
        next.pre=pre;
        node.next=null;
        node.pre=null;

    }

    public void moveNodeToHead(Node<k, v> node) {
        //�����������Ƴ�
        removeFromList(node);
        //��ӵ�ͷ��
        addToHead(node);

    }

    private class Node<k, v> {
        k key;
        v value;
        Node pre;
        Node next;
        public Node(k key,v value){
            this.key=key;
            this.value=value;
        }

    }
	
	
}
