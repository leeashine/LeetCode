package aleetcode.contest2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Contest160 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// 11,10,00,01
		List a = circularPermutation(2, 3);
		System.out.println(a.toString());

		String []arr={"cha","r","act","ers"};
		int n=new Contest160().maxLength2(Arrays.asList(arr));
		
		int cnt=new Contest160().tilingRectangle(11, 13);
		System.out.println(cnt);
	}

	Map<Long, Integer> set = new HashMap<>();
    int res = Integer.MAX_VALUE;
    public int tilingRectangle(int n, int m) {
        if (n == m) return 1;
        if (n > m) {
            int temp = n;
            n = m;
            m = temp;
        }
        dfs(n, m, new int[n + 1], 0);
        return res;
    }
    
    private void dfs(int n, int m, int[] h, int cnt) {
        if (cnt >= res) return;
        boolean isFull = true;
        int pos = -1, minH = Integer.MAX_VALUE;
        for (int i = 1; i <= n; i++) {
            if (h[i] < m) isFull = false;
            if (h[i] < minH) {
                pos = i;
                minH = h[i];
            }
        }
        if (isFull) {
            res = Math.min(cnt, res);
            return;
        }
        
        long key = 0, base = m + 1;
        for (int i = 1; i <= n; i++) {
            key += h[i] * base;
            base *= m + 1;
        }
        if (set.containsKey(key) && set.get(key) <= cnt) return;
        set.put(key, cnt);
        
        int end = pos;
        while (end + 1 <= n && h[end + 1] == h[pos] && (end + 1 - pos + 1 + minH) <= m) end++;
        for (int j = end; j >= pos; j--) {
            int curH = j - pos + 1;
            
            int[] next  = Arrays.copyOf(h, n + 1);
            for (int k = pos; k <= j; k++) {
                next[k] += curH;
            }
            dfs(n, m, next, cnt + 1);
        }
        
    }
	
	
	
	private int result = 0;

    public int maxLength2(List<String> arr) {
        if (arr == null || arr.size() == 0) {
            return 0;
        }

        dfs(arr, "", 0);

        return result;
    }

    private void dfs(List<String> arr, String path, int idx) {
        boolean isUniqueChar = isUniqueChars(path);
        
        if (isUniqueChar) {
            result = Math.max(path.length(), result);
        }

        if (idx == arr.size() || !isUniqueChar) {
            return;
        }
        
        for (int i = idx; i < arr.size(); i++) {
            dfs(arr, path + arr.get(i), i + 1);
        }
    }

    private boolean isUniqueChars(String s) {
        Set<Character> set = new HashSet<>();

        for (char c : s.toCharArray()) {
            if (set.contains(c)) {
                return false;
            }
            set.add(c);
        }
        return true;
    }
	// arr = ["cha","r","act","ers"]
	public static int maxLength(List<String> arr) {

		int []cnt=new int[26];
		int res=0;
		StringBuilder maxS=new StringBuilder();
		for(String s:arr){
			
			for(int i=0;i<s.length();i++){
				if(cnt[s.charAt(i)-'a']==0){
					cnt[s.charAt(i)-'a']++;//第一次访问
					maxS.append(s.charAt(i));
				}else{
					//访问过
				}
			}
			
		}
		System.out.println(maxS);
		return maxS.length();
		
	}

	public List<Integer> circularPermutation2(int n, int start) {
		List<Integer> res = new ArrayList<>();
		List<Integer> tmp = oneBitDiffPermutation(n); // generate one bit diff
														// permutations

		// rotate the tmp list to make it starts from "start"
		// before: {0, 1, ..., start, ..., end}
		// after: {start, ..., end, 0, 1, ...}
		int i = 0;
		for (; i < tmp.size(); i++) {
			if (tmp.get(i) == start) {
				break;
			}
		}

		for (int k = i; k < tmp.size(); k++) {
			res.add(tmp.get(k));
		}

		for (int q = 0; q < i; q++) {
			res.add(tmp.get(q));
		}

		return res;
	}

	// generate one bit diff permutations
	// 0, 1
	// 0, 1, 11, 10
	// 000, 001, 011, 010, 110, 111, 101, 100
	public List<Integer> oneBitDiffPermutation(int n) {
		List<Integer> tmp = new ArrayList<>();
		tmp.add(0);
		if (n == 0) {
			return tmp;
		}

		for (int i = 0; i < n; i++) {
			for (int j = tmp.size() - 1; j >= 0; j--) {
				tmp.add(tmp.get(j) + (1 << i));
			}
		}

		return tmp;
	}

	public static List<Integer> circularPermutation(int n, int start) {

		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < 1 << n; ++i)
			res.add(start ^ i ^ i >> 1);
		return res;

	}

	// f(x, y) < f(x + 1, y)
	// f(x, y) < f(x, y + 1)
	public List<List<Integer>> findSolution(CustomFunction customfunction, int z) {
		List<List<Integer>> result = new ArrayList<>();
		for (int x = 1; x <= 1000; ++x) {
			int low = 1, high = 1001;
			while (low < high) {
				int mid = (low + high) / 2;
				if (customfunction.f(x, mid) < z) {
					low = mid + 1;
				} else {
					high = mid;
				}
			}
			if (customfunction.f(x, low) == z)
				result.add(Arrays.asList(x, low));

		}
		return result;
	}

	interface CustomFunction {
		// Returns positive integer f(x, y) for any given positive integer x and
		// y.
		public int f(int x, int y);

	};
}
