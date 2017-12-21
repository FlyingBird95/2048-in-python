package controller;

import model.Model;
import view.View;

import java.util.Observable;

public class Controller extends Observable {

    private Model model;
    private View view;

    public Controller(){
        this.model = GameLogic.newModel();

        this.view = View.createView();
        addObserver(this.view);
        modelChanged();
    }


    public void modelChanged() {
        setChanged();
        notifyObservers(model);
    }
}