package aleetcode;

import java.util.HashMap;
import java.util.Map;

public class CollectionDemo {

    public static void main(String[] args) {
        //map
        Map map1= new  HashMap();
        map1.put(1,1);
        map1.put("name","lzx");
        map1.put(3,2.3f);
        //map.get() key存在则返回value 否则返回Null
        System.out.println("map.get(): " +map1.get(1));
        System.out.println("map.get(): "+map1.get(2));

        Map<Object,Object> map2= new  HashMap();
        map2.put("name1","lzx");
        map2.put("name2","lzx");
//        map遍历
        for(Map.Entry<Object,Object> entry : map2.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }

//        1.8 map新api
//        getOrDefault
        map2.getOrDefault("name3","如果不存在就返回默认值");


//        putIfAbsent   如果传入key对应的value已经存在，就返回存在的value，不进行替换。
//        如果不存在，就添加key和value，返回null
        map2.putIfAbsent("name3","如果没有这个值就放进map");

        map2.forEach((k,v)->{
            System.out.println("map2----"+k+":"+v);
        });


    }

}
