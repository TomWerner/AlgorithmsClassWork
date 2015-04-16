package com.example;

/**
 * Created by test on 4/4/15.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class fourinarow {

    private static final int playerX = 1;
    private static final int playerO = 2;
    static int turn = 1;

    private static int opponent(int player) {
        return 3-player;
    }

    private static int fromLevel(int level) {
        if (level % 2 == 1) return playerX; else return playerO;
        // if level is odd, return playerX; else ...
    }

    class Point {

        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void copy(Point p) {
            this.x = p.x;
            this.y = p.y;
        }
    }

    // A board is 3x3 array whose values are 0 (empty),
    // 1 (taken by playerX), or 2 (taken by playerO)
    int[][] board = new int[5][5];

    Point bestMove = new Point(0, 0);

    //Get the playable locations available on the board
    public List<Point> getAvailablePositions() {
        List<Point> availablePoints = new ArrayList<Point>();

        for (int i = 0; i < 5; ++i)
            for (int j = 0; j < 5; ++j)
                if (board[i][j] == 0 && (neighborsPiece(i, j)  || turn == 1)) {
                    availablePoints.add(new Point(i, j));
                }
        return availablePoints;
    }

    private boolean neighborsPiece(int row, int col) {

        return (get(row - 1, col - 1) +
                get(row - 1, col) +
                get(row - 1, col + 1) +
                get(row + 1, col - 1) +
                get(row + 1, col) +
                get(row + 1, col + 1) +
                get(row, col - 1) +
                get(row, col + 1)) > 0;
    }

    public boolean isGameOver() {
        // Game is over when someone has won, or board is full (draw)
        return (hasWon(playerX) || hasWon(playerO) || getAvailablePositions().isEmpty());
    }

    public int get(int row, int col) {
        if (row < 0) row = 0;
        if (row >= 5) row = (5 - 1);
        if (col < 0) col = 0;
        if (col >= 5) col = (5 - 1);
        return board[row][col];
    }

    public boolean hasWon(int piece) {
        int inARowToWin = 4;
        int boardSize = 5;
        //Check horizontal
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize - inARowToWin + 1; col++) {
                if (get(row, col) == piece) {
                    boolean win = true;
                    for (int i = 0; i < inARowToWin; i++)
                        if (get(row, col + i) != piece)
                            win = false;
                    if (win)
                        return true;
                }

            }
        }

        //Check vertical
        for (int row = 0; row < boardSize - inARowToWin + 1; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (get(row, col) == piece) {
                    boolean win = true;
                    for (int i = 0; i < inARowToWin; i++)
                        if (get(row + i, col) != piece)
                            win = false;
                    if (win)
                        return true;
                }

            }
        }

        //Check diagonal one
        for (int row = 0; row < boardSize - inARowToWin + 1; row++) {
            for (int col = 0; col < boardSize - inARowToWin + 1; col++) {
                if (get(row, col) == piece) {
                    boolean win = true;
                    for (int i = 0; i < inARowToWin; i++)
                        if (get(row + i, col + i) != piece)
                            win = false;
                    if (win)
                        return true;
                }
            }
        }

        //Check diagonal two
        for (int row = 0; row < boardSize - inARowToWin + 1; row++) {
            for (int col = inARowToWin - 1; col < boardSize; col++) {
                if (get(row, col) == piece) {
                    boolean win = true;
                    for (int i = 0; i < inARowToWin; i++)
                        if (get(row + i, col - i) != piece)
                            win = false;
                    if (win)
                        return true;
                }

            }
        }


        return false;
    }

    public void makeMove(Point point, int player) {
        board[point.x][point.y] = player;   //player = 1 for X, 2 for O
    }

    public void makeMove(int x, int y, int player) {
        board[x][y] = player;   //player = 1 for X, 2 for O
    }

    public void undoMove(Point point) {
        board[point.x][point.y] = 0;
    }

    //The actual MinMax idea is in this function
    public int minimaxScore (int depth) {
        List<Point> listPoints = getAvailablePositions();

        if (hasWon(playerX)) {
            return +10;
        }
        else if (hasWon(playerO)) {
            return -10;
        }
        else if (listPoints.isEmpty()) {
            return 0;
        }
        else if (depth == 7) {
            int player = fromLevel(depth);
            if (player == playerX)
                return evaluate(player);
            else
                return -evaluate(player);
        }
        else {
            int bestScore, score, player;
            player = fromLevel(depth);
            if (player == playerX) bestScore = -9; else bestScore = 9;

            // Rule 1: If this move is a winning position, take it.
            for (Point point : listPoints) {
                this.makeMove(point, player);
                if (hasWon(player)) {
                    this.undoMove(point);
                    if (player == playerX) {
                        if (depth == 1) bestMove.copy(point);
                        return +8;
                    } else return -8;
                }
                this.undoMove(point);
            }


            // Rule 2: If this is the opponent's winning move, block it.
            for (Point point : listPoints) {
                this.makeMove(point, opponent(player));
                if (hasWon(opponent(player))) {
                    // We have to take this position, but we don't know the score of this move
                    this.makeMove(point, player);
                    if (player == playerX && depth == 1) bestMove.copy(point);
                    score = this.minimaxScore(depth+1);
                    this.undoMove(point);
                    return score;
                }
                this.undoMove(point);
            }

            // Rule 5: Now minimax rules
            for (Point point : listPoints) {
                this.makeMove(point, player);
                score = this.minimaxScore(depth+1);

                if (player == playerX) {
                    //if (depth < 5) System.out.println("1 score("+point.x+","+point.y+") = "+score);
                    if (score > bestScore) {
                        if (depth == 1) bestMove.copy(point);
                        bestScore = score;
                    }
                } else {
                    if (score < bestScore) bestScore = score;
                    //if (depth < 5) System.out.println("2 score("+point.x+","+point.y+") = "+score);
                }

                this.undoMove(point);
            }
            return bestScore;
        }
    }

    public int countInARow(int piece, int inARowToWin, int numInARow) {
        int count = 0;
        //Check horizontal
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5 - inARowToWin + 1; col++) {
                if (get(row, col) == piece) {
                    boolean win = true;
                    int subcount = 0;
                    for (int i = 0; i < inARowToWin; i++) {
                        if (get(row, col + i) == opponent(piece))
                            win = false;
                        if (get(row, col + i) == (piece))
                            subcount++;
                    }
                    if (subcount == numInARow)
                        count++;
                }

            }
        }

        //Check vertical
        for (int row = 0; row < 5 - inARowToWin + 1; row++) {
            for (int col = 0; col < 5; col++) {
                if (get(row, col) == piece) {
                    boolean win = true;
                    int subcount = 0;
                    for (int i = 0; i < inARowToWin; i++) {
                        if (get(row + 1, col) == opponent(piece))
                            win = false;
                        if (get(row + 1, col) == (piece))
                            subcount++;
                    }
                    if (subcount == numInARow)
                        count++;
                }

            }
        }

        //Check diagonal one
        for (int row = 0; row < 5 - inARowToWin + 1; row++) {
            for (int col = 0; col < 5 - inARowToWin + 1; col++) {
                if (get(row, col) == piece) {
                    boolean win = true;
                    int subcount = 0;
                    for (int i = 0; i < inARowToWin; i++) {
                        if (get(row + i, col + i) == opponent(piece))
                            win = false;
                        if (get(row + i, col + i) == (piece))
                            subcount++;
                    }
                    if (subcount == numInARow)
                        count++;
                }
            }
        }

        //Check diagonal two
        for (int row = 0; row < 5 - inARowToWin + 1; row++) {
            for (int col = inARowToWin - 1; col < 5; col++) {
                if (get(row, col) == piece) {
                    boolean win = true;
                    int subcount = 0;
                    for (int i = 0; i < inARowToWin; i++) {
                        if (get(row + i, col - i) == opponent(piece))
                            win = false;
                        if (get(row + i, col - i) == (piece))
                            subcount++;
                    }
                    if (subcount == numInARow)
                        count++;
                }

            }
        }


        return count;
    }
    
    private int evaluate(int player) {
        if (countInARow(player, 4, 3) > 1)
            return +7;
        else if (countInARow(opponent(player), 4, 3) > 1)
            return -7;
        else if (countInARow(player, 4, 3) > 0)
            return +6;
        else if (countInARow(opponent(player), 4, 3) > 0)
            return -6;
        else if (countInARow(player, 4, 2) > 1)
            return +5;
        else if (countInARow(opponent(player), 4, 2) > 1)
            return -5;
        else if (countInARow(player, 4, 2) > 0)
            return +4;
        else if (countInARow(opponent(player), 4, 2) > 0)
            return -4;
        return 0;
    }

    public void displayBoard() {
        System.out.println();
        System.out.println("  +---+---+---+---+---+");
        for (int row = 0; row < 5; row++) {
            System.out.print((5 - row - 1) + " ");
            for (int col = 0; col < 5; col++) {
                System.out.print("| " + ((board[row][col] == 0) ? " " : ((board[row][col] == playerX) ? "X" : "O")) + " ");
            }
            System.out.println("|");
            System.out.println("  +---+---+---+---+---+");
        }
        System.out.println("    0   1   2   3   4");
    }

    void placeFirstMove() {
        Random rand = new Random();
        Point p = new Point(rand.nextInt(5), rand.nextInt(5));
        makeMove(p, playerX);
    }

    public static void main(String[] args) {

        fourinarow b = new fourinarow();
        b.displayBoard();
        Scanner scan = new Scanner(System.in);

        try {

            System.out.println("Who's gonna move first? (1) You : (2) Rot? ");
            int choice = scan.nextInt();

            if (choice == 2) {
                b.placeFirstMove();
                turn++;
                b.displayBoard();
            }

            while (!b.isGameOver()) {
                System.out.println("Your move (row=0-4; col=0-4): ");
                int y = scan.nextInt();
                int x = 4 - scan.nextInt();
                if (x < 0 || x > 4 || y < 0 || y > 4 || b.board[x][y] != 0) {
                    System.out.println("Invalid position! Try again.");
                    continue;
                }
                b.makeMove((x), (y), playerO);
                turn++;
                if (b.isGameOver()) break;
                b.displayBoard();

                System.out.println("\nThinking...");
                choice = b.minimaxScore(1);
                System.out.println("Score("+b.bestMove.x+","+b.bestMove.y+") = "+choice);
                b.makeMove(b.bestMove, playerX);
                turn++;
                b.displayBoard();
            }

            if (b.hasWon(playerX)) {
                System.out.println("Unfortunately, you lost!");
            } else if (b.hasWon(playerO)) {
                System.out.println("Segmentation fault. Computer always wins or draws :)\n");
            } else {
                System.out.println("It's a draw!");
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        scan.close();
    }
}
