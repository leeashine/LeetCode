package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		
		Integer [] arr={2, 2, 1, 2, 1, 2, 2, 1, 1, 2};
		int []arr2={1,1,2,1,2,1,2,2,1,1,1};
		SegmentTree<Integer> segTree=new SegmentTree<Integer>(arr, (a,b)-> -1 );

		System.out.println(segTree);
//		
	
		
//		System.out.println(segTree.query(2, 4));
		
//		SegmentTree<Integer> segTree=new SegmentTree<Integer>(arr, (a,b)-> a+b );
		
		
//		Test test=new Test();
//		int a=test.majorityElement(1,6);
//		System.out.println(a);
		
		
		
		Map map=new HashMap();
		
//		//getOrDefault ���û�����key,�ͷ���Ĭ��ֵ���ڶ�������
//		String a=map.getOrDefault("a", "").toString();
//		System.out.println("map.getOrDefault(): "+a);
//		//putIfAbsent ���key����Ӧ�ĵ�value�����ھͷ������ֵ
//		map.putIfAbsent("a", "123");
//		map.putIfAbsent("a", "bbb");
//		System.out.println("map.get(): "+map.get("a"));
		
		// һ������д
//		list = map.get("list-1");
//		if (list == null) {
//		    list = new LinkedList<>();
//		    map.put("list-1", list);
//		}
//		list.add("one");
//		
//		// ʹ�� computeIfAbsent ��������д
//		list = map.computeIfAbsent("list-1", k -> new ArrayList<>());
//		list.add("one");
		
		
	}
	

}
