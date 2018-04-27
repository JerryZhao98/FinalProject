package GameObjects;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import GameComponents.Entity;
import Mechanics.Level;
import Mechanics.LevelController;

/**
 * Bed class, enables the user to save. There is one every level at the end.
 * (Represented as 'B' on the character grid)
 * 
 * @author Barbara Guo and Jerry Zhao
 * @version Final Project 2016
 *
 */
public class Bed extends Entity {

	private final int LEVELS = 10;
	private int currentLevel;
	private LevelController levelControl;

	/**
	 * Constructor for the bed, creates it as an object under the entities class
	 * 
	 * @param xCord
	 *            X position of the bed
	 * @param yCord
	 *            Y position of the bed
	 * @param width
	 *            The width of the bed
	 * @param height
	 *            The height of the bed
	 * @param objectType
	 *            The object type ("Bed")
	 * @param imageName
	 *            The name of the image (useless in this case)
	 * @param currentLevel
	 *            The current level of which the bed is located on
	 */
	public Bed(double xCord, double yCord, int width, int height,
			String objectType, String imageName, int currentLevel, LevelController levelControl) {
		super(xCord, yCord, width, height, objectType, imageName);

		this.levelControl = levelControl;
		this.currentLevel = currentLevel;
	}

	/**
	 * Saves the game by writing to a player text file the current level of the
	 * player and the highest level that they have completed
	 */
	public void save() {
		int highestLvlCompleted = levelControl.getHighestLevel();
		
		//Try to print the numbers (representing levels) into a text file
		try {
			PrintWriter fileOut = new PrintWriter(new FileWriter(
					"playerFile.txt"));

			if (currentLevel != LEVELS) {
				fileOut.println(currentLevel + 1);
			} else {
				fileOut.println(currentLevel);
			}

			if (currentLevel >= highestLvlCompleted) {
				fileOut.println(currentLevel);
				highestLvlCompleted = currentLevel;
			} else {
				fileOut.println(highestLvlCompleted);
			}
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the level the player will be entering
	 * @return The level the player will be starting
	 */
	public int getNextCurrentLevel() {
		if (currentLevel != 0) {
			return currentLevel + 1;
		} else {
			return 0;
		}
	}

}
