package Suduko;

import java.util.List;

public class SudokuGrid {
    public int[][] grid;

    public SudokuGrid(List<List<Integer>> sudokuGrid) {
        grid = new int[sudokuGrid.size()][];
        for (int i = 0; i < grid.length; i++) {
            List<Integer> row = sudokuGrid.get(i);
            grid[i] = row.stream().mapToInt(Integer::intValue).toArray();
        }
    }

    public void printGrid() {
        for (int[] i : grid) {
            for (int j : i) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
    }

    public String cellValue(int row, int col) {
        int cell = grid[row][col];
        return String.valueOf(cell);
    }

}
