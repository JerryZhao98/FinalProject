package Mechanics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JTextField;

import GUI.GUI;
import GameComponents.Entity;
import GameComponents.Player;
import GameObjects.Conveyor;
import GameObjects.Lamp;
import GameObjects.LightSource;
import GameObjects.Rope;

/**
 * Handles the physics, hitboxing, and keeps track of variables regarding all
 * the entities in the game. Performs calculations necessary for the game play
 * to run.
 * 
 * @author Jerry Zhao and Barbara Guo
 * @version Final Project 2016
 *
 */
public class PhysicsEngine {

	// GUI
	public GUI gui;

	// Light source values
	public static double lightSourceDiameter = 750;
	public static int lightSourceHeight = 100;
	public static int lightSourceWidth = 100;

	// Object Speeds
	public static double xSpeed;
	public static double ySpeed;

	// Object hitbox
	public static int objectWidth;
	public static int objectHeight;

	// Player
	public static Player player;
	public static boolean isPlayer;

	// Player hitbox
	public static int playerHeight = 257;
	public static int playerWidth = 200;

	// Player Position Values
	public static double xObjectPos;
	public static double yObjectPos;

	// Horizontal movement variable
	public static double percentOfMaxSpeed = 0;
	public static int xObjectDirection = 0;
	public static double objectSpeedPerSecond = 1000;
	public static int delayPerFrame = 30;
	public static double objectMaxSpeed = objectSpeedPerSecond * delayPerFrame
			/ 1000;
	public static double timeToMaxSpeed = 1000;
	public static long movementTimeStart = 0;
	public static long movementTimeEnd = 0;
	public static long movementTimeTotal = 0;
	public static int xDirectionPrevention = 0;

	// Conveyor values
	public static boolean onConveyor = false;
	public static double conveyorSpeedPerSec = 100;
	public static double conveyorSpeed = conveyorSpeedPerSec * delayPerFrame
			/ 1000;

	// Climbing values
	public static double climbSpeedPerSec = 500;
	public static double climbSpeed = climbSpeedPerSec * delayPerFrame / 1000;
	public static double departureSpeed = 500;

	// Jump mechanic variables
	public static boolean autoJumpPrevention = true;
	public static boolean jumpStart = false;
	public static double defaultJumpHeight = 100;
	public static double jumpHeight = defaultJumpHeight + defaultJumpHeight
			* percentOfMaxSpeed;
	public static double maxFallSpeedPerSec = 1000;
	public static double maxFallSpeed = maxFallSpeedPerSec * delayPerFrame
			/ 1000;
	public static double yDeccelerationPerSec = 100;
	public static double yDecceleration = yDeccelerationPerSec * delayPerFrame
			/ 1000;
	// Sigma notation for initial Jump Speed
	// Upper limit is initialJumpSpeed/3
	// Lower limit is k = 0
	// Argument is (-3)*k + initialJumpSpeed
	// And total is jumpHeight * -1
	public static double initialJumpSpeed = (-yDecceleration + Math
			.sqrt(yDecceleration * (yDecceleration + 8 * jumpHeight))) / (-2);

	// Timer delay
	private static Timer respawnDelay;

	/**
	 * Constructor for engine
	 * 
	 * @param delayPerFrame
	 *            time between per frame
	 * @param gui
	 *            Current game GUI
	 */
	public PhysicsEngine(int delayPerFrame, GUI gui) {
		this.delayPerFrame = delayPerFrame;
		this.objectMaxSpeed = objectSpeedPerSecond * delayPerFrame / 1000;
		this.maxFallSpeed = maxFallSpeedPerSec * delayPerFrame / 1000;
		this.yDecceleration = yDeccelerationPerSec * delayPerFrame / 1000;
		this.gui = gui;
		respawnDelay = new Timer();
	}

