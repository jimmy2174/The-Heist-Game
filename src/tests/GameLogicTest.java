package tests;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import game.Door;
import game.Money;
import game.Player;
import game.Player.Type;
import game.control.gameAction;
import game.control.moveAction;
import game.items.Desk;
import game.items.Safe;
import graphics.GameCanvas;
import graphics.GameCanvas.State;
import graphics.MainMenu;

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Tests for the Game Logic
 * @author Lachlan Lee ID# 300281826
 *
 */
public class GameLogicTest extends TestCase{

	Player p = new Player("Bob", new Point(4,4), Type.robber);
	List<Player> play = new ArrayList<Player>();
	GameCanvas canvas = new GameCanvas(new Dimension(1366, 768),p , play);
	gameAction g = new gameAction("P", p, canvas);
	gameAction gDrop = new gameAction("B", p, canvas);
	moveAction m = new moveAction("Left", p, canvas);
	moveAction m2 = new moveAction("Up", p, canvas);
	moveAction m3 = new moveAction("Right", p, canvas);
	moveAction m4 = new moveAction("Down", p, canvas);
	
	public @Test void test_unlock_door_true(){
		Door d = new Door(true, new Point(4,5));
		p.getInventory().put("Key", 1);
		assertTrue(p.unlockDoor(d));
	}
	
	public @Test void test_unlock_door_false(){
		Door d = new Door(true, new Point(4,5));
		assertTrue(!p.unlockDoor(d));
	}
	
	public @Test void test_desk_position_north(){
		Desk d = new Desk(new Point(4,4), null);
		d.setPositions("N");
		assertTrue(d.getPositions().contains(new Point(4,3)));
	}
	
	public @Test void test_desk_position_east(){
		Desk d = new Desk(new Point(4,4), null);
		d.setPositions("E");
		assertTrue(d.getPositions().contains(new Point(3,4)));
	}
	
	public @Test void test_desk_position_south(){
		Desk d = new Desk(new Point(4,4), null);
		d.setPositions("S");
		assertTrue(d.getPositions().contains(new Point(4,5)));
	}
	
	public @Test void test_desk_position_west(){
		Desk d = new Desk(new Point(4,4), null);
		d.setPositions("W");
		assertTrue(d.getPositions().contains(new Point(5,4)));
	}
	
	public @Test void test_loot_safe_locked_true(){
		Safe s = new Safe(new Point(4, 5), 10000, true);
		p.getInventory().put("Safe Combination", 1);
		p.lootContainer(s, canvas);
		assertTrue(!s.isLocked());
		assertTrue(s.getMoney() == 0);
		assertTrue(p.getMoneyHeld() == 10000);
	}
	
	public @Test void test_loot_safe_locked_false(){
		Safe s = new Safe(new Point(4, 5), 10000, true);
		p.lootContainer(s, canvas);
		assertTrue(s.isLocked());
		assertTrue(s.getMoney() != 0);
	}
	
	public @Test void test_loot_safe_unlocked_true(){
		Safe s = new Safe(new Point(4, 5), 10000, false);
		p.lootContainer(s, canvas);
		assertTrue(!s.isLocked());
		assertTrue(s.getMoney() == 0);
		assertTrue(p.getMoneyHeld() == 10000);
	}
	
	//Also tests checkForInteract for the case of finding money
	public @Test void test_pick_up_cash(){
		Money m = new Money(10000, new Point(4,4));
		canvas.getItems().add(m);
		g.actionPerformed(new ActionEvent(m, 0, "P")); //Attempts to interact with an item
		assertTrue(p.getMoneyHeld() == 10000);
	}
	
	public @Test void test_check_for_container_north(){
		Safe s = new Safe(new Point(4, 5), 10000, false);
		p.setDirection(0);
		canvas.getItems().add(s);
		g.actionPerformed(new ActionEvent(s, 0, "P")); //Attempts to interact with an item
		assertTrue(s.getMoney() == 0);
	}
	
