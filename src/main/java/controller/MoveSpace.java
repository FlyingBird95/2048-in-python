package controller;

import java.util.Random;

import model.MyModel;
import org.deeplearning4j.rl4j.space.ActionSpace;

/**
 * Created by T on 28-11-2017.
 */
public class MoveSpace implements ActionSpace<Integer>{

    protected Random random;
    protected MyModel model;

    public MoveSpace(MyModel model){
        this.model = model;
        this.random = new Random();
    }

    @Override
    public Integer randomAction() {
        return this.random.nextInt(this.getSize());
    }

    @Override
    public void setSeed(int seed) {
        this.random = new Random(seed);
    }

    @Override
    public Object encode(Integer integer) {
        return this.model.potentialMoves[integer];
    }

    @Override
    public int getSize() {
        return this.model.potentialMoves.length;
    }

    @Override
    public Integer noOp() {
        //This statement should never be reached, i think !!!
        return -1;
    }
}
