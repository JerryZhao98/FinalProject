package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.plaf.basic.BasicArrowButton;

import Mechanics.Control;
import Mechanics.LevelController;
import Mechanics.ScrollButton;

@SuppressWarnings("serial")
/**
 * Generic menu object. Using a variable (menu), this menu object becomes specialized and will carry out the functions of the specified menu
 * @author Barbara Guo and Jerry Zhao
 * @version Final Project 2016
 *
 */
public class Menu extends JPanel 
{
	/**
	 * Number of levels in the game
	 */
	private final int NO_LEVELS = 3;

	/**
	 * Name of button names for each of the menus
	 */
	private String[][] btnNames = {
			{ "play", "help", "exit" },
			{ "1", "2", "3" },
			{ "return", "home", "gamecontrols", "return_light", "home_light",
					"gamecontrols_light" } };

	/**
	 * Image for the help panel
	 */
	private BufferedImage help;

	/**
	 * String that distinguishes which menu is being shown "MAIN" is the main
	 * menu "LEVEL_SELECT" is the level selection menu "HELP" is the help menu
	 */
	private String menu;

	/**
	 * The current menu background for the animation
	 */
	private int backgroundNo;

	/**
	 * Keeps track of whether or not the animation is done (whether or not the
	 * animation should be played again)
	 */
	private boolean doneAnimation;

	/**
	 * Array of buttons to be used for the main menu
	 */
	private JButton[] mainMenuBtns;

	/**
	 * Array of the images used for the main menu buttons
	 */
	private Image[] mainMenuImages;

	/**
	 * Array of images used for the main menu background
	 */
	private Image[] mainScreenBG;

	/**
	 * Images used for the buttons in the level selection menu
	 */
	private Image[] levelSelectionBarOptions;

	/**
	 * Images used in the level selector as icon for each level
	 */
	private Image[] lvlImages;

	private Control control;
	private LevelController lvlControl;
	
	/**
	 * Creates and initiates the basic components for each menu
	 * 
	 * @param menu
	 *            The name of the menu being shown
	 * @param control
	 *            Reference to the control class
	 */
	public Menu(String menu, Control control) {

		this.backgroundNo = 0;
		this.menu = menu;
		this.control = control;
		this.mainScreenBG = new Image[43];
		this.mainMenuBtns = new JButton[btnNames[0].length];
		this.mainMenuImages = new Image[btnNames[0].length];
		this.levelSelectionBarOptions = new Image[btnNames[2].length];
		this.lvlImages = new Image[NO_LEVELS];
		this.lvlControl = control.levelControl;
		this.doneAnimation = false;

		// Tries to load the images for the menu that's being shown
		try {

			if (menu.equals("MAIN")) {
				for (int i = 0; i < 42; i++) {
					mainScreenBG[i] = ImageIO
							.read(new File("menu" + i + ".png"));
				}

				for (int i = 0; i < 3; i++) {
					mainMenuImages[i] = ImageIO.read(new File(btnNames[0][i]
							+ ".png"));
				}
			} else if (menu.equals("HELP")) {
				help = ImageIO.read(new File("HelpMenu.png"));
			} else if (menu.equals("LEVEL_SELECT")) {

				for (int i = 0; i < btnNames[2].length; i++) {
					levelSelectionBarOptions[i] = new ImageIcon(btnNames[2][i]
							+ ".png").getImage().getScaledInstance(50, 50,
							Image.SCALE_SMOOTH);
				}

				for (int i = 0; i < lvlImages.length; i++) {
					lvlImages[i] = ImageIO.read(new File(i + ".png"));
				}
			}

		} catch (IOException ex) {
			System.out.println("Image not found");
		}
	}

	/**
	 * Determines which method to call depending on the menu
	 */
	public void display() {
		if (this.menu.equals("MAIN")) {
			mainMenu();
		} else if (this.menu.equals("LEVEL_SELECT")) {
			levelSelect();
		} else if (this.menu.equals("HELP")) {
			help();
		}
	}

