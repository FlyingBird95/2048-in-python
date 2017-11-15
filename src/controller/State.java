package controller;

/**
 * A State is used to transfer information from the model to the view.
 * A State can also be used for doing deep learning or iterating all possible moves.
 */
public class State {

    private int[] values;
    private boolean won;
    private boolean lose;
    private int score;

    public State(int[] values, boolean won, boolean lose, int score){
        this.values = values;
        this.won = won;
        this.lose = lose;
        this.score = score;
    }

    public boolean hasWon() {
        return won;
    }

    public boolean hasLost() {
        return lose;
    }

    public int getScore() {
        return score;
    }

    public int getValue(int index){
        return values[index];
    }
}
