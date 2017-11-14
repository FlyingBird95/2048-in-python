package controller;

import model.GameModel;

/**
 * GameThread is responsible for updating the model every FRAME_PERIOD ms.
 */
public class GameThread extends Thread {

    private GameModel model;
    private boolean isRunning;

    private final static int MAX_FPS = 30; //desired fps
    private final static int FRAME_PERIOD = 1000 / MAX_FPS; // the frame period

    GameThread(GameModel model){
        this.model = model;
    }

    @Override
    public void start(){
        isRunning = true;
        super.start();
    }

    public void stopGameThread(){
        isRunning = false;
    }

    @Override
    public void run() {
        while(isRunning) {
            long started = System.currentTimeMillis();

            // update
            model.update();

            model.draw();

            float deltaTime = (System.currentTimeMillis() - started);
            int sleepTime = (int) (FRAME_PERIOD - deltaTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                }
                catch (InterruptedException e) {
                }
            }
            while (sleepTime < 0) {
                model.update();
                sleepTime += FRAME_PERIOD;
            }
        }
    }
}
