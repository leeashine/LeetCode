package aleetcode;

import java.util.*;

public class Contest175 {
    public static void main(String[] args) {


        TweetCounts obj=new Contest175().new TweetCounts();
        obj.recordTweet("tweet3",0);
        obj.recordTweet("tweet3",60);
        obj.recordTweet("tweet3",10);
        List<Integer> param_2 = obj.getTweetCountsPerFrequency("minute","tweet3",0,60);
        System.out.println(param_2.toString());
        char[][] seats={{'#','.','.','.','#'},
                {'.','#','.','#','.'},
                {'.','.','#','.','.'},
                {'.','#','.','#','.'},
                {'#','.','.','.','#'}};
        int i = new Contest175().new Solution().maxStudents(seats);

    }

    //1349. Maximum Students Taking Exam
    // time complexity O(n * 2 ^ (2 * m))
//    每一行的落座情况仅和上一行相关，因此用preMask表示上一行的落座情况，curMask表示当前的落座情况，递推求解即可。
    class Solution {

        public int maxStudents(char[][] seats) {
            int n = seats.length, m = seats[0].length;
            int[][] dp = new int[n + 1][1 << m];
            for(int i = n - 1; i >= 0; i--){
                for(int preMask = 0; preMask < (1 << m); preMask++){
                    int res = 0;
                    for(int curMask = 0; curMask < (1 << m); curMask++){
                        if(isValid(curMask, preMask, seats, i)){
                            res = Math.max(res, Integer.bitCount(curMask) + dp[i + 1][curMask]);
                        }
                    }
                    dp[i][preMask] = res;
                }
            }
            return dp[0][0];
        }

        private boolean isValid(int mask, int preMask, char[][] seats, int r) {
            int m = seats[0].length;
            for (int i = 0; i < m; i++) {
                if ((mask & (1 << i)) == 0) {
                    continue;
                }
                if (seats[r][i] == '#') {
                    return false;
                }
                if (i > 0 && seats[r][i - 1] == '.' && (mask & (1 << (i - 1))) != 0) {
                    return false;
                }
                if (i < m - 1 && seats[r][i + 1] == '.' && (mask & (1 << (i + 1))) != 0) {
                    return false;
                }
                if (r > 0 && i > 0 && seats[r - 1][i - 1] == '.' && (preMask & (1 << (i - 1))) != 0) {
                    return false;
                }
                if (r > 0 && i < m - 1 && seats[r - 1][i + 1] == '.' && (preMask & (1 << (i + 1))) != 0) {
                    return false;
                }
            }
            return true;
        }
    }



    class TweetCounts {

        Map<String,List<Integer>> record;
        public TweetCounts() {
            record=new HashMap<>();
        }

        public void recordTweet(String tweetName, int time) {

            List<Integer> orDefault = record.getOrDefault(tweetName, new ArrayList());
            orDefault.add(time);
            record.put(tweetName,orDefault);
        }

        //类似于时间窗口
        public List<Integer> getTweetCountsPerFrequency(String freq, String tweetName, int startTime, int endTime) {
            List<Integer> res=new ArrayList<>();
            List<Integer> list = record.get(tweetName);
            Collections.sort(list);//排好序的list  0 10 60
            int delta=60;//时间间隔   [0,60]
            if(freq.equalsIgnoreCase("minute")){
                delta=60;
            }else if(freq.equalsIgnoreCase("hour")){
                delta=3600;
            } else if(freq.equalsIgnoreCase("day")){
                delta=3600*24;
            }
            int i=(endTime-startTime)/delta;// i=1
            while(i>=0){
                //时间窗口 [0,60) [60,61)

                int cnt=0;
                for (int idx=0;idx<list.size();idx++){
                    int val = list.get(idx);
                    if(val>=startTime&&val<endTime){
                        cnt++;
                    }

                }
                res.add(cnt);
                startTime=startTime+i*delta; //0+60
                endTime=Math.min(startTime + delta*(i+1), endTime + 1);//61
                i--;
            }

            return res;

        }
    }



    //    Input: arr = [10,2,5,3]
//    Output: true
//    Explanation: N = 10 is the double of M = 5,that is, 10 = 2 * 5.
//    考虑几个0的情况
    public boolean checkIfExist(int[] arr) {

        Set set=new HashSet();
        int cn=0;
        for (int i : arr) {
            if(i!=0)
                set.add(i);
            else
                cn++;
        }
        for (int i : arr) {
            if(cn>=2)
                return true;
            if(set.contains(i*2))
                return true;
        }

        return false;

    }
//    Input: s = "bab", t = "aba"
//    Output: 1
//    Explanation: Replace the first 'a' in t with b, t = "bba" which is anagram of s.
//    计算需要调换几个字母成为和他相同的异位数 用一个数组 1.8流式编程 把数组转化为流进行操作
    public int minSteps(String s, String t) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) // count the occurrences of chars in s.
            ++count[c - 'a'];
        for (char c : t.toCharArray()) // compute the difference between s and t.
            --count[c - 'a'];
        return Arrays.stream(count).map(Math::abs).sum() / 2; // sum the absolute of difference and divide it by 2
    }

}
