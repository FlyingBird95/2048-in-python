package model;

import java.util.Arrays;

/**
 * Created by T on 28-11-2017.
 */
public class MyModel {

    public int[] values;

    public int[] potentialMoves;
    public int score;
    public boolean win = false;
    public boolean lose = false;

    public MyModel(int size) {
        this.values = new int[size * size];
        this.score = 0;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.values);
    }
}