	public @Test void test_check_for_container_east(){
		Safe s = new Safe(new Point(5, 4), 10000, false);
		p.setDirection(1);
		canvas.getItems().add(s);
		g.actionPerformed(new ActionEvent(s, 0, "P")); //Attempts to interact with an item
		assertTrue(s.getMoney() == 0);
	}
	
	public @Test void test_check_for_container_south(){
		Safe s = new Safe(new Point(4, 3), 10000, false);
		p.setDirection(2);
		canvas.getItems().add(s);
		g.actionPerformed(new ActionEvent(s, 0, "P")); //Attempts to interact with an item
		assertTrue(s.getMoney() == 0);
	}
	
	public @Test void test_check_for_container_west(){
		Safe s = new Safe(new Point(3, 4), 10000, false);
		p.setDirection(3);
		canvas.getItems().add(s);
		g.actionPerformed(new ActionEvent(s, 0, "P")); //Attempts to interact with an item
		assertTrue(s.getMoney() == 0);
	}
	
	public @Test void test_check_for_door_unlock_true_north(){
		Door d = new Door(true, new Point(4, 5));
		p.setDirection(0);
		p.getInventory().put("Key", 1);
		canvas.getDoors().add(d);
		g.actionPerformed(new ActionEvent(d, 0, "P")); //Attempts to interact with an item
		assertTrue(!d.isLocked());
	}
	
	public @Test void test_check_for_door_unlock_true_east(){
		Door d = new Door(true, new Point(5, 4));
		p.setDirection(1);
		p.getInventory().put("Key", 1);
		canvas.getDoors().add(d);
		g.actionPerformed(new ActionEvent(d, 0, "P")); //Attempts to interact with an item
		assertTrue(!d.isLocked());
	}
	
	public @Test void test_check_for_door_unlock_true_south(){
		Door d = new Door(true, new Point(4, 3));
		p.setDirection(2);
		p.getInventory().put("Key", 1);
		canvas.getDoors().add(d);
		g.actionPerformed(new ActionEvent(d, 0, "P")); //Attempts to interact with an item
		assertTrue(!d.isLocked());
	}
	
	public @Test void test_check_for_door_unlock_true_west(){
		Door d = new Door(true, new Point(3, 4));
		p.setDirection(3);
		p.getInventory().put("Key", 1);
		canvas.getDoors().add(d);
		g.actionPerformed(new ActionEvent(d, 0, "P")); //Attempts to interact with an item
		assertTrue(!d.isLocked());
	}
	
	public @Test void test_drop_money_full_stack(){
		Money m = new Money(1000, new Point(4,4));
		int oldcanvasitems = canvas.getItems().size();
		p.pickUpMoney(m);
		gDrop.actionPerformed(new ActionEvent(m, 0, "B")); //Attempts to drop money
		assertTrue(p.getMoneyHeld() == 500);
		assertTrue(oldcanvasitems == canvas.getItems().size()-1); //Checks the dropped money was added to canvas
	}
	
	public @Test void test_drop_money_partial_stack(){
		Money m = new Money(322, new Point(4,4));
		int oldcanvasitems = canvas.getItems().size();
		p.pickUpMoney(m);
		gDrop.actionPerformed(new ActionEvent(m, 0, "B")); //Attempts to drop money
		assertTrue(p.getMoneyHeld() == 0);
		assertTrue(oldcanvasitems == canvas.getItems().size()-1); //Checks the dropped money was added to canvas
	}
	
	public @Test void test_drop_money_fail(){
		int oldcanvasitems = canvas.getItems().size();
		gDrop.actionPerformed(new ActionEvent(p, 0, "B")); //Attempts to drop money
		assertTrue(p.getMoneyHeld() == 0);
		assertTrue(oldcanvasitems == canvas.getItems().size()); //Checks that no money was added to the canvas
	}
	
