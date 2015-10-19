package imbacad.gui.docking;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class LayerTest implements ComponentListener {
	private JFrame frame = new JFrame();
	private JLayeredPane lpane = new JLayeredPane();
	private JPanel panelBlue = new JPanel();
	private JPanel panelGreen = new JPanel();

	public LayerTest() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 500, 500);
		frame.setLayout(new GridLayout(1, 1));

		frame.add(lpane);
		
		lpane.setLayout(null);
		lpane.addComponentListener(this);
	
		
		panelBlue.setBackground(Color.BLUE);
		//panelBlue.setBounds(0, 0, 600, 400);
		panelBlue.setOpaque(true);

		panelGreen.setBackground(Color.GREEN);
		panelGreen.setBounds(200, 100, 100, 100);
		panelGreen.setOpaque(true);

		lpane.add(panelBlue, new Integer(0), 0);
		lpane.add(panelGreen, new Integer(1), 0);

		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new LayerTest();
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
		panelBlue.setBounds(0, 0, lpane.getWidth(),	lpane.getHeight());
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}