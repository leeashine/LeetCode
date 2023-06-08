package aleetcode.contest2020;

import java.util.*;


public class Contest195 {
    public static void main(String[] args) {

//        boolean b=new Contest195().isPathCrossing("nes");
//        System.out.println(b);

//        int [] arr={1,2,3,4,5,10,6,7,8,9};
//        int [] arr={-1,1,-2,2,-3,3,-4,4};
//        int k=3;
//        boolean b = new Contest195().canArrange(arr, k);
//        System.out.println(b);


        int [][] connections = {{0,1},{2,3},{3,6},{1,2}};

        int[][] ints = new Contest195().makeConnected(connections);
        System.out.println(Arrays.toString(ints));

    }
    public int [][] makeConnected(int[][] connections){
        int [][] res=new int[connections.length][2];
        Map<Integer,Integer> map=new HashMap();
        for (int i = 0; i < connections.length; i++) {
            map.put(connections[i][0],connections[i][1]);
        }
        int value=map.get(0);
        res[0]=connections[0];
        int i=1;
        while (map.get(value)!=null){
            res[i][0]=value;
            value=map.get(value);
            res[i][1]=value;
            i++;
        }
        return res;
    }


    //    [3,5,6,7]
//    输入：nums = [3,5,6,7], target = 9
//    输出：4
//    [3] -> 最小元素 + 最大元素 <= target (3 + 3 <= 9)
//    [3,5] -> (3 + 5 <= 9)
//    [3,5,6] -> (3 + 6 <= 9)
//    [3,6] -> (3 + 6 <= 9)
//    双指针
    public int numSubseq(int[] A, int target) {
        Arrays.sort(A);
        int res = 0, n = A.length, l = 0, r = n - 1, mod = (int)1e9 + 7;
        int[] pows = new int[n];
        pows[0] = 1;
        for (int i = 1 ; i < n ; ++i)
            pows[i] = pows[i - 1] * 2 % mod;
        while (l <= r) {
            if (A[l] + A[r] > target) {
                r--;
            } else {
                res = (res + pows[r - l++]) % mod;
            }
        }
        return res;
    }

//    输入：arr = [1,2,3,4,5,10,6,7,8,9], k = 5
//    输出：true
//    解释：划分后的数字对为 (1,9),(2,8),(3,7),(4,6) 以及 (5,10) 。
//    数组中的元素分别对K取余，存入新的数组tmp.
//    然后把 tmp 中的0取出来，若个数不为偶数，返回False.
//    首尾是否相等。
    public boolean canArrange(int[] arr, int k) {

        int [] mod=new int[k];

        for(int i:arr){
            int j=(i%k+k)%k;//负数情况
            mod[j]++;
        }
        if(mod[0]%2!=0)
            return false;
        for(int i=1;i<k/2;i++){
            if(mod[i]!=mod[k-i])
                return false;
        }
        return true;

    }

//    n s e w
//    北南东西
//    输入：path = "NES"
//    输出：false
//    解释：该路径没有在任何位置相交。
    public boolean isPathCrossing(String path) {
        Set<Point> set=new HashSet<>();
        int x=0,y=0;
        set.add(new Point(0,0));
        for(char c:path.toCharArray()){

            if(c=='N'){
                y++;
            }else if(c=='S'){
                y--;
            }else if(c=='E'){
                x++;
            }else if(c=='W'){
                x--;
            }

            if(set.contains(new Point(x,y))){
                return true;
            }else{
                set.add(new Point(x,y));
            }

        }
        return false;

    }
    class Point{
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}
