package imbacad.gui;

import java.awt.GridLayout;

import imbacad.ImbaCAD;
import imbacad.Mesh;
import imbacad.gui.docking.Dockable;
import imbacad.gui.docking.DockingCanvas;
import imbacad.gui.docking.DockingRoot;
import imbacad.util.Vec3;

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
		
		animator.start();
		
		this.setLayout(new GridLayout(1, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(30, 30, 800, 400);
		
		
		
//		CControl dockingControl = new CControl(this);
//		this.add(dockingControl.getContentArea());
//		
//		SingleCDockable modeling1 = new DefaultSingleCDockable("Modeling1", "Modeling1", new ModelingWindow(animator));
//		SingleCDockable modeling2 = new DefaultSingleCDockable("Modeling2", "Modeling2", new ModelingWindow(animator));
//		
//		dockingControl.addDockable(modeling1);
//		dockingControl.addDockable(modeling2);
//		
//		modeling1.setLocation(CLocation.base().normalWest(0.4));
//		modeling1.setVisible(true);
//
//		modeling2.setLocation(CLocation.base().normalEast(0.3));
//		modeling2.setVisible(true);
		
		
		DockingCanvas dockingCanvas = new DockingCanvas(this, false);
		
		
		Dockable dockable1 = new Dockable(this, new JLabel("dockable1"), "dockable1");
		Dockable dockable2 = new Dockable(this, new JLabel("dockable2"), "dockable2");
		Dockable dockable3 = new Dockable(this, new JLabel("dockable3"), "dockable3");
		Dockable dockable4 = new Dockable(this, new JLabel("dockable4"), "dockable4");
		
		dockable1.getContentPane().setLayout(new GridLayout(1, 1));
		dockable1.getContentPane().add(new ModelingWindow(animator));
		
		dockable2.getContentPane().setLayout(new GridLayout(1, 1));
		dockable2.getContentPane().add(new ModelingWindow(animator));
		
		dockable3.getContentPane().setLayout(new GridLayout(1, 1));
		dockable3.getContentPane().add(new ModelingWindow(animator));
		
		dockable4.getContentPane().setLayout(new GridLayout(1, 1));
		dockable4.getContentPane().add(new JButton("Miau"));
		
		
		DockingRoot root = new DockingRoot(dockable1);
		
		dockingCanvas.add(root);
		
		root.add(dockable2, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
		root.getRight().add(dockable3, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
		root.getRight().getLeft().add(dockable4, DockingRoot.VERTICAL, DockingRoot.RIGHT);
		
		
		this.add(dockingCanvas);
		
		
		this.setVisible(true);
		
		
		//new ModelingWindow(this, animator).setLocation(100, 100);
		//new ModelingWindow(this, animator).setLocation(500, 200);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
