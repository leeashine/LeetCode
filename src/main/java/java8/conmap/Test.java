package java8.conmap;

import java.util.HashMap;
import java.util.Map;

public class Test {

    public static void main(String[] args) {

        //16*0.75=12会扩容
        Map<Integer, Integer> map = new HashMap<>();

        map.put(1, 1);
        map.put(2, 1);
        map.put(3, 1);
        map.put(4, 1);
        map.put(5, 1);
        map.put(6, 1);
        map.put(7, 1);
        map.put(8, 1);
        map.put(9, 1);
        map.put(10, 1);
        map.put(11, 1);
        map.put(12, 1);
        map.put(13, 1);
        map.put(14, 1);
        map.put(15, 1);
        map.put(16, 1);
        map.put(17, 1);
        map.put(18, 1);




    }

}
