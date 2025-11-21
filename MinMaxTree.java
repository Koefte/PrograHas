public class MinMaxTree {
    private GameState state;
    private final boolean min;

    private MinMaxTree[] successors;

    public MinMaxTree(boolean min,GameState state){
        this.min = min;
        this.state = state;

        this.successors = null;
    }

    public MinMaxTree[] getSuccessors(){
        return successors;
    }

    public MinMaxTree unfold(){
        MinMaxTree ergebnis = new MinMaxTree(min,state);
        GameState[] possibleStates = state.getSuccessors(min);
        if (possibleStates == null || possibleStates.length == 0){
            ergebnis.successors = null;
            return ergebnis;
        }
        ergebnis.successors = new MinMaxTree[possibleStates.length];
        for (int i = 0; i < possibleStates.length; i++){
            ergebnis.successors[i] = new MinMaxTree(!min,possibleStates[i]);    
        }
        return ergebnis;

    }

    public MinMaxTree unfold(int depth){
        MinMaxTree ergebnis = new MinMaxTree(min,state);
        if(depth == 0) return ergebnis;
        ergebnis = ergebnis.unfold();
        if(ergebnis.successors == null) return ergebnis;

        for(MinMaxTree succ : ergebnis.successors){
            succ.unfold(depth-1);
        }
        return ergebnis;


    }

    public int score(){
        if(successors == null){
            return state.evaluate();
        }
        if(min){
            int min = Integer.MIN_VALUE;
            for(MinMaxTree succ : this.successors){
                if(min > succ.score()) min = succ.score();
                
            }
            return min;
        }
        else{
            int max = Integer.MAX_VALUE;
            for(MinMaxTree succ: this.successors){
                if(max < succ.score()) max = succ.score();
            }
            return max;
        }
    }

    public MinMaxTree makeMove(){
        MinMaxTree ergebnis = null;
        if(this.successors == null) throw new RuntimeException("No succesors on current tree");
        if(min){
            int min = Integer.MIN_VALUE;
            for(MinMaxTree succ: this.successors){
                if(min > succ.score()) {
                    min = succ.score();
                    ergebnis = succ;
                }
            }
        }
        else{
            int max = Integer.MAX_VALUE;
            for(MinMaxTree succ: this.successors){
                if(max > succ.score()){
                    max = succ.score();
                    ergebnis = succ;
                }
            }
        }
        return ergebnis;
    }



    public GameState getState(){
        return state;
    }

    public boolean isMin(){
        return min;
    }
}