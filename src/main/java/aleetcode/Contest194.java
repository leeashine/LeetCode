package aleetcode;

import java.util.*;

public class Contest194 {
    public static void main(String[] args) {

//        int i = new Contest194().xorOperation(10, 5);
//        System.out.println(i);

//        String[] names={"onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece"};
//        new Contest194().getFolderNames(names);

        int [] rains={1,2,0,0,2,1};
//        int [] rains={1,2,0,1,2};
        int[] ints = new Contest194().avoidFlood(rains);
        System.out.println(Arrays.toString(ints));


    }

//    避免洪水泛滥
//    输入：rains = [1,2,0,0,2,1]
//    输出：[-1,-1,2,1,-1,-1]
//    map+treeset  treeset.higher方法返回大于这个数的最小元素 如果不存在返回Null
    public int[] avoidFlood(int[] rains) {
        int n = rains.length;
        int[] ans = new int[n];
        Map<Integer, Integer> lake2fullDay = new HashMap<>();
        TreeSet<Integer> dryDays = new TreeSet<>();
        for (int i = 0; i < n; i++){
            if (rains[i] == 0){
                dryDays.add(i);
                ans[i] = 1;
            } else {
                int lake = rains[i];
                if (lake2fullDay.containsKey(lake)){
                    // find a day in "drydays" to dry this lake.
                    Integer day = dryDays.higher(lake2fullDay.get(lake));
                    if (day == null){
                        //couldn't stop flooding.
                        return new int[]{};
                    }
                    ans[day] = lake;
                    dryDays.remove(day);
                }
                lake2fullDay.put(lake, i);
                ans[i] = -1;
            }
        }
        return ans;
    }



//    保证文件名唯一
//    输入：names = ["pes","fifa","gta","pes(2019)"]
//    输出：["pes","fifa","gta","pes(2019)"]
    public String[] getFolderNames(String[] names) {

        String [] res=new String[names.length];

        Map<String,Integer>map=new HashMap<>();

        for(int i=0;i<names.length;i++){
            String name=names[i];
//            map.put(name,map.getOrDefault(name,0)+1);
            if(!map.containsKey(names[i])){
                res[i]=name;
                map.put(name,1);
            }else{
                int count=map.get(name);
                //取出之前出现的次数
                while(map.containsKey(names[i] + "(" + count + ")")){
                    count++;
                }
                map.put(names[i] + "(" + count + ")", 1);
                map.put(names[i],map.get(name)+1);

                res[i]=names[i]+"(" + (count) + ")";
            }

        }
        return res;

    }

    public int xorOperation(int n, int start) {

        int num;
        int res=0;
        for(int i=0;i<n;i++){

            num=start+2*i;
            res^=num;

        }
        return res;

    }

}
