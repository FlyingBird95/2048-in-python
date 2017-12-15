package controller;

import org.deeplearning4j.rl4j.space.DiscreteSpace;

import java.util.Random;

public class MoveSpace extends DiscreteSpace{

    private Random random;
    private Controller controller;

    public MoveSpace(Controller controller){
        super(Util.Move.values().length);
        this.controller = controller;
        this.random = new Random();
    }

    @Override
    public Integer randomAction() {
        // Choose an integer from list of possible moves
        int[] moves = Util.toIntArray(controller.getPossibleMoves());
        int index = this.random.nextInt(moves.length);
        return moves[index];
    }

    @Override
    public void setSeed(int seed) {
        this.random = new Random(seed);
    }

    @Override
    public Object encode(Integer integer) {
        return integer;
    }

    @Override
    public int getSize() {
        return controller.getPossibleMoves().length;
    }

    @Override
    public Integer noOp() {
        //This statement should never be reached, i think !!!
        throw new IllegalArgumentException("Shouldn't call this method");
    }
}
