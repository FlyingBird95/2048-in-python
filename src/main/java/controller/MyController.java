package controller;

import model.MyModel;

import java.util.Arrays;
import java.util.Observable;
import java.util.Random;

/**
 * Created by Willem on 28-11-2017.
 */
public class MyController extends Observable {

    public MyModel model;

    public static final int SIZE = 4;
    private static final int TARGET = 2048;

    public boolean myWin = false;
    public boolean myLose = false;
    private int myScore = 0;

    public MyController(MyModel m){
        this.model = m;
    }




    public void doMove(int move) {
        // Calculate score
    }

    public int[] getMoves(){
        // Calculate possible moves
        return null;
    }

    public void left() {
        //boolean needAddTile = false;
        for (int i = 0; i < SIZE; i++) {
            int[] line = getLine(i);
            int[] merged = mergeLine(moveLine(line));
            setLine(i, merged);
            //if (!needAddTile && !MyTile.compare(line, merged)) {
             //   needAddTile = true;
            //}
        }

        //if (needAddTile) {
            addTile();
        //}
        if (!myWin && !canMove()) {
            myLose = true;
        }
    }

    public void right() {
        this.model.values = rotate(180);
        left();
        this.model.values = rotate(180);
    }

    public void up() {
        this.model.values = rotate(270);
        left();
        this.model.values = rotate(90);
    }

    public void down() {
        this.model.values = rotate(90);
        left();
        this.model.values = rotate(270);
    }

    private int[] rotate(int angle) {
        int[] newTiles = new int[SIZE * SIZE];
        int offsetX = 3, offsetY = 3;
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
        return newTiles;
    }

    private int[] moveLine(int[] oldLine) {
        int[] list = new int[SIZE];
        int j = 0;
        for (int i = 0; i < SIZE; i++) {
            if (oldLine[i] != 0)
                list[j++] = oldLine[i];
        }
        if (j == 0) {
            return oldLine;
        } else {
            return list;
        }
    }

    private int[] mergeLine(int[] oldLine) {
        int[] list = new int[SIZE];

        int j = 0;
        for (int i = 0; i < SIZE && oldLine[i] != 0; i++) {
            int num = oldLine[i];

            if (i < 3 && oldLine[i] == oldLine[i + 1]) {
                num *= 2;
                myScore += num;
                if (num == TARGET) {
                    myWin = true;
                }
                i++;
            }
            list[j++] = num;
        }

        if (list.length == 0) {
            return oldLine;
        } else {
            return list;
        }
    }

    private int[] getLine(int index) {
        int[] result = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            result[i] = tileAt(i, index);
        }
        return result;
    }

    private void setLine(int index, int[] re) {
        System.arraycopy(re, 0, this.model.values, index * SIZE, SIZE);
    }


    public void resetModel(){
        myScore = 0;
        myWin = false;
        myLose = false;

        this.model.values = new int[16];
        this.addTile();
        this.addTile();
    }

    private void addTile() {
        int j = 0;
        int[] temp = new int[this.model.values.length];
        for (int i = 0; i < this.model.values.length; i++) {
            if (0 == this.model.values[i]) {
                temp[j++] = i;
            }
        }
        int [] resultingIndices = new int[j];
        System.arraycopy( temp, 0, resultingIndices, 0, j );

        if (resultingIndices.length != 0 ) {
            int rnd = new Random().nextInt(resultingIndices.length);
            this.model.values[resultingIndices[rnd]] = Math.random() < 0.9 ? 2 : 4;
        }
        modelChanged();
    }

    private int[] availableSpace() {
        int j = 0;
        int[] temp = new int[this.model.values.length];
        for(int i = 0; i < this.model.values.length; i++){
            if(this.model.values[i] == 0){
                temp[j++] = this.model.values[i];
            }
        }
        int [] newArray = new int[j];
        System.arraycopy( temp, 0, newArray, 0, j );

        return newArray;
    }

    private boolean isFull() {
        return availableSpace().length == 0;
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