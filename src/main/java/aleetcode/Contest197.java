package aleetcode;

public class Contest197 {
    public static void main(String[] args) {

    }
    //好数对的数目
//    输入：nums = [1,2,3,1,1,3]
//    输出：4
//    解释：有 4 组好数对，分别是 (0,3), (0,4), (3,4), (2,5) ，下标从 0 开始

    public int numIdenticalPairs(int[] nums) {
        int res=0;
        for (int i = 0; i < nums.length-1; i++) {
            for (int j = i+1; j < nums.length; j++) {

                if(nums[i]==nums[j])
                  res++;

            }
        }
        return res;
    }
}
