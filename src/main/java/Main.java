import model.Model;
import view.View;

public class Main {

    /**
     * The main method is responsible for creating the model and the view.
     *
     * After creation, the view is bounded to the model using Observer-Observable-pattern.
     * The model is bounded to the view, using a keyListener.
     */
    public static void main(String[] args) {

        /*
         * EITHER USE THIS
         */
        View view = View.createView();
        Model model = new Model();
        model.addObserver(view);
        model.modelChanged();
        view.addKeyListener(model.getKeyListener());

        /*
         * OR THIS:
         */
        //new ControllerManager(SemiRandomController.class).start();
    }
}