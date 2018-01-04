//package controller;
//
//import Util.MoveUtil;
//import model.Model;
//import org.apache.commons.lang3.ArrayUtils;
//import org.junit.Test;
//
//import static org.junit.Assert.assertTrue;
//
//public class ControllerTestPossibleMoves {
//
//    @Test
//    public void moveEmpty() throws Exception {
//        available(new int[]{
//                0, 0, 0, 0,
//                0, 0, 0, 0,
//                0, 0, 0, 0,
//                0, 0, 0, 0
//        }, new MoveUtil.Move[]{
//                MoveUtil.Move.DOWN,
//                MoveUtil.Move.UP,
//                MoveUtil.Move.LEFT,
//                MoveUtil.Move.RIGHT
//        });
//    }
//
//    @Test
//    public void moveSimple() throws Exception {
//        available(new int[]{
//                2, 2, 2, 2,
//                4, 4, 4, 4,
//                2, 2, 2, 2,
//                4, 4, 4, 4
//        }, new MoveUtil.Move[]{
//                MoveUtil.Move.LEFT,
//                MoveUtil.Move.RIGHT
//        });
//    }
//
//    @Test
//    public void move() throws Exception {
//        available(new int[]{
//                2, 2, 4, 8,
//                8, 4, 8, 4,
//                4, 8, 4, 8,
//                8, 4, 8, 4
//        }, new MoveUtil.Move[]{
//                MoveUtil.Move.LEFT,
//                MoveUtil.Move.RIGHT
//        });
//    }
//
//    @Test
//    public void moveAdvanced() throws Exception {
//        available(new int[]{
//                4, 2, 4, 2,
//                2, 4, 2, 4,
//                4, 2, 4, 2,
//                2, 4, 2, 0
//        }, new MoveUtil.Move[]{
//                MoveUtil.Move.DOWN,
//                MoveUtil.Move.UP,
//                MoveUtil.Move.LEFT,
//                MoveUtil.Move.RIGHT
//        });
//    }
//
//    @Test
//    public void moveFull() throws Exception {
//        available(new int[]{
//                4, 2, 4, 2,
//                2, 4, 2, 4,
//                4, 2, 4, 2,
//                2, 4, 2, 4
//        }, new MoveUtil.Move[]{
//                // No moves
//        });
//    }
//
//    private void available(int[] input, MoveUtil.Move[] output) throws Exception {
//        Model m = new Model();
//        Controller c = new Controller(m);
//
//        m.values = input;
//
//        MoveUtil.Move[] moveList = c.getPossibleMoves();
//        assertTrue(output.length == moveList.length);
//        for(MoveUtil.Move move : output){
//            assertTrue(ArrayUtils.contains(moveList, move));
//        }
//    }
//}
