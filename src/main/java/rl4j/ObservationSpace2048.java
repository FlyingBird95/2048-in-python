package rl4j;

import model.Model;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.nd4j.linalg.api.ndarray.INDArray;

public class ObservationSpace2048 implements ObservationSpace<Model> {

    @Override
    public String getName() {
        return "game-2048";
    }

    @Override
    public int[] getShape() {
        return new int[]{16};
    }

    @Override
    public INDArray getLow() {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public INDArray getHigh() {
        throw new RuntimeException("Method not implemented");
    }
}
