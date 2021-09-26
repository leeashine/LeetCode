package aleetcode.util;

import java.util.HashMap;
import java.util.Map;

public class AllOne {

    private class Node {

        final String key;
        int val = 1;
        Node prev;
        Node next;

        Node(final String key) {
            this.key = key;
        }

        void bubbleRight() {
            while (next != null && val > next.val) {
                swap(this, next);
            }
        }

        void bubbleLeft() {
            while (prev != null && val < prev.val) {
                swap(prev, this);
            }
        }

        void setNext(final Node newNext) {
            next = newNext;

            if (newNext != null) {
                newNext.prev = this;
            }
        }

        void setPrev(final Node newPrev) {
            prev = newPrev;

            if (newPrev != null) {
                newPrev.next = this;
            }
        }

        void unlinkInMiddle() {
            prev.setNext(next);
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        map.forEach((s, node) -> sb.append("[" + s).append(", " + node.val + "], "));
        return sb.toString();
    }

    private void swap(final Node a, final Node b) {
        if (a == head) {
            makeHead(b);
        }

        if (b == tail) {
            makeTail(a);
        }

        final Node aPrev = a.prev;
        final Node bNext = b.next;

        b.setPrev(aPrev);
        a.setNext(bNext);
        b.setNext(a);
    }

    private Node head;
    private Node tail;

    private void makeHead(final Node node) {
        if (node != null) {
            node.prev = null;
        }

        head = node;
    }

    private void makeTail(final Node node) {
        if (node != null) {
            node.next = null;
        }

        tail = node;
    }

    private final Map<String, Node> map = new HashMap<>();

    public void inc(final String key) {
        if (!map.containsKey(key)) {
            final Node newHead = new Node(key);
            map.put(key, newHead);
            newHead.setNext(head);

            if (head == null) {
                makeTail(newHead);
            }

            makeHead(newHead);
            return;
        }

        final Node node = map.get(key);
        node.val++;
        node.bubbleRight();
    }

    public void dec(final String key) {
        if (!map.containsKey(key)) {
            return;
        }

        final Node node = map.get(key);

        if (node.val == 1) {
            if (node == head) {
                makeHead(node.next);
            } else if (node == tail) {
                makeTail(node.prev);
            } else {
                node.unlinkInMiddle();
            }

            map.remove(key);
            return;
        }

        node.val--;
        node.bubbleLeft();
    }

    public String getMaxKey() {
        if (tail == null) {
            return "";
        }

        return tail.key;
    }

    public String getMinKey() {
        if (head == null) {
            return "";
        }

        return head.key;
    }

   

}
