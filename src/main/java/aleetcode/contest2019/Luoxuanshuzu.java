package aleetcode.contest2019;

import com.alibaba.fastjson.JSONObject;

public class Luoxuanshuzu {

    public static void main(String[] args) {
        int[][] matrix = createMatrix2(5);
        System.out.println(JSONObject.toJSONString(matrix));
    }

    /**
     * [[1,2,3],[4,5,6],[7,8,9]]
     *
     * @param n
     * @return
     */
    public static int[][] createMatrix2(int n) {
        int[][] arr = new int[n][n];

        int right = 1;
        int down = 2;
        int left = 3;
        int up = 4;

        int i = 0;
        int j = 0;

        int dre = right;
        for (int p = 1; p <= n * n; p++) {

            arr[i][j] = p;
            if (dre == right) {
                if (j + 1 < n && arr[i][j + 1] == 0) {
                    j++;
                } else {
                    i++;
                    dre = down;
                    continue;
                }
            }
            if (dre == down) {
                if (i + 1 < n && arr[i + 1][j] == 0) {
                    i++;
                } else {
                    j--;
                    dre = left;
                    continue;
                }
            }
            if (dre == left) {
                if (j - 1 >= 0 && arr[i][j - 1] == 0) {
                    j--;
                } else {
                    i--;
                    dre = up;
                    continue;
                }
            }
            if (dre == up) {
                if (i - 1 >= 0 && arr[i - 1][j] == 0) {
                    i--;
                } else {
                    j++;
                    dre = right;
                    continue;
                }
            }
        }

        return arr;

    }

    public static int[][] createMatrix(int n) {
        int[][] matrix = new int[n][n];
        int right = 1, down = 2, left = 3, up = 4;
        int direction = right;

        int numb = n * n;
        int i = 0, j = 0;
        for (int p = 1; p <= numb; p++) {
            matrix[i][j] = p;

            if (direction == right) {
                if (j + 1 < n && matrix[i][j + 1] == 0) {
                    j++;
                } else {
                    i++;
                    direction = down;
                    continue;
                }
            }
            if (direction == down) {
                if (i + 1 < n && matrix[i + 1][j] == 0) {
                    i++;
                } else {
                    j--;
                    direction = left;
                    continue;
                }
            }

            if (direction == left) {
                if (j - 1 >= 0 && matrix[i][j - 1] == 0) {
                    j--;
                } else {
                    i--;
                    direction = up;
                    continue;
                }
            }
            if (direction == up) {
                if (i - 1 >= 0 && matrix[i - 1][j] == 0) {
                    i--;
                } else {
                    j++;
                    direction = right;
                    continue;
                }
            }
        }
        return matrix;
    }

}
