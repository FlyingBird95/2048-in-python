package rl4j;

import Util.MoveUtil;
import org.deeplearning4j.rl4j.space.DiscreteSpace;

import controller.Controller;


public class MoveSpace extends DiscreteSpace{

    private Controller controller;

    public MoveSpace(Controller controller){
        super(MoveUtil.Move.values().length);
        this.controller = controller;
    }

    @Override
    public Integer randomAction() {
        // Choose an integer from list of possible moves
        int[] moves = MoveUtil.toIntArray(controller.getPossibleMoves());
        int index = this.rd.nextInt(moves.length);
        return moves[index];
    }

    @Override
    public int getSize() {
        return controller.model.moveList.length;
    }

    @Override
    public Integer noOp() {
        throw new IllegalArgumentException("Shouldn't call this method");
    }
}
