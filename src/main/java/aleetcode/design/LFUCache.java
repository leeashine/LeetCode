package aleetcode.design;

import java.util.HashMap;
import java.util.Map;

/**
 * 请你为 最不经常使用（LFU）缓存算法设计并实现数据结构。它应该支持以下操作：get 和 put。
 *
 *         get(key) - 如果键存在于缓存中，则获取键的值（总是正数），否则返回 -1。
 *         put(key, value) - 如果键已存在，则变更其值；如果键不存在，请插入键值对。
 *         当缓存达到其容量时，则应该在插入新项之前，使最不经常使用的项无效。
 *         在此问题中，当存在平局（即两个或更多个键具有相同使用频率）时，应该去除最久未使用的键。
 *         「项的使用次数」就是自插入该项以来对其调用 get 和 put 函数的次数之和。
 *         使用次数会在对应项被移除后置为 0 。
 *         进阶：
 *         你是否可以在 O(1) 时间复杂度内执行两项操作？
 *
 */

class LFUCache {
    Map<Integer, Node> cache; // 存储缓存的内容
    Map<Integer, DoublyLinkedList> freqMap; // 存储每个频次对应的双向链表
    int size;
    int capacity;
    int min; // 存储当前最小频次

    public LFUCache(int capacity) {
        cache = new HashMap<>(capacity);
        freqMap = new HashMap<>();
        this.capacity = capacity;
    }

    public int get(int key) {
        Node node = cache.get(key);
        if (node == null) {
            return -1;
        }
        freqInc(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        Node node = cache.get(key);
        if (node != null) {
            node.value = value;
            freqInc(node);
        } else {
            if (size == capacity) {
                DoublyLinkedList minFreqLinkedList = freqMap.get(min);
                cache.remove(minFreqLinkedList.tail.pre.key);
                minFreqLinkedList.removeNode(minFreqLinkedList.tail.pre); // 这里不需要维护min, 因为下面add了newNode后min肯定是1.
                size--;
            }
            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            DoublyLinkedList linkedList = freqMap.get(1);
            if (linkedList == null) {
                linkedList = new DoublyLinkedList();
                freqMap.put(1, linkedList);
            }
            linkedList.addNode(newNode);
            size++;
            min = 1;
        }
    }

    void freqInc(Node node) {
        // 从原freq对应的链表里移除, 并更新min
        int freq = node.freq;
        DoublyLinkedList linkedList = freqMap.get(freq);
        linkedList.removeNode(node);
        if (freq == min && linkedList.head.post == linkedList.tail) {
            min = freq + 1;
        }
        // 加入新freq对应的链表
        node.freq++;
        linkedList = freqMap.get(freq + 1);
        if (linkedList == null) {
            linkedList = new DoublyLinkedList();
            freqMap.put(freq + 1, linkedList);
        }
        linkedList.addNode(node);
    }
}

class Node {
    int key;
    int value;
    int freq = 1;
    Node pre;
    Node post;

    public Node() {}

    public Node(int key, int value) {
        this.key = key;
        this.value = value;
    }
}

//自定义一个双向链表
class DoublyLinkedList {
    Node head;
    Node tail;

    public DoublyLinkedList() {
        head = new Node();
        tail = new Node();
        head.post = tail;
        tail.pre = head;
    }

    void removeNode(Node node) {
        node.pre.post = node.post;
        node.post.pre = node.pre;
    }

    void addNode(Node node) {
        node.post = head.post;
        head.post.pre = node;
        head.post = node;
        node.pre = head;
    }
}
/**
 * Your LFUCache object will be instantiated and called as such:
 * LFUCache obj = new LFUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
