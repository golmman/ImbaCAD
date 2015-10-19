package imbacad.gui.docking.dnd;

/**
 * 
 * drop listener
 * 
 * @author Dirk Kretschmann
 *
 */
public interface DropListener {
	
	/**
	 * Called when a drag enabled Component is dropped onto the drop enabled Component.
	 * @param e
	 */
	public void dropped(DNDEvent e);
	
	/**
	 * Called when a drag enabled Component is hovering over the drop enabled Component.
	 * @param e
	 */
	public void hovering(DNDEvent e);
	
	/**
	 * Called when a drag enabled Component entered the drop enabled Component.
	 * @param e
	 */
	public void entered(DNDEvent e);
	
	/**
	 * Called when a drag enabled Component exited the drop enabled Component.
	 * @param e
	 */
	public void exited(DNDEvent e);
}
