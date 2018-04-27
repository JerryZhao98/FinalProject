import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import GUI.GUI;
import Mechanics.Control;

/**
 * Starting point for the program, sets up the JFrame and creates/initiates a
 * control class to run and handle the rest of the game.
 * 
 * @author Barbara Guo and Jerry Zhao
 * @version Final Project 2016
 *
 */
@SuppressWarnings("serial")
public class Main extends JFrame {
	private JFrame frame;
	private Control control;

	/**
	 * Constructor for the main class. Sets up the JFrame and the control thread
	 * (main thread for running the game)
	 * 
	 */
	Main() {

		super("INSOMNIA");

		this.frame = new JFrame();
		this.frame.setTitle("Insomnia");
		this.control = new Control();
		ImageIcon icon = new ImageIcon("Icon3.png");
		this.frame.setIconImage(icon.getImage());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setVisible(true);
		this.frame.setResizable(false);
		this.frame.setSize(Control.SCREEN_WIDTH, Control.SCREEN_HEIGHT + 25);

		// Initiates the main thread which controls other threads/the overall
		// operation of the game
		Thread controlThread = new Thread(control);
		controlThread.start();

		frame.add(control, BorderLayout.CENTER);
		pack();
		
		//Main menu is always the first panel to be shown
		this.control.showMain();

	}

	public static void main(String[] args) {

		new Main();

	}

}
