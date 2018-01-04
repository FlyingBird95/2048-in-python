package model;

import Util.MoveUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.deeplearning4j.rl4j.space.Encodable;

import java.util.Arrays;

public class Model implements Encodable {

    public int[] values;
    public int totalScore;
    public int reward;
    private MoveUtil.Move lastMove;

    public static final int SIZE = 4;

    public Model() {
        this.values = new int[SIZE * SIZE];
        this.totalScore = 0;
        this.reward = 0;
        lastMove = null;
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
        m.lastMove = this.lastMove;
        return m;
    }

    public double[] toArray() {
        double[] array = new double[values.length];
        for(int i = 0; i<values.length; i++){
            array[i] = values[i];
        }
        return array;
    }
}
