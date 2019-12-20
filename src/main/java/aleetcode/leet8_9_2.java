package aleetcode;

public class leet8_9_2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String s = "ghiabcdefhelloadamhelloabcdefghi";
		int ss = new leet8_9_2().longestDecomposition(s);
		System.out.println(ss);
	}


	public int longestDecomposition(String S) {
		int res = 0, n = S.length();
		String l = "", r = "";
		for (int i = 0; i < n; ++i) {
			l = l + S.charAt(i);
			r = S.charAt(n - i - 1) + r;
			if (l.equals(r)) {
				++res;
				l = "";
				r = "";
			}
		}
		return res;
	}
}
