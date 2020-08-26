package aleetcode.design;

import java.util.Deque;
import java.util.LinkedList;

//实现一个队列 插入删除O（1） 获取最大值O（1）
public class MaxQueue {
    Deque<Integer> res, max;
    public MaxQueue() {
        res = new LinkedList<Integer>();
        max = new LinkedList<Integer>();
    }

    public int max_value() {
        if(max.isEmpty()) return -1;
        return max.peekFirst();
    }

    public void push_back(int value) {
        res.addLast(value);
        while(!max.isEmpty() && max.peekLast()<value) max.removeLast();
        max.addLast(value);
    }

    public int pop_front() {
        if(res.isEmpty()) return -1;
        int temp = res.peekFirst();
        if(temp == max.peekFirst()) max.removeFirst();
        res.removeFirst();
        return temp;
    }
}
