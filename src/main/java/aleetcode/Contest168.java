package aleetcode;

import java.util.*;

public class Contest168 {

    public static void main(String[] args) {

        int[] nums = {3, 3, 2, 2, 1, 1};
        boolean b = new Contest168().isPossibleDivide(nums, 3);
//        System.out.println(b);
        new Contest168().maxFreq2("aababcaab", 2, 3, 4);

        int [] status={1,0,0,0,0,0},candies = {1,1,1,1,1,1},initialBoxes = {0};
        int [][] keys = {{1,2,3,4,5},{},{},{},{},{}}, containedBoxes = {{1,2,3,4,5},{},{},{},{},{}};

        maxCandies(status,candies,keys,containedBoxes,initialBoxes);
    }

//    Input: status = [1,0,1,0], candies = [7,5,4,100], keys = [[],[],[1],[]], containedBoxes = [[1,2],[3],[],[]], initialBoxes = [0]
//    Output: 16
//    Explanation: You will be initially given box 0. You will find 7 candies in it and boxes 1 and 2. Box 1 is closed and you don't have a key for it so you will open box 2. You will find 4 candies and a key to box 1 in box 2.
//    In box 1, you will find 5 candies and box 3 but you will not find a key to box 3 so box 3 will remain closed.
//    Total number of candies collected = 7 + 4 + 5 = 16 candy.
    public static int maxCandies(int[] status, int[] candies, int[][] keys, int[][] containedBoxes, int[] initialBoxes) {

        int n = status.length; // count of boxes
        boolean[] usedBoxes = new boolean[n]; // this are used once
        boolean[] boxFound = new boolean[n];// new box we found
        Queue<Integer> q = new LinkedList<>();
        for (int v : initialBoxes) {
            q.add(v);
            boxFound[v] = true; // initial boxes
        }
        int candy = 0;
        while (!q.isEmpty()) {
            int cur = q.poll();
            if (status[cur] == 1 && !usedBoxes[cur]) { // not used and box open
                candy += candies[cur];
                usedBoxes[cur] = true;
                for (int k : keys[cur]) { // all keys in that box
                    status[k] = 1;
                    if (boxFound[k]) q.add(k);// box was found and we have the key
                }
                for (int k : containedBoxes[cur]) {// all boxes in cur box
                    boxFound[k] = true;
                    q.add(k);
                }
            }
        }
        return candy;

    }


    //    Input: s = "aababcaab", maxLetters = 2, minSize = 3, maxSize = 4
//    Output: 2
    public int maxFreq2(String s, int maxLetters, int minSize, int maxSize){

        int res=0;
        Map<String,Integer> map=new HashMap();
        int l=0,r=0,letter=0;
        int [] ch=new int [26];
        while(r<s.length()){
            if(ch[ s.charAt(r)-'a']==0){

                letter++;

            }
            ch[ s.charAt(r)-'a']++;
            r++;
            while(letter>maxLetters||(r-l)>minSize){
                if(ch[ s.charAt(l)-'a']==1){

                    letter--;
                }
                ch[ s.charAt(l)-'a']--;
                l++;
            }
            if((r-l)== minSize){
                String sb=s.substring(l,r);
                map.put(sb,map.getOrDefault(sb,0)+1);
                res=Math.max(res,map.get(sb));
            }
        }

        return res;
    }

    public int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
        HashMap<String,Integer> map=new HashMap<>();
        int res=0;
        int[] ch=new int[26];
        int l=0, r=0, letter=0;
        while(r<s.length()){
            if(ch[s.charAt(r++)-'a']++==0) letter++;
            while(letter>maxLetters || (r-l)>minSize){
                if(ch[s.charAt(l++)-'a']--==1) letter--;
            }
            if((r-l)==minSize){
                String sb=s.substring(l, r);
                map.put(sb, map.getOrDefault(sb,0)+1);
                res=Math.max(res, map.get(sb));
            }
        }
        return res;
    }

    //    nums = [1,2,3,3,4,4,5,6], k = 4
//    true
    public boolean isPossibleDivide(int[] nums, int k) {
        int cnt = nums.length;
        if (cnt % k != 0) {
            return false;
        }
        Map<Integer, Integer> map = new HashMap();
        //统计次数
        for (int n : nums) {
            if (map.get(n) == null) {
                map.put(n, 1);
            } else {
                map.put(n, map.get(n) + 1);
            }
        }
        //int 数组排序
        Arrays.sort(nums);

        for (int j = 0; j <= nums.length - k; j++) {
            outer:
            for (int i = 0; i < k; i++) {
                int n = nums[j];
                if (map.get(n + i) == null) {
                    return false;
                }
                int cn = map.get(n + i);
                if (map.get(n + i) > 0) {
                    cn = cn - 1;
                    map.put(n + i, cn);
                } else {
                    break outer;
                }

            }
        }
        for (int n : map.values()) {
            if (n != 0) return false;
        }

        return true;
    }


    public int findNumbers(int[] nums) {

        int res = 0;

        for (int i : nums) {
            int n = getN(i);
            if (n % 2 == 0) {
                res++;
            }
        }

        return res;
    }

    public int getN(int num) {
        int n = 1;
        while (num / 10 > 0) {
            n++;
            num = num / 10;
        }
        return n;
    }

}
