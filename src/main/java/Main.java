//import model.Model;
//import view.View;

import controller.MyController;
import model.MyModel;
import view.MyView;

public class Main {

    /**
     * The main method is responsible for creating the model and the view.
     *
     * After creation, the view is bounded to the model using Observer-Observable-pattern.
     * The model is bounded to the view, using a keyListener.
     */
    public static void main(String[] args) {

//        View view = View.createView();
//        Model model = new Model();
//        model.addObserver(view);
//        model.modelChanged();
//        view.addKeyListener(model.getKeyListener());

        int modelSize = 4;
        MyModel model = new MyModel(modelSize);
        MyController controller = new MyController(model, modelSize);
        controller.resetModel();
        controller.model.values = new int[]{2, 2, 0, 0,
                                            2, 0, 2, 0,
                                            0, 2, 2, 2,
                                            2, 0, 2, 2};

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