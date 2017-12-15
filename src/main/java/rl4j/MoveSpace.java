package rl4j;

import java.util.Random;

import controller.Controller;
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
        int index = this.random.nextInt(this.getSize());
        return this.model.moveList[index];
    }

    @Override
    public void setSeed(int seed) {
        this.random = new Random(seed);
    }

    @Override
    public Object encode(Controller.Move move) {
        return move;
    }

    @Override
    public int getSize() {
        return this.model.moveList.length;
    }

    @Override
    public Controller.Move noOp() {
        //This statement should never be reached, i think !!!
        return null;
    }
}
