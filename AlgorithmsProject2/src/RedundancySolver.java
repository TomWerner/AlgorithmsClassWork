
public class RedundancySolver
{
    public RedundancySolver()
    {
        
    }
    
    public void findRedundancies(int[][] board)
    {
        new SudokuSolver().displaySudoku(board);
        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                if (board[row][col] != 0)
                {
                    int oldVal = board[row][col];
                    board[row][col] = 0; //Empty
                    int solutions = new SudokuSolver().solveSudoku(board);
                    if (solutions == 1)
                        System.out.println(oldVal + " at row " + row + " , col " + col + " is redundant.");
                    else
                        board[row][col] = oldVal;
                }
            }
        }
    }
}
