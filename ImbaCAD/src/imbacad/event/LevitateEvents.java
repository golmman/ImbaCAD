package imbacad.event;


import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class LevitateEvents extends MKEvents {


	public LevitateEvents() {
		// TODO Auto-generated constructor stub
	}



	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}


	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
