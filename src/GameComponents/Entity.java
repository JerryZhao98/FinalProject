package GameComponents;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Generic class for all the objects in the game (including the player)
 * @author Barbara Guo and Jerry Zhao
 * @version Final Project 2016
 *
 */
public class Entity {

	protected double xCord;
	protected double yCord;
	private double hSpeed;
	private double vSpeed;
	private int width;
	private int height;
	private boolean exist;
	private String direction;
	private String objectType;
	private String imageName;
	private Image image;
	protected double origXCord;
	protected double origYCord;

	/**
	 * Constructor for the entity, initiates it
	 * @param xCord X cord of the entity
	 * @param yCord Y cord of the entity
	 * @param width Width of the entity
	 * @param height Height of the entity
	 * @param objectType Object type (Class name)
	 * @param name Name of the entity image
	 */
	public Entity(double xCord, double yCord, int width, int height,
			String objectType, String name) {
		this.xCord = xCord;
		this.yCord = yCord;
		this.width = width;
		this.height = height;
		this.objectType = objectType;
		this.origXCord = xCord;
		this.origYCord = yCord;
		this.exist = true;
		this.imageName = name;
		
		//Tries to load the image for the entity
		try {
			image = ImageIO.read(new File(name));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.print("failed");
		}
	}

	/**
	 * Checks for collisions between the entity and other entities/walls/etc.
	 * @param entity Entity being check
	 * @return Whether or not a collision has taken place
	 */
	public boolean collisionCheck(Entity entity) {
		if ((this.xCord + this.width) >= entity.getXCord()
				&& this.xCord < entity.getXCord() + entity.getWidth()
				&& (this.yCord + this.height) >= entity.getYCord()
				&& this.yCord <= entity.getYCord() + entity.getHeight()) {
			return true;
		}
		else
		{
			return false;
		}
	}
	public double getOrigXCord() {
		return origXCord;
	}

	public void setOrigXCord(double origXCord) {
		this.origXCord = origXCord;
	}

	public double getOrigYCord() {
		return origYCord;
	}

	public void setOrigYCord(double origYCord) {
		this.origYCord = origYCord;
	}
	
	public void setImageName(String name)
	{
		this.imageName = name;
	}
	
	public String getImageName()
	{
		return this.imageName;
	}
	
	public Image getImage()
	{
		return this.image;
	}
	
	public void setImage(Image img)
	{
		this.image = img;
	}
	

	public void setDirection(String direction)
	{
		this.direction = direction;
	}
	
	public String getDirection()
	{
		return this.direction;
	}
	public double getXCord() {
		return this.xCord;
	}

	public void setXCord(double xCord) {
		this.xCord = xCord;
	}

	public double getYCord() {
		return this.yCord;
	}

	public void setYCord(double yCord) {
		this.yCord = yCord;
	}

	public double getHSpeed()
	{
		return this.hSpeed;
	}
	
	public void setHSpeed(double hSpeed)
	{
		this.hSpeed = hSpeed;
	}
	
	public double getVSpeed()
	{
		return this.vSpeed;
	}
	
	public void setVSpeed(double vSpeed)
	{
		this.vSpeed = vSpeed;
	}
	
	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isFalling() {
		return this.isFalling();
	}

	public boolean isExist() {
		return this.exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
	}
	
	public String getType()
	{
		return this.objectType;
	}
	
	public void setType(String objectType)
	{
		this.objectType = objectType;
	}

	/**
	 * Resets the entity
	 */
	public void reset() {
		xCord = origXCord;
		yCord = origYCord;
		hSpeed = 0;
		vSpeed = 0;
	}
}
