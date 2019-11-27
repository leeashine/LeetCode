package aleetcode;

import org.omg.CORBA.INTERNAL;

import java.util.*;

public class Contest164 {

    public static void main(String[] args) {

        int[][] points = {{1, 1}, {3, 4}, {-1, 0}};

        int cnt = minTimeToVisitAllPoints(points);
        System.out.println(cnt);
        int[][] grid = {{1, 1, 0, 0}, {0, 0, 1, 0}, {0, 0, 1, 0}, {0, 0, 0, 1}};
//		int [][] grid={{1,0},{1,1}};
        System.out.println(countServers(grid));

//        String [] products = {"mobile","mouse","moneypot","monitor","mousepad"};
        String [] products = {"bags","baggage","banner","box","cloths"};
        String searchWord = "bags";
        List<List<String>> list=suggestedProducts(products,searchWord);
        System.out.println();
//        System.out.println(stringToInt("bagg"));
//        System.out.println(stringToInt("bags"));
//        System.out.println(stringToInt("bann"));

    }
    public static List<List<String>> suggestedProducts(String[] products, String searchWord) {

        List<List<String>> list=new ArrayList<>();
        List<String> indexs=new ArrayList<>();
        StringBuilder sb=new StringBuilder();
        String[] productss=new String[products.length];
        for(int i=0;i<searchWord.length();i++){

            sb.append(searchWord.charAt(i));
            indexs.add(sb.toString());
        }
//        indexs.forEach(System.out::println);
        //按字典排序
        Arrays.sort(products);

        for(int i=0;i<products.length;i++){
            if(i>=searchWord.length()){
                break;
            }
            productss[i]=products[i];
        }

        for (int i=0;i<indexs.size();i++){
            List<String> values=new ArrayList<>();
            int cnt=0;
            for(int j=0;j<productss.length-1;j++){

                if(cnt==3){
                    break;
                }
                if(indexs.get(i).length()>productss[j].length()){
                    if(productss[j].contains(indexs.get(i))){
                        values.add(productss[j]);
                        cnt++;
                    }
                }else{
                    if(productss[j].substring(0,indexs.get(i).length()).contains(indexs.get(i))){
                        values.add(productss[j]);
                        cnt++;
                    }
                }


            }
            list.add(values);
        }

        return  list;
    }

    //先加后减
    public static int countServers(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        int cnt = 0;
        int[] rowCount = new int[grid.length];
        int[] colCount = new int[grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    rowCount[i]++;
                    colCount[j]++;
                    cnt++;
                }
            }
        }

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    if (rowCount[i] == 1 && colCount[j] == 1) {
                        cnt--;
                    }
                }
            }
        }


        return cnt;
    }


    public static int minTimeToVisitAllPoints(int[][] points) {
        int ans = 0;
        for (int i = 1; i < points.length; ++i) {
            int[] cur = points[i], prev = points[i - 1];
            ans += Math.max(Math.abs(cur[0] - prev[0]), Math.abs(cur[1] - prev[1]));
        }
        return ans;
    }

}
