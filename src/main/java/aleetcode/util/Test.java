package aleetcode.util;

import java.util.*;

public class Test {

    public static void main(String[] args) {

        Integer[] arr = {2, 2, 1, 2, 1, 2, 2, 1, 1, 2};
        int[] arr2 = {1, 1, 2, 1, 2, 1, 2, 2, 1, 1, 1};
        SegmentTree<Integer> segTree = new SegmentTree<Integer>(arr, (a, b) -> -1);

        System.out.println(segTree);
//		


//		System.out.println(segTree.query(2, 4));

//		SegmentTree<Integer> segTree=new SegmentTree<Integer>(arr, (a,b)-> a+b );


//		Test test=new Test();
//		int a=test.majorityElement(1,6);
//		System.out.println(a);


        Map map = new HashMap();

//		//getOrDefault 如果没有这个key,就返回默认值即第二个参数
//		String a=map.getOrDefault("a", "").toString();
//		System.out.println("map.getOrDefault(): "+a);
//		//putIfAbsent 如果key所对应的的value不存在就放入这个值
//		map.putIfAbsent("a", "123");
//		map.putIfAbsent("a", "bbb");
//		System.out.println("map.get(): "+map.get("a"));

        // 一般这样写
//		list = map.get("list-1");
//		if (list == null) {
//		    list = new LinkedList<>();
//		    map.put("list-1", list);
//		}
//		list.add("one");
//		
//	    使用 computeIfAbsent 可以这样写
//		如果key对应的value不存在就重新new 一个list放进去
        List list = new ArrayList();
        list = (List) map.computeIfAbsent("list-1", k -> new ArrayList<>());
        list.add("one");


    }


}
