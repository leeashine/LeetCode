package aleetcode.sort;

import java.util.Arrays;

public class QucikSort2 {


    public static void main(String[] args) {
        int [] arr=new int [] {10,2,4,6,7,2};
        sort(arr);
        System.out.println(Arrays.toString(arr));

    }

    public static void sort(int []arr){

        sort(arr,0,arr.length-1);

    }

    public static void sort(int [] arr,int l,int r){

        if (l >= r) {
            return;
        }
        int p=partition(arr,l,r);
        sort(arr,l,p-1);
        sort(arr,p+1,r);

    }

    public static int partition(int [] arr,int l,int r){

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

    public static void swap(int [] arr,int a,int b){
        int t=arr[a];
        arr[a]=arr[b];
        arr[b]=t;
    }


}
