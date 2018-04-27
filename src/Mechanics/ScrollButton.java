package Mechanics;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;

/**
 * Personalized button used for scrolling in the level selection menu. Sourced
 * from: https://tips4java.wordpress.com/2008/12/06/action-map-action/
 *
 */
public class ScrollButton extends AbstractAction {

	private Action action;
	private JComponent component;
	private String actionCommand = "";

	/**
	 * Constructor for the scroll button, replaces the default action with a
	 * customized action (in this case scrolling)
	 * 
	 * @param name
	 *            Name of the action
	 * @param component
	 *            Component the action will act upon
	 * @param action
	 *            Identifier for the action
	 */
	public ScrollButton(String name, JComponent component, String action) {
		super(name);

		this.action = component.getActionMap().get(action);
		this.component = component;
	}

	public void setActionCommand(String action) {
		this.actionCommand = action;
	}

	/**
	 * Performs action based on the source event (clicking the button to scroll)
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		event = new ActionEvent(component, ActionEvent.ACTION_PERFORMED,
				actionCommand, event.getWhen(), event.getModifiers());

		this.action.actionPerformed(event);

	}

}