	/**
	 * Goes through every entity in the current level and updates them along
	 * with light source radii
	 */
	public void moveObjects() {
		ArrayList<Entity> entities = LevelController.getCurrentLevel()
				.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			objectMovement(entities.get(i));
			if (entities.get(i).getType().equals("LightSource")) {
				((LightSource) entities.get(i)).updateRadius();
			}
		}
	}

	/**
	 * Resets everything in level along with player
	 * 
	 * @param player
	 *            Player entity to reset
	 */
	public void resetLevel(Player player) {
		
		//Include a delay before reseting for easier transition
		respawnDelay.schedule(new TimerTask() {
			@Override
			public void run() {
				player.playerReset();
				for (int i = 0; i < GUI.gameObjects.size(); i++) {
					GUI.gameObjects.get(i).reset();
				}
				
				//Transition effects
				gui.alpha += 0.005f;
				if (gui.alpha > 1) {
					gui.alpha = 1;
				}
				gui.isFading = true;
			}
		}, 500);
	}

	/**
	 * Main method to handle all interactions between objects
	 * 
	 * @param object
	 *            Entity to update
	 */
	public void objectMovement(Entity object) {
		
		// Gets all basic values from object
		objectWidth = object.getWidth();
		objectHeight = object.getHeight();
		xObjectPos = object.getXCord();
		yObjectPos = object.getYCord();
		xSpeed = object.getHSpeed();
		ySpeed = object.getVSpeed();
		jumpStart = false;

		// Horizontal movement speed calculations
		if (object.getType() == "Player") {
			isPlayer = true;
			player = (Player) object;
			jumpStart = player.getJumpStart();

			// Player speed is based on direction and time spent running
			percentOfMaxSpeed = player.getPercentOfMaxSpeed();
			xObjectDirection = player.getPlayerDirection();
			if (xObjectDirection != xDirectionPrevention) {
				if (xDirectionPrevention != 0) {
					xDirectionPrevention = 0;
				}
				if (xObjectDirection != 0) {
					movementTimeStart = player.getMovementTimeStart();
					movementTimeEnd = System.currentTimeMillis();
					movementTimeTotal = movementTimeEnd - movementTimeStart;
					// Speed based on percent of max speed
					if (movementTimeTotal < timeToMaxSpeed) {
						percentOfMaxSpeed = movementTimeTotal / timeToMaxSpeed;
						xSpeed = percentOfMaxSpeed * objectMaxSpeed
								* xObjectDirection;
					}
					// Speed is max speed
					else {
						percentOfMaxSpeed = 1;
						xSpeed = objectMaxSpeed * xObjectDirection;
					}
				}

			}
			// No horizontal movement
			else {
				xSpeed = 0;
				percentOfMaxSpeed = 0;
			}

		} else {
			isPlayer = false;
		}

		// Jump movement and fall speed calculations
		if (jumpStart) {
			// Prevents players from double jumping
			player.setJumpStart(false);
			// Max possible jump height of current jump is default height +
			// component of movement speed
			jumpHeight = defaultJumpHeight + defaultJumpHeight
					* percentOfMaxSpeed;
			// Initial jump velocity is calculated using quadratics/sigma
			// notation
			initialJumpSpeed = (-yDecceleration + Math.sqrt(yDecceleration
					* (yDecceleration + 8 * jumpHeight)))
					/ (-2);
			ySpeed = initialJumpSpeed;

		} else {
			// Decelerate until max fall speed
			if (ySpeed + yDecceleration < maxFallSpeed) {
				ySpeed += yDecceleration;
			}
			// Max fall speed reached
			else {
				ySpeed = maxFallSpeed;
			}
		}

		// Ensures only player is affected by light
		boolean inLight = true;
		if (isPlayer) {
			// Check is player is attempting to sleep/save/progress levels
			if (player.isPlayerSleep()) {
				// Check if player hitbox is in contact with bed hitbox
				if (xObjectPos <= LevelController.currentLevel.getBed()
						.getXCord()
						&& xObjectPos + (objectWidth - 1) >= LevelController.currentLevel
								.getBed().getXCord()) {
					if (yObjectPos <= LevelController.currentLevel.getBed()
							.getYCord()
							&& yObjectPos + (objectHeight - 1) >= LevelController.currentLevel
									.getBed().getYCord()) {
						// Saves user progress and progresses to next level
						LevelController.currentLevel.getBed().save();
						LevelController.changeLevel(Control.level + 1);
						return;
					}
				}
			}
			// Assumes player is in dark unless inLight is registered
			inLight = false;
			ArrayList<Entity> entities = GUI.gameObjects;
			double lightXCord;
			double lightYCord;
			// Goes through each light source in the level
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i).getType().equals("LightSource")) {
					LightSource currentLight = (LightSource) entities.get(i);
					lightXCord = currentLight.getXCord();
					lightYCord = currentLight.getYCord();
					double lightXCenter = lightXCord
							+ (currentLight.getWidth() + 1) / 2;
					double lightYCenter = lightYCord
							+ (currentLight.getHeight() + 1) / 2;
					double radius = currentLight.getRadius();
					double playerXCenter = xObjectPos + (objectWidth + 1) / 2;
					double playerYCenter = yObjectPos + (objectHeight + 1) / 2;
					// Checks if player's center is within radius of light
					if (Math.pow(Math.abs(playerXCenter - lightXCenter)
							- (objectWidth + 1) / 2, 2)
							+ Math.pow(Math.abs(playerYCenter - lightYCenter)
									- (objectHeight + 1) / 2, 2) <= radius
							* radius / 4) {
						inLight = true;
						// Checks if player wants to perform interaction in
						// light
						if (player.isPerformingAction()) {
							if (!player.isHolding()) {
								// Checks if player's hitbox is contacting the
								// light source's hitbox
								if (lightXCord >= xObjectPos
										&& lightXCord <= (xObjectPos
												+ objectWidth - lightSourceWidth)) {
									if (lightYCord >= yObjectPos
											&& lightYCord <= (yObjectPos
													+ objectHeight - lightSourceWidth)) {
										// If light source is available then
										// player picks it up
										if (!currentLight.isOnConveyor()
												&& !currentLight.isLamp()) {
											currentLight.setIsHeld(true);
											player.setIsHolding(true);
											player.setPerformingAction(false);
											player.setLightSource(currentLight);
											break;
										}
									}
								}
							}
						}
					}
				}
				// Checks if player wants to interact with lamp
				else if (player.isPerformingAction()) {
					if (entities.get(i).getType().equals("Lamp")) {
						Lamp currentLamp = (Lamp) entities.get(i);
						// Check is player hitbox is contacting lamp's hitbox
						if (currentLamp.getXCord() <= xObjectPos + objectWidth
								- 1
								&& currentLamp.getXCord()
										+ currentLamp.getWidth() - 1 >= xObjectPos) {
							if (currentLamp.getYCord() <= yObjectPos
									+ objectHeight - 1
									&& currentLamp.getYCord()
											+ currentLamp.getHeight() - 1 >= yObjectPos) {
								// Lamp is lit if player has an available light
								// source
								if (player.isHolding()
										&& !currentLamp.isLit()
										&& player.getLightSource().getRadius() > 0) {
									player.getLightSource()
											.setLamp(currentLamp);
									player.setIsHolding(false);
									currentLamp.light();
									player.setPerformingAction(false);
									i--;
								}
							}
						}

					}
				}
			}
			// If player has nothing to interact with
			if (player.isPerformingAction()) {
				if (!player.isHolding()) {
					player.setPerformingAction(false);
				}
			}
			// Checks if player wants to drop current light source
			if (inLight) {
				if (player.isPerformingAction()) {
					if (player.isHolding()) {
						player.setIsHolding(false);
						player.setPerformingAction(false);
						player.getLightSource().setIsHeld(false);
						;
					}
				}
				// Calculates player's climbing movement
				if (player.isInClimb()) {
					// Resets movement timer to maintain constant departure land
					// speed
					player.setMovementTimeStart(System.currentTimeMillis());
					// Checks if player wants to jump off rope
					if (jumpStart) {
						player.setInClimb(false);
						xSpeed = departureSpeed;
					}
					// Calculates player's movement on rope
					else {
						xSpeed = 0;
						xObjectDirection = 0;
						// Checks if player wants to move
						if (player.getClimbDirection() != 0) {
							ySpeed = climbSpeed * player.getClimbDirection()
									* (-1);
							// Calculates player's climbing speed
							if (yObjectPos + (objectHeight - 1) + ySpeed < player
									.getCurrentRope().getYCord()) {
								ySpeed = player.getCurrentRope().getYCord()
										- yObjectPos - (objectHeight - 1);
								player.setInClimb(false);
							} else if (yObjectPos + ySpeed > player
									.getCurrentRope().getYCord()
									+ player.getCurrentRope().getHeight() - 1) {
								ySpeed = player.getCurrentRope().getYCord()
										+ player.getCurrentRope().getHeight()
										- 1 - yObjectPos;
								player.setInClimb(false);
							}

						}
						// Player doesn't move
						else {
							ySpeed = 0;
						}
					}
				} else {
					// Checks if player wants to attempt to climb a nearby rope
					if (player.getClimbDirection() != 0) {
						ArrayList<Rope> ropes = LevelController.currentLevel
								.getRopes();
						for (int i = 0; i < ropes.size(); i++) {
							if (ropes.get(i).getXCord() >= xObjectPos
									&& ropes.get(i).getXCord() <= xObjectPos
											+ objectWidth - 1) {
								if (ropes.get(i).getYCord() <= yObjectPos
										+ objectHeight - 1
										&& ropes.get(i).getYCord()
												+ ropes.get(i).getHeight() - 1 >= yObjectPos) {
									player.setCurrentRope(ropes.get(i));
									player.setInClimb(true);
									xSpeed = 0;
									xObjectPos = ropes.get(i).getXCord()
											- (objectWidth - 1) / 2;
									xObjectDirection = 0;
									player.setAutoJumpPrevention(true);
								}
							}
						}
					}
				}
			} else {
				// Sets player climb and jump to false if out of light
				player.setInClimb(false);
				player.setAutoJumpPrevention(false);
			}

		}
		// For non player objects
		else {
			// Returns if object is light source being held
			if (object.getType().equals("LightSource")
					&& (((LightSource) object).isHeld() || ((LightSource) object)
							.isLamp())) {
				return;
			}
			// Calculates objects horizontal direction
			if (xSpeed > 0) {
				xObjectDirection = 1;
			} else if (xSpeed < 0) {
				xObjectDirection = -1;
			} else {
				xObjectDirection = 0;
			}

		}
		// Light source movement for conveyors
		if (object.getType().equals("LightSource")) {
			if (LevelController.currentLevel.getConveyor() != null) {
				onConveyor = ((LightSource) object).isOnConveyor();
				Conveyor conveyor = LevelController.currentLevel.getConveyor();
				// Calculates if lightsource is contacting conveyor
				if (conveyor.getXCord() <= xObjectPos + objectWidth - 1
						&& conveyor.getXCord() + conveyor.getWidth() - 1 >= xObjectPos) {
					if (conveyor.getYCord() <= yObjectPos + objectHeight - 1
							&& conveyor.getYCord() + conveyor.getHeight() - 1 >= yObjectPos) {
						// Calculates speed of conveyor and direction
						if (conveyor.isHorizontal()) {
							xSpeed = conveyorSpeed
									* conveyor.getConveyorDirection();
							ySpeed = 0;
						} else {
							ySpeed = conveyorSpeed
									* conveyor.getConveyorDirection();
							xSpeed = 0;
						}
						onConveyor = true;
						((LightSource) object).setOnConveyor(true);
					} else {
						((LightSource) object).setOnConveyor(false);
					}
				} else {
					((LightSource) object).setOnConveyor(false);
				}
			}
		}

		// Character map loaded from text file of level
		char[][] currentLevel = GUI.grid;

		// Calculates horizontal direction
		if (xSpeed > 0) {
			xObjectDirection = 1;
		} else if (xSpeed < 0) {
			xObjectDirection = -1;
		}
		if (xObjectDirection != 0) {
			// Calculates areas of possible collision on horizontal plane of
			// object
			int startIndexInt = (int) ((xObjectPos + ((objectWidth - 1)
					* (xObjectDirection + 1) / 2)) / 10)
					+ xObjectDirection;
			int rightReach = ((int) ((xSpeed * xObjectDirection + xObjectPos
					+ objectWidth - 1) / 10) + 1);
			int leftReach = ((int) ((xSpeed + xObjectPos) / 10) - 1)
					* xObjectDirection * (-1);
			// Calculates a certain area around object to detect if collision
			// happens horizontally
			outerLoop: for (int x = startIndexInt; x <= rightReach
					&& x >= leftReach; x += xObjectDirection) {
				for (int y = (int) (yObjectPos / 10); y <= (int) ((yObjectPos
						+ objectHeight - 1) / 10); y++) { // Check
					// Makes sure solid tiles are within light's radius
					if (currentLevel[y][x] == '1' && inLight) {
						xObjectPos = x * 10 - objectWidth
								* (xObjectDirection + 1) / 2 + (10)
								* (xObjectDirection - 1) / (-2);
						xSpeed = 0;
						if (isPlayer) {
							xDirectionPrevention = xObjectDirection;
						}
						break outerLoop;

					}
					// Checks if player has hit death tile
					else if (currentLevel[y][x] == '2') {
						if (isPlayer) {
							resetLevel(player);
						}
						return;
					}
				}
			}
		}

		// Calculates vertical direction of object
		int yObjectDirection = 0;
		if (ySpeed > 0) {
			yObjectDirection = 1;
		} else if (ySpeed < 0) {
			yObjectDirection = -1;
		}
		if (yObjectDirection != 0) {
			// Calculates areas of possible collision on vertical plane of
			// object
			int startIndexInt = (int) ((yObjectPos + ((objectHeight - 1)
					* (yObjectDirection + 1) / 2)) / 10)
					+ yObjectDirection;
			int bottomReach = ((int) ((ySpeed * yObjectDirection + yObjectPos
					+ objectHeight - 1) / 10) + 1);
			int topReach = ((int) ((ySpeed + yObjectPos) / 10) - 1)
					* yObjectDirection * (-1);
			// Calculates a certain area around object to detect if collision
			// happens vertically
			outerLoop: for (int y = startIndexInt; y <= bottomReach
					&& y >= topReach; y += yObjectDirection) {
				for (int x = (int) (xObjectPos / 10); x <= (int) ((xObjectPos
						+ objectWidth - 1) / 10); x++) {
					// Makes sure solid tiles are within light's radius
					if (currentLevel[y][x] == '1' && inLight) {
						yObjectPos = y * 10 - objectHeight
								* (yObjectDirection + 1) / 2 + (10)
								* (yObjectDirection - 1) / (-2);
						ySpeed = 0;
						if (isPlayer) {
							player.setAutoJumpPrevention(true);
						} else if (!onConveyor) {
							xSpeed = 0;
						}
						break outerLoop;
					}
					// Checks if player has hit death tile
					else if (currentLevel[y][x] == '2') {
						if (isPlayer) {
							resetLevel(player);
						}
						return;
					}

				}
			}
		}

		// Replaces object values with new calculated values
		xObjectPos += xSpeed;
		yObjectPos += ySpeed;
		object.setXCord(xObjectPos);
		object.setYCord(yObjectPos);
		object.setHSpeed(xSpeed);
		object.setVSpeed(ySpeed);

		// Sets player's light source to the same as the player's if available
		if (isPlayer && player.isHolding()) {
			player.getLightSource().setXCord(
					(xObjectPos + objectWidth / 2) - (lightSourceWidth / 2));
			player.getLightSource().setYCord(
					(yObjectPos + objectHeight / 2) - (lightSourceHeight / 2));
			player.getLightSource().setHSpeed(xSpeed);
			player.getLightSource().setVSpeed(ySpeed);
		}
		// Resets conveyor checker
		onConveyor = false;
	}
}