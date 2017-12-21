package controller;

import model.Model;
import rl.TestRL;
import view.View;

import java.util.Observable;

public class Controller extends Observable {

    private Model model;

    private static Controller instance = null;

    public static Controller getInstance(){
        if (instance == null){
            instance = new Controller();
            instance.start();
        }
        return instance;
    }

    private Controller(){
        model = GameLogic.newModel();

        View view = View.createView();
        addObserver(view);
        modelChanged();
    }

    private void start(){
        TestRL<Model> test = new TestRL<>() ;
        test.train();
    }

    public Model getModel(){
        return model;
    }

    public void setModel(Model model){
        this.model = model;
        modelChanged();
    }

    private void modelChanged() {
        setChanged();
        notifyObservers(model);
    }
}