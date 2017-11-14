package launcher;

import java.awt.EventQueue;

import controller.GameController;

public class Launcher {

    /**
     * Start of the program, creates the controller
     * @param args the args for the program (currently not used)
     */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
            try {
                GameController controller = new GameController();
                controller.showView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}
}
