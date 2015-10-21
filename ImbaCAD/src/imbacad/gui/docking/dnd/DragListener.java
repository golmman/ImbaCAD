package imbacad.gui.docking.dnd;

/**
 * 
 * @author Dirk Kretschmann
 *
 */
public interface DragListener {
	/**
	 * Called when the drag enabled Component is dropped onto a drop enabled Component.
	 * @param e
	 */
	public void dropped(DNDEvent e);
	
//	/**
//	 * Called when the drag enabled Component is hovering over a drop enabled Component.
//	 * @param e
//	 */
//	public void hovering(DNDEvent e);
//	
//	/**
//	 * Called when the drag enabled Component entered a drop enabled Component.
//	 * @param e
//	 */
//	public void entered(DNDEvent e);
//	
//	/**
//	 * Called when the drag enabled Component exited a drop enabled Component.
//	 * @param e
//	 */
//	public void exited(DNDEvent e);
}