	public @Test void test_move_left_true(){
		canvas.initialize();
		Point old = p.getLocation();
		Point newP = new Point((int)old.getX(),(int) old.getY()-1);
		m.actionPerformed(new ActionEvent(p, 0, "Left"));
		assertTrue(p.getOldLocation().equals(old));
		assertTrue(p.getLocation().equals(newP));
	}
	
	public @Test void test_move_right_true(){
		canvas.initialize();
		Point old = p.getLocation();
		Point newP = new Point((int)old.getX(),(int) old.getY()+1);
		m3.actionPerformed(new ActionEvent(p, 0, "Right"));
		assertTrue(p.getOldLocation().equals(old));
		assertTrue(p.getLocation().equals(newP));
	}
	
	public @Test void test_move_up_true(){
		canvas.initialize();
		Point old = p.getLocation();
		Point newP = new Point((int)old.getX()+1,(int) old.getY());
		m2.actionPerformed(new ActionEvent(p, 0, "Up"));
		assertTrue(p.getOldLocation().equals(old));
		assertTrue(p.getLocation().equals(newP));
	}
	
	public @Test void test_move_down_true(){
		canvas.initialize();
		Point old = p.getLocation();
		Point newP = new Point((int)old.getX()-1,(int) old.getY());
		m4.actionPerformed(new ActionEvent(p, 0, "Down"));
		assertTrue(p.getOldLocation().equals(old));
		assertTrue(p.getLocation().equals(newP));
	}
	
	public @Test void test_move_left_false(){
		canvas.initialize();
		p.setLocation(new Point(1,1));
		m.actionPerformed(new ActionEvent(p, 0, "Left"));
		//The points are different as oldLocation was initialised as (4,4)
		//and the current location is set at (1,1) so they should stay the same when you cant move
		assertTrue(p.getOldLocation().equals(new Point(4,4)));
		assertTrue(p.getLocation().equals(new Point(1,1)));
	}
	
	public @Test void test_move_right_false(){
		canvas.initialize();
		p.setLocation(new Point(1,38));
		m3.actionPerformed(new ActionEvent(p, 0, "Right"));
		//The points are different as oldLocation was initialised as (4,4)
		//and the current location is set at (1,38) so they should stay the same when you cant move
		assertTrue(p.getOldLocation().equals(new Point(4,4)));
		assertTrue(p.getLocation().equals(new Point(1,38)));
	}
	
	public @Test void test_move_up_false(){
		canvas.initialize();
		p.setLocation(new Point(38,1));
		m.actionPerformed(new ActionEvent(p, 0, "Up"));
		//The points are different as oldLocation was initialised as (4,4)
		//and the current location is set at (38,1) so they should stay the same when you cant move
		assertTrue(p.getOldLocation().equals(new Point(4,4)));
		assertTrue(p.getLocation().equals(new Point(38,1)));
	}
	
	public @Test void test_move_down_false(){
		canvas.initialize();
		p.setLocation(new Point(1,1));
		m.actionPerformed(new ActionEvent(p, 0, "Down"));
		//The points are different as oldLocation was initialised as (4,4)
		//and the current location is set at (1,1) so they should stay the same when you cant move
		assertTrue(p.getOldLocation().equals(new Point(4,4)));
		assertTrue(p.getLocation().equals(new Point(1,1)));
	}
	
	public @Test void test_player_rotate_clockwise_wraparound(){
		p.setDirection(0);
		p.rotatePlayer("clockwise");
		assertTrue(p.getDirection().equals("W"));
	}
	
	public @Test void test_player_rotate_clockwise_regular(){
		p.setDirection(1);
		p.rotatePlayer("clockwise");
		assertTrue(p.getDirection().equals("N"));
	}
	
	public @Test void test_player_rotate_anticlockwise_wraparound(){
		p.setDirection(3);
		p.rotatePlayer("anti-clockwise");
		assertTrue(p.getDirection().equals("N"));
	}
	
	public @Test void test_player_rotate_anticlockwise_regular(){
		p.setDirection(2);
		p.rotatePlayer("anti-clockwise");
		assertTrue(p.getDirection().equals("W"));
	}
}
