package imbacad;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test {
	
	private static JPanel p0 = new JPanel();
	private static JPanel p1 = new JPanel();
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(20, 20, 300, 300);
		frame.setLayout(new GridLayout(1, 2));
		frame.add(p0);
		
		JPanel p2 = new JPanel();
		p2.add(p1);
		frame.add(p2);
		
		JButton b = new JButton("!");
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p1.add(new JButton("?"));
				
				frame.revalidate();
				frame.repaint();
			}
		});
		p0.add(b);
		
		frame.setVisible(true);
	}

}
