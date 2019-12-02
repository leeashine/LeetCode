package aleetcode;

public class Contest165 {

	public static void main(String[] args) {

		int [][] moves={{0,0},{2,0},{1,1},{2,1},{2,2}};
		tictactoe(moves);
		
	}

	//借助辅助数组统计每行每列和对角线的和
	public static String tictactoe(int[][] moves) {
		int[] A = new int[8], B = new int[8]; // 3 rows, 3 cols, 2 diagonals
		for (int i = 0; i < moves.length; i++) {
			int r = moves[i][0], c = moves[i][1];
			int[] player = (i % 2 == 0) ? A : B;
			player[r]++;
			player[c + 3]++;
			if (r == c)
				player[6]++;
			if (r == 2 - c)
				player[7]++;
		}
		for (int i = 0; i < 8; i++) {
			if (A[i] == 3)
				return "A";
			if (B[i] == 3)
				return "B";
		}
		return moves.length == 9 ? "Draw" : "Pending";
	}

}
