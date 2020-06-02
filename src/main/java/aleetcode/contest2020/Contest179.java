package aleetcode.contest2020;

public class Contest179 {


    public static void main(String[] args) {

//        String s = generateTheString(4);
//        System.out.println(s);

//        int []manager ={1,2,3,4,5,6,-1};
//        int []informTime ={0,6,5,4,3,2,1};
//        int i = numOfMinutes(7,6,manager ,informTime);
//        System.out.println(i);

//        canThreePartsEqualSum(new int[]{10,-10,10,-10,10,-10,10,-10});


    }


    //领导通知时间(递归)
    private static int[] count;
    private static int sum = 0;

    public static int numOfMinutes(int n, int headID, int[] manager, int[] informTime) {
        count = new int[n];
        findNext(manager, headID, informTime);
        return sum;
    }

    public static void findNext(int[] par, int dest, int[] informTime) {
        for (int i = 0; i < par.length; i++) {
            if (par[i] == dest && count[par[i]] != 1) {
                count[par[i]] = 1;
                sum += informTime[dest];
                findNext(par, i, informTime);
            }
        }
    }


    //灯泡 当前位置及左边全开才变蓝
    public static int numTimesAllBlue(int[] light) {

        int res = 0;
        int[] count = new int[light.length];
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < light.length; i++) {

            int idx = light[i];
            count[idx - 1] = 1;
            int ment = 0;
            max = Math.max(max, idx);
            for (int j = 0; j < max; j++) {
                if (count[j] == 1)
                    ment++;
                else break;
            }
            if (ment == (max))
                res++;

        }
        return res;

    }

    public static String generateTheString(int n) {

        String res = "";
        String c = "a";
        if (n % 2 == 0) {

            for (int i = 0; i < n - 1; i++) {
                res += c;
            }
            res += "b";

        } else {
            for (int i = 0; i < n; i++) {
                res += c;
            }

        }
        return res;

    }
}
