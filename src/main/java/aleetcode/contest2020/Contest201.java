package aleetcode.contest2020;

import java.util.Stack;

public class Contest201 {
    public static void main(String[] args) {

//        char c='a';
//        char b=(char) (c-32);
//        System.out.println(b);

//        String s="leEeetcode";
//        String s1 = makeGood(s);
//        System.out.println(s1);
//        char invert = findKthBit(3,1);
//        System.out.println(invert);


    }
//    S1 = "0"
//    当 i > 1 时，Si = S(i-1) + "1" + reverse(invert(Si-1))
    public char findKthBit(int n, int k) {
        int count = 0, l = (1 << n) - 1;
        while (k > 1) {
            if (k == l / 2 + 1)
                return count % 2 == 0 ? '1' : '0';
            if (k > l / 2) {
                k = l + 1 - k;
                count++;
            }
            l /= 2;
        }
        //翻转2次等于没翻转 o->1->0
        return count % 2 == 0 ? '0' : '1';
    }


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
