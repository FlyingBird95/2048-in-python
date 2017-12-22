package model;

import Util.MoveUtil;
import controller.Controller;
import controller.GameLogic;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;
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
        return totalScore;
    }

    @Override
    public INDArray toArray() {
        float[] array = new float[values.length];
        for(int i = 0; i<values.length; i++){
            array[i] = values[i];
        }
        return Nd4j.create(array);
    }

    @Override
    public boolean hasTerminated(){
        return GameLogic.hasWon(this) || GameLogic.hasLost(this);
    }
}
