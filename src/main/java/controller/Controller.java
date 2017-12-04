package controller;

import model.Model;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * Created by Willem on 28-11-2017.
 */
public class Controller extends Observable {


    /**
     * Define an enum with the available moves
     */
    public enum Move {
            UP, RIGHT, DOWN, LEFT
    }

    // class variables
    public Model model;
    private final int SIZE;

    public Controller(Model m, int size){
        this.model = m;
        this.SIZE = size;
        resetModel();
    }

    public void resetModel(){
        model.score = 0;
        model.win = false;
        model.lose =false;
        this.model.values = new int[SIZE * SIZE];

        // The game starts with 2 tiles
        this.addTile();
        this.addTile();
    }

    /**
     * Rotates the tiles by a given angle
     * @param angle the angle in degrees
     */
    private void rotate(int angle) {
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

    public void doMove(Move move) {
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
        this.addTile();
        this.modelChanged();
    }

    private void doMove(){
        int[] line = new int[SIZE];
        for (int i = 0; i < SIZE; i++) {
            this.getLine(i, line);
            this.model.score += this.mergeLine(line);
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

    /**
     * The method works by first doing the move, and then see if the values
     * in the model are changed
     * @return a list of enum-Move with the possible moves.
     */
    public List<Move> getPossibleMoves(){
        List<Move> possibleMoves = new ArrayList<>();

        for(Move move : Move.values()){
            if (checkMove(move)){
                possibleMoves.add(move);
            }
        }

        return possibleMoves;
    }

    /**
     * Checks if a certain move can be done
     * @param move the move to be checked
     * @return true if the move can be done, false otherwise
     */
    public boolean checkMove(Move move){
        boolean answer = false;

        // keep a copy to return to the previous move
        int[] copy = ArrayUtils.clone(model.values);

        doMove(move);
        if (!Arrays.equals(copy, model.values)){
            answer = true;
        }
        // reset the values
        model.values = ArrayUtils.clone(copy);
        return answer;
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

    /**
     * The player can do a move, if there is at least 1 item in possibleMoves;
     * @return
     */
    public boolean canMove() {
        return getPossibleMoves().size() > 0;
    }

    public void modelChanged() {
        setChanged();
        notifyObservers(this);
    }
}