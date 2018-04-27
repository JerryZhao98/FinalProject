package GameComponents;

import GameObjects.LightSource;
import GameObjects.Rope;
import Mechanics.Level;

/**
 * Player class. Player in the game. Keeps track of the players direction, ability to perform actions, speed, etc.
 * @author Jerry Zhao and Barbara Guo
 * @version Final Project 2016
 *
 */
public class Player extends Entity {

	// References to the width and heigh of the frame
	private final static int FRAME_WIDTH = 1000;
	private final static int FRAME_HEIGHT = 573;

	// Sets the initial position of the player
	private final static int PLAYER_X = 0;
	private final static int PLAYER_Y = 60;

	/**
	 * True when the player can jump and false when the player is jumping
	 * (prevents double jumps)
	 */
	private static boolean autoJumpPrevention;
	private static double percentOfMaxSpeed = 0;
	private static int playerDirection = 0;
	private static long movementTimeStart = 0;
	private static boolean jumpStart;
	private static boolean canPerformAction;
	private static boolean performingAction;
	private LightSource lightSource;
	private int levelNumber;
	private boolean playerSleep;
	private int climbDirection = 0;
	private boolean inClimb = false;
	private Rope currentRope;

	public boolean isInClimb() {
		return inClimb;
	}

	public void setCurrentRope(Rope rope) {
		this.currentRope = rope;
	}

	public Rope getCurrentRope() {
		return currentRope;
	}

	public void setInClimb(boolean inClimb) {
		if (!inClimb) {
			currentRope = null;
		}
		this.inClimb = inClimb;
	}

	public int getClimbDirection() {
		return climbDirection;
	}

	public void setClimbDirection(int climbDirection) {
		this.climbDirection = climbDirection;
	}

	public int levelNumber() {
		return levelNumber;
	}

	/**
	 * Monitors whether or not the x position has updated
	 */
	private boolean updateX;

	/**
	 * Monitors whether or not the y position has updated
	 */
	private boolean updateY;

	/**
	 * Which direction the player is facing, either left ('L') or right ('R')
	 */
	private char direction;

	/**
	 * Direction the player is moving in (-1 for left, 1 for right) If the
	 * player continues to move in the same direction after collision, they can
	 */
	private int movingDirection;

	/**
	 * Boolean for whether or not the player is dead, (for determining if the
	 * level should restart)
	 */
	private boolean isDead;

	/**
	 * Boolean for whether or not the player is holding a light source
	 */
	private boolean isHolding;

	/**
	 * The speed at which the player runs at
	 */
	private int runSpeed;

	/**
	 * The speed at which the player jumps
	 */
	private int jumpSpeed;
	private int width;
	private int height;
	private Level level;

	public Player(int height, int width, double xCord, double yCord,
			String objectType, int levelNumber) {
		super(xCord, yCord, width, height, objectType, "player.png");
		this.levelNumber = levelNumber;
		this.width = width;
		this.height = height;
		this.isHolding = false;
		this.isDead = false;
		this.movingDirection = 0;
		this.canPerformAction = true;
		this.performingAction = false;
		this.runSpeed = 10;
		this.jumpSpeed = 15;
		this.playerSleep = false;
	}

	public boolean isPlayerSleep() {
		return playerSleep;
	}

	public void setPlayerSleep(boolean playerSleep) {
		this.playerSleep = playerSleep;
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

	public double getPercentOfMaxSpeed() {
		return percentOfMaxSpeed;
	}

	public void setPercentOfMaxSpeed(double percentOfMaxSpeed) {
		this.percentOfMaxSpeed = percentOfMaxSpeed;
	}

	public int getPlayerDirection() {
		return playerDirection;
	}

	public void setPlayerDirection(int playerDirection) {
		this.playerDirection = playerDirection;
	}

	public void setMovementTimeStart(long movementTimeStart) {
		this.movementTimeStart = movementTimeStart;
	}

	public long getMovementTimeStart() {
		return movementTimeStart;
	}

	public boolean getJumpStart() {
		return jumpStart;
	}

	public void setJumpStart(boolean jumpStart) {
		this.jumpStart = jumpStart;
	}

	public boolean getAutoJumpPrevention() {
		return autoJumpPrevention;
	}

	public void setAutoJumpPrevention(boolean autoJumpPrevention) {
		this.autoJumpPrevention = autoJumpPrevention;
	}

	public void pickUpLight() {
		this.performingAction = true;
	}

	public void dropLight() {
		this.performingAction = true;
	}

	public boolean isHolding() {
		return isHolding;
	}

	public void setIsHolding(boolean isHolding) {
		this.isHolding = isHolding;
	}

	public static boolean canPerformAction() {
		return canPerformAction;
	}

	public void setCanPerformAction(boolean canPerformAction) {
		this.canPerformAction = canPerformAction;
	}

	public static boolean isPerformingAction() {
		return performingAction;
	}

	public void setPerformingAction(boolean performingAction) {
		this.performingAction = performingAction;
	}

	public LightSource getLightSource() {
		return lightSource;
	}

	public void setLightSource(LightSource lightSource) {
		this.lightSource = lightSource;
	}

	public void setDirection(String direction) {
		if (!isDead) {
			setDirection(direction);
		}
	}

	/**
	 * Resets the player
	 */
	public void playerReset() {
		xCord = origXCord;
		yCord = origYCord;
		this.width = width;
		this.height = height;
		this.isHolding = false;
		this.isDead = false;
		this.movingDirection = 0;
		this.canPerformAction = true;
		this.performingAction = false;
	}

}
