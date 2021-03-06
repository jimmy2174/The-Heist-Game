package game.items;


import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * An unlocked container containing items, usually a small amount of money + a key and/or safe combination
 * @author Lachlan Lee ID# 300281826 Cameron Porter 300279891
 *
 */
public class Desk extends Container implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String filename = "_obj_desk.png";
	String direction = "N";
	double[] size = {0.6, 0.8};
	private ArrayList<Point> positions = new ArrayList<Point>();
	

	public Desk(Point position, Map<String, Integer> items) {
		super(position, items, 0);
		this.positions.add(new Point((int) position.getX(), (int) position.getY() - 1));
	}

	@Override
	public String getFilename() {
		return this.filename;
	}

	@Override
	public String getDirection() {
		return this.direction;
	}
	
	@Override
	public double[] getSize(){
		return this.size;
	}
	
	/**
	 * Sets the position of the desk based on which way the view is rotated
	 * @param direction
	 */
	public void setPositions(String direction){
		this.positions.clear();
		this.direction = direction;
		
		Point p = this.getPosition();
		this.positions.add(p);
		if(direction.equals("N")){
			this.positions.add(new Point((int) p.getX(), (int) p.getY() - 1));
		}
		else if(direction.equals("E")){
			this.positions.add(new Point((int) p.getX() - 1, (int) p.getY()));
		}
		else if(direction.equals("S")){
			this.positions.add(new Point((int) p.getX(), (int) p.getY() + 1));
		}
		else if(direction.equals("W")){
			this.positions.add(new Point((int) p.getX() + 1, (int) p.getY()));
		}
	}
	
	
	public ArrayList<Point> getPositions(){
		return this.positions;
	}
}