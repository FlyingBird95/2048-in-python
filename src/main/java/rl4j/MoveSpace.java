package rl4j;

import Util.MoveUtil;
import org.deeplearning4j.rl4j.space.DiscreteSpace;

public class MoveSpace extends DiscreteSpace {

    public MoveSpace() {
        super(MoveUtil.Move.values().length); // number of possible moves
    }
}
