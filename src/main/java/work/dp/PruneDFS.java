package work.dp;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * 剪枝+回溯/递归/dfs
 */
public class PruneDFS {

    private static class Coupon {
        String id; int save; int bit;
        Coupon(String id,int save,int bit){this.id=id;this.save=save;this.bit=bit;}
    }

    // 输入
    List<Coupon> coupons;
    // 预处理
    int[] suffixMax;              // 剩余最大可省
    // 结果
    int bestSave = 0;
    List<String> bestPath = new ArrayList<>();
    List<String> path = new ArrayList<>();

    void solve() {
        // ① 按 saveAmount 降序排
        coupons.sort((a,b)-> b.save - a.save);
        // ② 预计算 suffixMax
        int n = coupons.size();
        suffixMax = new int[n+1];
        for (int i=n-1;i>=0;i--){
            suffixMax[i] = suffixMax[i+1] + coupons.get(i).save;
        }
        // ③ DFS
        dfs(0,0,0);
        System.out.println("best="+bestSave+"  "+bestPath);
    }

    void dfs(int idx,int used,int cur){
        // 上界剪枝
        if (cur + suffixMax[idx] <= bestSave){
            return;
        }
        // 终止条件
        if (idx == coupons.size()) {
            bestSave = cur;
            bestPath = new ArrayList<>(path);
            return;
        }
        // 不选
        dfs(idx+1,used,cur);

        // 选
        Coupon c = coupons.get(idx);
        if ((used & c.bit) == 0){
            path.add(c.id);
            dfs(idx+1, used|c.bit, cur + c.save);
            path.remove(path.size()-1);
        }
    }

    /* Demo */
    public static void main(String[] args){
        List<Coupon> list = Lists.newArrayList(
            new Coupon("A",50,1),  // G1
            new Coupon("B",25,1),  // G1
            new Coupon("C",50,2),  // G2
            new Coupon("D",30,4)   // G3
        );
        PruneDFS p = new PruneDFS();
        p.coupons = new ArrayList<>(list);
        p.solve();
    }
}