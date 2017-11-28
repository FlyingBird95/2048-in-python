package model;

/**
 * Created by T on 28-11-2017.
 */
public class MyModel {

    protected int[] values = new int[16];
    protected int[] potentialMoves = new int[]{1,2,3,4}; //dummie values
    protected int score;


    public MyModel(){

    }


    public int[] getPotentialMoves(){
        return this.potentialMoves;
    }
}
