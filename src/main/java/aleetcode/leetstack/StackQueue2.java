package aleetcode.leetstack;

import java.util.Stack;

//栈模拟队列 先进先出
public class StackQueue2 {

    Stack<Integer> s1=new Stack<>();
    Stack<Integer> s2=new Stack<>();
    int front;

    public void push(int x){

        if (s1.empty())
            front = x;
        s1.push(x);


    }


    public int pop(){

        int pop=s2.pop();
        if(!s1.empty()){
            s2.push(s1.pop());
        }
        return pop;

    }

    public int peek() {
        if(!s2.empty())
            return s2.peek();
        return front;

    }

    public boolean empty() {

        return s1.isEmpty()&&s2.isEmpty();
    }


    public static void main(String[] args) {
        StackQueue2 stackQueue=new StackQueue2();
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
