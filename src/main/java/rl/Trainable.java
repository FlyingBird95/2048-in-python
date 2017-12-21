package rl;

import org.nd4j.linalg.api.ndarray.INDArray;

public interface Trainable {

    /**
     * Must return an new instance of the sub-class.
     */
    Trainable clone();


    /**
     * @return an array with the possibilities of the actions of the current map.
     */
    int[] getActionMask();

    /**
     * Must return a new instance of the model.
     * @param move the move to be taken
     * @return a new instance of the model with the applied move.
     */
    Trainable doMove(int move);

    /**
     *
     * @return a double with the reward value
     */
    float getReward();

    INDArray toArray();

    boolean hasTerminated();

}
