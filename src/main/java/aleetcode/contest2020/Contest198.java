package aleetcode.contest2020;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Contest198 {
    public static void main(String[] args) {

        int n=7;
        int [][]edges={{0,1},{0,2},{1,4},{1,5},{2,3},{2,6}};
        String labels="abaedcd";
        new Contest198().countSubTrees(n,edges,labels);

        double refuseCount = 158;
        double totalappcount = 525;
        double approverefuserate=0.00d;
        DecimalFormat df   = new DecimalFormat("#0.00");
        if(totalappcount>0)
            approverefuserate=refuseCount/totalappcount*100;
        System.out.println(approverefuserate);
        approverefuserate=Double.valueOf(df.format(approverefuserate));
        System.out.println(approverefuserate);
    }

    //子树中标签相同的节点数
    //dfs遍历 相同标签的累加
//    输入：n = 7, edges = [[0,1],[0,2],[1,4],[1,5],[2,3],[2,6]], labels = "abaedcd"
    public int[] countSubTrees(int n, int[][] edges, String labels) {

        Map<Integer, List<Integer>> edgesMap = new HashMap<Integer, List<Integer>>();
        for (int[] edge : edges) {
            int node0 = edge[0], node1 = edge[1];
            List<Integer> list0 = edgesMap.getOrDefault(node0, new ArrayList<Integer>());
            List<Integer> list1 = edgesMap.getOrDefault(node1, new ArrayList<Integer>());
            list0.add(node1);
            list1.add(node0);
            edgesMap.put(node0, list0);
            edgesMap.put(node1, list1);
        }
        //遍历
        int[] counts = new int[n];
        boolean[] visited = new boolean[n];
        dfs(0, counts, visited, edgesMap, labels);
        return counts;


    }

    public int[] dfs(int node, int[] counts, boolean[] visited, Map<Integer, List<Integer>> edgesMap, String labels){

        visited[node] = true;
        int[] curCounts = new int[26];
        curCounts[labels.charAt(node) - 'a']++;
        List<Integer> nodesList = edgesMap.get(node);
        for (int nextNode : nodesList) {
            if (!visited[nextNode]) {
                int[] childCounts = dfs(nextNode, counts, visited, edgesMap, labels);
                for (int i = 0; i < 26; i++) {
                    curCounts[i] += childCounts[i];
                }
            }
        }
        counts[node] = curCounts[labels.charAt(node) - 'a'];
        return curCounts;

    }


//    换酒问题 几瓶空瓶可以换一瓶 最后一共能喝几瓶？
//    输入：numBottles = 15, numExchange = 4
//    所以最多能喝到 15 + 3 + 1 = 19 瓶酒。
//    另外一种解法
//    一个空瓶价值为1的话，numBottles * numExchange为总价值，至少一个空瓶最后留在手里换不了，所以总价值-1，numExchange-1为一份酒的价值
    public int numWaterBottles(int numBottles, int numExchange) {
        int total = numBottles;
        while (numBottles >= numExchange) {
            int change = numBottles / numExchange;
            total += change;
            numBottles = change + numBottles % numExchange;
        }
        return total;
    }



}
