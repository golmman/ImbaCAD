package imbacad.gui.docking;

import imbacad.gui.docking.dnd.DND;
import imbacad.gui.docking.dnd.DNDEvent;
import imbacad.gui.docking.dnd.DropListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.JPanel;

/**
 * Parent container for all {@link Dockable Dockables}.
 * @author Dirk Kretschmann
 *
 */
public class DockingCanvas extends JPanel implements DropListener {

	private static final long serialVersionUID = 3317553968573865171L;
	
	private Window owner;
	private boolean disposable = true;
	
	private boolean mouseOver = false;
	
	public DockingCanvas(Window owner, boolean disposable) {
		this.setLayout(new GridLayout(1, 1));
		this.owner = owner;
		this.disposable = disposable;
		
		if (!disposable) {
			DND.supportDrop(this, this);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (mouseOver) {
			// fill it with the translucent green
			g.setColor(new Color(0, 128, 0, 128));
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
		}
	}
	
	@Override
	public Component add(Component comp) {
		if (this.getComponentCount() >= 1) {
			throw new IllegalStateException("A DockingCanvas must not have more than one Component.");
		}
		return super.add(comp);
	}
	
	public void add(DockingRoot dr) {
		super.add(dr.getComponent());
		
		dr.setDockingCanvas(this);
	}
	
	public void add(Dockable d) {
		this.add(d.getDockingRoot());
		
//		super.add(d);
//		d.getDockingRoot().setDockingCanvas(this);
	}

	public Window getOwner() {
		return owner;
	}

	public boolean isDisposable() {
		return disposable;
	}

	public void setDisposable(boolean disposable) {
		this.disposable = disposable;
	}

	@Override
	public void dropped(DNDEvent e) {
		// TODO: quick but really dirty...
		//Dockable sourceDockable = (Dockable)e.getDragSource().getParent().getParent().getParent().getParent();
		// TODO: less dirty...
		Dockable sourceDockable = (Dockable)((DockableTitleBar)e.getDragSource().getParent()).getDockable();
		
		DockingCanvas sourceCanvas = sourceDockable.getDockingRoot().getDockingCanvas();
		
		// remove drag source
		sourceDockable.getDockingRoot().remove();
		if (sourceCanvas.isDisposable() && sourceCanvas.getComponentCount() == 0) {
			sourceCanvas.getOwner().dispose();
		}
		
		// add to drop target
		this.add(sourceDockable);
		
		
		// re-validate and repaint target canvas
		this.revalidate();
		this.repaint();
		
		// re-validate and repaint source canvas if different to target canvas
		sourceCanvas.revalidate();
		sourceCanvas.repaint();
		
		mouseOver = false;
	}

	@Override
	public void hovering(DNDEvent e) {}

	@Override
	public void entered(DNDEvent e) {
		mouseOver = true;
		this.repaint();
	}

	@Override
	public void exited(DNDEvent e) {
		mouseOver = false;
		this.repaint();
	}

}
