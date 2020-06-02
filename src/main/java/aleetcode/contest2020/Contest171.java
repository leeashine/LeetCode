package aleetcode.contest2020;

/**
 * 并查集总结（3个方法）：
 * 1.找上级 findParent([],i)
 * 2.连接/江湖势力合并 makeConnected(n,con)
 * 3.求联通分量/有几个江湖势力 getComponents(n,[])
 */
public class Contest171 {
    public static void main(String[] args) {

        int n = 6;
        int [][] connections = {{0,1},{0,2},{0,3},{1,2},{1,3}};
        makeConnected(n,connections);

    }
    //union find 递归（dfs） 并查集
    public static int findParent(int[] par, int i) {
        if (par[i] != i) {
            par[i] = findParent(par, par[i]);
        }
//        如果一个人的上级就是他自己，那说明他就是掌门人了，查找到此为止。
        return par[i];
    }

    public static int makeConnected(int n, int[][] connections) {
        int[] parent = new int[n];
        for(int i = 0; i < n; i++) parent[i] = i;//初始化 自己就是自己的上级
        int m = connections.length;
        int components = 0;
        int extraEdge = 0;
        for(int i = 0; i < m; i++) {
            // 找到所属门派掌门 p1,p2联通起来
            int p1 = findParent(parent, connections[i][0]);
            int p2 = findParent(parent, connections[i][1]);
            if(p1 == p2) extraEdge++;//多余的边
            else parent[p1] = p2;
        }
        for(int i = 0; i < n; i++)
            if(parent[i] == i)
                components++;//计算有几个连通分量 （几个江湖势力）
        return (extraEdge >= components - 1) ? components - 1 : -1;
    }

    //a >> (i - 1) & 1 表示a所代表的二进制数的第i号为是0还是1
    public int minFlips2(int a, int b, int c) {
        int count = 0;
        for(int i = 1; i <= 32; i++) {
            int b1 = 0, b2 = 0, b3 = 0;
            if(((a >> (i - 1)) & 1) >= 1) b1 = 1; // check the ith bit of a
            if(((b >> (i - 1)) & 1) >= 1) b2 = 1; // check the ith bit of b
            if(((c >> (i - 1)) & 1) >= 1) b3 = 1; // check the ith bit of c
            //b3为0 ===>b1 b2 里有一个1 count+1(翻转1次)  b1 b2 有2个1 count+2(翻转2次)
            if(b3 == 0 && (b1 == 1 || b2 == 1)) count += b1 + b2; // if the ith bit of c is 0 and any of the ith bits of a or b is 1
            //b3为1 ===> b1=0 b2=0翻转其中任何一个就行(count+1)
            else if(b3 == 1 && b1 == 0 && b2 == 0) count++; // if the ith bit of c is 1, check the ith bits of a and b
        }
        return count;
    }
    //位运算操作题   |运算：两个位都为0时，结果才为0
    //(a | b) ^ c是0，a | b和c是平等的，1为不相等
    //第ith位(a | b) ^ c，使用1 << i掩码做&操作检查位是否0; 如果不是，ith则a | b和位c不相同，我们至少需要1翻转；有3种情况：
    public int minFlips(int a, int b, int c) {
        int ans = 0, ab = a | b, equal = ab ^ c;
        for (int i = 0; i < 31; ++i) {
            int mask = 1 << i;
            if ((equal & mask) > 0)  //ith bits of a | b and c are not same, need at least 1 flip.
                // ans += (ab & mask) < (c & mask) || (a & mask) != (b & mask) ? 1 : 2;
                //c第mask位为0并且a b的第mask位为1才需要反转2次 其他都是一次
                ans += (a & mask) == (b & mask) && (c & mask) == 0 ? 2 : 1; // ith bits of a and b are both 1 and that of c is 0?
        }
        return ans;
    }


    //1010
    public int[] getNoZeroIntegers(int n) {

        int [] res=new int [2];
        int num1=1;
        int num2=n-1;
        if(!containZero(num2)||!containZero(num1)){
            num2-=1;
            num1+=1;
        }
        res[0]=num1;
        res[1]=num2;

        return res;

    }

    public static boolean containZero(int num){

        while(num>0){
            int n=num%10;
            if(n==0)
                return false;
            num/=10;
        }
        return true;

    }

}
