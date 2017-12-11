package controller;

import java.util.Random;

import model.Model;
import org.deeplearning4j.rl4j.space.ActionSpace;

public class MoveSpace implements ActionSpace<Controller.Move>{

    private Random random;
    private Model model;

    public MoveSpace(Model model){
        this.model = model;
        this.random = new Random();
    }

    @Override
    public Controller.Move randomAction() {
//        return this.random.nextInt(this.getSize());
        return null;
    }

    @Override
    public void setSeed(int seed) {
        this.random = new Random(seed);
    }

    @Override
    public Object encode(Controller.Move move) {
        //        return this.model.potentialMoves[integer];
        return null;
    }

    @Override
    public int getSize() {
//        return this.model.potentialMoves.length;
        return -1;
    }

    @Override
    public Controller.Move noOp() {
        //This statement should never be reached, i think !!!
        return null;
    }
}
