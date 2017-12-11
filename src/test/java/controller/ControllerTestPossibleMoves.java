package controller;

import model.Model;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ControllerTestPossibleMoves {

    @Test
    public void moveEmpty() throws Exception {
        available(new int[]{
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
        }, new Controller.Move[]{
                Controller.Move.DOWN,
                Controller.Move.UP,
                Controller.Move.LEFT,
                Controller.Move.RIGHT
        });
    }

    @Test
    public void moveSimple() throws Exception {
        available(new int[]{
                2, 2, 2, 2,
                4, 4, 4, 4,
                2, 2, 2, 2,
                4, 4, 4, 4
        }, new Controller.Move[]{
                Controller.Move.LEFT,
                Controller.Move.RIGHT
        });
    }

    @Test
    public void move() throws Exception {
        available(new int[]{
                2, 2, 4, 8,
                8, 4, 8, 4,
                4, 8, 4, 8,
                8, 4, 8, 4
        }, new Controller.Move[]{
                Controller.Move.LEFT,
                Controller.Move.RIGHT
        });
    }

    @Test
    public void moveAdvanced() throws Exception {
        available(new int[]{
                4, 2, 4, 2,
                2, 4, 2, 4,
                4, 2, 4, 2,
                2, 4, 2, 0
        }, new Controller.Move[]{
                Controller.Move.DOWN,
                Controller.Move.UP,
                Controller.Move.LEFT,
                Controller.Move.RIGHT
        });
    }

    @Test
    public void moveFull() throws Exception {
        available(new int[]{
                4, 2, 4, 2,
                2, 4, 2, 4,
                4, 2, 4, 2,
                2, 4, 2, 4
        }, new Controller.Move[]{
                // No moves
        });
    }

    private void available(int[] input, Controller.Move[] output) throws Exception {
        Model m = new Model(4);
        Controller c = new Controller(m, 4);

        m.values = input;
//        List<Controller.Move> moveList = c.getPossibleMoves();

//        assertTrue(output.length == moveList.size());
//        for(Controller.Move move : output){
//            assertTrue(moveList.contains(move));
//        }

        Controller.Move[] moveList = c.getPossibleMoves();
        assertTrue(output.length == moveList.length);
        for(Controller.Move move : output){
            assertTrue(ArrayUtils.contains(moveList, move));
        }
    }
}
