package game.items;

import java.awt.Point;

/**
 * Interface for all the different items that can be placed in rooms of the bank
 * @author Lachlan
 *
 */
public interface Item {

	Point getPosition();

	String getFilename();

	String getDirection();

}