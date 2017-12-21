package model;

import Util.MoveUtil;
import controller.Controller;
import org.apache.commons.lang3.ArrayUtils;
import org.deeplearning4j.rl4j.space.Encodable;

import java.util.Arrays;

public class Model {

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
}
