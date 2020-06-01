package aleetcode.contest2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Contest158 {

	public static void main(String[] args) {

		String s = "RLLLLRRRLR";

		System.out.println(balancedStringSplit(s));
	}
	
	 public List<List<Integer>> queensAttacktheKing(int[][] queens, int[] king) {
	        List<List<Integer>> result = new ArrayList<>();
	        boolean[][] seen = new boolean[8][8];
	        for (int[] queen : queens) seen[queen[0]][queen[1]] = true;
	        int[] dirs = {-1, 0, 1};
	        for (int dx : dirs) {
	            for (int dy : dirs) {
	                if (dx == 0 && dy == 0) continue;
	                int x = king[0], y = king[1];
	                while (x + dx >= 0 && x + dx < 8 && y + dy >= 0 && y + dy < 8) {
	                    x += dx;
	                    y += dy;
	                    if (seen[x][y]) {
	                        result.add(Arrays.asList(x, y));
	                        break;
	                    }
	                }
	            }
	        }
	        return result;
	    }   
	
	public static int balancedStringSplit2(String s) {

		int count=0;
		int result=0;
		for(char c:s.toCharArray()){
			
			if(c=='R'){
				count++;
			}else
				count--;
			
			if(count==0)
				result++;
			
			
		}
		
		return result;
	}
	
	
	public static int balancedStringSplit(String s) {

		int counter = 0;
		int topCounter = -1;
		Stack stack = new Stack();

		for (int i = 0; i < s.length(); i++) {

			char c=s.charAt(i);
			if(stack.empty()){
				counter++;
				stack.push(c);
			}else{
				
				if((char)stack.peek()==c)
					stack.push(c);
				else
					stack.pop();
				
			}

		}
		return counter;

	}

}
