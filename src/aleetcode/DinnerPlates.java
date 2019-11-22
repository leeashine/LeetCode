package aleetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class DinnerPlates {

    Map<Integer, Stack<Integer>> map;
    int cap;
    int curr;
    int last;
    int count;

    public DinnerPlates(int capacity) {
        cap = capacity;
        curr = 0; //where to push element
        last = 0; //where to pop element
        count = 0; //number of elements
        map = new HashMap<>();
        map.put(curr, new Stack<Integer>());
    }

    public void push(int val) {
        //do some preprocessing to update current index
        while(map.containsKey(curr) && map.get(curr).size()==cap){
            curr++;
        }
        if(!map.containsKey(curr)){
            map.put(curr, new Stack<>());
        }
        map.get(curr).push(val);
        last = Math.max(last, curr);
        count++;
    }

    public int pop() {
        if(count==0) return -1;
        while(last>=0 && map.get(last).isEmpty()){
            last--;
        }
        count--;
        curr=Math.min(curr, last);
        return map.get(last).pop();
    }

    public int popAtStack(int index) {
        if(!map.containsKey(index) || map.get(index).isEmpty()){
            return -1;
        }
        count--;
        curr=Math.min(curr, index);
        return map.get(index).pop();
    }
}
