package GameObjects;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;

import GameComponents.Entity;
import Mechanics.PhysicsEngine;

/**
 * Light source object. The light source illuminates an area for the player to
 * move around. They can be carried and anything outside the radius of a light
 * source is considered void (not collision detecting)
 * 
 * @author Jerry Zhao and Barbara Guo
 * @version Final Project 2016
 *
 */
public class LightSource extends Entity {

	private double origRadius;
	private double radius;
	private boolean isHeld;
	private boolean onConveyor = false;
	private boolean isLamp = false;
	protected int expireTime = 60000;
	protected long timeStart;
	protected long timeEnd;

	/**
	 * Initiates the light object
	 * 
	 * @param xCord
	 *            X position of the light
	 * @param yCord
	 *            Y position of the light
	 * @param width
	 *            Width of the light
	 * @param height
	 *            Height of the light
	 * @param objectType
	 *            Object type: "LightSource"
	 * 
	 * @param radius
	 *            Radius of the light source
	 * @param name
	 *            Name of the light source image
	 */
	public LightSource(double xCord, double yCord, int width, int height,
			String objectType, double radius, String name) {
		super(xCord, yCord, width, height, objectType, name);

		this.origRadius = radius;
		this.isHeld = false;
	}

	/**
	 * Continuously updates the radius of the light source so that it shrinks
	 * over time
	 */
	public void updateRadius() {
		timeEnd = System.currentTimeMillis();
		if (origRadius - (timeEnd - timeStart) * 1.0 / expireTime * origRadius <= 0) {
			setRadius(0);
			return;
		}
		setRadius(origRadius - (timeEnd - timeStart) * 1.0 / expireTime
				* origRadius);
	}

	/**
	 * Resets the light source
	 */
	public void reset() {
		setXCord(getOrigXCord());
		setYCord(getOrigYCord());
		setOnConveyor(false);
		setIsHeld(false);
		setIsLamp(false);
		origRadius = PhysicsEngine.lightSourceDiameter;
		setRadius(origRadius);
		timeStart = System.currentTimeMillis();
	}

	/**
	 * Checks if the light source is a lamp
	 * 
	 * @return Whether or not the light source is a lamp
	 */
	public boolean isLamp() {
		return isLamp;
	}

	/**
	 * Sets the boolean keeping track of whether or not a light source is a lamp
	 * to true or false
	 * 
	 * @param isLamp
	 *            Whether or not the light source is a lamp
	 */
	public void setIsLamp(boolean isLamp) {
		this.isLamp = isLamp;
		// this.lightType = "L";
	}

	/**
	 * Sets the lightsource to a lamp
	 * 
	 * @param lamp
	 *            Lamp to be set up
	 */
	public void setLamp(Lamp lamp) {
		setIsLamp(true);
		setXCord(lamp.getXCord() + (lamp.getWidth() - 1) / 2 - (getWidth() - 1)
				/ 2);
		setYCord(lamp.getYCord() - (getHeight() - 1) / 2);
		setRadius(lamp.getRadius());

		// Starts the timer
		timeStart = System.currentTimeMillis();
		expireTime = 120000;
		origRadius = lamp.getRadius();
	}

	/**
	 * Gets whether or not the light source is on a conveyor (it cannot be
	 * picked up while it is so)
	 * 
	 * @return Whether or not a light source is on a conveyor
	 */
	public boolean isOnConveyor() {
		return onConveyor;
	}

	/**
	 * Sets the light source so that it is on or off a conveyor
	 * 
	 * @param onConveyor
	 *            Whether or not the lamp is to be on a conveyor
	 */
	public void setOnConveyor(boolean onConveyor) {
		this.onConveyor = onConveyor;
	}

	/**
	 * Gets whether or not the light is being held by the player
	 * 
	 * @return True if the light is being held, false if otherwise
	 */
	public boolean isHeld() {
		return isHeld;
	}

	/**
	 * Sets the light to being held by the player
	 * @param isHeld The light is held by the player
	 */
	public void setIsHeld(boolean isHeld) {
		this.isHeld = isHeld;
	}

	/**
	 * Sets the radius of the light source
	 * @param radius The radius of the light source
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

	/**
	 * Gets the radius of the light source
	 * @return The radius of the light source
	 */
	public double getRadius() {
		return this.radius;
	}

	public void run() {

	}

}
