
import java.io.*;
public class Main {
    boolean min;
    MinMaxTree state;
    private static final java.io.BufferedReader READER = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

    public Main(boolean min){
        this.min = min;

        state = new MinMaxTree(
                min,
                new GameState()
        );

        state = state.unfold(9);

    }

  

    private void makeAIMove(){
        if (!state.isMin())
            throw new AssertionError();
        state = state.makeMove();
    }

    private boolean makePlayerMove(String input){
        if (state.isMin())
            throw new AssertionError();
        GameState next = state.getState().parseAndSetPosition(input, state.isMin());
        if (next == null)
            return false;
        state = new MinMaxTree(!state.isMin(), next);
        state = state.unfold(9);
        return true;
    }

    private boolean isGameOver(){
        MinMaxTree current = state.unfold();
        return current.getSuccessors() == null || current.getSuccessors().length == 0;
    }

    public void main(){
        System.out.println("randomly determining who starts...");
        if (min)
            System.out.println("the computer makes the first move");
        else
            System.out.println("you make the first move");
        System.out.println(state.getState().toString());
        while (!isGameOver()){
            if (state.isMin()){
                System.out.println("");
                System.out.println("the computer is pondering on its next move...");
                makeAIMove();
                System.out.println("it has decided to make the following move:");
                System.out.println(state.getState().toString());
            } else {
                System.out.println("");
                System.out.println("make your move by typing row and column, e.g. B3, and then hitting enter.");
                String input = readLine();
                while(!makePlayerMove(input)){
                    System.out.println("move not recognized.");
                    System.out.println("make your move by typing row and column, e.g. B3, and then hitting enter.");
                    input = readLine();
                }
                System.out.println("your move changed the game to the following state:");
                System.out.println(state.getState().toString());
            }
        }
        switch(state.getState().checkWin()){
            case BLANK -> {
                System.out.println("it is a draw");
            }
            case MIN -> {
                System.out.println("you were defeated by the computer");
            }
            case MAX -> {
                System.out.println("you dominated the computer and won the game");
            }
        }
    }

    private String readLine(){
        try{
            return READER.readLine();
        } catch (java.io.IOException e){
            return null;
        }
    }

    public static void main(String[] args){
        Main m = new Main(
        //        new Random().nextBoolean()
                false
        );
        m.main();
    }
}
