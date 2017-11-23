package controller;

import model.Model;

public class SemiRandomController extends AbstractController {

    /**
     * Must have a default constructor with no arguments
     */
    public SemiRandomController(){
        super();
    }

    public SemiRandomController(Model model){
        super();
        setModel(model);
    }

    @Override
    void move(){
        double random = Math.random();

        if (random < 0.4){
            model.left();
        } else if (random < 0.8){
            model.right();
        } else if (random < 0.9){
            model.up();
        } else {
            model.down();
        }
    }
}
