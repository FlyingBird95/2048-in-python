package controller;

import model.GameModel;
import view.Board;

public class GameController {

	private GameModel model;
	private Board view;
	private GameThread gameThread;

	public GameController() {
		initialize();

		gameThread = new GameThread(this.model);
		gameThread.start();
	}

	private void initialize() {
		// create model
		this.model = new GameModel();

		// create view
		this.view = new Board();

		// ad view as observer for model
		model.addObserver(view);
	}

	public void showView() {
		view.getFrame().setVisible(true);
	}

	/**
	 * TODO: somewhere use this function
	 */
	public void stop(){
		gameThread.stopGameThread();
	}

}
