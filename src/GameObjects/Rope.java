package GameObjects;

import GameComponents.Entity;

/**
 * Rope object. Players can climb ropes. The start of a rope is marked by a 'Q'
 * and the end is marked by a 'R' on the character grid.
 * 
 * @author Jerry Zhao and Barbara Guo
 * @version Final Project 2016
 *
 */
public class Rope extends Entity {
	private double ropeYEnd = 0;
	private boolean ropeFinished = false;

	/**
	 * Constructor for the rope, initiates it
	 * @param xCord X position of the rope
	 * @param yCord Y position of the rope
	 * @param width Width of the rope
	 * @param height Height of the rope
	 * @param objectType Object type: "Rope"
	 * @param imageName Rope image name (not applicable)
	 */
	public Rope(double xCord, double yCord, int width, int height,
			String objectType, String imageName) {
		super(xCord, yCord, 1, 1, objectType, imageName);
		this.ropeFinished = false;
	}

	/**
	 * Sets the Y value for the end of the rope
	 * @param ropeYEnd The Y value for the end of the rope
	 */
	public void setRopeYEnd(double ropeYEnd) {
		this.ropeYEnd = ropeYEnd;
		setHeight((int) Math.round(ropeYEnd - yCord));
		this.ropeFinished = true;
	}

	/**
	 * Gets whether or not the rope is finished
	 * @return Whether or not the rope is finished
	 */
	public boolean isRopeFinished() {
		return this.ropeFinished;
	}
}
