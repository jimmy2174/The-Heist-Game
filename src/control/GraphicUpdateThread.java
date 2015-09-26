package control;

import graphics.GameFrame;

/**
 * Thread for continually updating the graphics display
 * @author godfreya
 */
public class GraphicUpdateThread extends Thread{
	private GameFrame display;
	private final int DELAY = 33; //delay between updates, set to 33 to give roughly 30 frames per second
	
	public GraphicUpdateThread(GameFrame gf){
		display = gf;
	}
	
	//run thread to update graphics forever
	public void run() {
		while(true) {		
			try {
				Thread.sleep(DELAY);
				display.repaint();
			} catch(InterruptedException e) {}			
		}
	}
	
}