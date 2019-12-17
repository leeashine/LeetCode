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

        int [] A={1,2,5,9};
        int threshold = 6;
        smallestDivisor(A,threshold);

    }

//    输入：nums = [1,2,5,9], threshold = 6
//    输出：5
    public static int smallestDivisor(int[] A, int threshold) {

        int left = 1, right = (int)1e6;
        while (left < right) {
            int m = left + (right - left) / 2, sum = 0;
            for (int i : A)
                sum += Math.ceil(i*1.0/m);//向上取整
            if (sum > threshold)
                left = m + 1;
            else
                right = m;
        }
        return left;

    }


    //    Input: groupSizes = [3,3,3,3,3,1,3]
    //    Output: [[5],[0,1,2],[3,4,6]]
    //    Explanation:
    //    Other possible solutions are [[2,1,6],[5],[0,4,3]] and [[5],[0,6,2],[4,3,1]].
    // 思路:hashmap
    public List<List<Integer>> groupThePeople(int[] arr) {

        int n = arr.length;
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        List<List<Integer>> ans = new ArrayList<>();

        for(int i = 0; i < n; i++){
            int curr = arr[i];
            List<Integer> temp = new ArrayList<>();
            if(map.containsKey(curr)) temp=map.get(curr);
            temp.add(i);
            map.put(curr,temp);//更新map
            if(temp.size()>=curr){//如果组满了
                ans.add(temp);
                map.remove(curr);//移除这一组 下一轮重新开始
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
