package imbacad.view.docking.dnd;

import java.awt.Component;

/**
 * 
 * Event object given by DNDListener. 
 * 
 * @author Dirk Kretschmann
 *
 */
public class DNDEvent {
	
	public static final int RESULT_UNKNOWN = 0;
	public static final int RESULT_SUCCESS = 1;
	public static final int RESULT_FAILURE = 2;
	
	private Component dragSource = null;
	private Component dropTarget = null;
	private int x = 0;
	private int y = 0;
	private int originX = 0;
	private int originY = 0;
	private int result = 0;
	
	/**
	 * Creates a new DNDEvent
	 * @param dragSource
	 * @param dropTarget
	 * @param x
	 * @param y
	 */
	public DNDEvent(Component dragSource, Component dropTarget, int x, int y, int originX, int originY, int result) {
		this.dragSource = dragSource;
		this.dropTarget = dropTarget;
		this.x = x;
		this.y = y;
		this.originX = originX;
		this.originY = originY;
		this.result = result;
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
	 * Returns the x coordinate relative to the Component which is hovered or
	 * the absolute screen coordinate if there is no target component.
	 * @return x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y coordinate relative to the Component which is hovered or
	 * the absolute screen coordinate if there is no target component.
	 * @return y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns the result of the current dnd
	 * @return DNDEvent.RESULT_UNKNOWN, DNDEvent.RESULT_SUCCESS or DNDEvent.RESULT_FAILURE
	 */
	public int getResult() {
		return result;
	}
	
	/**
	 * Returns the drag origins x coordinate relative to the Component which was dragged.
	 * @return x
	 */
	public int getOriginX() {
		return originX;
	}

	/**
	 * Returns the drag origins y coordinate relative to the Component which was dragged.
	 * @return y
	 */
	public int getOriginY() {
		return originY;
	}

}
