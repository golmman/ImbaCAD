package imbacad.view;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.io.File;

import imbacad.ImbaCAD;
import imbacad.model.Light;
import imbacad.model.Vec3;
import imbacad.model.mesh.TextureMesh;
import imbacad.model.mesh.primitive.Line;
import imbacad.model.mesh.primitive.PrimitiveArray;
import imbacad.model.mesh.primitive.PrimitiveID;
import imbacad.model.mesh.primitive.Triangle;
import imbacad.model.mesh.vertex.ColorVertex;
import imbacad.model.mesh.vertex.TextureVertex;
import imbacad.model.mesh.vertex.VertexArray;
import imbacad.model.mesh.ColorMesh;
import imbacad.model.mesh.Mesh2D;
import imbacad.model.mesh.Plaster;
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
	public static TextureVertex[] testVertices = {
		new TextureVertex(0.5f,  0.5f, -0.5f,  	1.0f,  0.0f, 	0.0f, 0.0f, 1.0f), 
		new TextureVertex(-0.5f,  0.5f, -0.5f,  	0.0f,  0.0f, 	0.0f, 0.0f, 1.0f), 
		new TextureVertex(-0.5f, -0.5f, -0.5f,  	0.0f,  1.0f, 	0.0f, 0.0f, 1.0f), 
		new TextureVertex( 0.5f, -0.5f, -0.5f,  	1.0f,  1.0f, 	0.0f, 0.0f, 1.0f)
	};
	public static Triangle[] testIndices = {
		new Triangle(0, 1, 2, new PrimitiveID()), 
		new Triangle(0, 2, 3, new PrimitiveID())
	};
	
	// 
	private static TextureVertex[] houseVerticesTexture = {
		new TextureVertex( 0.5f,  0.5f, -0.5f,  	1.0f,  0.0f,	0.57735026f, 0.57735026f, -0.57735026f),	// bottom
		new TextureVertex(-0.5f,  0.5f, -0.5f,  	0.0f,  0.0f, 	-0.57735026f, 0.57735026f, -0.57735026f),
		new TextureVertex(-0.5f, -0.5f, -0.5f,  	0.0f,  1.0f, 	-0.57735026f, -0.57735026f, -0.57735026f),
		new TextureVertex( 0.5f, -0.5f, -0.5f,  	1.0f,  1.0f, 	0.57735026f, -0.57735026f, -0.57735026f),
		 
		new TextureVertex( 0.5f,  0.5f,  0.5f,  	1.0f,  0.0f,	0.6906652f, 0.6113837f, 0.38625336f),	// top
		new TextureVertex(-0.5f,  0.5f,  0.5f,  	0.0f,  0.0f, 	-0.6906652f, 0.6113837f, 0.38625336f),
		new TextureVertex(-0.5f, -0.5f,  0.5f,  	0.0f,  1.0f, 	-0.6906652f, -0.6113837f, 0.38625336f),
		new TextureVertex( 0.5f, -0.5f,  0.5f,  	1.0f,  1.0f, 	0.6906652f, -0.6113837f, 0.38625336f),
		 
		new TextureVertex( 0.3f,  0.0f,  1.0f,  	0.0f,  0.0f,	0.46133813f, 0.0f, 0.8872245f),	// roof
		new TextureVertex(-0.3f,  0.0f,  1.0f,  	0.0f,  0.0f,	-0.46133813f, 0.0f, 0.8872245f)
	};
	private static ColorVertex[] houseVerticesColor = {
			new ColorVertex( 0.5f,  0.5f, -0.5f,  	1.0f, 0.0f, 0.0f, 1.0f),	// bottom
			new ColorVertex(-0.5f,  0.5f, -0.5f,  	1.0f, 0.0f, 0.0f, 1.0f),
			new ColorVertex(-0.5f, -0.5f, -0.5f,  	1.0f, 0.0f, 0.0f, 1.0f),
			new ColorVertex( 0.5f, -0.5f, -0.5f,  	1.0f, 0.0f, 0.0f, 1.0f),
			 
			new ColorVertex( 0.5f,  0.5f,  0.5f,  	1.0f, 0.0f, 0.0f, 1.0f),	// top
			new ColorVertex(-0.5f,  0.5f,  0.5f,  	1.0f, 0.0f, 0.0f, 1.0f),
			new ColorVertex(-0.5f, -0.5f,  0.5f,  	1.0f, 0.0f, 0.0f, 1.0f),
			new ColorVertex( 0.5f, -0.5f,  0.5f,  	1.0f, 0.0f, 0.0f, 1.0f),
			 
			new ColorVertex( 0.3f,  0.0f,  1.0f,  	0.0f, 1.0f, 0.0f, 1.0f),	// roof
			new ColorVertex(-0.3f,  0.0f,  1.0f,  	0.0f, 1.0f, 0.0f, 1.0f)
		};
	public static Triangle[] houseIndices = {
		new Triangle(0, 3, 2, new PrimitiveID()), new Triangle(0, 2, 1, new PrimitiveID()), 	// bottom
		new Triangle(0, 4, 7, new PrimitiveID()), new Triangle(0, 7, 3, new PrimitiveID()), 	// front (+x)
		new Triangle(1, 2, 6, new PrimitiveID()), new Triangle(1, 6, 5, new PrimitiveID()), 	// back (-x)
		new Triangle(0, 1, 5, new PrimitiveID()), new Triangle(0, 5, 4, new PrimitiveID()), 	// right (+y)
		new Triangle(2, 3, 7, new PrimitiveID()), new Triangle(2, 7, 6, new PrimitiveID()),		// left (-y)
		
		new Triangle(4, 8, 7, new PrimitiveID()), 			// front gable
		new Triangle(5, 6, 9, new PrimitiveID()), 			// back gable
		
		new Triangle(4, 5, 9, new PrimitiveID()), new Triangle(4, 9, 8, new PrimitiveID()), 	// right roof side
		new Triangle(6, 7, 8, new PrimitiveID()), new Triangle(6, 8, 9, new PrimitiveID())		// left roof side
	};
	
	public static Plaster[] testPlasters = {
		new Plaster(1.0f, 1.0f, 1.0f, 0.25f, 1.0f, 0.0f),
		new Plaster(1.0f, 0.25f, 1.0f, -0.25f, 1.0f, 0.8f),
		new Plaster(1.0f, -0.25f, 1.0f, -1.0f, 1.0f, 0.0f),
		//new Plaster(1.0f, -1.0f, 
	};

	
	public static ColorVertex[] colorMeshVertices = {
		new ColorVertex(0.0f, 0.0f, 0.0f,	1.0f, 0.0f, 0.0f, 1.0f),
		new ColorVertex(0.2f, 0.8f, 0.0f,	1.0f, 0.0f, 0.0f, 1.0f),
		new ColorVertex(1.0f, 1.0f, 0.0f,	1.0f, 0.0f, 0.0f, 1.0f),
		new ColorVertex(0.5f, 0.5f, 0.0f,	1.0f, 0.0f, 0.0f, 1.0f),
		new ColorVertex(2.0f, 0.0f, 0.0f,	1.0f, 0.0f, 0.0f, 1.0f),
		new ColorVertex(0.0f, 0.0f, 0.0f,	1.0f, 0.0f, 0.0f, 1.0f)
	};
	public static Line[] colorMeshIndices = {
		new Line(0, 1, new PrimitiveID()),
		new Line(2, 3, new PrimitiveID()), 
		new Line(4, 5, new PrimitiveID()), 
	};
	
	private Animator animator = new Animator();
	
	public MainWindow(String title) {
		super(title);
		
		// add test meshes
		TextureMesh mesh1 = TextureMesh.createMesh(
				new File("test2.jpg"), 
				new VertexArray<TextureVertex>(testVertices), 
				new PrimitiveArray<Triangle>(testIndices), 
				"flippers");
		
		TextureMesh mesh2 = TextureMesh.createMesh(
				new File("test.bmp"), 
				new VertexArray<TextureVertex>(testVertices), 
				new PrimitiveArray<Triangle>(testIndices),
				"test");
		mesh2.setPosition(new Vec3(-0.5f, 1.5f, 0.0f));
		
		TextureMesh mesh3 = TextureMesh.createFlatShadedMesh(
				new File("white.bmp"), 
				new VertexArray<TextureVertex>(houseVerticesTexture), 
				new PrimitiveArray<Triangle>(houseIndices), 
				"testHouse");
		mesh3.setPosition(new Vec3(2.5f, -0.5f, 0.0f));
		mesh3.setRotation(new Vec3(0.0f, 0.0f, 0.2f));
		
		Mesh2D mesh2D = new Mesh2D(testPlasters, "doorway");
		TextureMesh mesh4 = mesh2D.to3D();
		
		ColorMesh<Line> mesh5 = ColorMesh.createColorGradientMesh(
				GL.GL_LINES, 
				new VertexArray<ColorVertex>(colorMeshVertices), 
				new PrimitiveArray<Line>(colorMeshIndices), 
				"LINES!", Line.COPY);
		
		ColorMesh<Triangle> mesh6 = ColorMesh.createColorGradientMesh(
				GL.GL_TRIANGLES, 
				new VertexArray<ColorVertex>(houseVerticesColor), 
				new PrimitiveArray<Triangle>(houseIndices), 
				"ColorHouse", Triangle.COPY);
		mesh6.setPosition(new Vec3(2.5f, -2.5f, 0.0f));
		mesh6.setRotation(new Vec3(0.0f, 0.0f, 0.0f));
		
		ImbaCAD.meshes.add(mesh1);
		ImbaCAD.meshes.add(mesh2);
		ImbaCAD.meshes.add(mesh3);
		ImbaCAD.meshes.add(mesh4);
		ImbaCAD.meshes.add(mesh5);
		ImbaCAD.meshes.add(mesh6);
		
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
