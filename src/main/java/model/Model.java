package model;

import Util.MoveUtil;
import controller.GameLogic;
import org.apache.commons.lang3.ArrayUtils;
import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import rl.Trainable;

import java.util.Arrays;

public class Model implements Trainable {

    public int[] values;
    public int totalScore;
    public int reward;

    public static final int SIZE = 4;

    public Model() {
        this.values = new int[SIZE * SIZE];
        this.totalScore = 0;
        this.reward = 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.values);
    }

    public Model clone(){
        Model m = new Model();
        m.values = ArrayUtils.clone(this.values);
        m.totalScore = this.totalScore;
        m.reward = this.reward;
        return m;
    }

    @Override
    public int[] getActionMask() {
        return MoveUtil.toIntArray(GameLogic.getPossibleMoves(this));

    }

    @Override
    public Trainable doMove(int move) {
        Model model = GameLogic.doMove(this, MoveUtil.getMove(move));
        GameLogic.addTile(model);
        return model;
    }

    @Override
    public float getReward() {
        return reward;
    }

    @Override
    public float getScore(){
        return totalScore;
    }

    @Override
    public INDArray encode() {
        return Nd4j.create(toArray());
    }

    @Override
    public boolean hasTerminated(){
        return GameLogic.hasWon(this) || GameLogic.hasLost(this);
    }

    public double[] toArray() {
        double[] array = new double[values.length];
        for(int i = 0; i<values.length; i++){
            array[i] = values[i];
        }
        return array;
    }
}
