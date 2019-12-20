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

        map1.putIfAbsent(4,2);


    }

}
