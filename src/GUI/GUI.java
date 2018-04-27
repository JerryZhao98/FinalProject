package GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import GameComponents.Entity;
import GameComponents.Player;
import GameObjects.LightSource;
import Mechanics.Control;
import Mechanics.Level;
import Mechanics.LevelController;
import Mechanics.LevelImage;
import Mechanics.PhysicsEngine;

/**
 * Handles and draws graphics for the actual game play
 * 
 * @author Barbara Guo and Jerry Zhao
 * @version Final Project 2016
 *
 */
public class GUI extends JPanel implements KeyListener, ActionListener {

	private final int CENTER_X;
	public static double hSpeed;
	public static Player player;
	public LevelController levelControl;
	public static Control control;
	public static ArrayList<ArrayList<Image>> imageLayers;
	public static char[][] grid;
	public static ArrayList<Image> gameObjectImages;
	public static ArrayList<Entity> gameObjects;
	static ArrayList<Level> levelList;
	private int keyCode;
	public static boolean isFading;
	private static Image playerImage;
	private int pixelsAboveGround = 100;
	private int playerXScreen;
	private int playerYScreen;
	private int backgroundXCord = 0;
	private int backgroundYCord = 0;
	public float alpha;
	private static ArrayList<Image> playerImages;
	private int previousDirection = 0;

	/**
	 * Constructor for the GUI
	 * 
	 * @param control
	 *            Reference to the control class
	 * @param levelControl
	 *            Reference to the Level Controller class
	 */
	public GUI(Control control, LevelController levelControl) {

		this.setFocusable(true);
		this.setDoubleBuffered(true);
		this.setBackground(Color.BLACK);

		this.control = control;
		this.levelControl = levelControl;
		this.CENTER_X = control.SCREEN_WIDTH / 2;
		this.gameObjects = LevelController.currentLevel.getEntities();
		this.levelList = levelControl.getLevelList();
		this.isFading = false;
		this.alpha = 0f;
		this.playerXScreen = CENTER_X - PhysicsEngine.playerWidth / 2;
		this.playerYScreen = Control.SCREEN_HEIGHT - PhysicsEngine.playerHeight
				- pixelsAboveGround;
		this.playerImages = new ArrayList<Image>();
		this.imageLayers = new ArrayList<ArrayList<Image>>();
		this.grid = LevelController.currentLevel.getCharMap();

		// Gets and sets the player for the level that's being draw
		setPlayer(LevelController.currentLevel.getPlayer());

		// Load images for each level
		loadImages();

		addKeyListener(this);

	}

