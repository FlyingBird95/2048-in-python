package view;

import controller.MyController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;
import java.util.Observer;

public class MyView extends JPanel implements Observer{

    private static final String TITLE = "2048 Game";
    private static final Color BG_COLOR = new Color(0xbbada0);
    private static final String FONT_NAME = "Arial";
    private static final int TILE_SIZE = 64;
    private static final int TILES_MARGIN = 16;

    private static final int WIDTH = 340;
    private static final int HEIGHT = 400;


    private int size;

    private MyController controller;

    private boolean keyPressed = false;

    /**
     * Creates the view
     * @return the view for the game
     */
    public static MyView createView(int size){
        JFrame game = new JFrame();
        game.setTitle(TITLE);
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(MyView.WIDTH, MyView.HEIGHT);
        game.setResizable(false);

        MyView view = new MyView(size);
        game.add(view);

        game.setLocationRelativeTo(null);
        game.setVisible(true);
        return view;
    }


    /**
     * The board is responsible for drawing the background and the score.
     */
    private MyView(int size) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.size = size;
        setFocusable(true);
    }


    public void setController(MyController c){
        this.controller = c;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        if (this.controller.model != null) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    drawTile(g, this.controller.model.values[y + x * size], y, x);
                }
            }
        }
    }

    private void drawTile(Graphics g2, int value, int x, int y) {
        Graphics2D g = ((Graphics2D) g2);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);
        g.setColor(MyTile.getBackground(value));
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
        g.setColor(MyTile.getForeground(value));
        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
        final Font font = new Font(FONT_NAME, Font.BOLD, size);
        g.setFont(font);

        String s = String.valueOf(value);
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

        if (value != 0)
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);

        if (this.controller.model == null){
            return;
        }

        if (this.controller.model.win|| this.controller.model.lose) {
            g.setColor(new Color(255, 255, 255, 30));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(78, 139, 202));
            g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
            if (this.controller.model.win) {
                g.drawString("You won!", 68, 150);
            }
            if (this.controller.model.lose) {
                g.drawString("Game over!", 50, 130);
                g.drawString("You lose!", 64, 200);
            }
            if (this.controller.model.win || this.controller.model.lose) {
                g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
                g.setColor(new Color(128, 128, 128, 128));
                g.drawString("Press ESC to play again", 80, getHeight() - 40);
            }
        }
        g.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
        g.drawString("Score: " + this.controller.model.score, 200, 365);

    }

    private static int offsetCoors(int arg) {
        return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
    }

    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof MyController){
            this.controller = (MyController) o;
        }
        repaint();
    }

    /**
     * KeyListener for the model. The following keys are supported:
     * - Escape: for resetting the game.
     * - Left, Right, Down and Up: for making a doMove.
     * @return the keyListener for the model.
     */
    public KeyListener getKeyListenerPressed(){
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(keyPressed)
                    return;
                keyPressed = true;

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    controller.resetModel();
                }
                if (!controller.canMove()) {
                    controller.myLose = true;
                }

                if (!controller.myWin && !controller.myLose) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            controller.doMove(3);
                            break;
                        case KeyEvent.VK_RIGHT:
                            controller.doMove(1);
                            break;
                        case KeyEvent.VK_DOWN:
                            controller.doMove(2);
                            break;
                        case KeyEvent.VK_UP:
                            controller.doMove(0);
                            break;
                    }
                }
            }
        };
    }

    public KeyListener getKeyListenerReleased(){
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                keyPressed = false;
            }
        };
    }
}