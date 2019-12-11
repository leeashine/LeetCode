package util;

import java.util.HashMap;
import java.util.Map;

public class LRU2<k,v>{

	//����
    private int capacity;
    //��ǰ�ж��ٽڵ��ͳ��
    private int count;
    //����ڵ�
    private Map<k, Node<k, v>> nodeMap;
    private Node<k, v> head;
    private Node<k, v> tail;

    public LRU2(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException(String.valueOf(capacity));
        }
        this.capacity = capacity;
        this.nodeMap = new HashMap<>();
        //��ʼ��ͷ�ڵ��β�ڵ㣬�����ڱ�ģʽ�����ж�ͷ����β�ڵ�Ϊ�յĴ���
        Node headNode = new Node(null, null);
        Node tailNode = new Node(null, null);
        headNode.next = tailNode;
        tailNode.pre = headNode;
        this.head = headNode;
        this.tail = tailNode;
    }

    public void put(k key, v value) {
        Node<k, v> node = nodeMap.get(key);
        if (node == null) {
            if (count >= capacity) {
                //���Ƴ�һ���ڵ�
                removeNode();
            }
            node = new Node<>(key, value);
            //��ӽڵ�
            addNode(node);
        } else {
            //�ƶ��ڵ㵽ͷ�ڵ�
            moveNodeToHead(node);
        }
    }

    public Node<k, v> get(k key) {
        Node<k, v> node = nodeMap.get(key);
        if (node != null) {
            moveNodeToHead(node);
        }
        return node;
    }

    private void removeNode() {
        Node node = tail.pre;
        //�����������Ƴ�
        removeFromList(node);
        nodeMap.remove(node.key);
        count--;
    }

    private void removeFromList(Node<k, v> node) {
        Node pre = node.pre;
        Node next = node.next;

        pre.next = next;
        next.pre = pre;

        node.next = null;
        node.pre = null;
    }

    private void addNode(Node<k, v> node) {
        //��ӽڵ㵽ͷ��
        addToHead(node);
        nodeMap.put(node.key, node);
        count++;
    }

    private void addToHead(Node<k, v> node) {
        //ͷ�ڵ�
        Node next = head.next;
        next.pre = node;
        node.next = next;
        node.pre = head;
        head.next = node;
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

        public Node(k key, v value) {
            this.key = key;
            this.value = value;
        }
    }
	
	
}
