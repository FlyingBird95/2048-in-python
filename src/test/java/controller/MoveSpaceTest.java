package controller;

import Util.MoveUtil;
import model.Model;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import rl4j.MoveSpace;

import static org.junit.Assert.*;

public class MoveSpaceTest {

    private MoveUtil.Move[] allMoves = new MoveUtil.Move[]{
            MoveUtil.Move.DOWN,
            MoveUtil.Move.UP,
            MoveUtil.Move.LEFT,
            MoveUtil.Move.RIGHT
    };
//    private int[] allMovesId = MoveUtil.toIntArray(allMoves);

    @Test
    public void moveZero() throws Exception {
        doTest( new int[]{
                2, 4, 2, 4,
                4, 2, 4, 2,
                2, 4, 2, 4,
                4, 2, 4, 2
        }, new MoveUtil.Move[]{});
    }

    @Test
    public void moveVertical() throws Exception {
        doTest( new int[]{
                2, 4, 2, 4,
                2, 4, 2, 4,
                2, 4, 2, 4,
                2, 4, 2, 4
        }, new MoveUtil.Move[]{MoveUtil.Move.UP, MoveUtil.Move.DOWN});
    }

    @Test
    public void moveHorizontal() throws Exception {
        doTest( new int[]{
                2, 2, 2, 2,
                4, 4, 4, 4,
                2, 2, 2, 2,
                4, 4, 4, 4
        }, new MoveUtil.Move[]{MoveUtil.Move.RIGHT, MoveUtil.Move.LEFT});
    }

    @Test
    public void moveAll() throws Exception {
        doTest( new int[]{
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0
        }, allMoves);
    }

    public void doTest(int[] input, MoveUtil.Move[] outputMoves) throws Exception {
        int runs = 100;

        Model m = new Model();
        m.values = input;
        Controller c = new Controller(m);

        int[] validIds = MoveUtil.toIntArray(outputMoves);
        int[] moveCount = new int[allMoves.length];

        MoveSpace ms = new MoveSpace(c);
        for (int i = 0; i < runs; i++)
            if(ms.randomAction() != -1)
                moveCount[ms.randomAction()]++;

        for (int i = 0; i < validIds.length; i++){
            if(ArrayUtils.contains(validIds, i)){
                assertTrue(moveCount[i] > 0);
            }
            else{
                assertTrue(moveCount[i] == 0);
            }
        }
//        System.out.println(ArrayUtils.toString(moveCount));
    }
}
