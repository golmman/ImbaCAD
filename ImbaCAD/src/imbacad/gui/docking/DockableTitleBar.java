package imbacad.gui.docking;

import imbacad.gui.docking.dnd.DND;
import imbacad.gui.docking.dnd.DNDEvent;
import imbacad.gui.docking.dnd.DragListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Adds title bar functionality for a {@link Dockable}.
 * @author Dirk Kretschmann
 *
 */
public class DockableTitleBar extends JPanel implements DragListener, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -7476683061433551220L;
	
	private static final Color COLOR_BUTTON = new Color(232, 232, 232);
	private static final Color COLOR_BUTTON_HOVER = new Color(208, 208, 208);
	private static final Color COLOR_BUTTON_DISABLED = new Color(160, 160, 160);
	
	private static final Font FONT_BUTTON = new Font("Courier", Font.PLAIN, 12);
	
	private Dockable dockable;
	
	private Component title = null;
	private JLabel minimize = new JLabel(" _ ");
	private JLabel maximize = new JLabel(" ¯ ");
	private JLabel close = new JLabel(" X ");

	public DockableTitleBar(Dockable dockable, Component title) {
		this.dockable = dockable;
		
		
		this.title = title;
		DND.supportDrag(this.title, this);
		
		
		this.minimize.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		this.minimize.setFont(FONT_BUTTON);
		this.minimize.setOpaque(true);
		this.minimize.setBackground(COLOR_BUTTON_DISABLED);
		//this.minimize.addMouseListener(this);
		//this.minimize.addMouseMotionListener(this);
		
		this.maximize.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		this.maximize.setFont(FONT_BUTTON);
		this.maximize.setOpaque(true);
		this.maximize.setBackground(COLOR_BUTTON_DISABLED);
		//this.maximize.addMouseListener(this);
		//this.maximize.addMouseMotionListener(this);
		
		this.close.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		this.close.setFont(FONT_BUTTON);
		this.close.setOpaque(true);
		this.close.setBackground(COLOR_BUTTON);
		this.close.addMouseListener(this);
		this.close.addMouseMotionListener(this);
		
		
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

	public Component getTitle() {
		return title;
	}

	public Dockable getDockable() {
		return dockable;
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == close) {
			DockingCanvas sourceCanvas = dockable.getDockingRoot().findRoot().getDockingCanvas();
			
			dockable.getDockingRoot().remove();
			
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

	@Override
	public void mouseEntered(MouseEvent e) {
		JLabel label = (JLabel)e.getSource();
		
		label.setBackground(COLOR_BUTTON_HOVER);
		label.repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		JLabel label = (JLabel)e.getSource();
		
		label.setBackground(COLOR_BUTTON);
		label.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
