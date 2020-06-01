package aleetcode.contest2019;

import java.util.HashMap;
import java.util.Map;

public class leet7_24 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		[[1,2],[2,1],[3,4],[5,6]]
//		1
		
	}

	public int numEquivDominoPairs(int[][] dominoes) {
        int cnt = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (int[] a : dominoes) {
            int max = Math.max(a[0], a[1]), min = Math.min(a[0], a[1]);
            int key = min * 10 + max;
//            int pairs = map.getOrDefault(key, 0);
//            cnt += pairs;
//            map.put(key, 1 + pairs);
                               
        }
        return cnt;
    }
	
}
