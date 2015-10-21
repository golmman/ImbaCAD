package imbacad.gui.docking;

import imbacad.gui.docking.dnd.DND;
import imbacad.gui.docking.dnd.DNDEvent;
import imbacad.gui.docking.dnd.DragListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


public class Dockable extends JPanel implements ComponentListener, DragListener {
	
	private static final long serialVersionUID = 8753675976711240289L;

	private static final int TITLE_HEIGHT = 25;
	
	private String name = null;
	
	private DockingRoot dockingRoot = null;
	
	private Window owner;
	
	//private JLayer<JPanel> front = null;
	private JLayeredPane layeredPane = new JLayeredPane();
	private JPanel back = new JPanel();
	private DockableLayer front = new DockableLayer(this);
	
	private JPanel top = new JPanel();
	private JPanel bottom = new JPanel();
	
	private Component title = null;
	private JLabel close = new JLabel(" X ");
	
	public Dockable(Window owner, Component title, String name) {
		super();
		
		this.setLayout(new GridLayout(1, 1));
		this.setBorder(BorderFactory.createEmptyBorder());
		this.addComponentListener(this);
		
		this.owner = owner;
		this.title = title;
		this.close.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.name = name;
		DND.supportDrag(this.title, this);
		
		
		back.setLayout(null);
		back.setBorder(BorderFactory.createEmptyBorder());
		
		top.setLayout(new TitleLayout());
		top.add(this.title);
		top.add(new JLabel(""));
		top.add(new JLabel(""));
		top.add(this.close);
		top.setBackground(Color.GRAY);
		
		bottom.setBackground(Color.LIGHT_GRAY);
		
		back.add(top);
		back.add(bottom);
		
		layeredPane.setLayout(null);
		layeredPane.add(back, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(front, JLayeredPane.DRAG_LAYER);
		
		this.add(layeredPane);
		
		
		

		
		//dialog.setUndecorated(true);
		//dialog.add(bottom);
		//dialog2 = new JInternalFrame(title);
		//dialog2.setVisible(false);
		//owner.add(dialog2);
	}
	

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		
		back.setBounds(0, 0, this.getWidth(), this.getHeight());
		front.setBounds(0, 0, this.getWidth(), this.getHeight());
		
		top.setBounds(0, 0, this.getWidth(), TITLE_HEIGHT);
		bottom.setBounds(0, TITLE_HEIGHT, this.getWidth(), this.getHeight() - TITLE_HEIGHT);
		
		back.revalidate();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}


	public DockingRoot getDockingRoot() {
		return dockingRoot;
	}


	public void setDockingRoot(DockingRoot dockingRoot) {
		this.dockingRoot = dockingRoot;
	}
	
	public String getName() {
		return name;
	}


	public JLayer<JPanel> getFront() {
		return null;// front;
	}
	
	
	
	public JPanel getContentPane() {
		return bottom;
	}


	public Component getTitle() {
		return title;
	}


	@Override
	public void dropped(DNDEvent e) {
		
		if (e.getResult() == DNDEvent.RESULT_FAILURE) {
			// there was no target for our drag
				
			
			// store the current DockingCanvas
			DockingCanvas sourceCanvas = dockingRoot.findRoot().getDockingCanvas();
			
			
			this.setPreferredSize(this.getSize());
			dockingRoot.remove();
			
			// create new Window to display the Dockable
			JDialog dialog = new JDialog(owner, "");
			DockingCanvas dialogDockingCanvas = new DockingCanvas(dialog, true);
			
			dialogDockingCanvas.add(this);
			
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


	public Window getOwner() {
		return owner;
	}
	

}
