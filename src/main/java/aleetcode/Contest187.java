package aleetcode;

import java.util.*;

public class Contest187 {

    public static void main(String[] args) {

        List<List<String>> paths=new ArrayList<>();
        paths.add(Arrays.asList("London","New York"));
        paths.add(Arrays.asList("New York","Lima"));
        paths.add(Arrays.asList("Lima","Sao Paulo"));
        destCity(paths);
        int [] nums={1,1,1,0};
        int k=3;
        System.out.println(kLengthApart(nums,k));

    }
//    输入：nums = [1,0,0,0,1,0,0,1], k = 2
//    输出：true
//    解释：每个 1 都至少相隔 2 个元素。
//    双指针问题
    public static boolean kLengthApart(int[] nums, int k) {
        int i=0;
        int j=1;
        while(i<nums.length){
            if(nums[i]==1){
                while(j<=k&&(i+j)<nums.length){
                    if(nums[i+j]!=0)
                        return false;
                    j++;
                }
                i+=j-1;
                j=1;
            }
            i++;
        }
        return true;
    }
//    旅行终点站
//    paths = [["London","New York"],["New York","Lima"],["Lima","Sao Paulo"]]
    public static String destCity(List<List<String>> paths) {

        Map<String,String> map=new HashMap();
        for(List<String> list:paths){

            String from=list.get(0);
            String to=list.get(1);
            map.put(from,to);

        }
        String from = paths.get(0).get(0);
        while(true){
            if(!map.containsKey(from))
                return from;
            from = map.get(from);
        }

    }


}
