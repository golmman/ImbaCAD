package imbacad.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.kitfox.docking.DockingContent;
import com.kitfox.docking.DockingRegionRoot;

public class RavenDocking_Test {

	public RavenDocking_Test() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		DockingRegionRoot dockPanel = new DockingRegionRoot();
		
		JFrame frame = new JFrame("RavenDocking");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 600, 300);
		frame.setLayout(new GridLayout(1, 1));
		
		// TODO: getContentPane?
		frame.getContentPane().add(dockPanel);
		
		JPanel panel1 = new JPanel();
        DockingContent cont1 = new DockingContent("Panel1", "Panel1", panel1);
        dockPanel.getDockingRoot().addDockContent(cont1);
        
        JPanel panel2 = new JPanel();
        DockingContent cont2 = new DockingContent("Panel1", "Panel1", panel2);
        dockPanel.getDockingRoot().addDockContent(cont2);
		
		
		frame.setVisible(true);
	}

}