	/**
	 * Creates and displays components of the main menu
	 */
	private void mainMenu() {

		// Sets the specifications for the grid bag layout and sets it as the
		// layout for the main menu
		GridBagLayout mainGBL = new GridBagLayout();
		mainGBL.rowHeights = new int[] { 0, 0, 0, 0, 0, 200, 233 };
		mainGBL.columnWidths = new int[] { 0, 402, 526, 0 };
		mainGBL.columnWeights = new double[] { 1.0, 0.0, 1.0, Double.MIN_VALUE };
		this.setLayout(mainGBL);

		// option panel (the panel that
		// hold the main menu buttons: play, help, exit)
		JPanel optionPanel = new JPanel();
		optionPanel.setLayout(new GridLayout(4, 1, 0, 0));
		optionPanel.setOpaque(false);

		// Sets the grid bag constraints for the option panel
		GridBagConstraints optionPanelGBC = new GridBagConstraints();
		optionPanelGBC.insets = new Insets(0, 0, 5, 5);
		optionPanelGBC.fill = GridBagConstraints.BOTH;
		optionPanelGBC.gridx = 1;
		optionPanelGBC.gridy = 6;

		this.add(optionPanel, optionPanelGBC);

		// Creates the buttons for the main menu option panel (only do this is
		// the
		// animation has not been completed - prevents multiple buttons from
		// being drawn when switching back and forth between the menus)
		if (doneAnimation == false) {
			for (int btn = 0; btn < mainMenuBtns.length; btn++) {
				JButton option = new JButton();
				option.setIcon(new ImageIcon(mainMenuImages[btn]));
				option.setBorderPainted(false);
				option.setContentAreaFilled(false);
				option.setOpaque(false);
				option.setFocusPainted(false);
				option.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent event) {
						if (event.getSource() == mainMenuBtns[0]) {
							control.showGame();
							System.out.print("Game");
						} else if (event.getSource() == mainMenuBtns[1]) {
							control.showHelp();
						} else {
							System.exit(0);
						}
					}

				});

