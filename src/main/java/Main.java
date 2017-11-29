//import model.Model;
//import view.View;

import com.google.common.primitives.Ints;
import controller.MyController;
import model.MyModel;
import view.MyView;

import java.lang.reflect.Array;
import java.util.Arrays;

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
//        View view = View.createView();
//        Model model = new Model();
//        model.addObserver(view);
//        model.modelChanged();
//        view.addKeyListener(model.getKeyListener());

        MyModel model = new MyModel();

        MyController controller = new MyController(model);
        MyView view = MyView.createView();
        controller.addObserver(view);
        controller.modelChanged();

        view.setController(controller);

        view.addKeyListener(view.getKeyListenerPressed());
        view.addKeyListener(view.getKeyListenerReleased());



        /*
         * OR THIS:
         */
        //new ControllerManager(SemiRandomController.class).start();
    }
}