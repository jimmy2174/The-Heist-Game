package graphics;

import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.swing.JFrame;

/**
 * Creates the game application window which displays the gui
 * @author godfreya
 *
 */

public class GameFrame extends JFrame{

	private Canvas canvas;

	public GameFrame(){
		super("The Heist");
		canvas = new GameCanvas();
		setLayout(new BorderLayout());
		add(canvas, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setUndecorated(true); makes full screen
		pack();
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		canvas.requestFocus();
	}

	public void repaint(){
		canvas.repaint();
	}

	//just for testing
	public static void main(String[] args){
		new GameFrame();
	}
}