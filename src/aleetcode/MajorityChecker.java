package aleetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MajorityChecker {

	public static void main(String[] args) {

		int[] arr = { 2, 2, 1, 2, 1, 2, 2, 1, 1, 2 };
		MajorityChecker majorityChecker = new MajorityChecker(arr);
		majorityChecker.query(2, 3, 2);
	}

	Map<Integer, List<Integer>> map = new HashMap<>();
	int[] nums;

	public MajorityChecker(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			if (map.containsKey(arr[i]) == false) {
				map.put(arr[i], new ArrayList<Integer>());
			}
			map.get(arr[i]).add(i);
		}
		this.nums = arr;
	}

	public static int leftBinSearch(List<Integer> arr, int target) {

		int l = 0, r = arr.size() - 1;
		while (l <= r) {
			int mid = l + (r - l) / 2; // 防止整形溢出
			if (target == arr.get(mid)) {
				return mid;
			}
			if (target > arr.get(mid)) {
				l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		return l;

	}

	public static int rightBinSearch(List<Integer> arr, int target) {

		int l = 0, r = arr.size() - 1;
		while (l <= r) {
			int mid = l + (r - l) / 2; // 防止整形溢出
			if (target == arr.get(mid)) {
				return mid;
			}
			if (target > arr.get(mid)) {
				l = mid + 1;
			} else {
				r = mid - 1;
			}
		}
		return r;

	}

	public int query(int left, int right, int threshold) {
		Random rand_gen = new Random();

		for (int i = 0; i < 20; i++) {
			int rand_index = rand_gen.nextInt(right - left + 1) + left;
			int num = this.nums[rand_index];

			int low_index = leftBinSearch(map.get(num), left);
			int high_index = rightBinSearch(map.get(num), right);

			if (high_index - low_index + 1 >= threshold) {
				// System.out.println(high_index + " " + low_index);
				return num;
			}
		}

		return -1;
	}

}
