package aleetcode.Contest2020;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Contest177 {

    public static void main(String[] args) {

//        int days=daysBetweenDates("1971-06-29","2010-09-23");
//        System.out.println(days);
//
//        boolean b = validateBinaryTreeNodes(4, new int[]{1, -1, 3, -1}, new int[]{2, 3, -1, -1});
//        System.out.println(b);

//        int res = getRes(123);
//        System.out.println(res);
        int[] ints = closestDivisors(1);//37 27
//        System.out.println(Arrays.toString(ints));


//        int[] digits = {8,6,7,1,0};
        int[] digits = {0,0,0,0,0,0};
        String s = largestMultipleOfThree(digits);
        System.out.println(s);

    }

    public static String largestMultipleOfThree(int[] digits) {

        int [] cnt=new int [10];
        int [] modulo=new int [3];
        int sum =0;
        for (int i :digits) {

            ++cnt[i];
            ++modulo[i%3];
            sum+=i;

        }
        //               要删除的个数
        int remove_mod=0,rest=0;
        if(sum%3==1){//余1 删除一个余1的或者删除2个余2的
            if(modulo[1]>=1){
                remove_mod=1;
                rest=1;
            }else {
                remove_mod=2;
                rest=2;
            }
        }else if(sum%3==2){//余2 删除2个余1  或者1个余2的

            if(modulo[2]>=1){
                remove_mod=2;
                rest=1;
            }else{
                remove_mod=1;
                rest=2;
            }


        }

        String ans="";
        for(int i=0;i<=9;i++){

            for(int j=0;j<cnt[i];j++){
                //跳过（删除）最小的若干数
                if(rest>0&&remove_mod==i%3){
                    rest--;
                }else{
                    ans+=i+"";
                }
            }
        }
        if(ans.length()>0&&ans.charAt(ans.length()-1)=='0'){
            ans="0";
        }

        return new StringBuilder(ans).reverse().toString();


    }

    //求最接近的因数
    //1,sqrt(n)范围内因数最大时，与其对称的sqrt(n),n范围内因数最小
    //因此问题实质时求1,sqrt(n)范围内最大因数
    public static int[] closestDivisors2(int x) {
        for (int a = (int)Math.sqrt(x + 2); a > 0; --a) {
            if ((x + 1) % a == 0)
                return new int[]{a, (x + 1) / a};
            if ((x + 2) % a == 0)
                return new int[]{a, (x + 2) / a};
        }
        return new int[]{};
    }

    public static  int [] getRes(int num){

        int []arr=new int[3];
        for (int i = (int)Math.sqrt(num); i > 0 ; i--) {
            int div2=num/i;
            if(num%i==0){
                arr[0]=i;
                arr[1]=div2;
                arr[2]=div2-i;
                break;
            }
        }
        return arr;
    }

    public static int[] closestDivisors(int num) {

        int [] res=new int[2];
//        int [] res1 = getRes(num);
        int [] res2 = getRes(num + 1);
        int [] res3 = getRes(num + 2);

       if(res2[2]<=res3[2]){
            res[0]=res2[0];
            res[1]=res2[1];
        }else{
            res[0]=res3[0];
            res[1]=res3[1];
        }


        return res;

    }

    //验证二叉树 根具有唯一性
//    对于一个包含 n 个节点 m 条边的无向图，如果它是一棵树，那么必须满足以下三个条件中的两个：
//    m = n - 1；
//    该无向图连通；
//    该无向图无环。
    public static boolean validateBinaryTreeNodes(int n, int[] leftChild, int[] rightChild) {
        //存放所有节点的入度 入度为0代表根节点
        int [] arr=new int [n];
        for(int i=0;i<n;i++){

            if(leftChild[i]!=-1)
                ++arr[leftChild[i]];
            if(rightChild[i]!=-1)
                ++arr[rightChild[i]];

        }

        //任意选一个根节点
        int root=-1;
        for (int i = 0; i < n; i++) {
            if(arr[i]==0){
                root=i;
                break;
            }
        }
        if(root==-1){
            return false;
        }

        Set<Integer> seen=new HashSet<>();

        Queue<Integer> queue=new LinkedList<>();

        seen.add(root);
        queue.add(root);
//        BFS
        while(!queue.isEmpty()){

            int u=queue.poll();//出队列（队首）
            if(leftChild[u]!=-1){
                if(seen.contains(leftChild[u]))
                    return false;
                seen.add(leftChild[u]);
                queue.add(leftChild[u]);
            }
            if(rightChild[u]!=-1){
                if(seen.contains(rightChild[u]))
                    return false;
                seen.add(rightChild[u]);
                queue.add(rightChild[u]);
            }

        }

        return seen.size()==n;

    }
    public static boolean validateBinaryTreeNodes2(int n, int[] leftChild, int[] rightChild) {
        //存放所有节点的入度 入度为0代表根节点
        int [] arr=new int [n];
        for(int i=0;i<n;i++){

            if(leftChild[i]!=-1)
                ++arr[leftChild[i]];
            if(rightChild[i]!=-1)
                ++arr[rightChild[i]];

        }

        //任意选一个根节点
        int root=-1;
        for (int i = 0; i < n; i++) {
            if(arr[i]==0){
                root=i;
                break;
            }
        }
        if(root==-1){
            return false;
        }

        Set<Integer> seen=new HashSet<>();
        return dfs(root,n,leftChild, rightChild, seen)&&seen.size()==n;


    }
    //DFS
    public static boolean dfs(int val, int n, int[] l, int[] r, Set seen) {
        if(val >= n || val == -1) {
            return true;
        }

        if(seen.contains(val)) {
            return false;
        }

        seen.add(val);
        boolean res1 = dfs(l[val], n, l, r, seen);
        boolean res2 = dfs(r[val], n, l, r, seen);

        return res1&res2;
    }

    //java8计算两个日期之间的天数
    public static int daysBetweenDates(String date1, String date2) {

//        2020-01-15
        int year1=Integer.parseInt( date1.substring(0,4) );
        int year2=Integer.parseInt( date2.substring(0,4) );
        int month1=Integer.parseInt( date1.substring(5,7) );
        int month2=Integer.parseInt( date2.substring(5,7) );
        int day1=Integer.parseInt( date1.substring(8,10) );
        int day2=Integer.parseInt( date2.substring(8,10) );

        LocalDate d1 = LocalDate.of(year1, month1, day1);
        LocalDate d2 = LocalDate.of(year2, month2, day2);

//        return Math.abs((int)( d1.until(d2, ChronoUnit.DAYS)));
        return Math.abs((int)ChronoUnit.DAYS.between(d2, d1));
    }
}
