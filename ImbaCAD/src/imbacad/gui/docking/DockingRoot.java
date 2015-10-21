package imbacad.gui.docking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;


import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;

public class DockingRoot implements ComponentListener, PropertyChangeListener {
	
	public static final int HORIZONTAL = JSplitPane.HORIZONTAL_SPLIT;
	public static final int VERTICAL = JSplitPane.VERTICAL_SPLIT;
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	DockingCanvas dockingCanvas = null;
	
	private Component component = null;
	
	private DockingRoot parent = null;
	private DockingRoot left = null;
	private DockingRoot right = null;
	
	private double dividerLocation = 0.5;
	private int compW = 0;
	private int compH = 0;
	
	
	public DockingRoot(Component c) {
		this.component = c;
		
		if (c instanceof Dockable) {
			((Dockable)c).setDockingRoot(this);
		}
	}
	
	/**
	 * Convenience method for {@link #add(Dockable, int, int) add(Dockable, int, int)} using direction from {@link DockableLayer}.
	 * @param dockable
	 * @param direction
	 */
	public void add(Dockable dockable, int direction) {
		switch (direction) {
		case DockableLayer.DIRECTION_EAST : this.add(dockable, DockingRoot.HORIZONTAL, DockingRoot.RIGHT); break;
		case DockableLayer.DIRECTION_NORTH: this.add(dockable, DockingRoot.VERTICAL  , DockingRoot.LEFT ); break;
		case DockableLayer.DIRECTION_WEST : this.add(dockable, DockingRoot.HORIZONTAL, DockingRoot.LEFT ); break;
		case DockableLayer.DIRECTION_SOUTH: this.add(dockable, DockingRoot.VERTICAL  , DockingRoot.RIGHT); break;
		default: throw new IllegalArgumentException("illegal direction");
		}
	}
	
	/**
	 * Add a new Dockable to this leaf. <br>
	 * The Component is invalidated afterwards and needs repaint(?).
	 * @param dockable
	 * @param orientation
	 * @param leftOrRight
	 */
	public void add(Dockable dockable, int orientation, int leftOrRight) {
		if (isLeaf()) {
			
			Container parentContainer = component.getParent();
			
			parentContainer.remove(component);
			
			// initialize new docking roots
			left  = new DockingRoot((leftOrRight == DockingRoot.LEFT)  ? dockable : component);
			right = new DockingRoot((leftOrRight == DockingRoot.RIGHT) ? dockable : component);
			left.parent = this;
			right.parent = this;
			left.dockingCanvas = dockingCanvas;
			right.dockingCanvas = dockingCanvas;
			
			component = new JSplitPane(orientation, left.component, right.component);
			component.addComponentListener(this);
			((JSplitPane)component).addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, this);
			((JSplitPane)component).setResizeWeight(0.5);
			// TODO: createEmptyBorder would be a nicer looking alternative. 
			// Unfortunately the still remaining (why?) 1-pixel border
			// is not repainted when the divider location changes.
			// A simple repaint() in PropertyChangeListener.propertyChange() seems not to resolve this issue.
			((JSplitPane)component).setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			
			parentContainer.add(component);
			
		} else {
			throw new IllegalStateException("Must only add to leafs!");
		}
		
		
		
	}
	
	/**
	 * Remove this DockingRoot if it is a leaf. <br>
	 * The parent container gets invalidated and needs repaint(?).
	 */
	public void remove() {
		
		if (isLeaf()) {
			
			if (isRoot()) {
				// if the only leaf is the root, just remove the component
				getDockingCanvas().remove(component);
				System.out.println("remove leaf");
			} else {
			
				DockingRoot sibling = getSibling();

				// connect sibling to new grand parent (which might be null if parent is root)
				sibling.parent = parent.parent;
				
				if (parent.isRoot()) {
					// remove affected component
					Container cont = parent.component.getParent();
					cont.remove(parent.component);
					
					// set new root
					parent.parent = null;
					parent.left = sibling.left;
					parent.right = sibling.right;
					parent.component = sibling.component;
					parent.dividerLocation = sibling.dividerLocation;
					
					// add component
					cont.add(parent.component);
					
				} else {
					
					// remove affected component
					// This triggers the ProperChangeListener but not the ComponentListener,
					// so the divider location is changed.
					double dl = parent.parent.dividerLocation;
					((JSplitPane)parent.parent.component).remove(parent.component);
					
					// connect grand parent to sibling
					if (parent.parent.left == parent) {
						// parent is left child
						parent.parent.left = sibling;
						((JSplitPane)parent.parent.component).setLeftComponent(sibling.component);
					} else {
						//parent is right child
						parent.parent.right = sibling;
						((JSplitPane)parent.parent.component).setRightComponent(sibling.component);
					}
					
					// restore divider location
					((JSplitPane)parent.parent.component).setDividerLocation(dl);
					
				}
				
			}
			
			
			// the remainder has no children (since its a leaf) and no parent ;-(
			parent = null;
			
			
		} else {
			throw new IllegalStateException("Must remove only leafs!");
		}
		
	}
	
	public boolean isRoot() {
		return parent == null;
	}
	
	public boolean isLeaf() {
		return left == null && right == null;
	}
	
	public DockingRoot getSibling() {
		if (isRoot()) throw new IllegalStateException("The root has no sibling!");
		
		if (this == parent.left) {
			return parent.right;
		} else {
			return parent.left;
		}
	}

	public Component getComponent() {
		return component;
	}

	public DockingRoot getLeft() {
		return left;
	}

	public DockingRoot getRight() {
		return right;
	}
	
	
	public void printTree(String s) {
		if (isLeaf()) {
			System.out.println(s + ((Dockable)component).getName());
		} else {
			left.printTree("- " + s);
			right.printTree("- " + s);
		}
	}
	
	
	public DockingRoot findRoot() {
		DockingRoot dr = this;
		
		while (!dr.isRoot()) {
			dr = dr.parent;
		}
		
		return dr;
	}

	public DockingCanvas getDockingCanvas() {
		return dockingCanvas;
	}

	public void setDockingCanvas(DockingCanvas dockingCanvas) {
		this.dockingCanvas = dockingCanvas;
	}

	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}
	
	@Override
	public void componentResized(ComponentEvent e) {
		if (component instanceof JSplitPane) {
			JSplitPane split = (JSplitPane)component;
			
			//System.out.println("resize: " + (float)split.getDividerLocation() / split.getMaximumDividerLocation());
			
			if (isRoot()) System.out.println("resize " + dividerLocation);
			
			
			split.setDividerLocation(dividerLocation);
			
			compW = split.getWidth();
			compH = split.getHeight();
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		
		
		JSplitPane split = (JSplitPane)component;
		
		
		// The proportional divider location is only changed if it is not a consequence of a resize.
		if (compW == split.getWidth() && compH == split.getHeight()) {
			
			dividerLocation = (double)split.getDividerLocation() / split.getMaximumDividerLocation();
			
			//System.out.println("change: " + (float)split.getDividerLocation() / split.getMaximumDividerLocation());
			//if (isRoot()) System.out.println("change  " + dividerLocation);
		}
			
	}

}









