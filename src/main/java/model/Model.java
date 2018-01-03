package model;

import Util.MoveUtil;
import controller.Controller;
import org.apache.commons.lang3.ArrayUtils;
import org.deeplearning4j.rl4j.space.Encodable;

import java.util.Arrays;

public class Model implements Encodable {

    public MoveUtil.Move[] moveList;
    public int[] values;
    public int totalScore;
    public int previousReward;
    public boolean win;
    public boolean lose;

    public static final int SIZE = 4;

    public Model() {
        this.values = new int[SIZE * SIZE];
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
        int[] VALUES = new int[]{2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 0};

        double[] array = new double[this.values.length * VALUES.length];

        for(int i = 0; i<values.length; i++){
            double[] bitarray = getBitArray(this.values[i], VALUES);
            for(int j = 0; j<bitarray.length; j++){
                array[i*VALUES.length + j] = bitarray[j];
            }
        }

//        System.out.println(ArrayUtils.toString(array));
        return array;
    }

    private double[] getBitArray(int value, int[] values) {
        double[] array = new double[values.length];
        for(int i = 0; i<values.length; i++){
            array[i] = (values[i] == value) ? 1 : 0;
        }
        return array;
    }

    public Model clone(){
        Model m = new Model();
        m.moveList = ArrayUtils.clone(this.moveList);
        m.values = ArrayUtils.clone(this.values);
        m.totalScore = this.totalScore;
        m.previousReward = this.previousReward;
        m.win = this.win;
        m.lose = this.lose;
        return m;
    }
}
