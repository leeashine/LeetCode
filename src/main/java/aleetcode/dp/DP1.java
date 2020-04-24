package aleetcode.dp;
//5. Longest Palindromic Substring
public class DP1 {

//动态规划经典问题：
//    1：走台阶问题  每次只能上1到2个台阶 问走到10层台阶有多少种走法
//    2：找零钱问题
    public static void main(String[] args) {

        int [] A={1,2,5,10};
        new DP1().zhaolin1(50,A);

        System.out.println(new DP1().zhaoling2(A,50));


    }

    public int zoutaijie(int n) {
        if(n<=2)
            return n;
        int f = 1%1000000007;
        int s = 2%1000000007;
        int t = 0;
        for(int i=3;i<=n;i++){
            t = (f+s)%1000000007;
            f = s;
            s = t;
        }
        return t;
    }

//    解析：设dp[n][m]为使用前n种货币凑成的m的种数，那么就会有两种情况：
//     使用第n种货币：dp[n-1][m]+dp[n-1][m-peney[n]]
//     不用第n种货币：dp[n-1][m]，为什么不使用第n种货币呢，因为penney[n]>m。
//     这样就可以求出当m>=penney[n]时 dp[n][m] = dp[n-1][m]+dp[n][m-peney[n]]，否则，dp[n][m] = dp[n-1][m]
    public int zhaoling2(int[] penny, int aim) {
        int n=penny.length;
        if(n==0||penny==null||aim<0){
            return 0;
        }
        int[][] dp = new int[n][aim+1];
        for(int i=0;i<n;i++){
            dp[i][0] = 1;
        }
        for(int i=1;penny[0]*i<=aim;i++){
            dp[0][penny[0]*i] = 1;
        }
        for(int i=1;i<n;i++){
            for(int j=0;j<=aim;j++){
                if(j>=penny[i]){
                    dp[i][j] = dp[i-1][j]+dp[i][j-penny[i]];
                }else{
                    dp[i][j] = dp[i-1][j];
                }
            }
        }
        return dp[n-1][aim];
    }


    public int zhaolin1(int money,int[]A){
        int dp[] = new int[money+1]; // dp[j] 表示 j元钱的零钱的组合方式
        dp[0] = 1;
        for(int i = 0;i<A.length;i++){
            for(int j = A[i];j<= money;j++){
                dp[j] = (dp[j] + dp[j -A[i]])%1000000007; // 面值j的零钱可以写出：j = A[i] + (j - A[i]) 求出所有组合方式就是答案
            }
        }
       return dp[money];
    }
}
