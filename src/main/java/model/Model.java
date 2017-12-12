package model;

import controller.Controller;
import org.deeplearning4j.rl4j.space.Encodable;
import java.util.Arrays;

public class Model implements Encodable {

    public Controller.Move[] moveList;
    public int[] values;
    public int totalScore;
    public int previousReward;
    public boolean win;
    public boolean lose;

    public Model(int size) {
        this.values = new int[size * size];
        this.totalScore = 0;
        this.previousReward = 0;
        this.win = false;
        this.lose = false;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.values);
    }

    @Override
    public double[] toArray() {
        // Convert model to double array
        return Arrays.stream(this.values).asDoubleStream().toArray();
    }
}
