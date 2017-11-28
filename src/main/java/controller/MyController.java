package controller;

import model.MyModel;

/**
 * Created by Willem on 28-11-2017.
 */
public class MyController {

    protected MyModel model;

    public MyController(MyModel m){
        this.model = m;
    }

    public void doMove(int move) {
        // Calculate score
    }

    public int[] getMoves(){
        // Calculate possible moves
        return null;
    }


}
