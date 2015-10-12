package game;

import game.items.Container;
import game.items.Desk;
import game.items.InteractableItem;
import game.items.Item;
import game.items.Key;
import game.items.Safe;
import game.items.Weapon;
import graphics.GameCanvas;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Player implements Serializable{
	private Room room;
	private Weapon weapon;
	private int score;
	private Type t;
	private int ID;
	private Point currentPosition, oldPosition; //Location(coords of the square with current room)
	private Map<String, Integer> inventory;	//contains player items
	private ArrayList<InteractableItem> items; //Contains player items (Temporarily using until can integrate methods with map)
	private int moneyHeld; //Amount of money held (Possibly temporary, not sure if money will be held in items map)
	private String[] directions = {"N", "E", "S", "W"};
	int direction = 0;
	
	/**
	 * @return the direction
	 */
	public String getDirection() {
		return this.directions[this.direction];
	}

	/**
	 * @param direction the direction to set
	 */
	public void rotatePlayer(String direction) {
		if(direction.equals("clockwise")){
			this.direction--;
			if(this.direction == -1){
				this.direction = 3;
			}
		}
		else if(direction.equals("anti-clockwise")){
			this.direction++;
			if(this.direction == 4){
				this.direction = 0;
			}
		}
	}
	
	public void setDirection(int direction){
		this.direction = direction;
	}

	//Robber or guard(can't pick up money, can open all doors) enum
	public enum Type{
		robber, guard
	}
	
	public Player(Weapon w, int PlayerNum, Point p, Type t)
	{
		this.weapon = w;
		this.currentPosition = p;
		this.oldPosition  = p;
		this.t = t;
	}
	
	/**
	 * Checks to see if you have to appropriate key to unlock a door, if so, the door is unlocked
	 * @param d
	 */
	public boolean unlockDoor(Door d){
		//Safety check to ensure you aren't trying to unlock an unlocked door
		if(d.isLocked()){
			//Iterates through your items looking for keys
			for(InteractableItem i : items){
				if(i instanceof Key){
					Key k = (Key) i; //Assigns the key to a key object
					if(d.unlockDoor(k) == true){
						return true; //The correct key has been found and the door is now unlocked
					}
				}
			}
			return false; //A correct key was not found therefore the door is still locked
		}
		return true; //The door was unlocked to begin with
	}
	
	/**
	 * Attempts to unlock the given safe with the given combination attempt.
	 * Combinations are stored as integer arrays of length 4
	 * @param combinatAttempt
	 * @param s
	 * @return true if safe was unlocked, false if safe still locked
	 */
	public boolean attemptSafeCrack(int[] combinatAttempt, Safe s){
		if(combinatAttempt.length != 4){ } //throw an exception here (need to make exception)
		return s.unlockSafe(combinatAttempt);
	}
	
	/**
	 * Loots the given container by adding the item/money in it to the characters items array/money integer
	 * If the container is a safe, checks if it is unlocked first.
	 * Removes the items from the container
	 * @param c
	 */
	public void lootContainer(Container c){
		if(c instanceof Safe){
			Safe s = (Safe) c;
			if(!s.isLocked()){
				if(s.getMoney() != 0){ moneyHeld += s.getMoney(); } //Increments the players money by the amount in the safe
				c.containerLooted(); //Sets the safe to empty
			}
		}
		else{
			Desk d = (Desk) c;
			if(d.getItems() != null){ 
				for(InteractableItem item : d.getItems()){
					//Adds the item to inventory
					if(!inventory.containsKey(item.getFilename())){
						inventory.put(item.getFilename(), 1); //Adds a new instance of quantity 1
					}
					else{ inventory.put(item.getFilename(), inventory.get(item.getFilename()));} //Increments the current quantity of the item by 1
				}
			}
			if(d.getMoney() != 0){ moneyHeld += d.getMoney(); }
			c.containerLooted(); //Sets Container to empty
		}
	}
	
	/**
	 * Checks one tile ahead in the players facing direction for an InteractableItem
	 * @return
	 */
	public InteractableItem checkforInteract(GameCanvas canvas){
		//Checks for money at the players current position
		if(findItem(this.getLocation(), canvas) != null){
			return findItem(this.getLocation(), canvas);
		}
		//Player facing north
		if(getDirection() == "N"){
			Point oneInFront = new Point(getLocation().x, getLocation().y+1); //The point in front of the character
			return findItem(oneInFront, canvas);
		}
		//Player facing East
		else if(getDirection() == "E"){
			Point oneInFront = new Point(getLocation().x+1, getLocation().y); //The point in front of the character
			return findItem(oneInFront, canvas);
		}
		//Player facing South
		else if(getDirection() == "S"){
			Point oneInFront = new Point(getLocation().x, getLocation().y-1); //The point in front of the character
			return findItem(oneInFront, canvas);
		}
		//Player facing West
		else{
			Point oneInFront = new Point(getLocation().x-1, getLocation().y); //The point in front of the character
			return findItem(oneInFront, canvas);
		}
	}
	
	/**
	 * Checks if the given point in the room has an interactable item on it
	 * @param oneInFront
	 * @return
	 */
	private InteractableItem findItem(Point pos, GameCanvas canvas){
		for(Item item : canvas.getItems()){
			if(item.getFilename().equals("_obj_desk.png")){
				Desk d = (Desk) item;
				if(d.getPositions().contains(pos) || d.getPosition().equals(pos)){
					return d;
				}
			}
			if(item.getFilename().equals("_obj_cashStack.png")){
				Money m = (Money) item;
				if(m.getPosition().equals(pos)){
					return m;
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks for a door in a given direction from the player
	 * @param canvas
	 * @return
	 */
	public Door checkForDoor(GameCanvas canvas){
		//Player facing north
				if(getDirection() == "N"){
					Point oneInFront = new Point(getLocation().x, getLocation().y+1); //The point in front of the character
					return findDoor(oneInFront, canvas);
				}
				//Player facing East
				else if(getDirection() == "E"){
					Point oneInFront = new Point(getLocation().x+1, getLocation().y); //The point in front of the character
					return findDoor(oneInFront, canvas);
				}
				//Player facing South
				else if(getDirection() == "S"){
					Point oneInFront = new Point(getLocation().x, getLocation().y-1); //The point in front of the character
					return findDoor(oneInFront, canvas);
				}
				//Player facing West
				else{
					Point oneInFront = new Point(getLocation().x-1, getLocation().y); //The point in front of the character
					return findDoor(oneInFront, canvas);
				}
	}
	
	/**
	 * Looks for a door at the given position and returns it
	 * @param pos
	 * @param canvas
	 * @return
	 */
	private Door findDoor(Point pos, GameCanvas canvas){
		for(Door d : canvas.getDoors()){
			if(d.getPosition().equals(pos)){
				return d;
			}
		}
		return null;
	}
	
	public void pickUpMoney(Money m){
		moneyHeld += m.getAmount();
	}
	
	public void dropMoney(int d){
		moneyHeld = moneyHeld - d;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public void setID(int i)
	{
		this.ID = i;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public Room getRoom()
	{
		return room;
	}
	
	public Weapon getWeapon()
	{
		return weapon;
	}
	
	public Type getPlayerType()
	{
		return t;
	}
	
	public void updateScore(int s)
	{
		score = s;
	}
	
	public void updateRoom(Room r)
	{
		room = r;
	}
	
	public void changeWeapon(Weapon w)
	{
		weapon = w;
	}
	
	public void setgetPlayerType(Type t)
	{
		this.t = t;
	}
	
	/**
	 * @return the location
	 */
	public Point getLocation() {
		return currentPosition;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Point location) {
		this.currentPosition = location;
	}
	
	/**
	 * @return the oldLocation
	 */
	public Point getOldLocation() {
		return oldPosition;
	}

	/**
	 * @param oldLocation the oldLocation to set
	 */
	public void setOldLocation(Point location) {
		this.oldPosition = location;
	}
	
	public ArrayList<InteractableItem> getItems(){
		return items;
	}
	
	public int getMoneyHeld(){
		return moneyHeld;
	}
	
	public Map<String, Integer> getInventory(){
		return inventory;
	}
	
	@Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
