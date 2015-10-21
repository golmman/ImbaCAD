package imbacad.gui.docking;

import imbacad.gui.docking.dnd.DND;
import imbacad.gui.docking.dnd.DNDEvent;
import imbacad.gui.docking.dnd.DragListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DockableTitleBar extends JPanel implements DragListener{

	private static final long serialVersionUID = -7476683061433551220L;
	
	private Dockable dockable;
	
	private Component title = null;
	private JLabel minimize = new JLabel(" _ ");
	private JLabel maximize = new JLabel(" ¯ ");
	private JLabel close = new JLabel(" X ");

	public DockableTitleBar(Dockable dockable, Component title) {
		this.dockable = dockable;
		
		
		this.title = title;
		DND.supportDrag(this.title, this);
		
		
		
		this.setLayout(new TitleLayout());
		this.add(this.title);
		this.add(this.minimize);
		this.add(this.maximize);
		this.add(this.close);
		this.setBackground(Color.GRAY);
	}

	@Override
	public void dropped(DNDEvent e) {
		
		if (e.getResult() == DNDEvent.RESULT_FAILURE) {
			// there was no target for our drag
				
			
			// store the current DockingCanvas
			DockingCanvas sourceCanvas = dockable.getDockingRoot().findRoot().getDockingCanvas();
			
			
			dockable.setPreferredSize(dockable.getSize());
			dockable.getDockingRoot().remove();
			
			// create new Window to display the Dockable
			JDialog dialog = new JDialog(dockable.getOwner(), "");
			DockingCanvas dialogDockingCanvas = new DockingCanvas(dialog, true);
			
			dialogDockingCanvas.add(dockable);
			
			Insets insets = sourceCanvas.getOwner().getInsets();
			dialog.setLocation(
					e.getX() - e.getOriginX() - insets.left - title.getX(), 
					e.getY() - e.getOriginY() - insets.top  - title.getY());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLayout(new GridLayout(1, 1));
			dialog.add(dialogDockingCanvas);
			dialog.pack();
			dialog.setVisible(true);
			
			
			if (sourceCanvas.isDisposable() && sourceCanvas.getComponentCount() == 0) {
				// disposable, empty window so remove it
				sourceCanvas.getOwner().dispose();
			} else {
				// re-validate and repaint the old DockingCanvas
				sourceCanvas.revalidate();
				sourceCanvas.repaint();
			}
		}
	}

}
