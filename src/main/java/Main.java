import controller.Controller;

public class Main {

    /**
     * The main method is responsible for creating the model and the view.
     *
     * After creation, the view is bounded to the model using Observer-Observable-pattern.
     * The model is bounded to the view, using a keyListener.
     */
    public static void main(String[] args) throws Exception{
        new Controller();

    }


}