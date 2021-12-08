package aleetcode.contest2020;

import java.util.*;

public class Contest197 {
    public static void main(String[] args) {


        int n = 3;
        int [][] edges = {{0,1},{1,2},{0,2}};
        double [] succProb = {0.5,0.5,0.2};
        int start = 0;
        int end = 2 ;
        new Contest197().maxProbability(n,edges,succProb,start,end);



    }

    class State {
        int node;
        double prob;
        State(int _node, double _prob) {
            node = _node;
            prob = _prob;
        }
    }
    //BFS 队列
    public double maxProbability(int n, int[][] edges, double[] succProb, int start, int end) {

        // build graph -> double[0]: node, double[1]: edge prob
        Map<Integer, List<double[]>> map = new HashMap<>();
        for (int i = 0; i < edges.length; ++i) {
            int[] edge = edges[i];

            map.putIfAbsent(edge[0], new ArrayList<>());
            map.putIfAbsent(edge[1], new ArrayList<>());

            map.get(edge[0]).add(new double[] {edge[1], succProb[i]});
            map.get(edge[1]).add(new double[] {edge[0], succProb[i]});
        }

        double[] probs = new double[n];  // best prob so far for each node
        Queue<State> queue = new LinkedList<>();
        queue.add(new State(start, 1.0));

        while (!queue.isEmpty()) {

            State state = queue.poll();
            int parent = state.node;
            double prob = state.prob;

            for (double[] child : map.getOrDefault(parent, new ArrayList<>())) {
                // add to queue only if: it can make a better prob
                if (probs[(int) child[0]] >= prob * child[1]) continue;

                queue.add(new State((int) child[0], prob * child[1]));
                probs[(int) child[0]] = prob * child[1];
            }

        }

        return probs[end];
    }


    //仅含1的子字符串
//    输入：s = "0110111"
//    输出：9
//    解释：共有 9 个子字符串仅由 '1' 组成 5+3+1
//    由1504题统计全1子矩形启发，本题可看作该题的降维版。
//    本题中可维护一个数组leftOnes，记录当前位置（包含）左侧的连续1个数。
//    示例1中，s = "0110111"，可得到leftOnes = [0,1,2,0,1,2,3]。
//    整个字符串中包含的1子串数即为leftOnes中各元素之和
    public int numSub(String s) {

        char [] chars=s.toCharArray();
        long res=0,length=0;
        int MOD = 1000000007;
        for (int i = 0; i < chars.length; i++) {
            length=chars[i]=='1'?length+1:0;
            res+=length;
        }
        return (int)(res%MOD);

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
