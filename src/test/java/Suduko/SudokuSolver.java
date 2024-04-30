package Suduko;

public class SudokuSolver {

    public static boolean isSafe(int[][] board, int row, int col, int num)  {
        int n = board.length;
        // col safe
        for (int i = 0; i < n; i++) {
            if (board[i][col] == num) {
                return false;
            }
        }
        // row safe
        for (int i = 0; i < n; i++) {
            if (board[row][i] == num) {
                return false;
            }
        }
        // box safe
        int sqrt = (int)Math.sqrt(n);
        int rowStart = row-(row%sqrt);
        int colStart = col-(col%sqrt);
        for (int i = rowStart; i < rowStart+sqrt; i++) {
            for (int j = colStart; j < colStart+sqrt; j++) {
                if (board[i][j] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean solveSudoku(int[][] grid, int n) {
        int row = -1;
        int col = -1;
        boolean empty = true;

        // Find first empty
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 0) {
                    row = i;
                    col = j;
                    empty = false;
                    break;
                }
            }
            if (!empty) {
                break;
            }
        }

        // Finished
        if (empty) {return true;}

        // backtrack through rows
        for (int i = 1; i < n+1; i++) {
            if (isSafe(grid, row, col, i)) {
                grid[row][col] = i;
                if (solveSudoku(grid, n)) {
                    return true;
                } else {
                    grid[row][col] = 0;
                }
            }
        }
        return false;
    }
}
