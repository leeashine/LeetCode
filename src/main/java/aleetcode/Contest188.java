package aleetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Contest188 {
    public static void main(String[] args) {

//        List<String> list = new Contest188().buildArray(new int[]{2,3,4}, 4);
//        System.out.println(list.toString());

        new Contest188().countTriplets(new int[]{2,3,1,6,7});

    }
//    输入：arr = [2,3,1,6,7]
//    输出：4
//    解释：满足题意的三元组分别是 (0,1,2), (0,2,2), (2,3,4) 以及 (2,4,4)
    public int countTriplets(int[] arr) {
        int result=0,len=arr.length;
        for(int j=1;j<len;j++)
        {
            int temp=arr[j];
            for(int i=j-1;i>=0;i--)
            {
                temp^=arr[i];
                if(temp==0)
                    result+=j-i;
            }
        }
        return result;
    }

//    public int countTriplets(int[] arr) {
//        // List<Integer> 存索引
//        HashMap<Integer, List<Integer>> map = new HashMap<>();
//        int num = 0;
//        int result = 0;
//        List<Integer> temp = new ArrayList<>();
//        temp.add(-1);
//        // 插入一个初始值，防止数组前两个异或为0的情况被漏掉
//        map.put(0, temp);
//        for (int i = 0; i < arr.length; i ++) {
//            // 利用num记录前0~i的元素异或的结果
//            num ^= arr[i];
//            if (!map.containsKey(num)) {
//                // 如果不存在则插入对应的索引i
//                List<Integer> index = new ArrayList<>();
//                index.add(i);
//                map.put(num, index);
//            }
//            else {
//                // 存在则遍历前面的结果
//                List<Integer> index = map.get(num);
//                for (Integer integer : index) {
//                    // 大于2，则被加在result中
//                    if (i - integer >= 2) {
//                        result += i - integer - 1;
//                    }
//                }
//                index.add(i);
//                // 更新result以后别忘记将索引更新
//                map.put(num, index);
//            }
//        }
//        return result;
//    }


//    输入：target = [1,3], n = 3
//    输出：["Push","Push","Pop","Push"]
//    解释：
//    读取 1 并自动推入数组 -> [1]
//    读取 2 并自动推入数组，然后删除它 -> [1]
//    读取 3 并自动推入数组 -> [1,3]
//    双指针问题
    public List<String> buildArray(int[] target, int n) {

        List<String> res=new ArrayList<>();
        int i=1;
        int j=0;
        while (j<target.length){
            int num=target[j];
            if(i<=n){
                if(i==num){
                    res.add("Push");
                    i++;
                    j++;
                }else{
                    res.add("Push");
                    res.add("Pop");
                    i++;
                }

            }
        }
        return res;

    }
}
