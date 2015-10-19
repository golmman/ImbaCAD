package imbacad.gui.docking;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class FullLayout implements LayoutManager {

	public FullLayout() {}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		
	}

	@Override
	public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        int maxWidth = parent.getWidth() - (insets.left + insets.right);
        int maxHeight = parent.getHeight() - (insets.top + insets.bottom);
		
		for (Component c : parent.getComponents()) {
			c.setBounds(0, 0, maxWidth, maxHeight);
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
        Insets insets = parent.getInsets();
        int maxWidth = parent.getWidth() - (insets.left + insets.right);
        int maxHeight = parent.getHeight() - (insets.top + insets.bottom);
        
		return new Dimension(maxWidth, maxHeight);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
        Insets insets = parent.getInsets();
        int maxWidth = parent.getWidth() - (insets.left + insets.right);
        int maxHeight = parent.getHeight() - (insets.top + insets.bottom);
        
		return new Dimension(maxWidth, maxHeight);
	}

	@Override
	public void removeLayoutComponent(Component comp) {}

}
