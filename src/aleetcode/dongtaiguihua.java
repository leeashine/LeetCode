package aleetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class dongtaiguihua {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String [] req_skills=new String []{"java","nodejs","reactjs"};
		List<List<String>> people=new ArrayList<List<String>>();
		List<String> s1=new ArrayList<String>();
		s1.add("java");
		List<String> s2=new ArrayList<String>();
		s2.add("nodejs");
		List<String> s3=new ArrayList<String>();
		s3.add("nodejs");
		s3.add("reactjs");
		
		people.add(s1);
		people.add(s2);
		people.add(s3);
		
		smallestSufficientTeam(req_skills, people);
//		|异或运算符
//		System.out.println(10|8);
//		<<位运算符  1<<8  2的8次方
		int a=Integer.parseInt("09");
		System.out.println(a);
		
		
		
	}
	
	
	
	public static int[] smallestSufficientTeam(String[] req_skills, List<List<String>> people) {
        int ns = req_skills.length, np = people.size();
        HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < ns; ++i) 
        	map.put(req_skills[i], i);
        List<Integer>[] suff = new List[1 << ns];
        suff[0] = new ArrayList<>();
        for (int i = 0; i < np; ++i) {
            int skill = 0;
            for (String s : people.get(i)) 
            	skill |= (1 << map.get(s));
            for (int prev = 0; prev < suff.length; ++prev) {
                if (suff[prev] == null) 
                	continue;
                int comb = prev | skill;
                if (suff[comb] == null || suff[prev].size() + 1 < suff[comb].size()) {
                    suff[comb] = new ArrayList<>(suff[prev]);
                    suff[comb].add(i);
                }
            }
        }
        List<Integer> res = suff[(1 << ns) - 1];
        int[] arr = new int[res.size()];
        for (int i = 0; i < arr.length; ++i) 
        	arr[i] = res.get(i);
        return arr;
    }
}
