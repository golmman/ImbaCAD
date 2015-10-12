package imbacad.gui;

import java.awt.Window;

import javax.swing.JFrame;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;

public class Modeling extends JFrame implements GLEventListener {

	private static final long serialVersionUID = -2636383013866025654L;
	
	private GLWindow glWindow;
	
	public Modeling(Window parent) {
		super("Title");
		
		GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL3));
		caps.setBackgroundOpaque(false);
		
		
		
		//glWindow = GLWindow.create(this);
		
		glWindow.setTitle("ImbaCAD");
		glWindow.setSize(800, 600);
		glWindow.setUndecorated(false);
		glWindow.setPointerVisible(true);
		glWindow.setRealized(true);
		glWindow.setVisible(true);
		
		
		
		//glWindow.addKeyListener(icad);
		//glWindow.addMouseListener(icad);
		//glWindow.addGLEventListener(icad);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		
	}

}
