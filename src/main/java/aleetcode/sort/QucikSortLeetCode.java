package aleetcode.sort;

import java.util.Arrays;

/**
 * 双路快排
 */
public class QucikSortLeetCode {


    public static void main(String[] args) {
        int[] arr = new int[]{10, 2, 4, 6, 7, 2, 21, 18, 29, 2};
        sort(arr);
        System.out.println(Arrays.toString(arr));

    }

    public static void sort(int[] arr) {

        sort(arr, 0, arr.length - 1);

    }

    public static void sort(int[] arr, int l, int r) {

        if (l >= r) {
            return;
        }
        int p = partition(arr, l, r);
        sort(arr, l, p - 1);
        sort(arr, p + 1, r);

    }

    /**
     * [l+1,i) (j,r]
     */
    public static int partition(int[] arr, int l, int r) {

        swap(arr, l, (int) (Math.random() * (r - l + 1)) + l);
        int v = arr[l];
        int i = l + 1;
        int j = r;
        while (true) {
            while (i <= r && arr[i] < v) i++;
            while (j >= l + 1 && arr[j] > v) j--;
            if (i > j) break;
            swap(arr, i, j);
            i++;
            j--;
        }
        swap(arr, l, j);
        return j;
    }

    public static void swap(int[] arr, int a, int b) {
        int t = arr[a];
        arr[a] = arr[b];
        arr[b] = t;
    }

    public static void swap2(int[] arr, int i, int j) {
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    /**
     * 原始的快排 [l+1,j] [j+1,i)
     *  ==> j=l
     *  for(i=l+1 ; i<=r ;i++)
     * 对有序的也要退化成O(n2)==>解决: 随机vs
     * 对于大量数据都相同的也要退化成O(n2) ==>解决：双路快排
     */
    public static int partitionSimple(int[] arr, int l, int r) {
        int v = arr[l];
        int j = l;
        for (int i = l + 1; i <= r; i++) {
            if (arr[i] < v) {
                swap(arr, i, j + 1);
                j++;
            }
        }
        swap(arr, l, j);
        return j;
    }


}
