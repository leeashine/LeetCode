package aleetcode.sort;

import java.util.Arrays;

public class InsertSort {

    public static void main(String[] args) {

        int [] arr={1};
        sort2(arr);
        System.out.println(Arrays.toString(arr));

    }

    public static void sort2(int[] arr){

        int n=arr.length;
        for(int i=1;i<n;i++){
            int e=arr[i];
            int j=i;
            for(;j>0&&arr[j-1]>e;j--){
                    arr[j]=arr[j-1];
            }
            arr[j]=e;

        }

    }
    public static void sort(int[] a)
    {
        //将a[]按升序排列
        int N=a.length;
        for (int i=1;i<N;i++)
        {
            //将a[i]插入到a[i-1]，a[i-2]，a[i-3]……之中
            for(int j=i;j>0&&(a[j]<(a[j-1]));j--)
            {
                int temp=a[j];
                a[j]=a[j-1];
                a[j-1]=temp;
            }
        }
    }

}
