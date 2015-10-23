package imbacad.view.docking;

import imbacad.view.docking.dnd.DND;
import imbacad.view.docking.dnd.DNDEvent;
import imbacad.view.docking.dnd.DropListener;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * Enables drop of dragged components and painting on top of {@link Dockable}.
 * @author Dirk Kretschmann
 *
 */
public class DockableLayer extends JPanel implements DropListener {

	private static final long serialVersionUID = -2018629049569901672L;
	
	public static final int DIRECTION_EAST = 0;
	public static final int DIRECTION_NORTH = 1;
	public static final int DIRECTION_WEST = 2;
	public static final int DIRECTION_SOUTH = 3;
	
	private Dockable dockable = null;

	private boolean mouseOver = false;
	private int direction = 0;

	public DockableLayer(Dockable dockable) {
		super();
		
		this.dockable = dockable;
		
		this.setOpaque(false);
		
		DND.supportDrop(this, this);
	}
	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int w = this.getWidth();
		int h = this.getHeight();
		
		//g.setColor(Color.BLUE);
		//g.drawLine(0, 0, w, h);
		//g.drawLine(w, 0, 0, h);
		
		if (mouseOver) {
			// fill it with the translucent green
			g.setColor(new Color(0, 128, 0, 128));
			
			switch (direction) {
			case DIRECTION_EAST : g.fillRect(w/2,   0, w/2,   h); break;
			case DIRECTION_NORTH: g.fillRect(  0,   0,   w, h/2); break;
			case DIRECTION_WEST : g.fillRect(  0,   0, w/2,   h); break;
			case DIRECTION_SOUTH: g.fillRect(  0, h/2,   w, h/2); break;
			default: break;
			}
			
		}
	}


	@Override
	public void dropped(DNDEvent e) {
		// Create new Dialog if drag source is from the same Dockable
		if (dockable.getTitle() == e.getDragSource()) {
			DockableTitleBar titleBar = (DockableTitleBar)dockable.getTitle().getParent();
			DNDEvent event = new DNDEvent(
					e.getDragSource(), 
					e.getDropTarget(), 
					titleBar.getLocationOnScreen().x + e.getX() - e.getOriginX(), 
					titleBar.getLocationOnScreen().y + e.getY() + e.getOriginY(), 
					e.getOriginX(), e.getOriginY(), 
					DNDEvent.RESULT_FAILURE);
			titleBar.dropped(event);
			return;
		}
		
		// TODO: quick but really dirty...
		//Dockable sourceDockable = (Dockable)e.getDragSource().getParent().getParent().getParent().getParent();
		// TODO: less dirty...
		Dockable sourceDockable = (Dockable)((DockableTitleBar)e.getDragSource().getParent()).getDockable();
		Dockable targetDockable = dockable;
		
		DockingCanvas sourceCanvas = sourceDockable.getDockingRoot().getDockingCanvas();
		DockingCanvas targetCanvas = targetDockable.getDockingRoot().getDockingCanvas();
		
		
		// remove drag source
		sourceDockable.getDockingRoot().remove();
		if (sourceCanvas.isDisposable() && sourceCanvas.getComponentCount() == 0) {
			sourceCanvas.getOwner().dispose();
		}
		
		// add to drop target
		targetDockable.getDockingRoot().add(sourceDockable, direction);
		
		
		// re-validate and repaint target canvas
		targetCanvas.revalidate();
		targetCanvas.repaint();
		
		// re-validate and repaint source canvas if different to target canvas
		if (targetCanvas != sourceCanvas) {
			sourceCanvas.revalidate();
			sourceCanvas.repaint();
		}
		
		mouseOver = false;
		
		//dockable.getDockingRoot().findRoot().printTree("");
	}


	@Override
	public void hovering(DNDEvent e) {
		// return if drag source is from the same Dockable
		if (dockable.getTitle() == e.getDragSource()) return;
		
		int oldOrientation = direction;
		boolean repaint = false;
		
		
		float w = (float)this.getWidth();
		float h = (float)this.getHeight();
		
		float x = (float)e.getX();
		float y = (float)e.getY();
		
		float m = h / w;
		
		float f = m * x;
		float g = h - m * x;
		
		if (y < f && y > g) {
			direction = DIRECTION_EAST;
		} else if (y < f && y < g) {
			direction = DIRECTION_NORTH;
		} else if (y > f && y < g) {
			direction = DIRECTION_WEST;
		} else if (y > f && y > g) {
			direction = DIRECTION_SOUTH;
		}

		//System.out.println(orientation);
		//System.out.println(me.getX() + " " + me.getY() + "    " + w + " " + h);
		
		if (oldOrientation != direction) {
			repaint = true;
		}
		
		if (repaint) {
			this.repaint();
		}
	}


	@Override
	public void entered(DNDEvent e) {
		// return if drag source is from the same Dockable
		if (dockable.getTitle() == e.getDragSource()) return;
		
		mouseOver = true;
		this.repaint();
	}


	@Override
	public void exited(DNDEvent e) {
		// return if drag source is from the same Dockable
		if (dockable.getTitle() == e.getDragSource()) return;
		
		mouseOver = false;
		this.repaint();
	}


	public Dockable getDockable() {
		return dockable;
	}
}
