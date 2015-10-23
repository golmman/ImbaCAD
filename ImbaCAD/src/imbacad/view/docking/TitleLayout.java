package imbacad.view.docking;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * Sets Layout to the title panel of a Dockable.
 * @author Dirk Kretschmann
 *
 */
public class TitleLayout implements LayoutManager {
	
	private static final int  BORDER_GAP = 5;
	private static final int  BUTTON_GAP = 3;
	
	public TitleLayout() {}



	@Override
	public void layoutContainer(Container container) {
		Insets insets = container.getInsets();
		int px = insets.left;
		int py = insets.top;
		int pw = container.getWidth() - (insets.right + insets.left);
		int ph = container.getHeight() - (insets.bottom + insets.top);
		int pr = px + pw;
		@SuppressWarnings("unused")
		int pb = py + ph;
		
		// title
		Component comp = container.getComponent(0);
		int cw = comp.getPreferredSize().width;
		int ch = comp.getPreferredSize().height;
		comp.setBounds(px + BORDER_GAP, py + (ph - ch) / 2 , cw, ch);
		
		// close
		comp = container.getComponent(3);
		cw = comp.getPreferredSize().width;
		ch = comp.getPreferredSize().height;
		int x = pr - cw - BORDER_GAP;
		comp.setBounds(x, py + (ph - ch) / 2 , cw, ch);
		
		// maximize
		comp = container.getComponent(2);
		cw = comp.getPreferredSize().width;
		ch = comp.getPreferredSize().height;
		x -= (cw + BUTTON_GAP);
		comp.setBounds(x, py + (ph - ch) / 2 , cw, ch);
		
		// minimize
		comp = container.getComponent(1);
		cw = comp.getPreferredSize().width;
		ch = comp.getPreferredSize().height;
		x -= (cw + BUTTON_GAP);
		comp.setBounds(x, py + (ph - ch) / 2 , cw, ch);
	}

	@Override
	public Dimension minimumLayoutSize(Container container) {
		int w = 0;
		int h = 0;
		
		for (Component c : container.getComponents()) {
			w += c.getPreferredSize().width;
			
			if (c.getPreferredSize().height > h) {
				h = c.getPreferredSize().height;
			}
		}
		return new Dimension(w + 3 * BUTTON_GAP + 2 * BORDER_GAP, h);
	}

	@Override
	public Dimension preferredLayoutSize(Container container) {
		return this.minimumLayoutSize(container);
	}

	
	@Override
	public void addLayoutComponent(String arg0, Component arg1) {}
	
	@Override
	public void removeLayoutComponent(Component arg0) {}

}
