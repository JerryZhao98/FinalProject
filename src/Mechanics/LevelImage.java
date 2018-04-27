package Mechanics;

/**
 * Object that stores details of each background image
 * @author Barbara Guo and Jerry Zhao
 * @version Final Project 2016
 *
 */
public class LevelImage {

	private String imageName;
	private int level;
	private int length;
	private int height;
	
	/**
	 * Types: Background ('b'), foreground ('f'), foreforeground ('g'), midground ('m'), continuation('c)
	 */
	private char type; 
	
	/**
	 * Constructor for the level image
	 * @param imageName Name of the level the image is used in
	 * @param level Level number
	 * @param length Length of the image
	 * @param height Height of the image
	 * @param type Type of image (used in determining the order for drawing the images)
	 */
	LevelImage(String imageName, int level, int length, int height, char type)
	{
		this.imageName = imageName;
		this.level = level;
		this.type = type;
		this.length = length;
		this.height = height;
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public void setLevel(int lvl)
	{
		this.level = lvl;
	}
	public String getName()
	{
		return this.imageName;
	}
	
	public int getLength()
	{
		return this.length;
	}
	
	public char getType()
	{
		return this.type;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
}
