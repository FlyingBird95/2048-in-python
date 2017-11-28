package model;

/**
 * Created by T on 28-11-2017.
 */
public class MyModel {

    public int[] values;
    public int[] potentialMoves;
    public int score;

    public MyModel(){
        this.values = new int[16];
        this.score = 0;
    }

//    public int[] getModel(){
//        return values;
//    }
//
//    public void setModel(int[] v){
//        this.values = v;
//    }
//
//    public int[] getPotentialMoves(){
//        return this.potentialMoves;
//    }
}
