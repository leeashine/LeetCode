package util;

import java.util.HashMap;
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
       
    	Node<k,v> node=nodeMap.get(key);
    	if(node==null){
    		if(count>capacity)
    			//�Ƴ��ڵ�
    			removeNode();
    		node = new Node(key, value);
    		addNode(node);
    	}else{
    		//�ƶ���ͷ�ڵ�
    		moveNodeToHead(node);
    	}
    	
    }

    public Node<k, v> get(k key) {
       Node node=nodeMap.get(key);
       if(node!=null){
    	   moveNodeToHead(node);
       }
       return node;
    }

    private void removeNode() {
       Node node=tail.pre;
       //�����������Ƴ�
       removeFromList(node);
       nodeMap.remove(node.key);
       count--;
    }

    private void removeFromList(Node<k, v> node) {
        Node pre=node.pre;
        Node next=node.next;
        
        pre.next=next;
        next.pre=pre;
        
        node.key=null;
        node.value=null;
    }

    private void addNode(Node<k, v> node) {
       
    	//��ӽڵ㵽ͷ��
    	addToHead(node);
    	nodeMap.put(node.key, node);
    	count++;
    	
    }

    private void addToHead(Node<k, v> node) {
       
    	Node next=head.next;
    	next.pre=node;
    	node.next=next;
    	node.pre=head;
    	head.next=node;
    	
    }

    public void moveNodeToHead(Node<k, v> node) {
    	//�����������Ƴ�
        removeFromList(node);
        //��ӽڵ㵽ͷ��
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
