package aleetcode;

public class leet_8_1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private int[] sums;//the sum from piles[i] to the end
    private int[][] hash;
    public int stoneGameII(int[] piles) {
        if(piles == null || piles.length == 0) return 0;
        int n = piles.length;
        sums = new int[n];
        sums[n-1] = piles[n-1];
        for(int i = n -2; i>=0;i--) {
            sums[i] = sums[i+1] + piles[i]; //the sum from piles[i] to the end
        }
        
        hash = new int[n][n];
        return helper(piles, 0, 1);
    }
    
    private int helper(int[] a, int i, int M) {
        if(i == a.length) return 0;
        if(2*M >= a.length - i) {
            return sums[i];
        }
        if(hash[i][M] != 0) return hash[i][M];
        int min = Integer.MAX_VALUE;//the min value the next player can get
        for(int x=1;x<=2*M;x++){
            min = Math.min(min, helper(a, i+x, Math.max(M,x)));
        }
        hash[i][M] = sums[i] - min;  //max stones = all the left stones - the min stones next player can get
        return hash[i][M];   
    }
	
	
	 public int largest1BorderedSquare(int[][] grid) {
	        if (grid.length==0) return 0;
	        int[][] dpr = new int[grid.length+1][grid[0].length+1];
	        int[][] dpc = new int[grid.length+1][grid[0].length+1];
	        int dist, max=0;
	        for (int r=1;r<=grid.length;r++){
	            for (int c=1;c<=grid[0].length;c++){
	                if (grid[r-1][c-1]==0) continue;
	                dpr[r][c] = dpr[r-1][c]+1;
	                dpc[r][c] = dpc[r][c-1]+1;
	                dist = Math.min(dpr[r][c],dpc[r][c]);
	                for (int i=dist;i>=1;i--){
	                    if (dpr[r][c-i+1]>=i && dpc[r-i+1][c]>=i){
	                        max = Math.max(max, i*i);
	                        break;
	                    }
	                }
	            }
	        }
	        return max;
	    }

	 
	 
	 
}
