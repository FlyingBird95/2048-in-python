import controller.RandomController;
import model.Model;
import view.View;

import javax.swing.*;

public class Main {

    private static final String TITLE = "2048 Game";

    /**
     * The main method is responsible for creating the model and the view.
     *
     * After creation, the view is bounded to the model using Observer-Observable-pattern.
     * The model is bounded to the view, using a keyListener.
     */
    public static void main(String[] args) {
        JFrame game = new JFrame();
        game.setTitle(TITLE);
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(View.WIDTH, View.HEIGHT);
        game.setResizable(false);

        Model model = new Model();
        View view = new View();
        model.addObserver(view);
        game.add(view);

        game.setLocationRelativeTo(null);
        game.setVisible(true);

        /*
         * Either run the program with a manual controller:
         *  view.addKeyListener(model.getKeyListener()))
         * or run the program with an automatic controller:
         *   new RandomController(model).start();
         */
        view.addKeyListener(model.getKeyListener());
        //new RandomController(model).start();
    }
}