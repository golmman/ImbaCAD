package imbacad.view;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.jogamp.opengl.GL;

import imbacad.ImbaCAD;
import imbacad.model.Light;
import imbacad.model.Vec3;
import imbacad.model.camera.XYPanUpdater;
import imbacad.model.dwg.DWG;
import imbacad.model.mesh.ColorMesh;
import imbacad.model.mesh.Mesh2D;
import imbacad.model.mesh.Plaster;
import imbacad.model.mesh.SelectionMesh;
import imbacad.model.mesh.TextureMesh;
import imbacad.model.mesh.primitive.Line;
import imbacad.model.mesh.primitive.PrimitiveArray;
import imbacad.model.mesh.primitive.Triangle;
import imbacad.model.mesh.vertex.ColorVertex;
import imbacad.model.mesh.vertex.TextureVertex;
import imbacad.model.mesh.vertex.VertexArray;
import imbacad.view.docking.Dockable;
import imbacad.view.docking.DockableLayer;
import imbacad.view.docking.DockingCanvas;
import imbacad.view.docking.DockingRoot;

public class MainWindow extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 280678482530286275L;
	
	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu menuFile = new JMenu("File");
	private JMenuItem menuDebug = new JMenuItem("debug");
	private JMenuItem menuImport = new JMenuItem("Import...");
	private JMenuItem menuExit = new JMenuItem("Exit");
	
	
	private DockingCanvas dockingCanvas;
	private DockingRoot root;
	
	
	
	// vertices and indices for test meshes
	public static TextureVertex[] testVertices = {
		new TextureVertex( 0.5f,  0.5f, -0.5f,  	1.0f,  0.0f, 	0.0f, 0.0f, 1.0f,	0.0f, 0.0f, 0.0f, 0.0f), 
		new TextureVertex(-0.5f,  0.5f, -0.5f,  	0.0f,  0.0f, 	0.0f, 0.0f, 1.0f,	0.0f, 0.0f, 0.0f, 0.0f), 
		new TextureVertex(-0.5f, -0.5f, -0.5f,  	0.0f,  1.0f, 	0.0f, 0.0f, 1.0f,	0.0f, 0.0f, 0.0f, 0.0f), 
		new TextureVertex( 0.5f, -0.5f, -0.5f,  	1.0f,  1.0f, 	0.0f, 0.0f, 1.0f,	0.0f, 0.0f, 0.0f, 0.0f)
	};
	public static Triangle[] testIndices = {
		new Triangle(0, 1, 2, 0L), 
		new Triangle(0, 2, 3, 1L)
	};
	
	// 
	private static TextureVertex[] houseVerticesTexture = {
		new TextureVertex( 0.5f,  0.5f, -0.5f,  	1.0f,  0.0f,	 0.57735026f,  0.57735026f, -0.57735026f,	0.0f, 0.0f, 0.0f, 0.0f),	// bottom
		new TextureVertex(-0.5f,  0.5f, -0.5f,  	0.0f,  0.0f, 	-0.57735026f,  0.57735026f, -0.57735026f,	0.0f, 0.0f, 0.0f, 0.0f),
		new TextureVertex(-0.5f, -0.5f, -0.5f,  	0.0f,  1.0f, 	-0.57735026f, -0.57735026f, -0.57735026f,	0.0f, 0.0f, 0.0f, 0.0f),
		new TextureVertex( 0.5f, -0.5f, -0.5f,  	1.0f,  1.0f, 	 0.57735026f, -0.57735026f, -0.57735026f,	0.0f, 0.0f, 0.0f, 0.0f),
		 
		new TextureVertex( 0.5f,  0.5f,  0.5f,  	1.0f,  0.0f,	 0.6906652f,  0.6113837f, 0.38625336f,		0.0f, 0.0f, 0.0f, 0.0f),	// top
		new TextureVertex(-0.5f,  0.5f,  0.5f,  	0.0f,  0.0f, 	-0.6906652f,  0.6113837f, 0.38625336f,		0.0f, 0.0f, 0.0f, 0.0f),
		new TextureVertex(-0.5f, -0.5f,  0.5f,  	0.0f,  1.0f, 	-0.6906652f, -0.6113837f, 0.38625336f,		0.0f, 0.0f, 0.0f, 0.0f),
		new TextureVertex( 0.5f, -0.5f,  0.5f,  	1.0f,  1.0f, 	 0.6906652f, -0.6113837f, 0.38625336f,		0.0f, 0.0f, 0.0f, 0.0f),
		 
		new TextureVertex( 0.3f,  0.0f,  1.0f,  	0.0f,  0.0f,	 0.46133813f, 0.0f, 0.8872245f,		0.0f, 0.0f, 0.0f, 0.0f),	// roof
		new TextureVertex(-0.3f,  0.0f,  1.0f,  	0.0f,  0.0f,	-0.46133813f, 0.0f, 0.8872245f,		0.0f, 0.0f, 0.0f, 0.0f)
	};
	private static ColorVertex[] houseVerticesColor = {
			new ColorVertex( 0.5f,  0.5f, -0.5f,  	0.0f, 0.0f, 1.0f, 1.0f),	// bottom
			new ColorVertex(-0.5f,  0.5f, -0.5f,  	0.0f, 1.0f, 0.0f, 1.0f),
			new ColorVertex(-0.5f, -0.5f, -0.5f,  	0.0f, 1.0f, 1.0f, 1.0f),
			new ColorVertex( 0.5f, -0.5f, -0.5f,  	1.0f, 0.0f, 0.0f, 1.0f),
			 
			new ColorVertex( 0.5f,  0.5f,  0.5f,  	1.0f, 0.0f, 1.0f, 1.0f),	// top
			new ColorVertex(-0.5f,  0.5f,  0.5f,  	1.0f, 1.0f, 0.0f, 1.0f),
			new ColorVertex(-0.5f, -0.5f,  0.5f,  	1.0f, 1.0f, 1.0f, 1.0f),
			new ColorVertex( 0.5f, -0.5f,  0.5f,  	0.0f, 0.0f, 0.0f, 1.0f),
			 
			new ColorVertex( 0.3f,  0.0f,  1.0f,  	0.5f, 0.5f, 0.5f, 1.0f),	// roof
			new ColorVertex(-0.3f,  0.0f,  1.0f,  	1.0f, 1.0f, 1.0f, 1.0f)
		};
	public static Triangle[] houseIndices = {
		new Triangle(0, 3, 2, 0L), new Triangle(0, 2, 1, 0L), 	// bottom
		new Triangle(0, 4, 7, 1L), new Triangle(0, 7, 3, 1L), 	// front (+x)
		new Triangle(1, 2, 6, 2L), new Triangle(1, 6, 5, 20L), 	// back (-x)
		new Triangle(0, 1, 5, 3L), new Triangle(0, 5, 4, 3L), 	// right (+y)
		new Triangle(2, 3, 7, 4L), new Triangle(2, 7, 6, 4L),	// left (-y)
		
		new Triangle(4, 8, 7, 5L), 								// front gable
		new Triangle(5, 6, 9, 6L), 								// back gable
		
		new Triangle(4, 5, 9, 7L), new Triangle(4, 9, 8, 7L), 	// right roof side
		new Triangle(6, 7, 8, 8L), new Triangle(6, 8, 9, 8L)	// left roof side
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
		new Line(0, 1, 0L),
		new Line(2, 3, 1L), 
		new Line(4, 5, 2L), 
	};
	
	
	public MainWindow(String title) {
		super(title);
		
		/*
		 * Test meshes
		 */
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
		
		ColorMesh<Line> mesh5 = ColorMesh.createFlatColorMesh(
				GL.GL_LINES, 
				new VertexArray<ColorVertex>(colorMeshVertices), 
				new PrimitiveArray<Line>(colorMeshIndices), 
				"LINES!");
		
//		ColorMesh<Triangle> mesh6 = ColorMesh.createFlatColorMesh(
//				GL.GL_TRIANGLES, 
//				new VertexArray<ColorVertex>(houseVerticesColor), 
//				new PrimitiveArray<Triangle>(houseIndices), 
//				"ColorHouse");
		
		SelectionMesh<Triangle> mesh6 = SelectionMesh.createSelectionMesh(mesh3);
//		mesh6.setPosition(new Vec3(2.5f, -2.5f, 0.0f));
//		mesh6.setRotation(new Vec3(0.0f, 0.0f, 0.0f));
		
		SelectionMesh<Line> mesh7 = SelectionMesh.createSelectionMesh(mesh5);
		
//		ColorMesh<Line> mesh8 = ColorMesh.createDWGMesh(new File(DWG.TEST_DIR + "ARC_GR_DA_3_009_2000.dwg"), "dwgtest");
//		mesh8.setPosition(new Vec3(2.5f, -2.5f, 0.0f));
		
		ImbaCAD.meshes.add(mesh1);
		ImbaCAD.meshes.add(mesh2);
		ImbaCAD.meshes.add(mesh3);
		ImbaCAD.meshes.add(mesh4);
		ImbaCAD.meshes.add(mesh5);
		ImbaCAD.meshes.add(mesh6);
		ImbaCAD.meshes.add(mesh7);
//		ImbaCAD.meshes.add(mesh8);
		
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


		/*
		 * Dockables
		 */
		dockingCanvas = new DockingCanvas(this, false);
		
		Dockable dockable1 = new Dockable(this, new JLabel("dockable1"), "dockable1");
//		Dockable dockable2 = new Dockable(this, new JLabel("dockable2"), "dockable2");
//		Dockable dockable3 = new Dockable(this, new JLabel("dockable3"), "dockable3");
//		Dockable dockable4 = new Dockable(this, new JLabel("dockable4"), "dockable4");
		
		dockable1.getContentPane().setLayout(new GridLayout(1, 1));
		dockable1.getContentPane().add(new ModelingPanel(dockable1));
		
//		dockable2.getContentPane().setLayout(new GridLayout(1, 1));
//		dockable2.getContentPane().add(new ModelingPanel(dockable2));
//		
//		dockable3.getContentPane().setLayout(new GridLayout(1, 1));
//		dockable3.getContentPane().add(new ModelingPanel(dockable3));
//		
//		dockable4.getContentPane().setLayout(new GridLayout(1, 1));
//		dockable4.getContentPane().add(new JButton("Miau"));
		
		root = new DockingRoot(dockable1);
		
		dockingCanvas.add(root);
		
//		root.add(dockable2, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
//		root.getRight().add(dockable3, DockingRoot.HORIZONTAL, DockingRoot.RIGHT);
//		root.getRight().getLeft().add(dockable4, DockingRoot.VERTICAL, DockingRoot.RIGHT);
		
		this.add(dockingCanvas);
		
		
		dockable1.setCursor(CustomCursor.createEraser(5, 5));
		
		
		
		// Menu
		this.setJMenuBar(menuBar);
		
		menuBar.add(menuFile);
		menuFile.add(menuDebug);
		menuFile.add(menuImport);
		menuFile.addSeparator();
		menuFile.add(menuExit);
		
		menuDebug.addActionListener(this);
		menuImport.addActionListener(this);
		menuExit.addActionListener(this);
		
		
		
		this.setVisible(true);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(menuExit)) {
			this.dispose();
			System.exit(0);
		} else if (e.getSource().equals(menuDebug)) {
			dockingCanvas.revalidate();
			dockingCanvas.repaint();
		} else if (e.getSource().equals(menuImport)) {
			
			// restore last directory
	    	StringBuilder sb = new StringBuilder();
	    	try {
	    		FileReader fr = new FileReader("config.txt");
	    		int c = 0;
	    		while ((c = fr.read()) != -1) {
	    			sb.append((char)c);
	    		}
				fr.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
	    	
	    	// open file chooser
		    JFileChooser chooser = new JFileChooser(sb.toString());
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("DWG files", "dwg");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(this);
		    
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	
		    	File file = chooser.getSelectedFile();	
		    	
		    	// creates meshes
		    	ColorMesh<Line> mesh = ColorMesh.createDWGMesh(file, file.getName());
				mesh.setPosition(new Vec3(-0.5f, -3.0f, 0.0f));
				ImbaCAD.meshes.add(mesh);
		    	
		    	SelectionMesh<Line> selMesh = SelectionMesh.createSelectionMesh(mesh);
		    	ImbaCAD.meshes.add(selMesh);
				
		    			
		    	Dockable dockable = new Dockable(this, new JLabel(file.getName()), file.getName());
		    	ModelingPanel modelingPanel = new ModelingPanel(dockable);
		    	
		    	modelingPanel.getRenderer().getCamera().setPosition(new Vec3(0.0f, -3.0f, 0.5f));
		    	modelingPanel.getRenderer().getCamera().setPolarAngle(3.14f);
		    	modelingPanel.getRenderer().getCamera().setAzimuthAngle(3.1415f / 2.0f);
		    	
		    	modelingPanel.setCameraUpdater(new XYPanUpdater());
		    	
		    	dockable.getContentPane().setLayout(new GridLayout(1, 1));
				dockable.getContentPane().add(modelingPanel);
				
				root.add(dockable, DockableLayer.DIRECTION_EAST);
				
				
				dockingCanvas.revalidate();
				dockingCanvas.repaint();

				
				// save directory
				try {
					FileWriter fw = new FileWriter("config.txt");
					fw.write(file.getAbsolutePath());
					fw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    }
		}
	}


}
