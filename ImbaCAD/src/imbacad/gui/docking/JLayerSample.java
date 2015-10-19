package imbacad.gui.docking;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;

public class JLayerSample {

    private static JLayer<JComponent> createLayer() {
        // This custom layerUI will fill the layer with translucent green
        // and print out all mouseMotion events generated within its borders
        LayerUI<JComponent> layerUI = new LayerUI<JComponent>() {

            public void paint(Graphics g, JComponent c) {
                // paint the layer as is
                super.paint(g, c);
                // fill it with the translucent green
                g.setColor(new Color(0, 128, 0, 128));
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }

            public void installUI(JComponent c) {
                super.installUI(c);
                // enable mouse motion events for the layer's subcomponents
                ((JLayer) c).setLayerEventMask(AWTEvent.MOUSE_MOTION_EVENT_MASK);
            }

            public void uninstallUI(JComponent c) {
                super.uninstallUI(c);
                // reset the layer event mask
                ((JLayer) c).setLayerEventMask(0);
            }

            // overridden method which catches MouseMotion events
            public void eventDispatched(AWTEvent e, JLayer<? extends JComponent> l) {
                System.out.println("AWTEvent detected: " + e);
            }
        };
        // create a component to be decorated with the layer
        JPanel panel = new JPanel();
        panel.add(new JButton("JButton"));

        // create the layer for the panel using our custom layerUI
        return new JLayer<JComponent>(panel, layerUI);
    }

    private static void createAndShowGUI() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // work with the layer as with any other Swing component
        frame.add(createLayer());

        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
