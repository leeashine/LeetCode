package aleetcode;


public class CustomStack {
    int []stack;
    int cur;
    public CustomStack(int maxSize) {
        stack=new int[maxSize];
        cur=0;
    }

    public void push(int x) {
        if(cur==stack.length)
            return;
        stack[cur]=x;
        cur++;

    }

    public int pop() {
        if(cur==0)
            return -1;
        cur--;
        int i = stack[cur];
        stack[cur]=0;
        return i;
    }

    public void increment(int k, int val) {
        if(k>stack.length){
            k=stack.length;
        }
        for (int i = 0; i < k; i++) {
            stack[i]=stack[i]+val;
        }

    }
}
