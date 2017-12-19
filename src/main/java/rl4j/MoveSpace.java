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
        int[] moves = MoveUtil.toIntArray(controller.getPossibleMoves());
        if(moves.length == 0)
            return this.noOp();
        int index = this.rd.nextInt(moves.length);
        return moves[index];
    }

    @Override
    public int getSize() {
        return controller.model.moveList.length;
    }

    @Override
    public Integer noOp() {
        return -1;
    }
}
