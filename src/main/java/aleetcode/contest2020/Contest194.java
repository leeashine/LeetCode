package aleetcode.contest2020;

import aleetcode.UnionFind;

import java.util.*;

public class Contest194 {
    public static void main(String[] args) {

//        int i = new Contest194().xorOperation(10, 5);
//        System.out.println(i);

//        String[] names={"onepiece","onepiece(1)","onepiece(2)","onepiece(3)","onepiece"};
//        new Contest194().getFolderNames(names);

        int [] rains={1,2,0,0,2,1};
//        int [] rains={1,2,0,1,2};
        int[] ints = new Contest194().avoidFlood(rains);
        System.out.println(Arrays.toString(ints));


    }

//    避免洪水泛滥
//    输入：rains = [1,2,0,0,2,1]
//    输出：[-1,-1,2,1,-1,-1]
//    map+treeset  treeset.higher方法返回大于这个数的最小元素 如果不存在返回Null
    public int[] avoidFlood(int[] rains) {
        int n = rains.length;
        int[] ans = new int[n];
        Map<Integer, Integer> lake2fullDay = new HashMap<>();
        TreeSet<Integer> dryDays = new TreeSet<>();
        for (int i = 0; i < n; i++){
            if (rains[i] == 0){
                dryDays.add(i);
                ans[i] = 1;
            } else {
                int lake = rains[i];
                if (lake2fullDay.containsKey(lake)){
                    // find a day in "drydays" to dry this lake.
                    Integer day = dryDays.higher(lake2fullDay.get(lake));
                    if (day == null){
                        //couldn't stop flooding.
                        return new int[]{};
                    }
                    ans[day] = lake;
                    dryDays.remove(day);
                }
                lake2fullDay.put(lake, i);
                ans[i] = -1;
            }
        }
        return ans;
    }



//    保证文件名唯一
//    输入：names = ["pes","fifa","gta","pes(2019)"]
//    输出：["pes","fifa","gta","pes(2019)"]
    public String[] getFolderNames(String[] names) {

        String [] res=new String[names.length];

        Map<String,Integer>map=new HashMap<>();

        for(int i=0;i<names.length;i++){
            String name=names[i];
//            map.put(name,map.getOrDefault(name,0)+1);
            if(!map.containsKey(names[i])){
                res[i]=name;
                map.put(name,1);
            }else{
                int count=map.get(name);
                //取出之前出现的次数
                while(map.containsKey(names[i] + "(" + count + ")")){
                    count++;
                }
                map.put(names[i] + "(" + count + ")", 1);
                map.put(names[i],map.get(name)+1);

                res[i]=names[i]+"(" + (count) + ")";
            }

        }
        return res;

    }

    public int xorOperation(int n, int start) {

        int num;
        int res=0;
        for(int i=0;i<n;i++){

            num=start+2*i;
            res^=num;

        }
        return res;

    }

    public List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {

        List<Integer>criticals = new ArrayList<>();
        List<Integer> pseduos = new ArrayList<>();

        Map<int[], Integer> map = new HashMap<>();
        for(int i =0;i<edges.length;i++){
            map.put(edges[i], i);
        }

        Arrays.sort(edges, (e1, e2)->Integer.compare(e1[2], e2[2]));
        int minCost = buildMST(n, edges, null, null);

        for(int i =0;i<edges.length;i++){
            int[] edge = edges[i];
            int index = map.get(edge);
            int costWithout = buildMST(n, edges, null, edge);
            if(costWithout > minCost){
                criticals.add(index);
            }else{
                int costWith = buildMST(n, edges, edge, null);
                if(costWith == minCost){
                    pseduos.add(index);
                }
            }

        }

        return Arrays.asList(criticals, pseduos);
    }

    private int buildMST(int n, int[][] edges, int[] pick, int[] skip){
        UnionFind uf = new UnionFind(n);
        int cost = 0;
        if(pick != null){
            uf.union(pick[0], pick[1]);
            cost += pick[2];
        }

        for(int[] edge : edges){
            if(edge != skip && uf.union(edge[0], edge[1])){
                cost += edge[2];
            }
        }
        return uf.count == 1? cost : Integer.MAX_VALUE;
    }

}
