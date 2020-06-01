package aleetcode.contest2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Contest154 {

	public static void main(String[] args) {

//		int n=new Contest154().maxNumberOfBalloons("leetcode");

		String s = "(ed(et(oc))el)";  //leetcode
		
		System.out.println(new Contest154().reverseParentheses(s));
	}
	
	public String reverseParentheses2(String s) {
        int begin = 0;
        int end;
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '(')
                begin = i;
            if(s.charAt(i) == ')'){
                end = i;
                String temp = s.substring(begin + 1, end);
                return reverseParentheses(s.substring(0, begin) + reverseString(temp) + s.substring(end + 1));
            }
        }
        return s;
    }

    String reverseString(String s){
        char[] temp = s.toCharArray();
        StringBuilder r = new StringBuilder();
        for (int i = temp.length-1; i>=0; i--)
            r.append(temp[i]);

        return r.toString();
    }
	
	public String reverseParentheses(String s) {
        int n = s.length();
        Stack<Integer> opened = new Stack<>();
        int[] pair = new int[n];
        for (int i = 0; i < n; ++i) {
            if (s.charAt(i) == '(')
                opened.push(i);
            if (s.charAt(i) == ')') {
                int j = opened.pop();
                pair[i] = j;
                pair[j] = i;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, d = 1; i < n; i += d) {
            if (s.charAt(i) == '(' || s.charAt(i) == ')') {
                i = pair[i];
                d = -d;
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

	public int maxNumberOfBalloons(String text) {
		if (text == null || text.length() == 0) return 0;
		
		int [] res=new int[26];
		for(char c:text.toCharArray()){
			
			res[c-'a']++;
			
		}
		int min=Integer.MAX_VALUE;
		
		min = Math.min(min, res['b'-'a']);
		min = Math.min(min, res['a'-'a']);
		min	= Math.min(min, res['l'-'a']/2);
		min = Math.min(min, res ['o']/2);
		min = Math.min(min, res ['n']);
	
		return min==Integer.MAX_VALUE? 0: min;
		
	}

	public List<List<Integer>> criticalConnections(int n,
			List<List<Integer>> connections) {
		int[] disc = new int[n], low = new int[n];
		// use adjacency list instead of matrix will save some memory, adjmatrix
		// will cause MLE
		List<Integer>[] graph = new ArrayList[n];
		List<List<Integer>> res = new ArrayList<>();
		Arrays.fill(disc, -1); // use disc to track if visited (disc[i] == -1)
		for (int i = 0; i < n; i++) {
			graph[i] = new ArrayList<>();
		}
		// build graph
		for (int i = 0; i < connections.size(); i++) {
			int from = connections.get(i).get(0), to = connections.get(i)
					.get(1);
			graph[from].add(to);
			graph[to].add(from);
		}

		for (int i = 0; i < n; i++) {
			if (disc[i] == -1) {
				dfs(i, low, disc, graph, res, 0);
			}
		}
		return res;
	}

	int time = 0; // time when discover each vertex

	private void dfs(int u, int[] low, int[] disc, List<Integer>[] graph,
			List<List<Integer>> res, int pre) {
		disc[u] = low[u] = ++time; // discover u
		for (int j = 0; j < graph[u].size(); j++) {
			int v = graph[u].get(j);
			if (v == pre) {
				continue; // if parent vertex, ignore
			}
			if (disc[v] == -1) { // if not discovered
				dfs(v, low, disc, graph, res, u);
				low[u] = Math.min(low[u], low[v]);
				if (low[v] > disc[u]) {
					// u - v is critical, there is no path for v to reach back
					// to u or previous vertices of u
					res.add(Arrays.asList(u, v));
				}
			} else { // if v discovered and is not parent of u, update low[u],
						// cannot use low[v] because u is not subtree of v
				low[u] = Math.min(low[u], disc[v]);
			}
		}
	}

}
