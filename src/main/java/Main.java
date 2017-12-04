//import model.Model;
//import view.View;

import controller.MoveSpace;
import controller.MyController;
import model.MyModel;
import org.deeplearning4j.rl4j.learning.Learning;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.space.ActionSpace;
import view.MyView;

public class Main {

    /**
     * The main method is responsible for creating the model and the view.
     *
     * After creation, the view is bounded to the model using Observer-Observable-pattern.
     * The model is bounded to the view, using a keyListener.
     */
    public static void main(String[] args) {

        int modelSize = 4;
        MyModel model = new MyModel(modelSize);
        MyController controller = new MyController(model, modelSize);
        controller.resetModel();

        MyView view = MyView.createView(modelSize);
        controller.addObserver(view);
        controller.modelChanged();

        view.setController(controller);

        view.addKeyListener(view.getKeyListenerPressed());
        view.addKeyListener(view.getKeyListenerReleased());


        // Learning methods
        //Learning<MyModel, Integer, MoveSpace, IDQN> dql = new QLearningDiscreteDense<MyModel>(mdp, TOY_NET, TOY_QL, manager);

    }
}