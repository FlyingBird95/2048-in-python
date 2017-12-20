package rl4j;

import model.Model;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class ObservationSpace2048 implements ObservationSpace<Model> {

    private Model model;

    private String name;
    private INDArray low;
    private INDArray high;

    public ObservationSpace2048(Model model){
        this.model = model;
        this.name = "Not sure";
        low = Nd4j.create(1);
        high = Nd4j.create(2);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int[] getShape() {
        return new int[]{this.model.toArray().length};
    }

    @Override
    public INDArray getLow() {
        return this.low;
    }

    @Override
    public INDArray getHigh() {
        return this.high;
    }
}