package imbacad.gui.docking.dnd;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.IOException;

/**
 * 
 * Manages drag and drop support of java awt components.
 * 
 * @author Dirk Kretschmann
 *
 */
public class DND {
	
	private static Component dragSource = null;
	
	private DND() {}

	
	/**
	 * Adds drag support to a Component via the given DragListener.
	 * @param c
	 * @param listener
	 */
	public static void supportDrag(Component c, DragListener listener) {
		DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(
            c,
            DnDConstants.ACTION_COPY_OR_MOVE,
            new DragGestureListener() {
            	
				@Override
				public void dragGestureRecognized(DragGestureEvent dge) {
					
					DragSource ds = dge.getDragSource();
					
					dragSource = dge.getComponent();
					
	                ds.startDrag(
	                	dge, 
	                	null,	
		                new Transferable() {
							
							@Override
							public boolean isDataFlavorSupported(DataFlavor flavor) {
								return false;
							}
							
							@Override
							public DataFlavor[] getTransferDataFlavors() {
								return null;
							}
							
							@Override
							public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
								return null;
							}
						},
	                	new DragSourceListener() {
							
							
							@Override
							public void dropActionChanged(DragSourceDragEvent dsde) {}
							
							@Override
							public void dragOver(DragSourceDragEvent dsde) {}
							
							@Override
							public void dragExit(DragSourceEvent dse) {}
							
							@Override
							public void dragEnter(DragSourceDragEvent dsde) {}
							
							@Override
							public void dragDropEnd(DragSourceDropEvent dsde) {}
						});
					
				}
			});
	}
	
	/**
	 * Adds drop support to a Component via the given DropListener.
	 * @param c
	 * @param listener
	 */
	public static void supportDrop(Component c, DropListener listener) {
		
		new DropTarget(
            c,
            DnDConstants.ACTION_COPY_OR_MOVE,
            new DropTargetListener() {
				
            	private int lastX = 0;
            	private int lastY = 0;
            	
				@Override
				public void dropActionChanged(DropTargetDragEvent dtde) {}
				
				@Override
				public void drop(DropTargetDropEvent dtde) {
					Component dropTarget = dtde.getDropTargetContext().getComponent();
					int x = (int)dtde.getLocation().getX();
					int y = (int)dtde.getLocation().getY();
					
					if (dragSource == dropTarget) return;
					
					listener.dropped(new DNDEvent(dragSource, dropTarget, x, y));
					
					lastX = x;
					lastY = y;
				}
				
				@Override
				public void dragOver(DropTargetDragEvent dtde) {
					Component dropTarget = dtde.getDropTargetContext().getComponent();
					int x = (int)dtde.getLocation().getX();
					int y = (int)dtde.getLocation().getY();
					
					if (dragSource == dropTarget) return;
					if (lastX == x && lastY == y) return;
					
					listener.hovering(new DNDEvent(dragSource, dropTarget, x, y));
					
					lastX = x;
					lastY = y;
				}
				
				@Override
				public void dragExit(DropTargetEvent dte) {
					Component dropTarget = dte.getDropTargetContext().getComponent();
					
					if (dragSource == dropTarget) return;
					
					listener.exited(new DNDEvent(dragSource, dropTarget, -1, -1));
					
					lastX = -1;
					lastY = -1;
				}
				
				@Override
				public void dragEnter(DropTargetDragEvent dtde) {
					Component dropTarget = dtde.getDropTargetContext().getComponent();
					int x = (int)dtde.getLocation().getX();
					int y = (int)dtde.getLocation().getY();
					
					if (dragSource == dropTarget) return;
					
					listener.entered(new DNDEvent(dragSource, dropTarget, x, y));
					
					lastX = x;
					lastY = y;
				}
			},
            true);
	}
	
}
