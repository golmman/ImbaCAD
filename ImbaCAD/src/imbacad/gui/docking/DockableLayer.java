package imbacad.gui.docking;

import imbacad.gui.docking.dnd.DND;
import imbacad.gui.docking.dnd.DNDEvent;
import imbacad.gui.docking.dnd.DropListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DockableLayer extends JPanel implements DropListener {

	private static final long serialVersionUID = -2018629049569901672L;
	
	private static final int EAST = 0;
	private static final int NORTH = 1;
	private static final int WEST = 2;
	private static final int SOUTH = 3;
	
	private Dockable dockable = null;

	private boolean mouseOver = false;
	private int orientation = 0;

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
		
		g.setColor(Color.BLUE);
		g.drawLine(0, 0, w, h);
		g.drawLine(w, 0, 0, h);
		
		if (mouseOver) {
			// fill it with the translucent green
			g.setColor(new Color(0, 128, 0, 128));
			
			switch (orientation) {
			case EAST : g.fillRect(w/2,   0, w/2,   h); break;
			case NORTH: g.fillRect(  0,   0,   w, h/2); break;
			case WEST : g.fillRect(  0,   0, w/2,   h); break;
			case SOUTH: g.fillRect(  0, h/2,   w, h/2); break;
			default: break;
			}
			
		}
	}


	@Override
	public void dropped(DNDEvent e) {
		if (dockable.getTitle() == e.getDragSource()) return;
		
		mouseOver = false;
		
		// remove drag source Dockable
		// TODO: quick but really dirty...
		Dockable source = (Dockable)e.getDragSource().getParent().getParent().getParent().getParent();
		source.getDockingRoot().remove();
		
		// add to drop target
		switch (orientation) {
		case EAST : dockable.getDockingRoot().add(source, DockingRoot.HORIZONTAL, DockingRoot.RIGHT); break;
		case NORTH: dockable.getDockingRoot().add(source, DockingRoot.VERTICAL  , DockingRoot.LEFT ); break;
		case WEST : dockable.getDockingRoot().add(source, DockingRoot.HORIZONTAL, DockingRoot.LEFT ); break;
		case SOUTH: dockable.getDockingRoot().add(source, DockingRoot.VERTICAL  , DockingRoot.RIGHT); break;
		default: break;
		}
		
		// re-validate and repaint root
		dockable.getDockingRoot().findRoot().getComponent().getParent().revalidate();
		dockable.getDockingRoot().findRoot().getComponent().getParent().repaint();
	}


	@Override
	public void hovering(DNDEvent e) {
		if (dockable.getTitle() == e.getDragSource()) return;
		
		int oldOrientation = orientation;
		boolean repaint = false;
		
		
		float w = (float)this.getWidth();
		float h = (float)this.getHeight();
		
		float x = (float)e.getX();
		float y = (float)e.getY();
		
		float m = h / w;
		
		float f = m * x;
		float g = h - m * x;
		
		if (y < f && y > g) {
			orientation = EAST;
		} else if (y < f && y < g) {
			orientation = NORTH;
		} else if (y > f && y < g) {
			orientation = WEST;
		} else if (y > f && y > g) {
			orientation = SOUTH;
		}

		//System.out.println(orientation);
		//System.out.println(me.getX() + " " + me.getY() + "    " + w + " " + h);
		
		if (oldOrientation != orientation) {
			repaint = true;
		}
		
		if (repaint) {
			this.repaint();
		}
	}


	@Override
	public void entered(DNDEvent e) {
		if (dockable.getTitle() == e.getDragSource()) return;
		
		mouseOver = true;
		this.repaint();
	}


	@Override
	public void exited(DNDEvent e) {
		if (dockable.getTitle() == e.getDragSource()) return;
		
		mouseOver = false;
		this.repaint();
	}
}
