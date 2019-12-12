package aleetcode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Contest166 {
    public static void main(String[] args) {

        BigDecimal b1 = new BigDecimal("-0.0085").multiply(new BigDecimal("3"));

        List list=new ArrayList();
        System.out.println(list.contains(3));

    }

    //    Input: groupSizes = [3,3,3,3,3,1,3]
    //    Output: [[5],[0,1,2],[3,4,6]]
    //    Explanation:
    //    Other possible solutions are [[2,1,6],[5],[0,4,3]] and [[5],[0,6,2],[4,3,1]].
    // ˼·:hashmap
    public List<List<Integer>> groupThePeople(int[] arr) {

        int n = arr.length;
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        List<List<Integer>> ans = new ArrayList<>();

        for(int i = 0; i < n; i++){
            int curr = arr[i];
            List<Integer> temp = new ArrayList<>();
            if(map.containsKey(curr)) temp=map.get(curr);
            temp.add(i);
            map.put(curr,temp);//����map
            if(temp.size()>=curr){//���������
                ans.add(temp);
                map.remove(curr);//�Ƴ���һ�� ��һ�����¿�ʼ
            }
        }

        return ans;

    }

    //Input: n = 234
    //Output: 15
    //Explanation:
    //Product of digits = 2 * 3 * 4 = 24
    //Sum of digits = 2 + 3 + 4 = 9
    //Result = 24 - 9 = 15
    public static int subtractProductAndSum(int n) {

        int result = 0;
        int digits = 1;
        int sum = 0;
        while (n / 10 > 0) {
            int c = n % 10;
            digits *= c;
            sum += c;
            n = n / 10;
        }
        digits *= n;
        sum += n;
        return digits - sum;
    }
}
