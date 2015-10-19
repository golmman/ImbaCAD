package imbacad.gui.docking.dnd;


import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DNDTest implements DropListener {
	
	private static JPanel p1 = new JPanel();
	private static JPanel p2 = new JPanel();
	private static JPanel p3 = new JPanel();
	private static JPanel p4 = new JPanel();
	
	public DNDTest() {
		
	}

	public static void main(String[] args) {
		p1.setBackground(Color.RED);
		p2.setBackground(Color.GREEN);
		p3.setBackground(Color.BLUE);
		p4.setBackground(Color.GRAY);
		
		// create drag and drop listener
		DNDTest listener = new DNDTest();
		
		// add dnd support to the panels
		DND.supportDrag(p1, null); DND.supportDrop(p1, listener);
		DND.supportDrag(p2, null); DND.supportDrop(p2, listener);
		DND.supportDrag(p3, null); DND.supportDrop(p3, listener);
		DND.supportDrag(p4, null); DND.supportDrop(p4, listener);
		
		// create frame
		JFrame frame = new JFrame("Main");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 600, 300);
		frame.setLayout(new GridLayout(1, 3));
		
		frame.add(p1);
		frame.add(p2);
		frame.add(p3);

		frame.setVisible(true);
		
		// create child dialog
		JDialog dialog = new JDialog(frame, "Child");
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setBounds(150, 450, 200, 200);
		dialog.setLayout(new GridLayout(1, 1));
		
		dialog.add(p4);
		
		dialog.setVisible(true);
	}
	

	@Override
	public void dropped(DNDEvent e) {
		Color source = e.getDragSource().getBackground();
		Color target = e.getDropTarget().getBackground();
		System.out.println(source + " dropped " + target + " at " + e.getX() + " " + e.getY());
	}



	@Override
	public void hovering(DNDEvent e) {
		Color source = e.getDragSource().getBackground();
		Color target = e.getDropTarget().getBackground();
		System.out.println(source + " hovering " + target + " at " + e.getX() + " " + e.getY());
	}



	@Override
	public void entered(DNDEvent e) {
		Color source = e.getDragSource().getBackground();
		Color target = e.getDropTarget().getBackground();
		System.out.println(source + " entered " + target + " at " + e.getX() + " " + e.getY());
	}



	@Override
	public void exited(DNDEvent e) {
		Color source = e.getDragSource().getBackground();
		Color target = e.getDropTarget().getBackground();
		System.out.println(source + " exited " + target + " at " + e.getX() + " " + e.getY());
	}
	
	
}
