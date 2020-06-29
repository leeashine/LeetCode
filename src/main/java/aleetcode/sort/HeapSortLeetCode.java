package aleetcode.sort;

public class HeapSortLeetCode {

    public int[] sortArray(int[] nums) {
        heapSort(nums);
        return nums;
    }
    public void maxHeapify(int[] a, int i, int heapSize) {
        int l = i * 2 + 1, r = i * 2 + 2, largest = i;
        if (l < heapSize && a[l] > a[largest]) {
            largest = l;
        }
        if (r < heapSize && a[r] > a[largest]) {
            largest = r;
        }
        if (largest != i) {
            swap(a, i, largest);
            maxHeapify(a, largest, heapSize);
        }
    }

    void buildMaxHeap(int [] nums, int len) {
        for (int i = len / 2; i >= 0; --i) {
            maxHeapify(nums, i, len);
        }
    }
    void heapSort(int [] nums) {
        int len = nums.length - 1;
        buildMaxHeap(nums, len);
        for (int i = len; i >= 1; --i) {
            swap(nums,i, 0);
            len -= 1;
            maxHeapify(nums, 0, len);
        }
    }

    private static void swap(int [] arr,int a,int b){
        int t=arr[a];
        arr[a]=arr[b];
        arr[b]=t;
    }
}
