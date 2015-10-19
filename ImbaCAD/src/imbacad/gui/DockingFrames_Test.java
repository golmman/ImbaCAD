package imbacad.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.SingleCDockable;

public class DockingFrames_Test {

	public DockingFrames_Test() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Docking Test");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		CControl control = new CControl(frame);

		frame.setLayout(new GridLayout(1, 1));
		frame.add(control.getContentArea());

		SingleCDockable red = create("Red", Color.RED);
		SingleCDockable green = create("Green", Color.GREEN);
		SingleCDockable blue = create("Blue", Color.BLUE);
		

		control.addDockable(red);
		control.addDockable(green);
		control.addDockable(blue);
		
		red.setVisible(true);

		green.setLocation(CLocation.base().normalSouth(0.4));
		green.setVisible(true);

		blue.setLocation(CLocation.base().normalEast(0.3));
		blue.setVisible(true);

		frame.setBounds(20, 20, 400, 400);
		frame.setVisible(true);
	}

	
	
	public static SingleCDockable create(String title, Color color) {
		JPanel background = new JPanel();
		background.setOpaque(true);
		background.setBackground(color);

		return new DefaultSingleCDockable(title, title, background);
	}
}
