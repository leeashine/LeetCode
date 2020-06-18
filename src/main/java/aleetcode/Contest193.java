package aleetcode;

import java.util.*;

public class Contest193 {

    public static void main(String[] args) {

        int [] nums={1,2,3,4};
        int[] ints = new Contest193().runningSum(nums);
        System.out.println(Arrays.toString(ints));
        int [] arr={13,22,100,22,5,62,13,24,81,15,99,14,20,2,61,10,40,47,33,7,38,47,92,31,15,40,73,48,24,55,81,63,37,23,59,78,5,50,10,51,67,9,18,78,89,40,71,7,32,67,6,34,69,59,19,39,96,64,81,96,64,5,82,59,29,93,42,72,38,60,82,40,97,91,4,22,85,80,33,51,10,21,54,91,2,94,38,38,19,75,37,7,76,7,27,8,76,11,25,5};
//        int [] arr={5,5,4};
        int n=new Contest193().findLeastNumOfUniqueInts2(arr,78);
        System.out.println(n);



    }

//    制作 m 束花所需的最少天数
//    如果mid天满足，那么mid+1天也满足，满足单调性，可以二分check
//    对于当前天数mid，
//    贪心计算有多少个连续的小于等于mid的k天，假设有cnt个连续k天，判断cnt是否大于等于m即可
    boolean check(int []a,int m,int k,int mid){
        int n=a.length;
        int cnt=0;
        int len=0;
        for(int i=0;i<n;i++){
            if(a[i]<=mid){
                len++;
                if(len==k){
                    cnt++;
                    len-=k;
                }
            }else{
                len=0;
            }
        }
        return cnt>=m;
    }

    int minDays(int[] a, int m, int k) {
        int l=1;
        int r= (int) 1e9;
        int ans=-1;
        while(l<=r){
            int mid=(l+r)/2;
            if(check(a,m,k,mid)){
                ans=mid;
                r=mid-1;
            }else{
                l=mid+1;
            }
        }
        return ans;
    }


    public int[] runningSum(int[] nums) {
        int sum=0;
        for(int i=0;i<nums.length;i++){
            sum=nums[i]+sum;
            nums[i]=sum;
        }

        return nums;

    }

    //arr.length个长度统计数组里数字出现的次数
    //[4,4,5]
    //[2,0,1]
    public int findLeastNumOfUniqueInts(int[] arr, int k) {
        Arrays.sort(arr);
        int[] numsCount = new int[arr.length];
        int num=1;
        for(int i=1;i<arr.length;++i){
            if(arr[i-1]!=arr[i]){
                ++numsCount[num-1];
                num = 1;
            }else{
                ++num;
            }
        }
        ++numsCount[num-1];
        int res =0;
        for(int i=0;i<arr.length;++i){

            if(numsCount[i]==0) continue;
            --numsCount[i];
            --i;
            if(k>0) {
                k -= i+2;
                if(k<0) res++;
                continue;
            }
            res++;
        }
        return res;
    }

    public int findLeastNumOfUniqueInts2(int[] arr, int k) {

        Map<Integer,Integer>map=new HashMap<>();
        for(int i:arr){
            map.put(i,map.getOrDefault(i,0)+1);
        }
//        int[] cnt = new int[size];
//        int m = 0;
//        // 用keySet把每一个key取出来，然后用map.get(key)来取值
//        // cnt数组仅代表统计数组出现的次数 wordcount 但是具体什么数出现了多少次需要关联
//        for(int key : map.keySet()){
//            cnt[m++] = map.get(key);
//        }
        List<Map.Entry<Integer, Integer>> teamRankList = new ArrayList<>(map.entrySet());
        Collections.sort(teamRankList, (team1, team2) -> {
            return team1.getValue() - team2.getValue();
        });
        Iterator<Map.Entry<Integer, Integer>> iterator = teamRankList.iterator();
        while (iterator.hasNext()&&k>0){
            Map.Entry<Integer, Integer> integerIntegerEntry = iterator.next();
            int val=integerIntegerEntry.getValue();
            if(val==1){
                iterator.remove();
                k--;
            }else{
                //1,2,2,3,3,3 删除前K个数字
                //cnt: 1,2,3
                if (k>=val){
                    k=k-val;
                    iterator.remove();
                }else{
                    break;
                }
            }

        }

        return teamRankList.size();

    }
}
