package aleetcode.leetstack;

import java.util.Stack;

public class Stack_01 {
//    整理字符串 题目描述 删除相邻两个相同字符（不区分大小写）直到字符串整理好为止。
//    输入：s = "abBAcC"
//    输出：""
//    输入：s = "leEeetcode"
//    输出："leetcode"
//    栈
    public static String makeGood(String s) {

        char[] chars = s.toCharArray();
        Stack<Character> stack=new Stack<>();
        for(int i=0;i<chars.length;i++){
            char cur=chars[i];

            if(stack.empty()){
                stack.push(cur);
                continue;
            }
            char tmp=stack.peek();
            if(Math.abs(tmp-cur)==32){
                stack.pop();
            }else{
                stack.push(cur);
            }

        }
        StringBuilder res=new StringBuilder();
        while(!stack.empty()){
            res.append(stack.pop());
        }

        return res.reverse().toString();

    }
}
