package controller;

import model.MyModel;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Observable;
import java.util.Random;

/**
 * Created by Willem on 28-11-2017.
 */
public class MyController extends Observable {

    // 0 = Up, 1 = Right, 2 = Down, 3 = Left
    protected final int[] allMoves = {0, 1, 2, 3};
    protected final int[] horizontalMoves = {1, 3};
    protected final int[] verticalMoves = {0, 2};
    protected final int[] noMoves = {};

    public MyModel model;

    public final int SIZE;
    private static final int TARGET = 2048;

    public boolean myWin = false;
    public boolean myLose = false;
    private int myScore = 0;

    public MyController(MyModel m, int size){
        this.model = m;
        this.SIZE = size;
    }

    public void resetModel(){
        myScore = 0;
        myWin = false;
        myLose = false;

        this.model.values = new int[SIZE * SIZE];
        this.addTile();
        this.addTile();
    }

    protected void rotate(int angle) {
        int[] newTiles = new int[SIZE * SIZE];
        int offsetX = SIZE - 1, offsetY = SIZE - 1;
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }

        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                newTiles[(newX) + (newY) * SIZE] = tileAt(x, y);
            }
        }
        this.model.values = newTiles;
    }

    public void doMove(int move) {
        switch (move){
            case 0: //Up
                this.rotate(270);
                this.doMove();
                this.rotate(90);
                break;
            case 1: //Right
                this.rotate(180);
                this.doMove();
                this.rotate(180);
                break;
            case 2: //Down
                this.rotate(90);
                this.doMove();
                this.rotate(270);
                break;
            case 3: //Left
                this.doMove();
                break;
        }
        this.addTile();
        this.modelChanged();
    }

    protected void doMove(){
        int[] line = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            this.getLine(i, line);
            this.myScore += this.mergeLine(line);
            this.setLine(i, line);
        }
    }

    protected void getLine(int index, int[] output) {
        System.arraycopy(this.model.values, index * SIZE, output, 0, SIZE);
    }

    protected void setLine(int index, int[] input) {
        System.arraycopy(input, 0, this.model.values, index * SIZE, SIZE);
    }

    protected int mergeLine(int[] line) {
        int[] list = this.trimLine(line);
        Arrays.fill(line, 0);

        int move = 0;
        int score = 0;
        for (int i = 0; i < SIZE && list[i] != 0; i++) {
            int current = list[i];

            if(i < (SIZE - 1) && current == list[i + 1]){
                current *= 2;
                i++;
            }

            line[move++] = current;
        }
        return score;
    }

    protected int[] trimLine(int[] line){
        int[] output = new int[SIZE];
        int counter = 0;
        for (int i = 0; i < SIZE; i++)
            if (line[i] != 0)
                output[counter++] = line[i];
        return output;
    }

    public void PossibleMoves(){
        if(!this.isFull()) {
            this.model.potentialMoves = this.allMoves.clone();
            return;
        }
        // Calculate possible moves
    }

    private void addTile() {
        if(this.isFull())
            return;

        int count = 0;
        int[] resultingIndices = new int[this.model.values.length];
        for (int i = 0; i < this.model.values.length; i++) {
            if (0 == this.model.values[i]) {
                resultingIndices[count++] = i;
            }
        }

        int rnd = new Random().nextInt(count);
        this.model.values[resultingIndices[rnd]] = Math.random() < 0.9 ? 2 : 4;
    }

    private boolean isFull() {
        return !this.contains(0);
    }

    private boolean contains(int value) {
        return ArrayUtils.contains(this.model.values, value);
    }

    private int tileAt(int x, int y) {
        return this.model.values[x + y * SIZE];
    }

    public boolean canMove() {
        if (!isFull()) {
            return true;
        }
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int i = tileAt(x, y);
                if ((x < 3 && i == tileAt(x + 1, y))
                        || ((y < 3) && i == tileAt(x, y + 1))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void modelChanged() {
        setChanged();
        notifyObservers(this);
    }
}