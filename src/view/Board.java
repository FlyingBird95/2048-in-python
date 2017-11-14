package view;

import java.awt.Window.Type;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import model.GameModel;

public class Board implements Observer {

    private static final int CANVAS_WIDTH = 960;
    private static final int CANVAS_HEIGHT = 640;

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public Board() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrame(new JFrame());
		getFrame().setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	}

	public JFrame getFrame() {
		return frame;
	}

	void setFrame(JFrame frame) {
		this.frame = frame;
		frame.setType(Type.UTILITY);
	}

	@Override
	public void update(Observable o, Object field) {
		if (o instanceof GameModel) {
			GameModel model = (GameModel) o;
			// TODO: update model
		}
	}

}
