package aleetcode.contest2019;

import aleetcode.IDGenerator;

import java.util.ArrayList;
import java.util.List;

public class Contest162 {

	public static void main(String[] args) {
		int n = 2, m = 3;
		int [][] indices = {{0,1},{1,1}};
		oddCells(n, m, indices);
		
		int upper = 9, lower = 2;
		int [] colsum = {0,1,2,0,0,0,0,0,2,1,2,1,2};
		//[[1,1,0],[0,0,1]]
		// 2,1,2,0,1,0,1,2,0,1
		//[1,1,1,0,1,0,0,1,0,0],[1,0,1,0,0,0,1,1,0,1]
		reconstructMatrix(upper, lower, colsum);
		
		int [][] grid={{0,0,1,0,0},{0,1,0,1,0},{0,1,1,1,0}};
		new Contest162().closedIsland(grid);
		System.out.println(IDGenerator.getID32());
		
	}
	
	int[][] dir = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    
    public int closedIsland(int[][] grid) {
        int res = 0;
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] == 0){//½��
                    if(dfs(grid, i, j)) res++;
                }
            }
        }
        
        return res;
    }
    public boolean dfs2(int [][] grid,int x,int y){
    	if(x<0 || x>=grid.length || y<0 || y>=grid[0].length) return false;
    	if(grid[x][y]==1) return true;
    	
    	grid[x][y]=1;
    	
    	boolean res=true;
    	for(int [] d:dir){
    		res=res & (dfs(grid,x+d[0],y+d[1]));
    	}
    	return res;
    }
    
    public boolean dfs(int[][] grid, int x, int y){
        
        if(x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) return false;
        
        if(grid[x][y] == 1) return true;//
        
        grid[x][y] = 1;//������ǣ�����������ݵ�ʱ���������һ��ʼ�ĵ�����ѭ��
        
        boolean res = true;
        
        for(int[] d : dir){
            res = res & dfs(grid, x + d[0], y + d[1]);
        }
        
        return res;
    }	
	
	public static List<List<Integer>> reconstructMatrix(int upper, int lower, int[] colsum) {
		List<List<Integer>> list=new ArrayList<List<Integer>>();
        List up=new ArrayList();
        List lo=new ArrayList();
        int sum=0;
        int newUpper=0;
        int newLower=0;
        for(int i:colsum){
        	sum+=i;
        	if(i==2){
        		newUpper++;
        		newLower++;
        	}
        }
        if((upper+lower)!=sum)
        	return list;
        upper-=newUpper;
        lower-=newLower;
        if(upper<0||lower<0)
        	return list;
		for(int i=0;i<colsum.length;i++){
			
			if(colsum[i]==0){
				up.add(0);
				lo.add(0);
			}else if(colsum[i]==2){
				up.add(1);
				lo.add(1);
			}else if(colsum[i]==1){
				
				if(upper>0){
					up.add(1);
					lo.add(0);
					upper--;
				}
				else if(lower>0){
					up.add(0);
					lo.add(1);
					lower--;
				}
				
			}
			
		}
		list.add(up);
		list.add(lo);
		return list;
    }
	
	public static int oddCells(int n, int m, int[][] indices) {
        boolean[] row = new boolean[n], col = new boolean[m];
        int cntRow = 0, cntCol = 0;
        for (int[] idx : indices) {
            row[idx[0]] ^= true;
            col[idx[1]] ^= true;
            cntRow += row[idx[0]] ? 1 : -1;
            cntCol += col[idx[1]] ? 1 : -1;
        }
        return m * cntRow + n * cntCol - 2 * cntRow * cntCol;
    }
}
