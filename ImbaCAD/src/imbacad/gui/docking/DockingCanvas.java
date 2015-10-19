package imbacad.gui.docking;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DockingCanvas extends JPanel {

	private static final long serialVersionUID = 3317553968573865171L;

	public DockingCanvas() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		//g.setColor(Color.RED);
		//g.drawLine(10, 10, 100, 200);

	}
	
	public void add(DockingRoot dr) {
		super.add(dr.getComponent());
		
		dr.setDockingCanvas(this);
	}
}
