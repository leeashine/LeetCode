package aleetcode.leetstack;

import java.util.Stack;

//栈模拟队列 先进先出
public class StackQueue {

    Stack<Integer> s1=new Stack<>();
    Stack<Integer> s2=new Stack<>();
    int front;

    public void push(int x){

        if (s1.empty())
            front = x;
        while (!s1.isEmpty())
            s2.push(s1.pop());
        s2.push(x);
        while (!s2.isEmpty())
            s1.push(s2.pop());

    }

    public int pop(){
        int top=s1.pop();
        if (!s1.empty())
            front = s1.peek();
        return top;
    }

    public int peek() {

        return front;

    }

    public boolean empty() {

        return s1.isEmpty();
    }


    public static void main(String[] args) {
        StackQueue stackQueue=new StackQueue();
        stackQueue.push(1);
        stackQueue.push(2);
        stackQueue.push(3);
        stackQueue.push(4);
        int a=stackQueue.pop();
        int b=stackQueue.pop();
        int c=stackQueue.pop();
        int d=stackQueue.pop();
        System.out.println(d);
    }

}
