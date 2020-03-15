package aleetcode;

import com.sun.org.apache.regexp.internal.RE;

import java.util.*;

public class Contest180 {

    public static void main(String[] args) {

        int [][] matrix={{36376,85652,21002,4510},{68246,64237,42962,9974},{32768,97721,47338,5841},{55103,18179,79062,46542}};
        luckyNumbers(matrix);


    }

    //找出这个幸运值
    //在同一行的所有元素中最小
    //在同一列的所有元素中最大
    public static List<Integer> luckyNumbers (int[][] matrix) {

        List<Integer> list=new ArrayList<>();

        int m=matrix.length;//行
        int min;
        int max;
        Map<Integer,Integer> map=new HashMap();
        int n=0;

        for (int i = 0; i < m; i++) {
            min=Integer.MAX_VALUE;
            n=matrix[i].length;//列
            //找同一行的最小值
            for (int j = 0; j < n; j++) {
                if(matrix[i][j]<min){
                    min=matrix[i][j];
                    map.put(i,j);
                }
            }
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            max=Integer.MIN_VALUE;
            int x=entry.getKey();
            int y=entry.getValue();
            if(matrix[x][y]>max){
                max=matrix[x][y];
            }
            for(int j=0;j<m;j++){
                if(matrix[j][y]>max){
                    max=matrix[j][y];
                }
            }
            if(max==matrix[x][y])
                list.add(max);
        }

        return list;
    }


}
