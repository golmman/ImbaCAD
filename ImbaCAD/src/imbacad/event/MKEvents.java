package imbacad.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public abstract class MKEvents implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	protected int mouseDx;
	protected int mouseDy;
	protected int mouseX;
	protected int mouseY;
	protected int mouseWheel;
	protected boolean[] keys = new boolean[256]; 
	protected boolean[] buttons = new boolean[32];
	
	
	
	/**
	 * Uses incoming events to act as specified
	 */
	public abstract void process();
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}


	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
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
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		mouseDx = e.getX() - mouseX;
		mouseDy = e.getY() - mouseY;
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheel = e.getWheelRotation();
	}
	
	
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
	
}
