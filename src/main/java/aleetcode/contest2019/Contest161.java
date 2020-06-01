package aleetcode.contest2019;

import java.util.HashMap;
import java.util.Map;

public class Contest161 {

	public static void main(String[] args) {
		//s1 = "xxyyxyxyxx", s2 = "xyyxyxxxyx"
		
		//nums = [1,1,2,1,1], k = 3
		int [] nums={1,1,2,1,1};
		int k=3;
		new Contest161().numberOfSubarrays(nums, k);
		
		int []A  = {1,2,1,2,3};
		int K = 2;
		//Subarrays formed with exactly 2 different integers: [1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2].
		new Contest161().subarraysWithKDistinct(A, K);
		
		int [] tree ={3,3,3,1,2,1,1,2,3,3,4};
		totalFruit(tree);
	}
	
	public static int totalFruit(int[] tree) {
        // track last two fruits seen
        int lastFruit = -1;
        int secondLastFruit = -1;
        int lastFruitCount = 0;
        int currMax = 0;
        int max = 0;
        
        for (int fruit : tree) {
        	if(fruit==lastFruit||fruit==secondLastFruit)
        		currMax=max++;
        	else 
        		currMax=lastFruitCount+1;//���һ��ˮ��������+��ˮ������(1)
        	
        	if(fruit==lastFruit)
        		lastFruitCount++;
        	else
        		lastFruitCount=1;
        	
        	if(fruit!=lastFruit){
        		secondLastFruit=lastFruit;
        		lastFruit=fruit;
        	}
        	
            max = Math.max(max, currMax);
        }
        
        return max;
    }
	
	
	
	public int subarraysWithKDistinct(int[] A, int K) {
        return atMostK(A, K) - atMostK(A, K - 1);
    }
    int atMostK(int[] A, int K) {
        int i = 0, res = 0;
        Map<Integer, Integer> count = new HashMap<>();
        for (int j = 0; j < A.length; ++j) {
            if (count.getOrDefault(A[j], 0) == 0) K--;
            count.put(A[j], count.getOrDefault(A[j], 0) + 1);
            while (K < 0) {
                count.put(A[i], count.get(A[i]) - 1);
                if (count.get(A[i]) == 0) K++;
                i++;
            }
            res += j - i + 1;
        }
        return res;
    }
	
	public int numberOfSubarrays(int[] nums, int k) {
        return atMost(nums, k) - atMost(nums, k - 1);
    }

    public int atMost(int[] A, int k) {
        int res = 0, i = 0, n = A.length;
        for (int j = 0; j < n; j++) {
            k -= A[j] % 2;
            while (k < 0)
                k += A[i++] % 2;
            res += j - i + 1;//����
        }
        return res;
    }
	
	public int minimumSwap(String s1, String s2) {
        int x1 = 0; // number of 'x' in s1 (skip equal chars at same index)
		int y1 = 0; // number of 'y' in s1 (skip equal chars at same index)
		int x2 = 0; // number of 'x' in s2 (skip equal chars at same index)
		int y2 = 0; // number of 'y' in s2 (skip equal chars at same index)

        for(int i = 0; i < s1.length(); i ++){
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(i);
            if(c1 == c2){ // skip chars that are equal at the same index in s1 and s2
                continue;
            }
            if(c1 == 'x'){
                x1 ++;
            }else{
                y1 ++;
            }
            if(c2 == 'x'){
                x2 ++;
            }else{
                y2 ++;
            }
        } // end for
        
        // After skip "c1 == c2", check the number of  'x' and 'y' left in s1 and s2.
        if((x1 + x2) % 2 != 0 || (y1 + y2) % 2 != 0){
            return -1; // if number of 'x' or 'y' is odd, we can not make s1 equals to s2
        }
        
        int swaps = x1 / 2 + y1 / 2 + (x1 % 2) * 2;
        // Cases to do 1 swap:
        // "xx" => x1 / 2 => how many pairs of 'x' we have ?
        // "yy" => y1 / 2 => how many pairs of 'y' we have ?
        // 
        // Cases to do 2 swaps:
        // "xy" or "yx" =>  x1 % 2
                 
        return swaps;        
    } 

}
