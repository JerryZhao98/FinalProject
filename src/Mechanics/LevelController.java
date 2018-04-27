package Mechanics;

import GameComponents.Entity;
import GameComponents.Player;
import GameObjects.Bed;
import GameObjects.Conveyor;
import GameObjects.Lamp;
import GameObjects.LightSource;
import GameObjects.Rope;
import Mechanics.Control;

import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelController {

	public static ArrayList<Level> allLevels; // Every level
	public static ArrayList<LevelImage> levelLayers; // Level iamge layers
	public static ArrayList<Entity> entities; // Game objects of current level
	public static Level currentLevel; // Current level object

	// Level values
	private String levelName;
	private int levelWidth;
	private int levelHeight;
	private char[][] charMap;

	// For text file import
	private String currentLine;
	private char currentChar;

	// Object import
	private int objectXCord;
	private int objectYCord;

	private int saveLevel;
	private int highestLevel;

	/*
	 * 0 = space | 1 = block | P = player B = bed | S = light source | L = lamp
	 * R & Q = rope | C & Y = conveyor
	 */

	/**
	 * Constructor that just initiates the levels
	 */
	public LevelController() {
		this.saveLevel = 1;
		this.highestLevel = 1;

	}

	/**
	 * Changes game to desired level
	 * 
	 * @param level
	 *            integer indicated desired level (0 = Level 1)
	 */
	public static void changeLevel(int level) {
		// Directs to main menu after last level is finished
		if (level >= allLevels.size()) {
			GUI.GUI.control.showMain();
			return;
		}
		// Reference level variables to new level
		Control.level = level;
		setCurrentLevel(allLevels.get(Control.level));
		GUI.GUI.gameObjects = currentLevel.getEntities();
		;
		GUI.GUI.grid = currentLevel.getCharMap();
		GUI.GUI.setPlayer(currentLevel.getPlayer());
		// Imports images of new Level
		GUI.GUI.gameObjectImages = new ArrayList<Image>();
		for (int image = 0; image < GUI.GUI.gameObjects.size(); image++) {
			GUI.GUI.gameObjectImages.add(GUI.GUI.gameObjects.get(image)
					.getImage());
		}
		// Displays new level
		GUI.GUI.control.showGame();
	}

	/**
	 * Imports all level text files
	 */
	public void addLevels() {

		// Initializes necessary variables
		Control control = null;
		allLevels = new ArrayList<Level>();
		levelLayers = new ArrayList<LevelImage>();
		entities = new ArrayList<Entity>();
		int levelNumber = 1;

		// Prepares scanner to correctly import level files
		Scanner levelReader = null;
		String fileName = "Level" + Integer.toString(levelNumber) + ".txt";
		File levelTextFile = new File(fileName);
		Player player = new Player(0, 0, 0, 0, null, 0);
		Bed bed = new Bed(0, 0, 0, 0, null, "exit.png", levelNumber, this);

		// Loads all files in correct format
		while (levelTextFile.exists()) {
			// Starts scanner
			try {
				levelReader = new Scanner(levelTextFile);
			} catch (FileNotFoundException e) {
				System.out.println("Text File error. " + fileName);
				e.printStackTrace();
			}

			// Reads basic level values
			this.levelName = levelReader.nextLine();
			this.levelWidth = Integer.parseInt(levelReader.nextLine());
			this.levelHeight = Integer.parseInt(levelReader.nextLine());
			this.charMap = new char[levelHeight][levelWidth];
			ArrayList<Rope> ropes = new ArrayList<Rope>();
			Conveyor conveyor = null;

			// Goes through level's character grid
			for (int y = 0; y < levelHeight; y++) {
				currentLine = levelReader.nextLine();
				for (int x = 0; x < levelWidth; x++) {

					// Saves character grid to 2D char array
					currentChar = currentLine.charAt(x);
					charMap[y][x] = currentChar;

					// If object is detected
					if (currentChar != '1' || currentChar != '0') {

						// Calculates coordinates
						objectXCord = x * 10;
						objectYCord = (levelHeight - (levelHeight - y)) * 10 - 1;

						// If light source is found
						if (currentChar == 'S') {
							entities.add(new LightSource(objectXCord,
									objectYCord,
									PhysicsEngine.lightSourceHeight,
									PhysicsEngine.lightSourceWidth,
									"LightSource",
									PhysicsEngine.lightSourceDiameter,
									"lightSource.png"));
						}

						// If lamp is found
						else if (currentChar == 'L') {
							entities.add(new Lamp(objectXCord, objectYCord,
									100, 300, "Lamp", "blank.png"));
						}

						// If start of rope is found
						else if (currentChar == 'R') {
							ropes.add(new Rope(objectXCord, objectYCord, 1, 1,
									"Rope", "exit.png"));
						}

						// If end of rope is found
						else if (currentChar == 'Q') {

							// Looks for correct start of rope
							for (int i = 0; i < ropes.size(); i++) {
								if (ropes.get(i).getXCord() == objectXCord
										&& !ropes.get(i).isRopeFinished()) {
									ropes.get(i).setRopeYEnd(objectYCord);
								}
							}
						}

						// If start of conveyor is found
						else if (currentChar == 'C') {
							if (conveyor == null) {
								conveyor = new Conveyor(objectXCord,
										objectYCord, 1, 1, "Conveyor",
										"blank.png", 1);
							} else {
								conveyor.setEndCords(objectXCord, objectYCord);
							}
						}

						// If end of conveyor is found
						else if (currentChar == 'Y') {
							if (conveyor == null) {
								conveyor = new Conveyor(objectXCord,
										objectYCord, 1, 1, "Conveyor",
										"blank.png", -1);
							} else {
								conveyor.setEndCords(objectXCord, objectYCord);
							}
						}

						// If player is found
						else if (currentChar == 'P') {
							player = new Player(PhysicsEngine.playerHeight,
									PhysicsEngine.playerWidth, objectXCord,
									objectYCord, "Player", levelNumber);
						}

						// If bed is found
						else if (currentChar == 'B') {
							bed = new Bed(objectXCord, objectYCord, 1, 1,
									"Bed", "blank.png", levelNumber, this);
							entities.add(bed);
						}

						// If unknown object is detected then it is ignored
						currentChar = '0';
					}
				}
			}

			// Reads in correct level images (layers) and adds the images to an
			// arraylist
			while (levelReader.hasNextLine()) {

				String str = levelReader.nextLine();
				int length = 0;
				int height;
				if (str.charAt(str.length() - 2) == 'n') {
					height = 573;
				} else {
					height = (int) str.charAt(str.length() - 2) * 1000;
				}

				LevelImage img = new LevelImage(str + ".png", levelNumber,
						length, height, str.charAt(0));
				levelLayers.add(img);
			}

			// Adds current level and prepares to read next level
			allLevels.add(new Level(charMap, levelName, entities,
					player, levelLayers, bed, ropes, conveyor));
			levelNumber++;
			fileName = "Level" + Integer.toString(levelNumber) + ".txt";
			levelTextFile = new File(fileName);
			levelLayers = new ArrayList<LevelImage>();
			entities = new ArrayList<Entity>();
		}

		// Reads player save file if found
		levelTextFile = new File("playerFile.txt");
		if (levelTextFile.exists()) {
			try {
				levelReader = new Scanner(levelTextFile);
			} catch (FileNotFoundException e) {
				System.out.println("Text File error. " + fileName);
				e.printStackTrace();

			}
			// Last level player has visited
			saveLevel = 0;
			if (levelReader.hasNextLine()) {
				saveLevel = levelReader.nextInt() - 1;
				if (saveLevel < allLevels.size()) {
					Control.level = saveLevel;
				}
				else {
					Control.level = 0;
				}
			}
			// Highest level player has reached
			highestLevel = 0;
			if (levelReader.hasNextLine()) {
				highestLevel = levelReader.nextInt() - 1;
				if (highestLevel < allLevels.size()) {
					
				}
				else {
					highestLevel = 1;
				}
			}
			
		}
	}

	/**
	 * Gets the last level the player saved on
	 * 
	 * @return the save level
	 */
	public int getSaveLevel() {
		return this.saveLevel;
	}

	/**
	 * Gets the highest level reached
	 * 
	 * @return
	 */
	public int getHighestLevel() {
		return this.highestLevel;
	}

	/**
	 * Getter for array list of entities of current level
	 * 
	 * @return Array list of all the entities on the current level
	 */
	public ArrayList<Entity> getEntities() {
		return this.entities;
	}

	/**
	 * Getter for a desired level
	 * 
	 * @param level
	 *            Level number of the desired level
	 * @return The desired level
	 */
	public Level get(int level) {
		return allLevels.get(level);
	}

	/**
	 * Getter for current level object
	 * 
	 * @return Reference of the Level object for the current level
	 */
	public static Level getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * Getter for current level value
	 * 
	 * @return The number of the current level
	 */
	public static int getCurrentLevelNo() {
		return currentLevel.getLevel();
	}

	/**
	 * Setter for changing current level
	 * 
	 * @param level
	 *            Reference to the Level that shall become the current level
	 */
	public static void setCurrentLevel(Level level) {
		currentLevel = level;
	}

	/**
	 * Getter for all level list
	 * @return Reference to the array list of all the levels
	 */
	public ArrayList<Level> getLevelList() {
		return this.allLevels;
	}
}
