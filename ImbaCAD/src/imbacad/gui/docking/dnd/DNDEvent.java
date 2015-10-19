package imbacad.gui.docking.dnd;

import java.awt.Component;

/**
 * 
 * Event object given by DNDListener. 
 * 
 * @author Dirk Kretschmann
 *
 */
public class DNDEvent {

	private Component dragSource = null;
	private Component dropTarget = null;
	private int x = 0;
	private int y = 0;
	
	/**
	 * Creates a new DNDEvent
	 * @param dragSource
	 * @param dropTarget
	 * @param x
	 * @param y
	 */
	public DNDEvent(Component dragSource, Component dropTarget, int x, int y) {
		this.dragSource = dragSource;
		this.dropTarget = dropTarget;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the source component of the current dnd.
	 * @return source Component
	 */
	public Component getDragSource() {
		return dragSource;
	}

	/**
	 * Returns the target component of the current dnd.
	 * @return target Component
	 */
	public Component getDropTarget() {
		return dropTarget;
	}

	/**
	 * Returns the x coordinate relative to the Component which is hovered.
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y coordinate relative to the Component which is hovered.
	 * @return y
	 */
	public int getY() {
		return y;
	}

}
