package aleetcode.contest2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Contest159 {

	public static void main(String[] args) {

		String[] folder = { "/a", "/a/b", "/c/d", "/c/d/e", "/c/f" };

		new Contest159().removeSubfolders(folder);

		String s = "WWEQERQWQWWRWWERQWEQ";

		System.out.println(balancedString(s));
	}

	public static int balancedString(String s) {
        int[] count = new int[26];
        int n = s.length(), res = n, i = 0, k = n / 4;
        for (int j = 0; j < n; ++j) {
            ++count[s.charAt(j)-'A'];
        }
        for (int j = 0; j < n; ++j) {
            --count[s.charAt(j)-'A'];
            while (i < n && count['Q'-'A'] <= k && count['W'-'A'] <= k && count['E'-'A'] <= k && count['R'-'A'] <= k) {
                res = Math.min(res, j - i + 1);
                ++count[s.charAt(i++)-'A'];
            }
        }
        return res;
    }

	public List<String> removeSubfolders(String[] folder) {

		Arrays.sort(folder, Comparator.comparing(s -> s.length()));

		List<String> seen = new ArrayList<String>();

		outer: for (String s : folder) {

			for (int i = 2; i < s.length(); i++) {

				if (s.charAt(i) == '/' && seen.contains(s.substring(0, i)))
					continue outer;
				seen.add(s);
			}

		}

		return seen;

	}

}
