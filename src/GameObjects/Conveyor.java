package GameObjects;

import GameComponents.Entity;

/**
 * Conveyor object. Used to move light sources both vertically and horizontally.
 * Starting position is marked by a 'C' and the ending position by a 'Y'.
 * 
 * @author Jerry Zhao and Barbara Guo
 * @version Final Project 2016
 *
 */
public class Conveyor extends Entity {

	private int direction = 0;
	private boolean isHorizontal = true;

	/**
	 * Constructor for the conveyor. Creates it
	 * 
	 * @param xCord
	 *            X position of where the conveyor starts
	 * @param yCord
	 *            Y position of where the conveyor starts
	 * @param width
	 *            The width of the conveyor (length of the conveyor)
	 * @param height
	 *            The height of the conveyor
	 * @param objectType
	 *            The object type ("conveyor")
	 * @param imageName
	 *            The image name (useless in this case)
	 * @param defaultDirection
	 *            The default direction of the conveyor (vertical)
	 */
	public Conveyor(double xCord, double yCord, int width, int height,
			String objectType, String imageName, int defaultDirection) {
		super(xCord, yCord, width, height, objectType, imageName);
		direction = defaultDirection;
	}

	/**
	 * Sets where the conveyor ends
	 * 
	 * @param xEndCord
	 *            X position of where the conveyor ends
	 * @param yEndCord
	 *            Y position of where the conveyor ends
	 */
	public void setEndCords(double xEndCord, double yEndCord) {
		if (xEndCord == xCord) {
			isHorizontal = false;
			setHeight((int) Math.round(yEndCord - yCord));
		} else {
			isHorizontal = true;
			setWidth((int) Math.round(xEndCord - xCord));
		}
	}

	/**
	 * Gets the direction of the conveyor
	 * 
	 * @return the direction of the conveyor
	 */
	public int getConveyorDirection() {
		return direction;
	}

	/**
	 * Sets the direction of the conveyor
	 * 
	 * @param direction
	 *            The direction of the conveyor (horizontal or vertical)
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * Whether or not the conveyor travels horizontally
	 * 
	 * @return
	 */
	public boolean isHorizontal() {
		return isHorizontal;
	}

	/**
	 * Sets whether or not the conveyor travels horizontally
	 * 
	 * @param isHorizontal
	 *            Whether or not the conveyor travels horizontally
	 */
	public void setHorizontal(boolean isHorizontal) {
		this.isHorizontal = isHorizontal;
	}

}
