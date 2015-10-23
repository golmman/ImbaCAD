package imbacad.view;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;

import imbacad.ImbaCAD;
import imbacad.model.Mesh;
import imbacad.model.Vec3;
import imbacad.view.docking.Dockable;
import imbacad.view.docking.DockingCanvas;
import imbacad.view.docking.DockingRoot;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jogamp.opengl.util.Animator;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 280678482530286275L;
	
	// vertices and indices for test meshes
	public static float testVertices[] = {
		 0.5f,  0.5f, -0.5f,  1.0f,  0.0f,
		-0.5f,  0.5f, -0.5f,  0.0f,  0.0f, 
		-0.5f, -0.5f, -0.5f,  0.0f,  1.0f, 
		 0.5f, -0.5f, -0.5f,  1.0f,  1.0f, 
	};
	public static int[] testIndices = {
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
		
		//animator.start();
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		
		this.setLayout(new GridLayout(1, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(0, 0, screenWidth, screenHeight);


		
		DockingCanvas dockingCanvas = new DockingCanvas(this, false);
		
		
		Dockable dockable1 = new Dockable(this, new JLabel("dockable1"), "dockable1");
		Dockable dockable2 = new Dockable(this, new JLabel("dockable2"), "dockable2");
		Dockable dockable3 = new Dockable(this, new JLabel("dockable3"), "dockable3");
		Dockable dockable4 = new Dockable(this, new JLabel("dockable4"), "dockable4");
		
		dockable1.getContentPane().setLayout(new GridLayout(1, 1));
		dockable1.getContentPane().add(new ModelingPanel(animator));
		
		dockable2.getContentPane().setLayout(new GridLayout(1, 1));
		dockable2.getContentPane().add(new ModelingPanel(animator));
		
		dockable3.getContentPane().setLayout(new GridLayout(1, 1));
		dockable3.getContentPane().add(new ModelingPanel(animator));
		
		dockable4.getContentPane().setLayout(new GridLayout(1, 1));
		dockable4.getContentPane().add(new JButton("Miau"));
		
		
		DockingRoot root = new DockingRoot(dockable1);
		
		dockingCanvas.add(root);
		
		root.add(dockable2, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
		root.getRight().add(dockable3, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
		root.getRight().getLeft().add(dockable4, DockingRoot.VERTICAL, DockingRoot.RIGHT);
		
		
		this.add(dockingCanvas);
		
		
		this.setVisible(true);
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
