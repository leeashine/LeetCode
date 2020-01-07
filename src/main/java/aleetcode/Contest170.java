package aleetcode;

public class Contest170 {

    public static void main(String[] args) {

        String s=freqAlphabets("1326#");

        int [] A={1,3,4,8};
        int [][] queries ={{0,1},{1,2},{0,3},{3,3}};
        int [] res=xorQueries(A,queries);

        System.out.println(1^0);

    }

    //前缀亦或^ 范围查询  [2,4] ---> [1]^[4]    [0,4] ---> [4]
    //类似前缀和  [2,4] ---> [4]-[1]          [0,4] ---> [4]
    public static int[] xorQueries(int[] A, int[][] queries) {
        int[] res = new int[queries.length], q;
        for (int i = 1; i < A.length; ++i)
            A[i] ^= A[i - 1];
        for (int i = 0; i < queries.length; ++i) {
            q = queries[i];
            res[i] = q[0] > 0 ? A[q[0] - 1] ^ A[q[1]] : A[q[1]];
        }
        return res;
    }

//    Input: s = "10#11#12"
//    Output: "jkab"
//    Explanation: "j" -> "10#" , "k" -> "11#" , "a" -> "1" , "b" -> "2".

    public static String freqAlphabets(String s) {
        int n = s.length();
        String ans = "";
        for(int i = 0; i < n; ) {
            if(i < n - 2 && s.charAt(i + 2) == '#') {
                int num = Integer.parseInt(s.substring(i, i + 2));
                ans += (char)(num + 96);
                i = i + 3;
                continue;
            }
            ans += (char)(97 + s.charAt(i) - '1');
            i++;
        }
        return ans;
    }

}
