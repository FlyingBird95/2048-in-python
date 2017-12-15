package Util;

public class MoveUtil {

    /**
     * Define an enum with the available moves
     */
    public enum Move {
        UP, RIGHT, DOWN, LEFT
    }

    public static Move getMove(Integer integer){
        switch (integer){
            case 0: return Move.UP;
            case 1: return Move.RIGHT;
            case 2: return Move.DOWN;
            case 3: return Move.LEFT;
            default: throw new IllegalArgumentException("Given value must be between 0-3");
        }
    }

    public static int moveToInt(Move move){
        switch (move){
            case UP: return 0;
            case RIGHT: return 1;
            case DOWN: return 2;
            case LEFT: return 3;
            default: throw new IllegalArgumentException("Given move is invalid");
        }
    }

    public static int[] toIntArray(Move[] moves){
        int[] array = new int[moves.length];
        for(int i=0; i<moves.length; i++){
            array[i] = moveToInt(moves[i]);
        }
        return array;
    }

}
