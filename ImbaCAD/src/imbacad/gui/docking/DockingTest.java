package imbacad.gui.docking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class DockingTest implements MouseListener {
	
	
	private static JPanel panel1 = new JPanel();
	private static JPanel panel2 = new JPanel();
	
	private static JButton button1 = new JButton("Button1");
	private static JButton button2 = new JButton("Button2");
	
	
	
	
	
	public DockingTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Toolkit.getDefaultToolkit().setDynamicLayout(false);
		
		
		JFrame frame = new JFrame("SimpleDocking");
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
		
		frame.add(dockingCanvas);
		
		root.add(dockable2, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
		root.getRight().add(dockable3, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
		root.getRight().getLeft().add(dockable4, DockingRoot.VERTICAL, DockingRoot.RIGHT);
		
		
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

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println("enter!");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("exit!");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}