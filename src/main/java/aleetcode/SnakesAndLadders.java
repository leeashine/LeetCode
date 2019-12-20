package aleetcode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

public class SnakesAndLadders {
	
	public static void main(String[] args) {
		int[][] board={
				{-1,-1,-1,-1,-1,-1},
				{-1,-1,-1,-1,-1,-1},
				{-1,-1,-1,-1,-1,-1},
				{-1,35,-1,-1,13,-1},
				{-1,-1,-1,-1,-1,-1},
				{-1,15,-1,-1,-1,-1}};
		
		System.out.println(snakesAndLadders(board));
	}
	//BFS
	public static int snakesAndLadders(int[][] board) {
        int n = board.length;
        int[] b = new int [n*n+1];
        boolean flag = true;
        int x = 1;
        for(int i = n-1; i >= 0; i--)
        {
            if(flag)
            {
                for(int j = 0; j < n; j++)
                    b[x++] = board[i][j];
                
            }else{
                for(int j = n-1; j >= 0; j--)
                    b[x++] = board[i][j];
            }
            flag = !flag; 
        }
        
        int[] counter = new int[n*n+1];
        Arrays.fill(counter, -1);
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(1);
        counter[1] = 0;
        while(!queue.isEmpty()){
            int cur = queue.poll();
            for(int i = 1; i <= 6; i++)
            {
                if(cur + i > n*n)
                    break;
                int next = cur + i;
                if(b[next] > -1)
                    next = b[next];
                if(next == n*n)
                    return counter[cur] + 1;
                if(counter[next] == -1)
                {
                    queue.add(next);
                    counter[next] = counter[cur] + 1;
                }
            }
        }
        return -1;
    }

}
