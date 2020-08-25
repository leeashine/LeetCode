package aleetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Contest203 {
    public static void main(String[] args) {

        mostVisited(7,new int[]{1,3,5,7});

    }
    public static List<Integer> mostVisited2(int n, int[] rounds) {

        //不管中间怎么变化 只要看起点到终点经过的扇区就行了
        int start=rounds[0];
        int end=rounds[rounds.length-1];
        ArrayList<Integer>list=new ArrayList<>();
        if (start<=end){
            while (start<=end)
                list.add(start++);
//            Arrays.asList(Arrays.copyOfRange(rounds,start,end+1));
        }else{
            //3 4 2   3-4 1-2
            int i=1;
            while(i<=end){
                list.add(i++);
            }
            while (start<=n){
                list.add(start++);
            }
        }
            return list;
    }

    public static List<Integer> mostVisited(int n, int[] rounds) {

        int [] count=new int[n+1];
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<rounds.length-1;i++){

                int j=i+1;
                int left=rounds[i];
                int right=rounds[j];
                if(right<=n&&left<=right){
                    if(i==0){
                        while(left<=right){
                            count[left++]++;
                        }
                    } else {
                        while(left<right){
                            count[++left]++;
                        }
                    }
                }else if(left>right){

                    if(i==0){
                        while (left<=n){
                            count[left++]++;
                        }
                    }else{
                        while (left<n){
                            count[++left]++;
                        }
                    }
                    left=left%n;
                    if(left==0||left==n){
                        left=1;
                    }
                    while(left<=right){
                        count[left]++;
                        left++;
                    }

                }

        }
        int Max=-1;
        for(int i:count){
            Max=Math.max(i,Max);
        }
        for (int i = 1; i < count.length; i++) {
            if(count[i]==Max){
                list.add(i);
            }

        }
        return list;

    }
}
