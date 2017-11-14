package model;

import java.util.Observable;

public class GameModel extends Observable {


	public GameModel(){

	}


	public void update(){

	}

	public void draw(){
		notifyObservers(this);
	}

}
