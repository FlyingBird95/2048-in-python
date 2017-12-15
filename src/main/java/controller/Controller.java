package controller;

import model.Model;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class Controller extends Observable {

    public Model model;
    private boolean testMode;

    public Controller(Model m){
        this.model = m;
        this.testMode = false;
    }

    public void resetModel(){
        model.totalScore = 0;
        model.win = false;
        model.lose = false;
        this.model.values = new int[Model.SIZE * Model.SIZE];
        this.model.moveList = this.getPossibleMoves();

        // The game starts with 2 tiles
        this.addTile();
        this.addTile();

        this.modelChanged();
    }

    public void setTestMode(boolean testMode){
        this.testMode = testMode;
    }

    /**
     * Rotates the tiles by a given angle
     * @param angle the angle in degrees
     */
    private void rotate(int angle) {
        int[] newTiles = new int[Model.SIZE * Model.SIZE];
        int offsetX = Model.SIZE - 1, offsetY = Model.SIZE - 1;
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }

        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        for (int x = 0; x < Model.SIZE; x++) {
            for (int y = 0; y < Model.SIZE; y++) {
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                newTiles[(newX) + (newY) * Model.SIZE] = tileAt(x, y);
            }
        }
        this.model.values = newTiles;
    }

    private int tileAt(int x, int y) {
        return this.model.values[x + y * Model.SIZE];
    }

    public void doMove(Util.Move move) {
        switch (move){
            case UP:
                this.rotate(270);
                this.doMove();
                this.rotate(90);
                break;
            case RIGHT:
                this.rotate(180);
                this.doMove();
                this.rotate(180);
                break;
            case DOWN:
                this.rotate(90);
                this.doMove();
                this.rotate(270);
                break;
            case LEFT:
                this.doMove();
                break;

        }
        if(!this.testMode)
            this.addTile();

        this.model.win = ArrayUtils.contains(this.model.values, 2048);
        this.model.lose = !this.canMove();
        this.model.moveList = this.getPossibleMoves();

        this.modelChanged();
    }

    private void doMove(){
        int[] line = new int[Model.SIZE];
        this.model.previousReward = 0;
        for (int i = 0; i < Model.SIZE; i++) {
            this.getLine(i, line);
            this.model.previousReward += this.mergeLine(line);
            this.setLine(i, line);
        }
        this.model.totalScore += this.model.previousReward;
    }

    private void getLine(int index, int[] output) {
        System.arraycopy(this.model.values, index * Model.SIZE, output, 0, Model.SIZE);
    }

    private void setLine(int index, int[] input) {
        System.arraycopy(input, 0, this.model.values, index * Model.SIZE, Model.SIZE);
    }

    private int mergeLine(int[] line) {
        int[] list = this.trimLine(line);
        Arrays.fill(line, 0);

        int move = 0;
        int score = 0;
        for (int i = 0; i < Model.SIZE && list[i] != 0; i++) {
            int current = list[i];

            if(i < (Model.SIZE - 1) && current == list[i + 1]){
                current *= 2;
                score += current;
                i++;
            }
            line[move++] = current;
        }
        return score;
    }

    private int[] trimLine(int[] line){
        int[] output = new int[Model.SIZE];
        int counter = 0;
        for (int i = 0; i < Model.SIZE; i++) {
            if (line[i] != 0) {
                output[counter++] = line[i];
            }
        }
        return output;
    }

    private void addTile() {
        if(this.isFull()) {
            return;
        }

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

    public Util.Move[] getPossibleMoves(){
        if(!this.isFull()) {
            return new Util.Move[]{Util.Move.LEFT, Util.Move.RIGHT, Util.Move.UP, Util.Move.DOWN};
        }

        boolean horizontal = isMovePossible();
        this.rotate(90);
        boolean vertical = isMovePossible();
        this.rotate(270);

        if(vertical && horizontal) {
            return new Util.Move[]{Util.Move.LEFT, Util.Move.RIGHT, Util.Move.UP, Util.Move.DOWN};
        }
        if(vertical) {
            return new Util.Move[]{Util.Move.UP, Util.Move.DOWN};
        }
        if(horizontal) {
            return new Util.Move[]{Util.Move.LEFT, Util.Move.RIGHT};
        }

        return new Util.Move[]{};
    }

    private boolean isMovePossible(){
        int[] line = new int[Model.SIZE];
        for (int i = 0; i < Model.SIZE; i++) {
            this.getLine(i, line);
            for(int j = 0; j < Model.SIZE - 1; j++) {
                if (line[j] == line[j + 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isFull() {
        return !this.contains(0);
    }

    private boolean contains(int value) {
        return ArrayUtils.contains(this.model.values, value);
    }

    /**
     * The player can do a move, if there is at least 1 item in possibleMoves;
     * @return
     */
    public boolean canMove() {
        return getPossibleMoves().length > 0;
    }

    public void modelChanged() {
        setChanged();
        notifyObservers(this);
    }
}