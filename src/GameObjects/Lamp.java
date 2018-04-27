package GameObjects;

import Mechanics.PhysicsEngine;

/**
 * Lamp object. A permanent (meaning the player can't retrieve the light source
 * afterwards) light source that has a larger radius of light. Players can put
 * their light sources into lamp objects.
 * 
 * @author Jerry Zhao and Barbara Guo
 * @version Final Project 2016
 *
 */
public class Lamp extends LightSource {

	private boolean isLit;
	private static final double RADIUS = 1500;

	/**
	 * Constructor for the lamp object, creates it.
	 * @param xCord X position of the lamp
	 * @param yCord Y position of the lamp
	 * @param width The width of the lamp
	 * @param height The height of the lamp
	 * @param objectType The object type ("Lamp")
	 * @param name Name of the image (not applicable)
	 */
	public Lamp(double xCord, double yCord, int width, int height,
			String objectType, String name) {
		super(xCord, yCord, width, height, objectType, RADIUS, name);
		this.isLit = false;
		setRadius(RADIUS);
	}

	/**
	 * Gets whether or not the lamp has been lit up 
	 * @return Whether or not the lamp is currently lit
	 */
	public boolean isLit() {
		return isLit;
	}

	/**
	 * Sets the lamp to being lit
	 * @param isLit Whether or not the lamp is lit (always true)
	 */
	public void setLit(boolean isLit) {
		this.isLit = isLit;
	}

	/**
	 * Lights the lamp
	 */
	public void light() {
		setLit(true);
		setRadius(RADIUS);
	}
	
	/**
	 * Unlights the lamp during level resets
	 */
	public void reset() {
		isLit = false;
	}

}
