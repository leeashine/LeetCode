package aleetcode;

import java.util.*;

public class Contest199 {

    public static void main(String[] args) {

        String codeleet = new Contest199().restoreString("codeleet", new int[]{4, 5, 6, 7, 0, 2, 1, 3});
        System.out.println(codeleet);

    }

//    输入：s = "codeleet", indices = [4,5,6,7,0,2,1,3]
//    输出："leetcode"
    public String restoreString(String s, int[] indices) {

        String res="";
        Map<Integer,Character> map=new HashMap<>();
        for(int i=0;i<indices.length;i++){

            map.put(indices[i],s.charAt(i));

        }
        for (Character value : map.values()) {
            res+=value;
        }

        return res;


    }

}
