package imbacad.gui.docking;

import imbacad.gui.docking.dnd.DND;
import imbacad.gui.docking.dnd.DNDEvent;
import imbacad.gui.docking.dnd.DropListener;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.plaf.LayerUI;

public class DockableLayer_OLD extends LayerUI<JPanel> implements DropListener {

	private static final long serialVersionUID = -5339417520468124860L;
	
	private static final int EAST = 0;
	private static final int NORTH = 1;
	private static final int WEST = 2;
	private static final int SOUTH = 3;

	private boolean mouseOver = false;
	private int orientation = 0;

	private Dockable dockable = null;

	public DockableLayer_OLD(Dockable dockable) {
		this.dockable = dockable;
		
		//DND.support(this.get, this);
	}

	@Override
	public void paint(Graphics g, JComponent c) {
		// paint the layer as is
		super.paint(g, c);

		int w = c.getWidth();
		int h = c.getHeight();
		
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

//	@SuppressWarnings("unchecked")
//	@Override
//	public void installUI(JComponent c) {
//		super.installUI(c);
//
//		// enable mouse motion events for the layer's subcomponents
//		((JLayer<JPanel>)c).setLayerEventMask(AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
//		
//	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public void uninstallUI(JComponent c) {
//		super.uninstallUI(c);
//		// reset the layer event mask
//
//		((JLayer<JPanel>)c).setLayerEventMask(0);
//	}

	// overridden method which catches MouseMotion events
	@Override
	public void eventDispatched(AWTEvent e, JLayer<? extends JPanel> l) {
		// System.out.println("AWTEvent detected: " + e);
		
		if (e instanceof MouseEvent) {
			MouseEvent me = (MouseEvent)e;
			int oldOrientation = orientation;
			boolean repaint = false;
			
			
			float w = (float)dockable.getFront().getWidth();
			float h = (float)dockable.getFront().getHeight();
			
			float x = (float)me.getX();
			float y = (float)me.getY();
			
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
			
			if (me.getID() == MouseEvent.MOUSE_DRAGGED) {
				
			}
			
			if (me.getID() == MouseEvent.MOUSE_ENTERED) {
				mouseOver = true;
				repaint = true;
			}

			if (me.getID() == MouseEvent.MOUSE_EXITED) {
				mouseOver = false;
				repaint = true;
			}

			
			if (repaint) {
				dockable.getFront().repaint();
			}
		}
	}

@Override
public void dropped(DNDEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void hovering(DNDEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void entered(DNDEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void exited(DNDEvent e) {
	// TODO Auto-generated method stub
	
}

}
