package aleetcode.contest2019;

import java.util.Arrays;

public class MaximumNestingDepthofTwoValidParenthesesStrings {

	public static void main(String[] args) {
		
		String seq = "(()())";
		String seq2="()(())()";
		
//		System.out.println(3&1);
		
		int [] res=maxDepthAfterSplit4(seq2);
		System.out.println(Arrays.toString(res));
	}
	 
	static int [] maxDepthAfterSplit4(String seq) {
		int n = seq.length(), res[] = new int[n];
        int level = 0, index = 0;
        while(index < seq.length()){
            if(seq.charAt(index) =='(')
                res[index] = ++level%2;
            else
                res[index] = level--%2;
            index++;
        }
        return res;
    }
	
	public static int[] maxDepthAfterSplit(String seq) {
		int n = seq.length(), res[] = new int[n];
        for (int i = 0; i < n; ++i)
            res[i] = seq.charAt(i) == '(' ? i & 1 : (1 - i & 1);
        return res;
    }
	
	public static int[] maxDepthAfterSplit2(String seq) {
        int A = 0, B = 0, n = seq.length();
        int[] res = new int[n];
        for (int i = 0; i < n; ++i) {
            if (seq.charAt(i) == '(') {
                if (A < B) {
                    ++A;
                } else {
                    ++B;
                    res[i] = 1;
                }
            } else {
                if (A > B) {
                    --A;
                } else {
                    --B;
                    res[i] = 1;
                }
            }
        }
        return res;
    }

	
	public static int[] maxDepthAfterSplit3(String seq) {
        int depth = 0, cur = 0, n = seq.length();
        for (int i = 0; i < n; ++i) {
            cur +=  seq.charAt(i) == '(' ?  1 : -1;
            depth = Math.max(depth, cur);
        }
        int[] res = new int[n];
        for (int i = 0; i < n; ++i) {
            if (seq.charAt(i) == '(') {
                if (++cur > depth / 2)
                    res[i] = 1;
            } else {
                if (cur-- > depth / 2)
                    res[i] = 1;
            }
        }
        return res;
    }
	
}
