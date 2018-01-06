package controller;

import Util.MoveUtil;
import model.Model;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Random;

public class GameLogic {

    public static Model newModel(){
        Model m = new Model();
        addTile(m);
        addTile(m);
        return m;
    }

    public static void addTile(Model model) {
        if(GameLogic.isFull(model)) {
            return;
        }

        int count = 0;
        int[] resultingIndices = new int[model.values.length];
        for (int i = 0; i < model.values.length; i++) {
            if (0 == model.values[i]) {
                resultingIndices[count++] = i;
            }
        }

        int rnd = new Random().nextInt(count);
        model.values[resultingIndices[rnd]] = Math.random() < 0.9 ? 2 : 4;
    }

    /**
     * Rotates the tiles in the model by a given angle
     * @param angle the angle in degrees
     */
    private static void rotate(Model model, int angle) {
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
                newTiles[(newX) + (newY) * Model.SIZE] = tileAt(model, x, y);
            }
        }
        model.values = newTiles;
    }

    private static int tileAt(Model model, int x, int y) {
        return model.values[x + y * Model.SIZE];
    }

    public static Model doMove(Model model, MoveUtil.Move move) {
        Model newModel = model.clone();
        switch (move){
            case UP:
                rotate(newModel, 270);
                doMove(newModel);
                rotate(newModel, 90);
                break;
            case RIGHT:
                rotate(newModel, 180);
                doMove(newModel);
                rotate(newModel, 180);
                break;
            case DOWN:
                rotate(newModel, 90);
                doMove(newModel);
                rotate(newModel, 270);
                break;
            case LEFT:
                doMove(newModel);
                break;
        }
        return newModel;
    }

    private static void doMove(Model model){
        int[] line = new int[Model.SIZE];
        model.reward = 0;
        for (int i = 0; i < Model.SIZE; i++) {
            getLine(model, i, line);
            model.reward += mergeLine(line);
            setLine(model, i, line);
        }
        model.totalScore += model.reward;
    }

    private static void getLine(Model model, int index, int[] output) {
        System.arraycopy(model.values, index * Model.SIZE, output, 0, Model.SIZE);
    }

    private static void setLine(Model model, int index, int[] input) {
        System.arraycopy(input, 0, model.values, index * Model.SIZE, Model.SIZE);
    }

    private static int mergeLine(int[] line) {
        int[] list = trimLine(line);
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

    private static int[] trimLine(int[] line){
        int[] output = new int[Model.SIZE];
        int counter = 0;
        for (int i = 0; i < Model.SIZE; i++) {
            if (line[i] != 0) {
                output[counter++] = line[i];
            }
        }
        return output;
    }

    /**
     * The order of the returned values are: [UP, RIGHT, DOwN, LEFT]
     * @return values of the array are: 1 if that move is possible, 0 otherwise
     */
    public static int[] getActionMask(Model model){
        int[] array = new int[]{0, 0, 0, 0};
        for(MoveUtil.Move m : getPossibleMoves(model)){
            array[MoveUtil.moveToInt(m)] = 1;
        }
        return array;
    }

    public static MoveUtil.Move[] getPossibleMoves(Model model){
        if(!isFull(model)) {
            return new MoveUtil.Move[]{MoveUtil.Move.UP, MoveUtil.Move.DOWN, MoveUtil.Move.RIGHT, MoveUtil.Move.LEFT};
        }

        boolean horizontal = isMovePossible(model);
        rotate(model,90);
        boolean vertical = isMovePossible(model);
        rotate(model,270);

        if(vertical && horizontal) {
            return new MoveUtil.Move[]{MoveUtil.Move.UP, MoveUtil.Move.DOWN, MoveUtil.Move.RIGHT, MoveUtil.Move.LEFT};
        }
        if(vertical) {
            return new MoveUtil.Move[]{MoveUtil.Move.UP, MoveUtil.Move.DOWN};
        }
        if(horizontal) {
            return new MoveUtil.Move[]{MoveUtil.Move.RIGHT, MoveUtil.Move.LEFT};
        }

        return new MoveUtil.Move[]{};
    }

    private static boolean isMovePossible(Model model){
        int[] line = new int[Model.SIZE];
        for (int i = 0; i < Model.SIZE; i++) {
            getLine(model, i, line);
            for(int j = 0; j < Model.SIZE - 1; j++) {
                if (line[j] == line[j + 1]) {
                    return true;
                }
            }
        }
        return false;
    }


    private static boolean isFull(Model model) {
        return !ArrayUtils.contains(model.values, 0);
    }

    public static boolean hasWon(Model model) {
        return ArrayUtils.contains(model.values, 2048);
    }

    public static boolean hasLost(Model model) {
        return getPossibleMoves(model).length == 0;
    }

    public static boolean isDone(Model model){
        return hasWon(model) || hasLost(model);
    }

    public static double getReward(Model model){
        return model.reward;
    }
}
