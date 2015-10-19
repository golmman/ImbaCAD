package imbacad.gui.docking;

import java.awt.Component;
import java.awt.Container;


import javax.swing.JSplitPane;

public class DockingRoot {
	
	public static final int HORIZONTAL = JSplitPane.HORIZONTAL_SPLIT;
	public static final int VERTICAL = JSplitPane.VERTICAL_SPLIT;
	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	DockingCanvas dockingCanvas = null;
	
	private Component component = null;
	
	private DockingRoot parent = null;
	private DockingRoot left = null;
	private DockingRoot right = null;
	
	
	public DockingRoot(Component c) {
		this.component = c;
		
		if (c instanceof Dockable) {
			((Dockable)c).setDockingRoot(this);
		}
	}
	
	/**
	 * Add a new Dockable to this leaf. <br>
	 * The Component is invalidated afterwards and needs repaint(?).
	 * @param c
	 */
	public void add(Dockable dockable, int orientation, int leftOrRight) {
		if (isLeaf()) {
			
			Container parentContainer = component.getParent();
			
			System.out.println(parentContainer.getWidth());
			
			parentContainer.remove(component);
			
			// initialize new docking roots
			left  = new DockingRoot((leftOrRight == DockingRoot.LEFT)  ? dockable : component);
			right = new DockingRoot((leftOrRight == DockingRoot.RIGHT) ? dockable : component);
			left.parent = this;
			right.parent = this;
			left.dockingCanvas = dockingCanvas;
			right.dockingCanvas = dockingCanvas;
			
			component = new JSplitPane(orientation, left.component, right.component);
			((JSplitPane)component).setResizeWeight(0.5);
			
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
				component.getParent().remove(component);
				System.out.println("leaf");
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
					
					// add component
					cont.add(parent.component);
					
//					cont.revalidate();
//					cont.repaint();
					
				} else {
					// remove affected component
					((JSplitPane)parent.parent.component).remove(parent.component);
					
					// connect grand parent to sibling
					if (parent.parent.left == parent) {
						// parent is left child
						parent.parent.left = sibling;
						System.out.println("left!");
						((JSplitPane)parent.parent.component).setLeftComponent(sibling.component);
					} else {
						//parent is right child
						parent.parent.right = sibling;
						System.out.println("right!");
						((JSplitPane)parent.parent.component).setRightComponent(sibling.component);
					}
				}
				
			}
			
		} else {
			throw new IllegalStateException("Must remove only leafs!");
		}
		
		findRoot().printTree("");
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

}









