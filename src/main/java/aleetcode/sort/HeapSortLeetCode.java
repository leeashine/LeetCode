package aleetcode.sort;

public class HeapSortLeetCode {

    public int[] sortArray(int[] nums) {
        heapSort(nums);
        return nums;
    }
    void maxHeapify(int[] nums, int i, int len) {
        for (; (i << 1) + 1 <= len;) {
            int lson = (i << 1) + 1;
            int rson = (i << 1) + 2;
            int large;
            if (lson <= len && nums[lson] > nums[i]) {
                large = lson;
            }
            else {
                large = i;
            }
            if (rson <= len && nums[rson] > nums[large]) {
                large = rson;
            }
            if (large != i) {
                swap(nums,i, large);
                i = large;
            }
            else break;
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
