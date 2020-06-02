package aleetcode.contest2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Contest170 {

    public static void main(String[] args) {

        String s=freqAlphabets("1326#");

        int [] A={1,3,4,8};
        int [][] queries ={{0,1},{1,2},{0,3},{3,3}};
        int [] res=xorQueries(A,queries);

        List<List<String>> watchedVideos=new ArrayList();

//        new Contest170().watchedVideosByFriends2();

        Boolean b= new Contest170().isPalindrome("ddcdd");

        System.out.println(b);
    }
    public int minInsertions(String s) {
        int n = s.length();
        int[][] dp = new int[n+1][n+1];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                dp[i + 1][j + 1] = s.charAt(i) == s.charAt(n - 1 - j) ? dp[i][j] + 1 : Math.max(dp[i][j + 1], dp[i + 1][j]);
        return n - dp[n][n];
    }

    public static boolean isPalindrome(String A){

        if(A.length()==1)
            return true;

        int head=0;
        int tail=A.length()-1;
        while(head<tail){

            if(A.charAt(head)==A.charAt(tail)){
                head++;
                tail--;
                continue;
            }

            return false;

        }

        return true;
    }

//    BFS + CompareSort
//    Input: watchedVideos = [["A","B"],["C"],["B","C"],["D"]], friends = [[1,2],[0,3],[0,3],[1,2]], id = 0, level = 1
//    Output: ["B","C"]
//    Explanation:
//    You have id = 0 (green color in the figure) and your friends are (yellow color in the figure):
//    Person with id = 1 -> watchedVideos = ["C"]
//    Person with id = 2 -> watchedVideos = ["B","C"]
//    The frequencies of watchedVideos by your friends are:
//    B -> 1
//    C -> 2
    public List<String> watchedVideosByFriends2(List<List<String>> watchedVideos, int[][] friends, int id, int level) {

        int N = friends.length;
        boolean[] visited = new boolean[N]; // keeping list of visited frnds
        visited[id] = true;// i'm my own frnd

        ArrayList<Integer> q = new ArrayList<>();
        q.add(id);

        for (int k = 0; k < level; k++) {// depth less than level
            ArrayList<Integer> newQ = new ArrayList<>();
            for (int v : q) {
                for (int w : friends[v]) {// this is frnds of frnds
                    if (!visited[w]) {
                        visited[w] = true;
                        newQ.add(w);
                    }
                }
            }
            q = newQ;//change the list to own frnd of frnd
        }

        HashMap<String, Integer> freq = new HashMap<>();

        for (int person : q) {
            for (String v : watchedVideos.get(person)) {
                freq.put(v, 1 + freq.getOrDefault(v, 0));
            }
        }

        List<String> ans = new ArrayList<>(freq.keySet());

        ans.sort((a, b) -> {// custom sort
            int fa = freq.get(a);
            int fb = freq.get(b);
            if (fa != fb) {
                return fa - fb;
            } else {
                return a.compareTo(b);
            }
        });

        return ans;
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