				mainMenuBtns[btn] = option;
				optionPanel.add(option);
			}
		}

		// Designates the background image to be drawn by the paint component
		// while the animation is not complete
		if (doneAnimation == false) {
			for (int no = 0; no < 41; no++) {
				repaint();
				backgroundNo++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			repaint();
		}

		this.setDoneAnimation(true);

	}

	/**
	 * Creates and displays components of the level selection menu
	 */
	private void levelSelect() {

		this.setLayout(new BorderLayout(0, 0));

		// Panel that holds buttons that give access to each level
		JPanel levelHeaders = new JPanel();
		levelHeaders.setLayout(new GridLayout(1, 0, 0, 100));
		levelHeaders.setBackground(Color.BLACK);

		// Goes through each level header image and assigns that image to a
		// button
		for (int level = 0; level < lvlImages.length; level++) {
			JButton lvl = new JButton();
			lvl.setIcon(new ImageIcon(lvlImages[level]));
			lvl.setBorderPainted(false);
			lvl.setContentAreaFilled(false);
			lvl.setOpaque(false);
			lvl.setFocusPainted(false);
			int i = level;
			lvl.addActionListener(new ActionListener() {

				// If the level button is selected change the level
				@Override
				public void actionPerformed(ActionEvent event) {

					if (lvlControl.getHighestLevel() >= i) {
						lvlControl.changeLevel(i);
					}
				}

			});

			levelHeaders.add(lvl);
		}

		// ScrollPane is used to allow users to scroll through all the levels
		JScrollPane lvlScroller = new JScrollPane(levelHeaders);
		lvlScroller
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		lvlScroller.getHorizontalScrollBar().setUnitIncrement(100);
		this.add(lvlScroller);

		// Use buttons instead of a scroll bar to perform the scrolling function
		JScrollBar horizontal = lvlScroller.getHorizontalScrollBar();
		BasicArrowButton right = new BasicArrowButton(BasicArrowButton.EAST);
		BasicArrowButton left = new BasicArrowButton(BasicArrowButton.WEST);
		right.setAction(new ScrollButton("", horizontal,
				"positiveUnitIncrement"));
		left.setAction(new ScrollButton("", horizontal, "negativeUnitIncrement"));

		this.add(right, BorderLayout.EAST);
		this.add(left, BorderLayout.WEST);

		// Creates a separate panel to hold the option buttons (return to game,
		// return to main menu, display help menu)
		JPanel lvlSelectionOption = new JPanel();
		lvlScroller.setColumnHeaderView(lvlSelectionOption);
		lvlSelectionOption.setBackground(Color.BLACK);

		// Creates and loads the buttons for the level selection option panel
		for (int i = 0; i < levelSelectionBarOptions.length / 2; i++) {
			JButton btn = new JButton();
			btn.setIcon(new ImageIcon(levelSelectionBarOptions[i]));
			btn.setBorderPainted(false);
			btn.setContentAreaFilled(false);
			btn.setOpaque(false);
			btn.setFocusPainted(false);
			btn.setRolloverEnabled(true);
			int bt = i;
			btn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {

					if (btnNames[2][bt].equals("return")) {
						control.showGame();
					} else if (btnNames[2][bt].equals("home")) {
						control.showMain();
					} else if (btnNames[2][bt].equals("gamecontrols")) {
						control.showHelp();
					}
				}

			});

			btn.setRolloverIcon(new ImageIcon(levelSelectionBarOptions[i + 3]));
			lvlSelectionOption.add(btn);
		}
	}

	/**
	 * Creates the help menu
	 */
	public void help() {

		// Allows the user to toggle back and forth between the help menu by
		// just clicking the screen
		this.addMouseListener(new MouseAdapter() {

			/**
			 * Depending on where the panel is accessed, determines which screen
			 * to display after the user clicks out
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				if (!control.getInGame()) {
					control.showMain();
					setDoneAnimation(true);
				} else {
					control.showLevelSelect();
				}
			}

			/**
			 * Nothing, necessary for the MouseListener
			 */
			@Override
			public void mouseReleased(MouseEvent e) {
			}

		});

		this.setBackground(Color.BLACK);
	}

	/**
	 * This occurs every 15 milliseconds
	 */
	public void updateMenu() {
		repaint();

	}

	/**
	 * Returns number of the background of the animation (for the main menu) is
	 * being displayed.
	 * 
	 * @return
	 */
	public int getBackgroundNo() {
		return this.backgroundNo;

	}

	/**
	 * Sets the background for the animation
	 * 
	 * @param number
	 */
	public void setBackgroundNo(int number) {
		this.backgroundNo = number;
	}

	/**
	 * Draws for the menus (Main and help only)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// If the menu is the main menu draw the background accordingly
		if (this.menu.equals("MAIN")) {

			// Only draw different images while the animation is on going
			if (doneAnimation == false) {
				g.drawImage(mainScreenBG[backgroundNo], 0, 0, null);
			}

			// Otherwise draw the last background (This is so the animation
			// doesn't
			// restart after the main menu returned to from the help menu or
			// game. It should only start when the game starts)
			else {
				g.drawImage(mainScreenBG[41], 0, 0, null);
			}
		}

		// If the menu is the help menu draw the background accordingly
		if (this.menu.equals("HELP")) {
			g.drawImage(help, 0, 0, null);
		}
	}

	/**
	 * Returns whether or not the animation is done
	 * 
	 * @return True is the animation is done and false if it is not
	 */
	public boolean isDoneAnimation() {
		return doneAnimation;
	}

	/**
	 * Set the animation as done or not done
	 * 
	 * @param doneAnimation
	 *            Boolean dictating whether or not the animation should be
	 *            finished or unfinished
	 */
	public void setDoneAnimation(boolean doneAnimation) {
		this.doneAnimation = doneAnimation;
	}

}
