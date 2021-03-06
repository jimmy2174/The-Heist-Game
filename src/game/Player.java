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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author james.barfoote, Lachlan Lee ID# 300281826,	Cameron Porter 300279891
 * Represents everything that a player is.
 * Holds methods for the interactions players have with objects and doors, eg: Picking up money, looting desks
 *
 */
public class Player implements Serializable{
	private String name;
	private Room room;
	private Weapon weapon;
	private int score;
	private Type t; //Whether the player is a robber or a guard
	private int ID;
	private Point currentPosition, oldPosition; //Location(coords of the square with current room)
	private Map<String, Integer> inventory;	//contains player items
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
	 * Rotates the player one cardinal direction clockwise/anti-clockwise
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

	/**
	 *
	 * @param direction The current direction the player is facing
	 */
	public void setDirection(int direction){
		this.direction = direction;
	}

	public enum Type{
		robber, guard
	}

	/**
	 *
	 * @param name The name of the player
	 * @param w The players weapon
	 * @param PlayerNum The players unique ID
	 * @param p The players location represented as a point on the canvas
	 * @param t The players type (robber or guard)
	 */
	public Player(String name, Point p, Type t)
	{
		this.name = name;
		this.currentPosition = p;
		this.oldPosition  = p;
		this.t = t;
		inventory = new HashMap<String, Integer>();
	}

	/**
	 * Checks to see if you have to appropriate key to unlock a door, if so, the door is unlocked
	 * @param d The door they want to unlock
	 */
	public boolean unlockDoor(Door d){
		//Safety check to ensure you aren't trying to unlock an unlocked door
		if(d.isLocked()){
			//Iterates through your items looking for keys
			for(String s : inventory.keySet()){
				//If you have at least one key you can unlock the door
				if(s.equals("Key")){
					d.unlockDoor();
					//Removes a key from your inventory
					if(inventory.get("Key") == 1){
						inventory.remove("Key");
					}
					else{
						inventory.put("Key", inventory.get("Key")-1);
					}
					return true; //You unlocked the door
				}
			}
			return false; //You do not have a key in your inventory
		}
		return true; //The door was unlocked to begin with
	}

//	/**
//	 * Attempts to unlock the given safe with the given combination attempt.
//	 * Combinations are stored as integer arrays of length 4
//	 * @param combinatAttempt
//	 * @param s
//	 * @return true if safe was unlocked, false if safe still locked
//	 */
//	public boolean attemptSafeCrack(int[] combinatAttempt, Safe s){
//		if(combinatAttempt.length != 4){ } //throw an exception here (need to make exception)
//		return s.unlockSafe(combinatAttempt);
//	}
//
	/**
	 * Loots the given container by adding the item/money in it to the characters items array/money integer
	 * If the container is a safe, checks if it is unlocked first.
	 * Removes the items from the container
	 * @param c
	 */
	public void lootContainer(Container contain, GameCanvas canvas){
		if(contain instanceof Safe){
			Safe safe = (Safe) contain;
			if(!safe.isLocked()){
				if(safe.getMoney() != 0){ moneyHeld += safe.getMoney(); } //Increments the players money by the amount in the safe
				contain.containerLooted(); //Sets the safe to empty
			}
			else{
				if(attemptSafeUnlock(safe)){
					if(safe.getMoney() != 0){ moneyHeld += safe.getMoney(); } //Increments the players money by the amount in the safe
					contain = safe; //Sets the container as the unlocked safe
					contain.containerLooted(); //Sets the safe to empty
				}
			}
		}
		else{
			Desk d = (Desk) contain;

			//Open the Inventory Trading screen for looting the desk
			canvas.openTrade(d);
		}
	}

	/**
	 * Attempt to unlock the safe with a safe combination.
	 * @param s
	 * @return true if you had a safe combination and the door gets unlocked
	 */
	private boolean attemptSafeUnlock(Safe s) {
		for(String str : inventory.keySet()){
			//If you have at least one safe combination in your inventory
			if(str.equals("Safe Combination")){
				//Unlocks the safe
				s.unlock();

				//Removes one Safe Combination from your inventory
				if(inventory.get("Safe Combination") == 1){
					inventory.remove("Safe Combination");
				}
				else{
					inventory.put("Safe Combination", inventory.get("Safe Combination")-1);
				}
				return true;
			}
		}
		return false; //You do no have a safe combination in your inventory
	}

	/**
	 * Checks one tile ahead in the players facing direction for an InteractableItem
	 * @return an InteractableItem
	 * @param The games canvas to draw onto
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
	 * @param The position of the item, canvas to draw to
	 * @return an InteractableItem if its present
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
			if(item.getFilename().equals("_obj_floorSafe.png")){
				Safe s = (Safe) item;
				if(s.getPosition().equals(pos)){
					return s;
				}
			}

		}
		return null;
	}

	/**
	 * Checks for a door in a given direction from the player
	 * @param canvas
	 * @return a door if its present
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
	 * @return door
	 */
	private Door findDoor(Point pos, GameCanvas canvas){
		for(Door d : canvas.getDoors()){
			if(d.getPosition().equals(pos)){
				return d;
			}
		}
		return null;
	}

	/**
	 * Adds the amount picked up to the money the player is holding
	 * @param Money that is picked up
	 */
	public void pickUpMoney(Money m){
		moneyHeld += m.getAmount();
	}

	/**
	 * Substracts the amount of money dropped from the money the player is holding
	 * @param Dollars the player is dropping
	 */
	public void dropMoney(int d){
		moneyHeld = moneyHeld - d;
	}

	/**
	 *
	 * @return The players id
	 */
	public int getID()
	{
		return ID;
	}

	/**
	 * Sets the players unique ID
	 * @param i The new ID for the player
	 */
	public void setID(int i)
	{
		this.ID = i;
	}

	/**
	 *
	 * @return The players current score
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * @return The room the player is currently in
	 */
	public Room getRoom()
	{
		return room;
	}

	/**
	 * @return Weapon The weapon the player is currently holding
	 */
	public Weapon getWeapon()
	{
		return weapon;
	}

	/**
	 * @return the type this player is
	 */
	public Type getPlayerType()
	{
		return t;
	}

	/**
	 * @return
	 * @param takes an integer and sets the score to the new score
	 */
	public void updateScore(int s)
	{
		score = s;
	}

	/**
	 * @return
	 * @param takes a room object and changes the player to that room
	 */
	public void updateRoom(Room r)
	{
		room = r;
	}

	/**
	 * @return
	 * @param takes a weapon object and changes the current weapon to the new one
	 */
	public void changeWeapon(Weapon w)
	{
		weapon = w;
	}

	/**
	 * @return
	 * @param takes player type and sets it
	 * @
	 */
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

	/**
	 * @return the amount of money the player current has on them
	 */
	public int getMoneyHeld(){
		return moneyHeld;
	}

	/**
	 * @return all the items the player is holding
	 */
	public Map<String, Integer> getInventory(){
		return inventory;
	}

	/**
	 * sets the items the player is holding - used for loading
	 */
	public void setInventory(Map<String, Integer> inventory){
		this.inventory = inventory;
	}

	/**
	 * @return the name of the player
	 */
	public String getName()
	{
		return this.name;
	}

	@Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

	public void setMoney(int money){
		moneyHeld = money;
	}
}
