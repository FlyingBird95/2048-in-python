package controller;

import model.Model;

public abstract class AbstractController {

    // sleep time in ms
    private static final int SLEEP_TIME_MS = 100;

    Model model;
    private int sleepTime;

    AbstractController(){
        sleepTime = SLEEP_TIME_MS;
    }

    public void setModel(Model model){
        this.model = model;
    }

    public void setSleepTime(int newSleepTime){
        sleepTime = newSleepTime;
    }

    abstract void move();

    /**
     * A model is trained using a sequence of moves (has to be implemented by the
     * child of this class). The score at the moment when the game is over, is returned
     * @return the score of the model;
     */
    public int train(){
        if (model == null){
            throw new IllegalArgumentException("Model not set!");
        }
        while(!model.gameOver()){
            sleep();
            move();
        }
        return model.getScore();
    }

    private void sleep(){
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e){
            System.out.println("Error in Thread.sleep: " + e.getMessage());
        }
    }
}
