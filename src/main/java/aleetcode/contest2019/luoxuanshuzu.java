package aleetcode.contest2019;

public class luoxuanshuzu {
	
	public static void main(String[] args) {
		createMatrix(5);
	}
	
	public static int[][] createMatrix(int n) {
		int[][] matrix = new int[n][n];// n*n�Ķ�ά���飬��ʼԪ��ֵ��Ϊ0
		int right = 1, down = 2, left = 3, up = 4;// �ֱ��ʾ���������ĸ�����
		int direction = right;

		int numb = n * n;// n�׾��󣬹���n��n����
		int i = 0, j = 0;
		for (int p = 1; p <= numb; p++) {
			matrix[i][j] = p;
			// �жϷ������ҵ����
			if (direction == right) {
				// �����ǰλ�õ�����λ�����ұ߽�����ֵ���ǳ�ʼֵ�����в��䣬�к������ƶ�һλ
				if (j + 1 < n && matrix[i][j + 1] == 0) {
					j++;
				} else {// ��������ұ߽߱磬���������Ԫ���Ѿ����޸Ĺ����������ƶ�һ�У��ҽ������Ϊ����
					i++;
					direction = down;
					continue;
				}
			}
			// �жϷ������µ����
			if (direction == down) {
				// �����ǰλ�õ�����λ�����±߽�����ֵ���ǳ�ʼֵ�����в��䣬�к������ƶ�һλ
				if (i + 1 < n && matrix[i + 1][j] == 0) {
					i++;
				} else {// ��������±߽磬���������Ԫ���Ѿ����޸Ĺ����������ƶ�һ�У��ҽ������Ϊ����
					j--;
					direction = left;
					continue;
				}
			}
			// �жϷ�����������
			if (direction == left) {
				// �����ǰλ�õ�����λ������߽�����ֵ���ǳ�ʼֵ�����в��䣬�к������ƶ�һλ
				if (j - 1 >= 0 && matrix[i][j - 1] == 0) {
					j--;
				} else {// ���������߽磬���������Ԫ���Ѿ����޸Ĺ����������ƶ�һ�У��ҽ������Ϊ����
					i--;
					direction = up;
					continue;
				}
			}
			// �жϷ������ϵ����
			if (direction == up) {
				// �����ǰλ�õ�����λ�����ϱ߽�����ֵ���ǳ�ʼֵ�����в��䣬�к������ƶ�һλ
				if (i - 1 >= 0 && matrix[i - 1][j] == 0) {
					i--;
				} else {// ��������ϱ߽磬���������Ԫ���Ѿ����޸Ĺ����������ƶ�һ�У��ҽ������Ϊ����
					j++;
					direction = right;
					continue;
				}
			}
		}
		return matrix;
	}

}
