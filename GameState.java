
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    private Tile[][] board;

    public GameState(){
        board = new Tile[3][3];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = Tile.BLANK;
            }
        }
    }

    public GameState setPosition(int row, int column, Tile overwrite){
        if (overwrite == Tile.BLANK)
            throw new AssertionError("No new information");

        GameState res = new GameState();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (i == row && j == column){
                    if (board[i][j] != Tile.BLANK)
                        throw new AssertionError("Tile already set");

                    res.board[i][j] = overwrite;
                } else {
                    res.board[i][j] = board[i][j];
                }
            }
        }

        return res;
    }

    public GameState parseAndSetPosition(String input, boolean min_turn){
        int row = input.charAt(0) - 'A';
        char column_indicator = input.charAt(1);
        if (row < 0 || row >= board.length || !Character.isDigit(column_indicator))
            return null;

        int column = Integer.parseInt(""+column_indicator) - 1;
        if (column < 0 || column >= board[0].length)
            return null;

        return setPosition(
                row,
                column,
                min_turn ? Tile.MIN : Tile.MAX
        );
    }

    public Tile checkWin(){
        // check rows
        for (int row = 0; row < board.length; row++) {
            Tile winner = board[row][0];
            for (int column = 1; column < board[0].length; column++) {
                if (board[row][column] != winner) {
                    winner = Tile.BLANK;
                    break;
                }
            }
            if (winner != Tile.BLANK)
                return winner;
        }

        // check columns
        for (int column = 0; column < board[0].length; column++) {
            Tile winner = board[0][column];
            for (int row = 1; row < board.length; row++) {
                if (board[row][column] != winner) {
                    winner = Tile.BLANK;
                    break;
                }
            }
            if (winner != Tile.BLANK)
                return winner;
        }

        // check if board is quadratic
        if (board.length != board[0].length)
            // no diagonal winners in non quadratic tic tac toe!
            return Tile.BLANK;

        // check diagonal top left to bottom right
        Tile winner = board[0][0];
        for (int i = 1; i < board.length; i++) {
            if (board[i][i] != winner){
                winner = Tile.BLANK;
                break;
            }
        }
        if (winner != Tile.BLANK)
            return winner;

        // check diagonal bottom left to top right
        winner = board[board.length - 1][0];
        for (int i = 1; i < board.length; i++) {
            if (board[board.length - 1 - i][i] != winner){
                winner = Tile.BLANK;
                break;
            }
        }
        return winner;
    }

    public int evaluate(){
        // simplest heuristic: only check if someone has won
        switch (checkWin()){
            case MAX -> {return 1;}
            case MIN -> {return -1;}
        }

        return 0;
    }

    public GameState[] getSuccessors(boolean min_turn){
        if (checkWin() != Tile.BLANK){
            return new GameState[0];
        }

        List<GameState> res = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == Tile.BLANK)
                    res.add(setPosition(i, j, min_turn ? Tile.MIN : Tile.MAX));
            }
        }
        return res.toArray(new GameState[0]);
    }

    public String toString(){
        StringBuilder res = new StringBuilder();

        // print column indicators
        res.append(" ");
        for (int i = 0; i < board[0].length; i++) {
            res.append("   ").append(i+1);
        }
        res.append("\n");


        // print state with row indicators
        res.append("  ").repeat("-", board[0].length*4 + 1);
        res.append("\n");

        char row_indicator = 'A';

        for (int i = 0; i < board.length; i++) {
            //res.repeat("|   ", board[0].length).append("|\n");
            res.append(row_indicator++).append(" ");
            for (int j = 0; j < board[0].length; j++) {
                res.append("| ");
                res.append(switch (board[i][j]){
                    case BLANK -> " ";
                    case MIN ->   "X";
                    case MAX ->   "O";
                });
                res.append(" ");
            }
            res.append("|\n");
            //res.repeat("|   ", board[0].length).append("|\n");
            res.append("  ").repeat("-", board[0].length*4 + 1);
            res.append("\n");
        }

        return res.toString();
    }

    public static void main(String[] args){
        GameState test = new GameState()
                .setPosition(0, 2, Tile.MIN)
                .setPosition(1, 1, Tile.MIN)
                .setPosition(2, 0, Tile.MIN);
        System.out.println(test.toString());
        System.out.println(test.checkWin());
    }
}
