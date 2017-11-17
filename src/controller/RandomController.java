package controller;

import model.Model;

public class RandomController extends Thread {

    // sleep time in ms
    private static final int SLEEP_TIME_MS = 100;

    private Model model;
    private boolean isRunning;

    public RandomController(Model model){
        this.model = model;
        isRunning = true;
    }

    @Override
    public void run(){
        while(isRunning) {

            sleep();

            move();

            if (model.gameOver()){
                isRunning = false;
                System.out.println("Final score: " + model.getScore());
            }
        }
    }

    private void sleep(){
        try {
            Thread.sleep(SLEEP_TIME_MS);
        } catch (InterruptedException e){
            System.out.println("Error in Thread.sleep: " + e.getMessage());
        }
    }

    private void move(){
        double random = Math.random();

        if (random < 0.25){
            model.left();
        } else if (random < 0.5){
            model.right();
        } else if (random < 0.75){
            model.up();
        } else {
            model.down();
        }
    }
}
