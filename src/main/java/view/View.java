package view;

import Util.MoveUtil;
import controller.Controller;
import model.Model;
import org.apache.commons.lang3.ArrayUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class View extends JPanel implements Observer{

    private static final String TITLE = "2048 Game";
    private static final String FONT_NAME = "Arial";
    private static final Color BG_COLOR = new Color(0xbbada0);
    private static final int TILE_SIZE = 64;
    private static final int TILES_MARGIN = 16;

    private static final int WIDTH = 340;
    private static final int HEIGHT = 400;

    private Controller controller;
    private boolean keyPressed = false;

    /**
     * Creates the view
     * @return the view for the game
     */
    public static View createView(){
        JFrame game = new JFrame();
        game.setTitle(TITLE);
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(View.WIDTH, View.HEIGHT);
        game.setResizable(false);

        View view = new View();
        game.add(view);

        game.setLocationRelativeTo(null);
        game.setVisible(true);
        return view;
    }

    /**
     * The board is responsible for drawing the background and the totalScore.
     */
    private View() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
    }

    public void setController(Controller c){
        this.controller = c;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);

        Model model = this.controller.model;
        if(model != null){
            this.drawTiles((Graphics2D) g, model.values, Model.SIZE);
            this.drawWinLose((Graphics2D) g, model.win, model.lose);
            this.drawScore((Graphics2D) g, model.totalScore);
        }
    }

    private void drawTiles(Graphics2D g, int[] values, int size){
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                drawTile(g, values[y + x * size], y, x);
            }
        }
    }

    private void drawTile(Graphics2D g, int value, int x, int y) {
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);

        g.setColor(getBackground(value));
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
        g.setColor(getForeground(value));
        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
        final Font font = new Font(FONT_NAME, Font.BOLD, size);
        g.setFont(font);

        String s = String.valueOf(value);
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int)fm.getLineMetrics(s, g).getBaselineOffsets()[2];

        if (value != 0)
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);
    }

    private int offsetCoors(int arg) {
        return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
    }

    private Color getForeground(int value) {
        return value < 16 ? new Color(0x776e65) :  new Color(0xf9f6f2);
    }

    private Color getBackground(int value) {
        switch (value) {
            case 2:    return new Color(0xeee4da);
            case 4:    return new Color(0xede0c8);
            case 8:    return new Color(0xf2b179);
            case 16:   return new Color(0xf59563);
            case 32:   return new Color(0xf67c5f);
            case 64:   return new Color(0xf65e3b);
            case 128:  return new Color(0xedcf72);
            case 256:  return new Color(0xedcc61);
            case 512:  return new Color(0xedc850);
            case 1024: return new Color(0xedc53f);
            case 2048: return new Color(0xedc22e);
        }
        return new Color(0xcdc1b4);
    }

    private void drawWinLose(Graphics2D g, boolean win, boolean lose){
        if (win || lose) {
            g.setColor(new Color(255, 255, 255, 30));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(78, 139, 202));
            g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
            if (win) {
                g.drawString("You won!", 68, 150);
            }
            if (lose) {
                g.drawString("Game over!", 50, 130);
                g.drawString("You lose!", 64, 200);
            }
            g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
            g.setColor(new Color(128, 128, 128, 128));
            g.drawString("Press ESC to play again", 80, getHeight() - 40);
        }
    }

    private void drawScore(Graphics2D g, int score){
        g.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
        g.drawString("Score: " + score, 200, 365);
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof Controller){
            this.controller = (Controller) o;
        }
        repaint();
    }

    /**
     * KeyListener for the model. The following keys are supported:
     * - Escape: for resetting the game.
     * - Left, Right, Down and Up: for making a doMove.
     * @return the keyListener for the model.
     */
    public KeyListener getKeyListener(){
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(keyPressed)
                    return;
                keyPressed = true;

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    controller.resetModel();
                }

                MoveUtil.Move[] moveList = controller.model.moveList;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (ArrayUtils.contains(moveList, MoveUtil.Move.LEFT)) {
                            controller.doMove(MoveUtil.Move.LEFT);
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (ArrayUtils.contains(moveList, MoveUtil.Move.RIGHT)) {
                            controller.doMove(MoveUtil.Move.RIGHT);
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (ArrayUtils.contains(moveList, MoveUtil.Move.DOWN)) {
                            controller.doMove(MoveUtil.Move.DOWN);
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (ArrayUtils.contains(moveList, MoveUtil.Move.UP)) {
                            controller.doMove(MoveUtil.Move.UP);
                        }
                        break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                keyPressed = false;
            }
        };
    }
}