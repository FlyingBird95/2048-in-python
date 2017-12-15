package controller;

import model.Model;
import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerTestDoMove {

    @Test
    public void moveEmpty() throws Exception {
        int[] values = new int[]{
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
        };
        allMoveTest(values, values);
    }

    @Test
    public void moveSimple() throws Exception {
        allMoveTest(new int[]{
                2, 0, 0, 0,
                0, 2, 0, 0,
                0, 0, 2, 0,
                0, 0, 0, 2
        }, new int[]{
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                2, 2, 2, 2
        }, new int[]{
                2, 0, 0, 0,
                2, 0, 0, 0,
                2, 0, 0, 0,
                2, 0, 0, 0
        }, new int[]{
                0, 0, 0, 2,
                0, 0, 0, 2,
                0, 0, 0, 2,
                0, 0, 0, 2
        }, new int[]{
                2, 2, 2, 2,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
        });
    }

    @Test
    public void move() throws Exception {
        allMoveTest(new int[]{
                2, 2, 4, 4,
                2, 2, 4, 4,
                4, 4, 0, 0,
                4, 4, 0, 8
        }, new int[]{
                0, 0, 0, 0,
                0, 0, 0, 0,
                4, 4, 0, 8,
                8, 8, 8, 8
        }, new int[]{
                4, 8, 0, 0,
                4, 8, 0, 0,
                8, 0, 0, 0,
                8, 8, 0, 0
        }, new int[]{
                0, 0, 4, 8,
                0, 0, 4, 8,
                0, 0, 0, 8,
                0, 0, 8, 8
        }, new int[]{
                4, 4, 8, 8,
                8, 8, 0, 8,
                0, 0, 0, 0,
                0, 0, 0, 0
        });
    }

    @Test
    public void moveAdvanced() throws Exception {
        allMoveTest(new int[]{
                0, 0, 2, 2,
                4, 4, 8, 8,
                16, 16, 32, 32,
                64, 64, 128, 128
        }, new int[]{
                0, 0, 2, 2,
                4, 4, 8, 8,
                16, 16, 32, 32,
                64, 64, 128, 128
        }, new int[]{
                4, 0, 0, 0,
                8, 16, 0, 0,
                32, 64, 0, 0,
                128, 256, 0, 0
        }, new int[]{
                0, 0, 0, 4,
                0, 0, 8, 16,
                0, 0, 32, 64,
                0, 0, 128, 256
        }, new int[]{
                4, 4, 2, 2,
                16, 16, 8, 8,
                64, 64, 32, 32,
                0, 0, 128, 128
        });
    }

    @Test
    public void moveFull() throws Exception {
        int[] values = new int[]{
                2, 4, 2, 4,
                4, 2, 4, 2,
                2, 4, 2, 4,
                4, 2, 4, 2
        };
        allMoveTest(values, values);
    }

    private void allMoveTest(int[] input, int[] outputD, int[] outputL, int[] outputR, int[] outputU) throws  Exception{
        moveTest(input, outputD, Util.Move.DOWN);
        moveTest(input, outputL, Util.Move.LEFT);
        moveTest(input, outputR, Util.Move.RIGHT);
        moveTest(input, outputU, Util.Move.UP);
    }

    private void allMoveTest(int[] input, int[] output) throws  Exception {
        allMoveTest(input, output, output, output, output);
    }

    private void moveTest(int[] input, int[] output, Util.Move move) throws  Exception {
        Model m = new Model();
        Controller c = new Controller(m);
        c.setTestMode(true);

        m.values = input.clone();
        c.doMove(move);

        assertArrayEquals(m.values, output);
    }
}