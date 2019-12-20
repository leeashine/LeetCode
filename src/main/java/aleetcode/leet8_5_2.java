package aleetcode;

public class leet8_5_2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] nums = { 1, 2, 3 };
		int[] nums2 = { 9, 6, 1, 6, 2 };
		
		movesToMakeZigzag2(nums2);
	}

	public int movesToMakeZigzag(int[] nums) {

		int n = nums.length;
		int dif = nums[0] - nums[1];

		for (int i = 1; i < n; i++) {
			

		}

		return 0;
	}
	
	
	public static int movesToMakeZigzag2(int[] A) {
        int res[] = new int[2],  n = A.length, left, right;
        for (int i = 0; i < n; ++i) {
            left = i > 0 ? A[i - 1] : 1001;
            right = i + 1 < n ? A[i + 1] : 1001;
            res[i % 2] += Math.max(0, A[i] - Math.min(left, right) + 1);
        }
        return Math.min(res[0], res[1]);
    }
}
