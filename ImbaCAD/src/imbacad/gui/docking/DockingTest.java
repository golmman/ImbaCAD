package imbacad.gui.docking;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DockingTest {
	
	
	public DockingTest() {}

	public static void main(String[] args) {
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		
		
		JFrame frame = new JFrame("DockingTest");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 700, 300);
		frame.setLayout(new GridLayout(1, 1));
		
		DockingCanvas dockingCanvas = new DockingCanvas(frame, false);
		
		
		Dockable dockable1 = new Dockable(frame, new JLabel("dockable1"), "dockable1");
		Dockable dockable2 = new Dockable(frame, new JLabel("dockable2"), "dockable2");
		Dockable dockable3 = new Dockable(frame, new JLabel("dockable3"), "dockable3");
		Dockable dockable4 = new Dockable(frame, new JLabel("dockable4"), "dockable4");
		
		dockable3.getContentPane().setLayout(new GridLayout(1, 1));
		dockable3.getContentPane().add(new JButton("Miau"));
		
		
		DockingRoot root = new DockingRoot(dockable1);
		
		dockingCanvas.add(root);
		
		root.add(dockable2, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
		root.getRight().add(dockable3, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
		root.getRight().getLeft().add(dockable4, DockingRoot.VERTICAL, DockingRoot.RIGHT);
		
		
		frame.add(dockingCanvas);
		

		
		
//		panel1.setBackground(Color.GRAY);
//		panel2.setBackground(Color.LIGHT_GRAY);
//		
//		panel1.addMouseListener(new SimpleDocking());
//		panel1.add(button1);
//		
//		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, dockable.getPanel());
//		split.setDividerLocation(frame.getWidth() / 2);
//		//split.setLayout(new GridLayout(1, 1));
//		
//		frame.add(split);
		
		frame.setVisible(true);
		
		root.printTree("");
		

	}

}