package aleetcode;

import java.util.Arrays;

public class Contest192 {
    public static void main(String[] args) {
//        int [] nums={2,5,1,3,4,7};
//        int n=3;
//        int[] shuffle = new Contest192().shuffle(nums, n);
//        System.out.println(Arrays.toString(shuffle));
        int [] arr={1,2,3,4,5};
        int k=2;
        int[] strongest = new Contest192().getStrongest(arr, k);
        System.out.println(Arrays.toString(strongest));

    }

//    输入：arr = [1,2,3,4,5], k = 2
//    输出：[5,1]
//    解释：中位数为 3，按从强到弱顺序排序后，数组变为 [5,1,4,2,3]。最强的两个元素是 [5, 1]。[1, 5] 也是正确答案。
//    注意，尽管 |5 - 3| == |1 - 3| ，但是 5 比 1 更强，因为 5 > 1 。
//    双指针题
    public int[] getStrongest(int[] arr, int k) {
        if (arr.length <= 1 || k >= arr.length) {
            return arr;
        }

        Arrays.sort(arr);
        int mid = arr[(arr.length - 1) / 2];
        int l = 0;
        int r = arr.length - 1;
        int[] ans = new int[k];
        int idx = 0;
        while (idx < k) {
            if (Math.abs(arr[r] - mid) >= Math.abs(arr[l] - mid)) {
                ans[idx++] = arr[r];
                r--;
            } else {
                ans[idx++] = arr[l];
                l++;
            }

        }
        return ans;

    }



//                 0 1 2 3 4 5
//    输入：nums = [2,5,1,3,4,7], n = 3
//    输出：[2,3,5,4,1,7]
    public int[] shuffle(int[] nums, int n) {
        int length=nums.length;
        int []res=new int[length];
        int offset=length/2;
        int j=0;
        for (int i = 0; i < length&&j<length; i++) {
            res[j++]=nums[i];
            res[j++]=nums[i+offset];
        }

        return res;

    }
}
