package imbacad.gui.docking;

import imbacad.gui.docking.dnd.DND;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Dockable extends JPanel implements ComponentListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8753675976711240289L;

	private static final int TITLE_HEIGHT = 25;
	
	private String name = null;
	
	private Frame owner = null;
	
	private DockingRoot dockingRoot = null;
	
	private JDialog dialog = null;
	
	//private JLayer<JPanel> front = null;
	private JLayeredPane layeredPane = new JLayeredPane();
	private JPanel back = new JPanel();
	private DockableLayer front = new DockableLayer(this);
	
	private JPanel top = new JPanel();
	private JPanel bottom = new JPanel();
	
	private Component title = null;
	
	public Dockable(Frame owner, Component title, String name) {
		super();
		
		
		
		this.setLayout(new GridLayout(1, 1));
		this.addComponentListener(this);
		
		this.owner = owner;
		this.title = title;
		this.name = name;
		DND.supportDrag(this.title, null);
		
		dialog = new JDialog(owner, name);
		
		
		back.setLayout(null);
		
		top.setLayout(new FlowLayout(FlowLayout.LEFT));
		top.add(this.title);
		top.setBackground(Color.YELLOW);
		
		bottom.setBackground(Color.CYAN);
		
		back.add(top);
		back.add(bottom);
		
		layeredPane.setLayout(null);
		layeredPane.add(back, JLayeredPane.DEFAULT_LAYER);
		layeredPane.add(front, JLayeredPane.DRAG_LAYER);
		
		this.add(layeredPane);
		
		
		dialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		dialog.setLayout(new GridLayout(1, 1));
		//dialog.add(bottom);
		//dialog2 = new JInternalFrame(title);
		//dialog2.setVisible(false);
		//owner.add(dialog2);
	}
	
	
	public JDialog getDialog() {
		return dialog;
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
	

}
