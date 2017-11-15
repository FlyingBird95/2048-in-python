package model;

import controller.State;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class Model extends Observable{

    public static final int SIZE = 4;
    private static final int TARGET = 2048;

    private Tile[] myTiles;
    private boolean myWin = false;
    private boolean myLose = false;
    private int myScore = 0;

    /**
     * Creates the model. The model consists of:
     * - an Array of Tiles.
     * - A boolean which indicates if the user has won (myWin)
     * - A boolean which indicates if the user has lost (myLose)
     * - A score (integer)
     */
    public Model(){
        resetGame();
    }

    private void resetGame() {
        myScore = 0;
        myWin = false;
        myLose = false;
        myTiles = new Tile[SIZE * SIZE];
        for (int i = 0; i < myTiles.length; i++) {
            myTiles[i] = new Tile();
        }
        addTile();
        addTile();
        modelChanged();
    }

    private void left() {
        boolean needAddTile = false;
        for (int i = 0; i < SIZE; i++) {
            Tile[] line = getLine(i);
            Tile[] merged = mergeLine(moveLine(line));
            setLine(i, merged);
            if (!needAddTile && !Tile.compare(line, merged)) {
                needAddTile = true;
            }
        }

        if (needAddTile) {
            addTile();
        }
    }

    private void right() {
        myTiles = rotate(180);
        left();
        myTiles = rotate(180);
    }

    private void up() {
        myTiles = rotate(270);
        left();
        myTiles = rotate(90);
    }

    private void down() {
        myTiles = rotate(90);
        left();
        myTiles = rotate(270);
    }

    private Tile tileAt(int x, int y) {
        return myTiles[x + y * SIZE];
    }

    private void addTile() {
        List<Tile> list = availableSpace();
        if (!availableSpace().isEmpty()) {
            int index = (int) (Math.random() * list.size()) % list.size();
            Tile emptyTime = list.get(index);
            emptyTime.setValue(Math.random() < 0.9 ? 2 : 4);
        }
    }

    private List<Tile> availableSpace() {
        final List<Tile> list = new ArrayList<>(16);
        for (Tile t : myTiles) {
            if (t.isEmpty()) {
                list.add(t);
            }
        }
        return list;
    }

    private boolean isFull() {
        return availableSpace().size() == 0;
    }

    private boolean canMove() {
        if (!isFull()) {
            return true;
        }
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Tile t = tileAt(x, y);
                if ((x < 3 && t.getValue() == tileAt(x + 1, y).getValue())
                        || ((y < 3) && t.getValue() == tileAt(x, y + 1).getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Tile[] rotate(int angle) {
        Tile[] newTiles = new Tile[SIZE * SIZE];
        int offsetX = 3, offsetY = 3;
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
        return newTiles;
    }

    private Tile[] moveLine(Tile[] oldLine) {
        LinkedList<Tile> l = new LinkedList<>();
        for (int i = 0; i < SIZE; i++) {
            if (!oldLine[i].isEmpty())
                l.addLast(oldLine[i]);
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[SIZE];
            ensureSize(l);
            for (int i = 0; i < SIZE; i++) {
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }

    private Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<>();
        for (int i = 0; i < SIZE && !oldLine[i].isEmpty(); i++) {
            int num = oldLine[i].getValue();
            if (i < 3 && oldLine[i].getValue() == oldLine[i + 1].getValue()) {
                num *= 2;
                myScore += num;
                if (num == TARGET) {
                    myWin = true;
                }
                i++;
            }
            list.add(new Tile(num));
        }
        if (list.size() == 0) {
            return oldLine;
        } else {
            ensureSize(list);
            return list.toArray(new Tile[SIZE]);
        }
    }

    private Tile[] getLine(int index) {
        Tile[] result = new Tile[SIZE];
        for (int i = 0; i < SIZE; i++) {
            result[i] = tileAt(i, index);
        }
        return result;
    }

    private void setLine(int index, Tile[] re) {
        System.arraycopy(re, 0, myTiles, index * SIZE, SIZE);
    }

    private static void ensureSize(List<Tile> l) {
        while (l.size() < SIZE) {
            l.add(new Tile());
        }
    }

    /**
     * KeyListener for the model. The following keys are supported:
     * - Escape: for resetting the game.
     * - Left, Right, Down and Up: for making a move.
     * @return the keyListener for the model.
     */
    public KeyListener getKeyListener(){
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    resetGame();
                }
                if (!canMove()) {
                    myLose = true;
                }

                if (!myWin && !myLose) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            left();
                            break;
                        case KeyEvent.VK_RIGHT:
                            right();
                            break;
                        case KeyEvent.VK_DOWN:
                            down();
                            break;
                        case KeyEvent.VK_UP:
                            up();
                            break;
                    }
                }

                if (!myWin && !canMove()) {
                    myLose = true;
                }
                modelChanged();
            }
        };
    }

    /**
     * Transfer a controller.State-object to every observer (the view)
     */
    public void modelChanged() {
        setChanged();
        notifyObservers(new State(getValues(), myWin, myLose, myScore) );
    }

    private int[] getValues(){
        int[] array = new int[SIZE*SIZE];
        for(int i =0; i<SIZE*SIZE; i++){
           array[i] = myTiles[i].getValue();
        }
        return array;
    }
}
