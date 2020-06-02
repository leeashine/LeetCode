package aleetcode.contest2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class Contest176 {
    public static void main(String[] args) {


        ProductOfNumbers productOfNumbers=new Contest176().new ProductOfNumbers();
        productOfNumbers.add(1);
        productOfNumbers.add(2);
        productOfNumbers.add(3);
        productOfNumbers.add(4);
        int numbersProduct = productOfNumbers.getProduct(3);
//        System.out.println(numbersProduct);

        int [][]events={{1, 2},{1, 2},{3, 3},{1, 5},{1, 5}};
        int i = maxEvents(events);
        System.out.println(i);


    }

    //贪婪算法 会议时间最优安排
    public static int maxEvents(int[][] A) {
        PriorityQueue<Integer> pq = new PriorityQueue<Integer>();
        Arrays.sort(A, (a, b) -> Integer.compare(a[0], b[0]));
        int i = 0, res = 0, n = A.length;
        for (int d = 1; d <= 100000; ++d) {
            while (!pq.isEmpty() && pq.peek() < d)
                pq.poll();
            while (i < n && A[i][0] == d)
                pq.offer(A[i++][1]);
            if (!pq.isEmpty()) {
                pq.poll();
                ++res;
            }
        }
        return res;
    }

    /**
     * 前缀乘法 类似于前缀和
     */
    class ProductOfNumbers {

        ArrayList<Integer> A;
        public ProductOfNumbers() {
            add(0);
        }

        public void add(int a) {
            if (a > 0)
                A.add(A.get(A.size() - 1) * a);//添加前缀乘
            else if( a == 0 ){
                A = new ArrayList();//遇到0前面的结果肯定都为0 重新开一个list
                A.add(1);
            }
        }

        public int getProduct(int k) {
            int n = A.size();
            return k < n ? A.get(n - 1) / A.get(n - k - 1)  : 0; //求区间乘积[l,r] 就是用 a[r]÷a[l] 0对应哪个重新开list区间对不上
        }
    }


    //梯形轮廓
    public int countNegatives(int[][] grid) {

        int m = grid.length, n = grid[0].length, r = m - 1, c = 0, cnt = 0;

        while(r>=0&&c<=n){

            if(grid[r][c]<0){

                r--;
                cnt+=n-c;

            }else{
                c++;
            }


        }


        return cnt;


    }
}
