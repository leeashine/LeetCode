package aleetcode;

import java.util.*;

public class Contest173 {
    public static void main(String[] args) {

    }
//    restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]],
//    veganFriendly = 1, maxPrice = 50, maxDistance = 10
//    [3 1 5]
    public List<Integer> filterRestaurants(int[][] restaurants, int veganFriendly, int maxPrice, int maxDistance) {

        HashMap<Integer, int[]> map = new HashMap<>();
        List<Integer> IDs = new ArrayList<>();
        for (int[] r : restaurants) {
            if (r[1] >= veganFriendly && r[3] <= maxPrice && r[4] <= maxDistance) {
                IDs.add(r[0]);
                map.put(r[0], r);
            }
        }
        Collections.sort(IDs, (id1, id2) -> {
            int rating1 = map.get(id1)[1], rating2 = map.get(id2)[1];
            if (rating1 == rating2) return id2 - id1; // If same rating, order them by id from highest to lowest
            return rating2 - rating1; // Ordered by rating from highest to lowest
        });
        return IDs;

    }

    public int removePalindromeSub(String s) {
        return s.isEmpty() ? 0 : (s.equals(new StringBuilder(s).reverse().toString()) ? 1:2);
    }

    public int removePalindromeSub2(String s) {
        if(s.length() == 0) return 0;
        if(isPal(s) || count(s, 'a') == s.length() || count(s, 'b') == s.length()) return 1;
        return 2;
    }
    //判断是否是回文数
    boolean isPal(String s) {
        for(int i = 0; i < s.length() / 2; i ++) {
            if(s.charAt(i) != s.charAt(s.length() - i - 1)) return false;
        }
        return true;
    }
    int count(String s, char c) {
        int co = 0;
        for(int i = 0; i < s.length() ; i ++) {
            co += s.charAt(i) == c ? 1 : 0;
        }
        return co;
    }
    class Restaurant{
        int id;
        int rating;
        int vegFlag;
        int price;
        int distance;

        public Restaurant(int id, int rating, int vegFlag, int price, int distance) {
            this.id = id;
            this.rating = rating;
            this.vegFlag = vegFlag;
            this.price = price;
            this.distance = distance;
        }
    }
}
