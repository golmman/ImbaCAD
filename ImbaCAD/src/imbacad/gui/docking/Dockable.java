package imbacad.gui.docking;


import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JLayer;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


public class Dockable extends JPanel implements ComponentListener {
	
	private static final long serialVersionUID = 8753675976711240289L;

	private static final int TITLE_HEIGHT = 25;
	
	private String name = null;
	
	private DockingRoot dockingRoot = null;
	
	private Window owner;
	
	//private JLayer<JPanel> front = null;
	private JLayeredPane layeredPane = new JLayeredPane();
	private JPanel back = new JPanel();
	private DockableLayer front = new DockableLayer(this);
	
	private DockableTitleBar titleBar = null;
	private JPanel contenPane = new JPanel();
	
	public Dockable(Window owner, Component title, String name) {
		super();
		
		this.setLayout(new GridLayout(1, 1));
		this.setBorder(BorderFactory.createEmptyBorder());
		this.addComponentListener(this);
		
		this.owner = owner;
		this.name = name;
		
		
		back.setLayout(null);
		back.setBorder(BorderFactory.createEmptyBorder());
		
		titleBar = new DockableTitleBar(this, title);
		
		contenPane.setBackground(Color.LIGHT_GRAY);
		
		back.add(titleBar);
		back.add(contenPane);
		
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
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentResized(ComponentEvent e) {
		
		back.setBounds(0, 0, this.getWidth(), this.getHeight());
		front.setBounds(0, 0, this.getWidth(), this.getHeight());
		
		titleBar.setBounds(0, 0, this.getWidth(), TITLE_HEIGHT);
		contenPane.setBounds(0, TITLE_HEIGHT, this.getWidth(), this.getHeight() - TITLE_HEIGHT);
		
		back.revalidate();
	}

	@Override
	public void componentShown(ComponentEvent e) {}


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
		return contenPane;
	}


	public Component getTitle() {
		return titleBar.getTitle();
	}


	public Window getOwner() {
		return owner;
	}
	

}
