package controller.statistics;

import controller.AbstractController;
import model.Model;

public class ControllerManager {

    public static int NUM_ITERATIONS = 100;

    private int iterations;
    private Class<? extends AbstractController> controllerClass;

    public ControllerManager(Class<? extends AbstractController> controllerClass){
        this.controllerClass = controllerClass;
        iterations = NUM_ITERATIONS;
    }

    public void setIterations(int newIterations){
        this.iterations = newIterations;
    }


    /**
     * Used to evaluate a controller (given in the constructor) x times
     * x is the amount of iterations (given in setIterations);
     */
    public void start(){
        long totalScore = 0;
        long totalTime = 0;

        System.out.println("Started running " + iterations + " iterations");
        for(int i = 1; i<=iterations; i++){
            int score = 0;
            long estimatedTime = 0;
            try {
                AbstractController controller = controllerClass.newInstance();
                controller.setModel(new Model());
                controller.setSleepTime(0); // don't sleep
                long startTime = System.currentTimeMillis();
                score = controller.train();
                estimatedTime = System.currentTimeMillis() - startTime;
            } catch (InstantiationException | IllegalAccessException e) {
                System.out.println("Cannot instantiate class: " + e.getMessage());
            }
            totalScore += score;
            totalTime += estimatedTime;
            System.out.println("Finished iteration " + i + ": " + score + " (took " + estimatedTime + "ms)");
        }

        long averageScore = totalScore / NUM_ITERATIONS;
        long averageTime = totalTime / NUM_ITERATIONS;
        System.out.println("\n\nAverage score: " + averageScore);
        System.out.println("Average time: " + averageTime);
        System.out.println("Total time: " + totalTime + " ms");
    }
}
