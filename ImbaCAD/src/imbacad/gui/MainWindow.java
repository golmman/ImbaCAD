package imbacad.gui;

import imbacad.ImbaCAD;
import imbacad.Mesh;
import imbacad.util.Vec3;

import javax.swing.JFrame;

import com.jogamp.opengl.util.Animator;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 280678482530286275L;
	
	// vertices and indices for test meshes
	private float testVertices[] = {
		 0.5f,  0.5f, -0.5f,  1.0f,  0.0f,
		-0.5f,  0.5f, -0.5f,  0.0f,  0.0f, 
		-0.5f, -0.5f, -0.5f,  0.0f,  1.0f, 
		 0.5f, -0.5f, -0.5f,  1.0f,  1.0f, 
	};
	private int[] testIndices = {
		0, 1, 2, 0, 2, 3
	};
	
	private Animator animator = new Animator();
	
	public MainWindow(String title) {
		super(title);
		
		// add test meshes
		Mesh mesh1 = new Mesh("test2.jpg", testVertices, testIndices);
		Mesh mesh2 = new Mesh("test.bmp", testVertices, testIndices);
		mesh2.setPosition(new Vec3(0.5f, -1.5f, 0.0f));
		ImbaCAD.meshes.add(mesh1);
		ImbaCAD.meshes.add(mesh2);
		
		animator.start();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(30, 30, 200, 100);
		
		this.setVisible(true);
		
		
		new ModelingWindow(this, animator).setLocation(100, 100);
		new ModelingWindow(this, animator).setLocation(500, 200);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
