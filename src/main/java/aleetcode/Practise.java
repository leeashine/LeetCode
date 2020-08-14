package aleetcode;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class Practise {

    public static void main(String[] args) {
//        int [] arr={1,3,7,5,2};
//        new Practise().quickSort(arr);
//        System.out.println(Arrays.toString(arr));

//        Stack<Integer> stack=new Stack<>();
//        stack.push(1);
//        stack.push(5);
//        stack.push(6);
//        stack.push(4);
//        stack.push(2);
//
//        stackSort(stack);

//        7,  3
//        1 1 6
//        1 2 5
//        count(7,3);
//        int gcd = gcd(3, 6);
//        System.out.println(gcd);

        String s="xcopy /s \"C:\\program files\" \"d:\"";
    }

    public void jiexi(String str){

        StringBuffer sb = new StringBuffer();
        int len = 0;
        int quotaNum = 0;
        for (int i = 0; i < str.length(); i++){
            //去除双引号
            if (str.charAt(i) == '\"'){
                quotaNum++;
                continue;
            }
            if (str.charAt(i) != ' '){
                sb.append(str.charAt(i));
            } else if (quotaNum % 2 == 0){
                //遇到新单词
                sb.append('\n');
                len++;
            }else {
                //引号里的空格
                sb.append(' ');
            }
        }
        System.out.println(len+1);
        System.out.println(sb.toString());

    }

    //堆翻转 堆倒置
    void stackReverse(Stack<Integer> stack){

        if(stack.empty()){
            return ;
        }
        int last=removeLast(stack);
        stackReverse(stack);//递归操作
        stack.push(last);

    }

    int removeLast(Stack<Integer> stack){

        int value=stack.pop();
        if(stack.isEmpty()){
            return value;
        }else{
            int last=removeLast(stack);
            stack.push(value);
            return last;
        }


    }

    //二进制数1的个数
    int numOfBit1(int a)
    {
        int cnt = 0;
        while(a != 0)
        {
            a &= a - 1;    //将最右边的1置为0；正负都可计算，负数是按照补码计算的，最后的符号位也被统计
            ++cnt;
            // a|=a-1;//统计的是0的个数
        }
        return cnt;
    }

    //最小公倍数就是两数之积/最大公约数
    //最大公约数
    public static int gcd(int a,int b){
        return b == 0 ? a : gcd(b, a % b);
    }

    //M个苹果放入N个盘子里的分法种数
    public static int countDp(int m,int n){

        int [][]dp=new int[m+1][n+1];
        for(int i=0;i<=m;i++){
            dp[i][1]=1;
            dp[i][0]=1;
        }
        for(int j=0;j<=n;j++){
            dp[1][j]=1;
            dp[0][j]=1;
        }
        //i个苹果 放到j个盘子里
        for(int i=2;i<=m;i++){
            for (int j=2;j<=n;j++){
                if(i>=j){
                    //如果每个盘里都有苹果
                    //       每个盘子都有苹果 + 有一个盘子没有苹果
                    dp[i][j]=dp[i-j][j]+dp[i][j-1];
                }else{
                    //注定有盘子空着 因为无关顺序 所以i个苹果放到i个盘子里
                    dp[i][j]=dp[i][j-1];
                }
            }
        }
        return dp[m][n];

    }

    //M个盘子放n个苹果题
    public static int count(int m,int n)
    {
        if(m<0||n<=0)
            return 0;
        //细分到苹果数为一或盘子数为一的情况返回一
        if(m==1||n==1||m==0)
            return 1;
        //将此事件无线细分
        return count(m,n-1)+count(m-n,n);
    }

    //567123
    //旋转数组 找到他的最小那个数的位置
    public int sort(int [] arr){

        int l=0;
        int r=arr.length-1;
        while(l<r){
            int mid=l+(r-l)/2;
            if(arr[mid]>arr[r]){
                l=mid+1;
            }else if(arr[mid]<arr[r]){
                r=mid;
            }else{
                //如果右半边数字都相同 需要一次次比较
                r--;
            }

        }
        return arr[l];
    }

    public static void stackSort(Stack<Integer> stack){
        Stack<Integer> tmp=new Stack<>();
        while(!stack.empty()){
            int cur=stack.pop();
            while(!tmp.empty()&&tmp.peek()>cur){
                stack.push(tmp.pop());
            }
            tmp.push(cur);
        }
        while(!tmp.empty()){
            stack.push(tmp.pop());
        }
    }


    public void quickSort(int [] arr){

        quickSort(arr,0,arr.length-1);

    }
    public void quickSort(int[] arr,int l,int r){

        if(l>=r)
            return;

        int p=partition(arr,l,r);

        quickSort(arr,l,p-1);
        quickSort(arr,p+1,r);

    }

    private int partition(int[] arr, int l, int r) {

        swap(arr,l,(int)(Math.random()*(r-l+1))+l);
        int v=arr[l];
        int i=l+1;
        int j=r;
        while(true){

            while(i<=r&&arr[i]<v) i++;
            while(j>=l+1&&arr[j]>v) j--;
            if(i>j) break;
            swap(arr,i,j);
            i++;
            j--;
        }
        swap(arr,l,j);

        return j;

    }
    private void swap(int []arr,int i,int j){

        int tmp=arr[i];
        arr[i]=arr[j];
        arr[j]=tmp;

    }
}
