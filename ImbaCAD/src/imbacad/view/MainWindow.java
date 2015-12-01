package imbacad.view;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.io.File;

import imbacad.ImbaCAD;
import imbacad.model.Light;
import imbacad.model.Vec3;
import imbacad.model.Vec4;
import imbacad.model.mesh.TextureMesh;
import imbacad.model.mesh.ColorMesh;
import imbacad.model.mesh.Mesh2D;
import imbacad.model.mesh.Plaster;
import imbacad.model.mesh.VertexArray;
import imbacad.view.docking.Dockable;
import imbacad.view.docking.DockingCanvas;
import imbacad.view.docking.DockingRoot;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.util.Animator;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 280678482530286275L;
	
	// vertices and indices for test meshes
	public static float testVertices[] = {
		 0.5f,  0.5f, -0.5f,  	1.0f,  0.0f, 	0.0f, 0.0f, 1.0f, 
		-0.5f,  0.5f, -0.5f,  	0.0f,  0.0f, 	0.0f, 0.0f, 1.0f, 
		-0.5f, -0.5f, -0.5f,  	0.0f,  1.0f, 	0.0f, 0.0f, 1.0f, 
		 0.5f, -0.5f, -0.5f,  	1.0f,  1.0f, 	0.0f, 0.0f, 1.0f
	};
	public static int[] testIndices = {
		0, 1, 2, 0, 2, 3
	};
	
	// 
	private static float houseVertices[] = {
		 0.5f,  0.5f, -0.5f,  	1.0f,  0.0f,	0.57735026f, 0.57735026f, -0.57735026f,	// bottom
		-0.5f,  0.5f, -0.5f,  	0.0f,  0.0f, 	-0.57735026f, 0.57735026f, -0.57735026f,
		-0.5f, -0.5f, -0.5f,  	0.0f,  1.0f, 	-0.57735026f, -0.57735026f, -0.57735026f,
		 0.5f, -0.5f, -0.5f,  	1.0f,  1.0f, 	0.57735026f, -0.57735026f, -0.57735026f,
		 
		 0.5f,  0.5f,  0.5f,  	1.0f,  0.0f,	0.6906652f, 0.6113837f, 0.38625336f,	// top
		-0.5f,  0.5f,  0.5f,  	0.0f,  0.0f, 	-0.6906652f, 0.6113837f, 0.38625336f,
		-0.5f, -0.5f,  0.5f,  	0.0f,  1.0f, 	-0.6906652f, -0.6113837f, 0.38625336f,
		 0.5f, -0.5f,  0.5f,  	1.0f,  1.0f, 	0.6906652f, -0.6113837f, 0.38625336f,
		 
		 0.3f,  0.0f,  1.0f,  	0.0f,  0.0f,	0.46133813f, 0.0f, 0.8872245f,	// roof
		-0.3f,  0.0f,  1.0f,  	0.0f,  0.0f,	-0.46133813f, 0.0f, 0.8872245f
	};
	public static int[] houseIndices = {
		0, 3, 2, 0, 2, 1, 	// bottom
		0, 4, 7, 0, 7, 3, 	// front (+x)
		1, 2, 6, 1, 6, 5, 	// back (-x)
		0, 1, 5, 0, 5, 4, 	// right (+y)
		2, 3, 7, 2, 7, 6,	// left (-y)
		
		4, 8, 7, 			// front gable
		5, 6, 9, 			// back gable
		
		4, 5, 9, 4, 9, 8, 	// right roof side
		6, 7, 8, 6, 8, 9	// left roof side
	};
	
	public static Plaster[] testPlasters = {
		new Plaster(1.0f, 1.0f, 1.0f, 0.25f, 1.0f, 0.0f),
		new Plaster(1.0f, 0.25f, 1.0f, -0.25f, 1.0f, 0.8f),
		new Plaster(1.0f, -0.25f, 1.0f, -1.0f, 1.0f, 0.0f),
		//new Plaster(1.0f, -1.0f, 
	};

	
	public static Vec3[] colorMeshTest = {
		new Vec3(0.0f, 0.0f, 0.0f),
		new Vec3(0.2f, 0.8f, 0.0f),
		new Vec3(1.0f, 1.0f, 0.0f),
		new Vec3(0.5f, 0.5f, 0.0f),
		new Vec3(2.0f, 0.0f, 0.0f),
		new Vec3(0.0f, 0.0f, 0.0f),
	};
	
	private Animator animator = new Animator();
	
	public MainWindow(String title) {
		super(title);
		
		// add test meshes
		TextureMesh mesh1 = TextureMesh.createMesh(new File("test2.jpg"), new VertexArray(testVertices), testIndices, "flippers");
		
		TextureMesh mesh2 = TextureMesh.createMesh(new File("test.bmp"), new VertexArray(testVertices), testIndices, "test");
		mesh2.setPosition(new Vec3(-0.5f, 1.5f, 0.0f));
		
		TextureMesh mesh3 = TextureMesh.createFlatShadedMesh(new File("white.bmp"), new VertexArray(houseVertices), houseIndices, "testHouse");
		mesh3.setPosition(new Vec3(2.5f, -0.5f, 0.0f));
		mesh3.setRotation(new Vec3(0.0f, 0.0f, 0.2f));
		
		Mesh2D mesh2D = new Mesh2D(testPlasters, "doorway");
		TextureMesh mesh4 = mesh2D.to3D();
		
		ColorMesh mesh5 = ColorMesh.createColorGradientMesh(colorMeshTest, new Vec4(1.0f,  0.0f, 0.0f, 0.0f), GL.GL_LINES, "LINES!");
		
		ImbaCAD.meshes.add(mesh1);
		ImbaCAD.meshes.add(mesh2);
		ImbaCAD.meshes.add(mesh3);
		ImbaCAD.meshes.add(mesh4);
		ImbaCAD.meshes.add(mesh5);
		
		// add directional lights
		Vec3 dirLightPos0 = new Vec3(1.0f, 1.0f, 1.0f).normalised();
		Vec3 dirLightPos1 = new Vec3(-1.0f, 1.0f, 1.0f).normalised();
		Vec3 dirLightPos2 = new Vec3(0.0f, -1.0f, 1.0f).normalised();
		float dirAmbient = 0.05f;
		
		// light intensity for a surface facing the sky should be 1.0f
		Vec3 dirLightColor = new Vec3((1.0f - dirAmbient) / (dirLightPos0.getZ() + dirLightPos1.getZ() + dirLightPos2.getZ()));
		
		Light directional0 = new Light(new File("shader/texture.fsh"), true, dirLightPos0, dirLightColor, 0.0f, dirAmbient);
		Light directional1 = new Light(new File("shader/texture.fsh"), true, dirLightPos1, dirLightColor, 0.0f, dirAmbient);
		Light directional2 = new Light(new File("shader/texture.fsh"), true, dirLightPos2, dirLightColor, 0.0f, dirAmbient);
		
		ImbaCAD.lights.add(directional0);
		ImbaCAD.lights.add(directional1);
		ImbaCAD.lights.add(directional2);
		
		//Light light2 = new Light(new File("shader/texture.fsh"), false, new Vec3(6.0f, 0.0f, 5.0f), new Vec3(1.0f, 0.0f, 0.0f), 0.0f, 0.1f);
		//ImbaCAD.lights.add(light2);
		
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


}
