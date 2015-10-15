package imbacad.event;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

public abstract class MKEvents implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	protected int mouseDx;
	protected int mouseDy;
	protected int mouseX;
	protected int mouseY;
	protected boolean[] keys = new boolean[256]; 
	protected boolean[] buttons = new boolean[32];
	
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
	
	// TODO: Remove necessity of these methods
	public void setMouseDx(int mouseDx) {
		this.mouseDx = mouseDx;
	}
	public void setMouseDy(int mouseDy) {
		this.mouseDy = mouseDy;
	}
	
}
