package aleetcode.design;
//LCA倍增法
//https://blog.csdn.net/Q_M_X_D_D_/article/details/89924963
//知识点
//1、将2的次幂排成一个序列，即1,2,4,8,16,32......在这个序列中取任意个不相等的数，相加可以得到所有正整数。例如5=1+4；
//2、如果c是a和b的LCA，那么c的所有祖先同样也是a和b的公共祖先，但不是最近的。
//这两个理论的意义就是：使两个结点不再一层一层地往上跳，而是每次都跳2的幂次方层。例如a点要向上跳10层，就可以跳两次：先跳8层，再跳2层。但是随之而来的新问题是：怎么知道我跳了8层之后到达了哪个结点？下面就要用ST算法来解决这个问题。
class TreeAncestor {

//  dp[node][j] 存储的是 node 节点距离为 2^j 的祖先是谁。
    int[][] dp;
    public TreeAncestor(int n, int[] parent) {
        dp = new int[n][(int)(Math.log(n) / Math.log(2)) +1];
        //初始化 i个向上第2^0个祖先就是parent本身的值
        for(int i = 0;i<n;i++) {
            dp[i][0] = parent[i];
        }
        for(int j =1;(1<<j) < n;j++) {
            for(int i=0;i<n;i++) {
                if(dp[i][j-1] != -1) {
                    //我要找i的向上第j个祖先=i向上第j-1个祖先然后这个节点再向上一个祖先
                    dp[i][j] = dp[dp[i][j-1]][j-1];
                }
                else dp[i][j] = -1;
            }
        }
    }
//  (5,3) -> (dp[5][1],2)
    public int getKthAncestor(int node, int k) {
        if(k ==0 || node == -1) return node;
//        对于每一个查询 k，把 k 拆解成二进制表示，然后根据二进制表示中 1 的位置，累计向上查询。
//        p代表位置 k-(1<<p)代表最接近k的偶数
        int p = (int)(Math.log(k) / Math.log(2));
        return getKthAncestor(dp[node][p], k - (1<<p));
    }


}

/**
 * Your TreeAncestor object will be instantiated and called as such:
 * TreeAncestor obj = new TreeAncestor(n, parent);
 * int param_1 = obj.getKthAncestor(node,k);
 */