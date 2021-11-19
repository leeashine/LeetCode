package aleetcode;

public class UnionFind {
    public final int[] parents;
    public int count;

    public UnionFind(int n){
        this.parents = new int[n];
        reset();
    }

    public void reset(){
        for(int i =0;i<parents.length;i++){
            parents[i] = i;
        }
        count = parents.length;
    }

    public int find(int i){
        int parent = parents[i];
        if(parent == i){
            return i;
        }else{
            int root = find(parent);
            parents[i] = root;
            return root;
        }
    }

    public boolean union(int i, int j){
        int r1 = find(i);
        int r2 = find(j);
        if(r1 != r2){
            count--;
            parents[r1] = r2;
            return true;
        }else{
            return false;
        }
    }
}
