package aleetcode;

import java.util.*;

public class Contest174 {
    public static void main(String[] args) {

        int [][]mat={{1,1,0,0,0}, {1,1,1,1,0}, {1,0,0,0,0}, {1,1,0,0,0}, {1,1,1,1,1}};
        int []arr = {3,3,3,3,5,5,5,2,2,7};

        kWeakestRows(mat,3);
        minSetSize(arr);
    }

    public static int minSetSize(int[] arr) {

        Map<Integer,Integer> map=new HashMap<Integer, Integer>();
        //java8 统计每个单词出现次数
        for(int num : arr){
            map.put(num,map.getOrDefault(num,0)+1);
            if(map.get(num) >= arr.length/2) return 1;
        }
        //对list排序 按从大到小 arrays.sort比collection.sort效率高
        List<Integer> orderedVal = new ArrayList<>(map.values());
        orderedVal.sort((a,b) -> b - a);

        int count = 0;
        int sum = 0;
        for(int val : orderedVal){
            sum += val;
            count++;
            if(sum >= arr.length/2) return count;
        }
        return count;


    }
    public static int[] kWeakestRows2(int[][] mat, int k) {
        int[][] tmp = new int[mat.length][2];
        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat[0].length; j++){
                tmp[i][0] = i;
                if(mat[i][j] == 1)  tmp[i][1] += 1;
            }
        }
//        数组排序
        Arrays.sort(tmp, (o1, o2) -> o1[1] - o2[1]);
        int[] res = new int[k];
        for(int i =0; i < k; i++){
            res[i] = tmp[i][0];
        }
        return res;
    }

    public static int[] kWeakestRows(int[][] mat, int k) {
        HashMap<Integer,Integer> map = new HashMap();
        for(int i=0;i<mat.length;i++){
            int count = 0;
            for(int j=0;j<mat[0].length;j++){
                if(mat[i][j] ==1){
                    count++;
                }else {
                    break;
                }
            }
            map.put(i,count);
        }
        List<Integer> indexList = new ArrayList(mat.length);//初始化arraylist长度 避免resize引起的资源消耗
        for(int i=0;i<mat.length;i++){
            indexList.add(i);
        }
//        下标数组indexlist并根据map中的统计值进行排序 集合排序
        Collections.sort(indexList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return map.get(o1)-map.get(o2);
            }
        });
        int[] res = new int[k];
        for(int i=0;i<k;i++){
            res[i] = indexList.get(i);
        }
        return res;
    }

}
