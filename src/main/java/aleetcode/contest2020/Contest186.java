package aleetcode.contest2020;

public class Contest186 {

    public static void main(String[] args) {

        String s = "011101";
//        maxScore(s);
        int [] cardPoints={11,49,100,20,86,29,72};
        maxScore(cardPoints,4);

    }
//    可获得的最大点数
//    输入：cardPoints = [1,2,3,4,5,6,1], k = 3
//    输出：12
//    先求出前k个数的总和，然后前面去掉一个，后面加上一个，前面去掉一个，后面加上一个，求最大值
    public static int maxScore(int[] cardPoints, int k) {

        int s=0;
        int t=k;
        for (int i = 0; i < k; i++)
        {
            s+=cardPoints[i];
        }
        int max=s;
        for (int i = cardPoints.length-1; i >=cardPoints.length-k; i--)
        {
            s=s-cardPoints[--t]+cardPoints[i];//前面去掉一个，后面加上一个
            if (s>max)
            {
                max=s;
            }
        }
        return max;

    }

    //    「分割字符串的得分」为 左 子字符串中 0 的数量加上 右 子字符串中 1 的数量。
    //     理解1：统计连续0的个数，直到碰到1停止
//         理解2：类似动态规划，每次遍历都统计取最大值
    public static int maxScore(String s) {
        int res = 0, cnt1 = 0, cnt0 = 0;        //cnt1统计右边1的个数，同理cnt0左边0的个数
        for (int i = 0; i < s.length(); i++) {
            cnt1 += s.charAt(i) - '0';            //先统计1的个数
        }                                       //由于左右区域的数至少为1，所以i不能等于len-1
        for (int i = 0; i < s.length() - 1; i++) {  //点i分为左右两个区域
            if (s.charAt(i) == '0') cnt0++;      //遇到01就统计，动态更新左右区域01个数
            else cnt1--;
            res = Math.max(res, cnt0 + cnt1);
        }
        return res;
    }


}
