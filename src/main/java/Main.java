//import model.Model;
//import view.View;

import controller.Controller;
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

        int modelSize = 4;
        Model model = new Model(modelSize);
        Controller controller = new Controller(model, modelSize);
        controller.resetModel();

        View view = View.createView(modelSize);
        controller.addObserver(view);
        controller.modelChanged();

        view.setController(controller);

        view.addKeyListener(view.getKeyListenerPressed());
        view.addKeyListener(view.getKeyListenerReleased());


        // Learning methods
        //Learning<Model, Integer, MoveSpace, IDQN> dql = new QLearningDiscreteDense<Model>(mdp, TOY_NET, TOY_QL, manager);

    }
}