	/**
	 * Loads the images necessary in the level
	 */
	public void loadImages() {

		// Gets all the images for the entities and stores them in an array list
		// for easier access
		this.gameObjectImages = new ArrayList<Image>();
		for (int image = 0; image < gameObjects.size(); image++) {
			this.gameObjectImages.add(this.gameObjects.get(image).getImage());
		}

		// Try to load images for the player
		try {
			playerImage = ImageIO.read(new File("player_right.png"));
			playerImages.add(ImageIO.read(new File("playerRight.png")));
			playerImages.add(ImageIO.read(new File("playerLeft.png")));
			playerImages.add(ImageIO.read(new File("playerJumpRight.png")));
			playerImages.add(ImageIO.read(new File("playerJumpLeft.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Goes through each level and adds the corresponding images to a array
		// list for easier access
		for (int levelNo = 0; levelNo < levelList.size(); levelNo++) {

			// Each level is a separate array list of images and this is all
			// stored in a larger array list that holds all the level background
			// array lists
			imageLayers.add(new ArrayList<Image>());
			for (int image = 0; image < levelList.get(levelNo).getLevelLayers()
					.size(); image++) {
				try {
					String name = levelList.get(levelNo).getLevelLayers()
							.get(image).getName();
					BufferedImage img = ImageIO.read(new File(name));
					imageLayers.get(levelNo).add(img);
				} catch (IOException e) {
					System.out
							.println("Something went wrong - Image load failure GUI Class");
				}
			}
		}
	}

	/**
	 * Draws the graphics for the game play
	 */
	@SuppressWarnings("static-access")
	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		// This is necessary for drawing the ellipses and clipping
		Graphics2D g2d = (Graphics2D) g.create();

		// Only do the following while the screen is not transitioning (fading)
		if (this.isFading == false) {

			GeneralPath gp = new GeneralPath();

			// Goes through all the game objects and if the game object is a
			// light source get the position of the light source and draw and
			// ellipse (light) around it
			for (int i = 0; i < gameObjects.size(); i++) {
				Entity object = gameObjects.get(i);
				if (object.getType().equals("LightSource")) {
					gp.append(
							getEllipseFromCenter(
									(object.getXCord() + backgroundXCord + object
											.getWidth() / 2),
									(backgroundYCord + imageLayers
											.get(Control.level).get(0)
											.getHeight(null))
											- (grid.length * 10 - object
													.getYCord())
											+ object.getHeight() / 2,
									((LightSource) object).getRadius()), false);
				}
			}

			// Clips the drawing so that only the part of the image that is
			// covered by the ellipse is shown
			g2d.setClip(gp);

			// Goes through each background image in the level and draws them on
			// the panel
			for (int image = 0; image < imageLayers.get(Control.level).size(); image++) {

				// Variables that keep track of x and y coordinates for the
				// background
				// This calibrates the background image with the position of the
				// character
				this.backgroundXCord = (int) Math.round(player.getXCord())
						* (-1) + playerXScreen;
				this.backgroundYCord = (int) Math.round((573 - imageLayers
						.get(Control.level).get(image).getHeight(null))
						+ (grid.length * 10 - player.getYCord()
								- player.getHeight() - pixelsAboveGround));

				LevelImage img = LevelController.currentLevel.getLevelLayers()
						.get(image);

				// If the image is of type 'f' or 'g' this means it is a image
				// that must be drawn on top of all entities including the
				// player.
				if (img.getType() == 'f' || img.getType() == 'g') {

					// Gets an image of the player based on it's direction or
					// action
					if (player.getPlayerDirection() == 1) {
						previousDirection = 1;
						if (player.getAutoJumpPrevention()) {
							playerImage = playerImages.get(0);
						} else
							playerImage = playerImages.get(2);
					} else if (player.getPlayerDirection() == -1) {
						previousDirection = -1;
						if (player.getAutoJumpPrevention()) {
							playerImage = playerImages.get(1);

						} else
							playerImage = playerImages.get(3);
					} else {
						if (previousDirection == 1) {
							playerImage = playerImages.get(0);
						} else
							playerImage = playerImages.get(1);
					}

					// Draws the player at it's x and y coordinates
					g2d.drawImage(playerImage, playerXScreen, playerYScreen,
							null);

					// Draws the rest of the objects at their corresponding
					// positions (based off the character grid)
					for (int objectIndex = 0; objectIndex < gameObjects.size(); objectIndex++) {
						Image entityImage = gameObjectImages.get(objectIndex);
						Entity object = gameObjects.get(objectIndex);
						int objectXCord = (int) Math.round(object.getXCord()
								+ backgroundXCord);
						int objectYCord = (int) Math
								.round((backgroundYCord + imageLayers
										.get(Control.level).get(image)
										.getHeight(null))
										- (grid.length * 10 - object.getYCord()));
						g2d.drawImage(entityImage, objectXCord, objectYCord,
								null);
					}
				}

				// Draws the background image
				g2d.drawImage(imageLayers.get(Control.level).get(image),
						backgroundXCord, backgroundYCord, null);
			}
		} else {

			// If the screen is set for transition draw the transition
			g2d.setComposite(AlphaComposite.getInstance(
					AlphaComposite.SRC_OVER, alpha));
			this.isFading = false;
		}

		g2d.dispose();
	}

	/**
	 * Creates an ellipse from a given center (opposed to one based off the 0,0
	 * coordinate)
	 * 
	 * @param x
	 *            Value of the x coordinate
	 * @param y
	 *            Value of the y coordinate
	 * @param radius
	 *            Value of the radius
	 * @return An ellipse created at a given center
	 */
	private Ellipse2D getEllipseFromCenter(double x, double y, double radius) {

		// Determines a the proper x and y coordinates using the radius as a
		// measurement guide
		double newX = x - radius / 2.0;
		double newY = y - radius / 2.0;
		Ellipse2D ellipse = new Ellipse2D.Double(newX, newY, radius, radius);
		return ellipse;
	}

	/**
	 * Gets the reference to the level controller
	 * @return A reference to the level controller
	 */
	public LevelController getLevelControl() {
		return this.levelControl;
	}

	/**
	 * Sets the player for the level
	 * @param newPlayer The new player for the level
	 */
	public static void setPlayer(Player newPlayer) {
		
		//Ensures that the player is new
		newPlayer.reset();
		
		player = newPlayer;
	}

	/**
	 * Gets the current level of the player
	 * @return The current level of the player
	 */
	public Level getLevel() {
		return LevelController.currentLevel;
	}

	/**
	 * Useless but mandatory
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Handles responses to certain keys being pressed
	 */
	public void keyPressed(KeyEvent e) {

		keyCode = e.getKeyCode();

		// For left and right arrow keys (adjust player movement accordingly)
		if (keyCode == 37 || keyCode == 39) {
			if (player.getPlayerDirection() != (38 - keyCode) * -1) {
				player.setMovementTimeStart(System.currentTimeMillis());
				player.setPlayerDirection((38 - keyCode) * -1);
			}
		}
		// For space bar (jumps)
		else if (keyCode == 32 && player.getAutoJumpPrevention()) {
			player.setAutoJumpPrevention(false);
			player.setJumpStart(true);
		}

		// For up and down arrow keys (climbs)
		else if (keyCode == 38 || keyCode == 40) {
			player.setClimbDirection((39 - keyCode));
		}

		// For X (interacting)
		else if (keyCode == 88) {
			if (player.canPerformAction()) {
				player.setPerformingAction(true);
				player.setCanPerformAction(false);
			}
		}

		// For R (restarts level, for debugging purposes)
		else if (keyCode == 82) {
			control.ENGINE.resetLevel(LevelController.currentLevel.getPlayer());
		}

		// For A (skips to next level, for debugging purposes)
		else if (keyCode == 65) {
			LevelController.changeLevel(Control.level + 1);
		}

		// For ENTER (saving progress and allowing advancement to the next
		// level)
		else if (keyCode == 10) {
			if (!player.isPlayerSleep()) {
				player.setPlayerSleep(true);
			}
		}

		// For ESC (showing in level selection menu)
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			control.showLevelSelect();
		}
	}

	/**
	 * Handles responses for when a key is released
	 */
	public void keyReleased(KeyEvent e) {

		keyCode = e.getKeyCode();

		// For left and right arrow keys (stops player for moving continously)
		if (keyCode == 37 || keyCode == 39) {
			if (38 - keyCode == player.getPlayerDirection() * -1) {
				player.setPlayerDirection(0);
			}
		}

		// For up and down arrow keys
		else if (keyCode == 38 || keyCode == 40) {
			if (39 - keyCode == player.getClimbDirection()) {
				player.setClimbDirection(0);
			}
		}

		// For X (allows player to interact with items again)
		else if (keyCode == 88) {
			player.setCanPerformAction(true);
		}

		// For ENTER
		else if (keyCode == 10) {
			player.setPlayerSleep(false);
		}

	}

	/**
	 * Useless but necessary for implementing interfaces
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

}