package aleetcode;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Contest152 {

    public static void main(String[] args) {

        new Contest152().numPrimeArrangements(100);

        int [] calories={6,5,0,0};
        new Contest152().dietPlanPerformance(calories,2,1,5);

        new Contest152().canMakePaliQueries("abcda",new int[][]{{3,3,0},{1,2,0},{0,3,1},{0,3,2},{0,4,1}});

//        int i=1_000_000_007;//jdk 1.7特性
//        System.out.println(i);

//        int count=0;
//        带标签的continue,break
//        outer:
//        for(int i=3;i*i<=100;i+=2){//除了2这个偶数 素数只可能出现在奇数里
//            for(int j=3;j*j<=i;j+=2){
//                if(i%j==0)
//                    continue outer;
//            }
//            count++;
//        }
    }

    public List<Boolean> canMakePaliQueries(String s, int[][] queries) {
        List<Boolean> res = new ArrayList();

        for (int[] query : queries) {
            res.add(canMakePal(s, query[0], query[1], query[2]));
        }

        return res;
    }

    private boolean canMakePal(String s, int start, int end, int max) {
        if (max >= 13) return true;
        Set<Character> set = new HashSet();
        for (int i = start; i <= end; i++) {
            if (!set.add(s.charAt(i))) //删选出不是回文数的字母
                set.remove(s.charAt(i));
        }
        return max >= set.size()/2; //数学 换掉一半以上就可以成为回文数
    }


    public int dietPlanPerformance(int[] calories, int k, int lower, int upper) {
        int point = 0;
        for (int lo = -1, hi = 0, win = 0; hi < calories.length; ++hi) {
            win += calories[hi];
            if (hi - lo > k) {
                win -= calories[++lo];//缩小窗口
            }
            if (hi - lo < k) { continue; } // not a k sequence yet.
            if (win < lower) {
                --point ;
            }else if (win > upper) {
                ++point;
            }
        }
        return point;
    }


    public int numPrimeArrangements(int n) {
        int cnt = 1; // # of primes, first prime is 2.
        outer:
        for (int i = 3; i <= n; i += 2) { // only odd number could be a prime, if i > 2.
            for (int factor = 3; factor * factor <= i; factor += 2)//奇数只可能被奇数整除
                if (i % factor == 0)
                    continue outer;
//            System.out.println(i);
            ++cnt;
        }
        long ans = 1;
        for (int i = 1; i <= cnt; ++i) // (# of primes)!
            ans = ans * i % 1_000_000_007;
        for (int i = 1; i <= n - cnt; ++i) // (# of non-primes)!
            ans = ans * i % 1_000_000_007;
        return (int)ans;
    }

    //统计<=n的素数个数
    //令 x =2;
    //将 2x、3x、4x 直至 ax<n 的数标记为非素数
    //令 x 为下一个没有被标记为非素数的数，重复 2；直至所有的数都已尝试完毕。
    private static int generatePrimes(int n) {

        boolean [] prime=new boolean[n+1];
        for(int p=2;p*p<=n;p++){
            if(prime[p]==true){
                for (int i=p*p;i<=n;i+=p){
                    prime[i]=false;
                }
            }
        }
        int count=0;
        for(int i = 2; i <= n; i++)
        {
            if(prime[i] == true)
                count++;
        }
        return  count;

    }



}
