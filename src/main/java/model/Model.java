package model;

import controller.Controller;
import org.deeplearning4j.rl4j.space.Encodable;
import java.util.Arrays;

public class Model implements Encodable {

    public Controller.Move[] moveList;
    public int[] values;
    public int score;
    public boolean win;
    public boolean lose;

    public Model(int size) {
        this.values = new int[size * size];
        this.score = 0;
        this.win = false;
        this.lose = false;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.values);
    }

    @Override
    public double[] toArray() {
        // TODO: implement method
        return new double[0];
    }
}
