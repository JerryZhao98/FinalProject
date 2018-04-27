package Mechanics;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import GUI.GUI;
import GUI.Menu;

/**
 * Control center for the game. Handles the main thread and creates necessary
 * mechanics for the game to run. Has a card layout that allows the switching
 * between different panels such as the main menu, help, level selection, and
 * game.
 * 
 * @author Barbara Guo and Jerry Zhao
 * @version Final Project 2016
 */
public class Control extends JPanel implements Runnable {

	// Variables that store the width and height of the screen (used in
	// calculations)
	public final static int SCREEN_WIDTH = 1000;
	public final static int SCREEN_HEIGHT = 573;

	public final PhysicsEngine ENGINE;
	private final String MAIN = "MAIN", LEVEL_SELECT = "LEVEL_SELECT",
			GAME = "GAME", HELP = "HELP";

	/**
	 * Certain actions can only be performed in or following up on in game play
	 */
	private boolean inGame;
	private CardLayout control;
	public LevelController levelControl;
	private Menu main, levelSelection, help;
	private GUI gui;
	private final int frameDelay = 30;
	public static int level;

	/**
	 * To keep track of what screen the player is on at the moment
	 */
	private String currentScreen;

	/**
	 * Constructor for setting up the control class
	 * 
	 * @throws IOException
	 */
	public Control(){

		setDoubleBuffered(true);
		this.control = new CardLayout();
		this.setLayout(control);

		// Creates the level controller and tells it to add load up all the
		// levels
		this.levelControl = new LevelController();
		levelControl.addLevels();

		// Sets the current level
		levelControl.setCurrentLevel(levelControl.allLevels.get(level));

		this.main = new Menu(MAIN, this);
		this.levelSelection = new Menu(LEVEL_SELECT, this);
		this.help = new Menu(HELP, this);
		this.gui = new GUI(this, levelControl);
		this.ENGINE = new PhysicsEngine(frameDelay, gui);
		this.add(main, MAIN);
		this.add(levelSelection, LEVEL_SELECT);
		this.add(gui, GAME);
		this.add(help, HELP);

		// Sets the current screen to main because that's the first screen the
		// user will see
		this.currentScreen = "MAIN";

	}

	/**
	 * Switches the displayed panel to the main menu panel
	 */
	public void showMain() {

		this.control.show(this, MAIN);
		this.inGame = false;

		// Prevents the unnecessary redrawing of the main panel
		if (!(currentScreen.equals("HELP")
				|| currentScreen.equals("LEVEL_SELECT") || currentScreen
					.equals("GAME"))) {
			main.display();
		}

	}

	/**
	 * Switches the displayed panel to the game panel. (All games start on the
	 * level the player last finished on)
	 */
	public void showGame() {
		this.control.show(this, GAME);
		this.currentScreen = GAME;

		// Allows key listeners to be used
		gui.requestFocus();
		// ENGINE.resetLevel(GUI.player);
	}

	/**
	 * Switches the displayed panel to the level selection menu
	 */
	public void showLevelSelect() {
		this.control.show(this, LEVEL_SELECT);
		levelSelection.display();
		this.currentScreen = LEVEL_SELECT;

		// The level selection menu can only be access during game play
		this.inGame = true;
	}

	/**
	 * Switches the displayed panel to the help menu
	 */
	public void showHelp() {

		this.control.show(this, HELP);
		help.display();
		this.currentScreen = HELP;
	}

	/**
	 * Actions to run with the main thread
	 */
	@Override
	public void run() {

		while (true) {

			// Allows for the main menu opening animation
			if (currentScreen.equals(MAIN) && main.isDoneAnimation()) {
				main.updateMenu();
			} else if (currentScreen.equals(GAME)) {
				gui.repaint();
				ENGINE.objectMovement(GUI.player);
				ENGINE.moveObjects();
			}

			// Tries to wait a time before refresh
			try {
				Thread.sleep(frameDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Gets whether or not a process is taking place in game
	 * 
	 * @return True or false statement on whether of not a process is happening
	 *         in game
	 */
	public boolean getInGame() {
		return this.inGame;
	}

	/**
	 * Sets the in game
	 * 
	 * @param b
	 *            Whether or not the in game should be true or false
	 */
	public void setInGame(boolean b) {
		this.inGame = b;
	}

}