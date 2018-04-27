package Mechanics;

import java.awt.Image;
import java.util.ArrayList;

import GameComponents.Entity;
import GameComponents.Player;
import GameObjects.Bed;
import GameObjects.Conveyor;
import GameObjects.Rope;

/**
 * An object that stores the necessary information for each level
 * 
 * @author Barbara Guo and Jerry Zhao
 * @version Final Project 2016
 */
public class Level {

	private ArrayList<LevelImage> levelLayers;
	private ArrayList<Entity> entities;
	private char[][] charMap;
	private String levelName;
	private Player player;
	private int level;
	private Bed bed;
	private ArrayList<Rope> ropes;
	private Conveyor conveyor;

	/**
	 * Constructor for the level, stores information from the Level Controller
	 * for later fetching
	 * 
	 * @param charMap
	 *            Reference to the character map
	 * @param levelName
	 *            Name of the level (eg. Level1)
	 * @param entities
	 *            Reference to an array of all the entities
	 * @param player
	 *            Reference to the player object
	 * @param levelLayers
	 *            Reference to all image layers used to build the background of
	 *            the level
	 * @param bed
	 *            Reference to the bed object in the level
	 * @param ropes
	 *            Reference to the rope object in the level
	 * @param conveyor
	 *            Reference to the conveyer object in the level
	 */
	public Level(char[][] charMap, String levelName,
			ArrayList<Entity> entities, Player player,
			ArrayList<LevelImage> levelLayers, Bed bed, ArrayList<Rope> ropes,
			Conveyor conveyor) {
		this.player = player;
		this.entities = entities;
		this.levelName = levelName;
		this.charMap = charMap;
		this.levelLayers = levelLayers;
		this.ropes = ropes;
		this.level = (int) levelName.charAt(levelName.length() - 1);
		this.bed = bed;
		this.conveyor = conveyor;
		add();

	}

	public Bed getBed() {
		return bed;
	}

	public void setBed(Bed bed) {
		this.bed = bed;
	}

	public Conveyor getConveyor() {
		return conveyor;
	}

	public void setConveyor(Conveyor conveyor) {
		this.conveyor = conveyor;
	}

	public ArrayList<Rope> getRopes() {
		return ropes;
	}

	public void setRopes(ArrayList<Rope> ropes) {
		this.ropes = ropes;
	}

	public void add() {

	}

	public char[][] getCharMap() {
		return this.charMap;
	}

	public String getLevelName() {
		return this.levelName;
	}

	public int getLevel() {
		return this.level;
	}

	public ArrayList<LevelImage> getLevelLayers() {
		return this.levelLayers;
	}

	public ArrayList<Entity> getEntities() {
		return entities;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
