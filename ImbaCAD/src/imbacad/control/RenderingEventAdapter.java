package imbacad.control;

import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;

import com.jogamp.opengl.awt.GLJPanel;

/**
 * Stores all basic input events for a GLJPanel
 * @author Dirk Kretschmann
 *
 */
public class RenderingEventAdapter implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener, FocusListener {

	protected int mouseDx = 0;
	protected int mouseDy = 0;
	protected int mouseX = 0;
	protected int mouseY = 0;
	protected int mouseW = 0;
	protected int mouseH = 0;
	protected int mouseWheel = 0;
	protected boolean[] keys = new boolean[256]; 
	protected boolean[] buttons = new boolean[32];
	
	
	private GLJPanel keyDownInPanel = null;
	private int keysPressed = 0;
	
	// Thread which checks for key down events.
	// In case there are any it calls display of the corresponding GLJPanel.
	private Thread keyDownThread = new Thread() {
		
		@Override
		public void run() {
			
			while (true) {
				if (keyDownInPanel == null) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					
					keyDownInPanel.display();
					
					Thread.yield();
					
//					try {
//						Thread.sleep(1);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				}
			}
			
		}
	};
	
	/**
	 * 
	 */
	public RenderingEventAdapter() {
		keyDownThread.start();
	}
	
	
	
	private void updateRenderer(ComponentEvent e) {
		if (e.getSource() instanceof GLJPanel) {
			GLJPanel p = (GLJPanel)e.getSource();
			mouseW = p.getWidth();
			mouseH = p.getHeight();
			p.requestFocusInWindow();
			p.display();
		}
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (!keys[e.getKeyCode()]) keysPressed++;
		
		if (e.getComponent() instanceof GLJPanel) {
			keyDownInPanel = (GLJPanel)e.getComponent();
		}
		
		keys[e.getKeyCode()] = true;

	}


	@Override
	public void keyReleased(KeyEvent e) {
		keysPressed--;
		
		if (e.getComponent() instanceof GLJPanel && keysPressed <= 0) {
			keyDownInPanel = null;
		}
		
		keys[e.getKeyCode()] = false;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
//		if (e.getSource() instanceof GLJPanel) {
//			((GLJPanel)e.getSource()).requestFocusInWindow();
//		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		buttons[e.getButton()] = true;
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton()] = false;
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDx = e.getX() - mouseX;
		mouseDy = e.getY() - mouseY;
		mouseX = e.getX();
		mouseY = e.getY();
		
		updateRenderer(e);
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		mouseDx = e.getX() - mouseX;
		mouseDy = e.getY() - mouseY;
		mouseX = e.getX();
		mouseY = e.getY();
		
		updateRenderer(e);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheel = e.getWheelRotation();
		
		updateRenderer(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
	public int getMouseDx() {
		return mouseDx;
	}
	public int getMouseDy() {
		return mouseDy;
	}
	public int getMouseX() {
		return mouseX;
	}
	public int getMouseY() {
		return mouseY;
	}
	public boolean getButton(int button) {
		return buttons[button];
	}
	public boolean getKey(int key) {
		return keys[key];
	}
	public void reset() {
		mouseDx = 0;
		mouseDy = 0;
		mouseWheel = 0;
	}

	@Override
	public void focusGained(FocusEvent e) {}


	@Override
	public void focusLost(FocusEvent e) {
		keyDownInPanel = null;
		keysPressed = 0;
		Arrays.fill(keys, false);
	}


	public int getMouseWheel() {
		return mouseWheel;
	}


	public int getMouseW() {
		return mouseW;
	}


	public int getMouseH() {
		return mouseH;
	}
	
}
