import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SudokuSolver
{

    private int[][]  board;
    public final int empty     = 0;
    public final int success   = -100;
    public final int fail      = -1;
    public long      calls     = 0;
    private int      successes = 1;

    public SudokuSolver()
    {
        successes = 1;
    }

    public void displaySudoku()
    {
        for (int i = 0; i < 9; i++)
        {
            System.out.println(" -------------------------------------");
            for (int j = 0; j < 9; j++)
            {
                if (board[i][j] > 0)
                    System.out.print(" | " + board[i][j]);
                else
                    System.out.print(" |  ");
            }
            System.out.println(" |");
        }
        System.out.println(" -------------------------------------");
    }

    public int next(int pos)
    {
        // pos: the last four bits are column number and the next four bits are row number.
        // look for next open position

        // fix for some java compilers which handle -1 as bit vector wrong.
        if (pos == -1)
        {
            if (board[0][0] == empty)
                return 0;
            else
                pos = 0;
        }

        int col = pos & 15;
        int row = (pos >> 4) & 15;

        while (true)
        {
            ++col;
            if (col >= 9)
            {
                col = 0;
                row++;
            }
            if (row >= 9)
                return success; // no more open positions
            if (board[row][col] <= empty)
                return (row << 4) + col;
        }
    }

    public boolean feasible(int row, int col, int k)
    {
        // check if k is feasible at position <row, col>
        for (int i = 0; i < 9; i++)
        {
            if (board[i][col] == k)
                return false; // used in the same column
            if (board[row][i] == k)
                return false; // used in the same row
        }

        int row0 = (row / 3) * 3;
        int col0 = (col / 3) * 3;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i + row0][j + col0] == k)
                    return false; // used in the same region

        return true;
    }

    public int backtrack(int pos)
    {
        // backtrack procedure
        calls++;
        if (pos == success)
        {
            displaySudoku();
            return success;
        }

        // pos: the last four bits are column number and the next four bits are row number.
        int col = pos & 15;
        int row = (pos >> 4) & 15;

        int suc = 0;

        for (int k = 1; k <= 9; k++)
        {
            if (feasible(row, col, k))
            {
                int orig = board[row][col];
                board[row][col] = k;
                if (backtrack(next(pos)) == success)
                {
                    board[row][col] = orig;
                    suc++;
                }
            }
        }
        board[row][col] = empty;
        
        if (suc > 0)
        {
            successes += (suc - 1);
            return success;
        }
        else
            return fail;
    }

    public int solveSudoku(int[][] board)
    {
        this.board = board;

        // Solve the puzzle by backtrack search and display the solution if there is one.
        backtrack(next(-1));
        
        return successes;
    }

    public static void main(String[] args)
    {
        int[][] board = new int[9][9];

        // read in a puzzle
        try
        {
            BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Please enter sudoku puzzle file name: ");
            String filename = read.readLine();

            Scanner scanner = new Scanner(new File(filename));
            for (int i = 0; i < 9; i++)
            {
                for (int j = 0; j < 9; j++)
                { // while(scanner.hasNextInt())
                    board[i][j] = scanner.nextInt();
                }
            }
            System.out.println("Read in:");
            new SudokuSolver().solveSudoku(board);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }
}