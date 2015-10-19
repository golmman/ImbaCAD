package imbacad.gui;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileReader;
import java.io.IOException;

import imbacad.ImbaCAD;
import imbacad.Mesh;
import imbacad.event.LevitateEvents;
import imbacad.util.Glm;
import imbacad.util.Vec3;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

public class ModelingWindow extends JPanel implements GLEventListener {

	private static final long serialVersionUID = -2636383013866025654L;
	
	private LevitateEvents events;
	
	private Animator animator;
	
	private GLCanvas glCanvas;
	
	private static int width = 800;
	private static int height = 600;

	private int shaderProgram;
	private int vertexShader;
	private int fragmentShader;
	
	private int ModelLocation;
	private int ViewLocation;
	private int ProjectionLocation;
	
	
	// TODO: only for debug
	private float alpha = 0.0f;
	
	
	
	public static void main(String[] args) {
		Glm.init();
		
		Mesh mesh1 = new Mesh("test2.jpg", MainWindow.testVertices, MainWindow.testIndices);
		Mesh mesh2 = new Mesh("test.bmp", MainWindow.testVertices, MainWindow.testIndices);
		mesh2.setPosition(new Vec3(0.5f, -1.5f, 0.0f));
		ImbaCAD.meshes.add(mesh1);
		ImbaCAD.meshes.add(mesh2);
		
		
		Animator ani = new Animator();
		
		JFrame frame = new JFrame("Test");
		frame.setBounds(100, 100, 400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1, 1));
		
		ModelingWindow mw = new ModelingWindow(ani);
		
		frame.add(mw);
		
		frame.setVisible(true);
		
		
		ani.start();
	}
	
	
	
	
	
	public ModelingWindow(Animator animator) {
		super(new GridLayout(1, 1));
		
		System.out.println("new ModelingWindow");
		
		events = new LevitateEvents();
		events.setPosition(new Vec3(3.0f, -1.0f, -1.5f));
		events.setAzimuthAngle(0.0f);
		events.setPolarAngle((float)(0.6f * Math.PI));
		events.setVelocity(0.001f);
		
		
		GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL3));
		caps.setBackgroundOpaque(false);
		
		glCanvas = new GLCanvas(caps);
		glCanvas.addGLEventListener(this);
		glCanvas.addKeyListener(events);
		glCanvas.addMouseListener(events);
		glCanvas.addMouseMotionListener(events);
		
		
		this.add(glCanvas);
		
		this.animator = animator;
		this.animator.add(glCanvas);
	}


	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();

		System.out.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
		System.out.println("INIT GL IS: " + gl.getClass().getName());
		System.out.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
		System.out.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
		System.out.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

		if (gl.isGL3core()) {
			System.out.println("GL3 core detected");
			//vertexShaderString = "#version 130\n" + vertexShaderString;
			//fragmentShaderString = "#version 130\n" + fragmentShaderString;
		}

		// Create GPU shader handles
		// OpenGL ES retuns a index id to be stored for future reference.
		vertexShader = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
		fragmentShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
		
		
		// load shaders from disk
		String vsString = null;
		String fsString = null;
		try {
			
			// read vertex shader
			FileReader fr = new FileReader("shader/alt_vertex.shader");
			StringBuffer sb = new StringBuffer();
			int c = 0;
			
			while ((c = fr.read()) != -1) {
				sb.append((char)c);
			}
			fr.close();
			vsString = sb.toString();
			
			// read vertex shader
			fr = new FileReader("shader/alt_fragment.shader");
			sb = new StringBuffer();
			c = 0;

			while ((c = fr.read()) != -1) {
				sb.append((char) c);
			}
			fr.close();
			fsString = sb.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// Compile the vertexShader String into a program.
		String[] vlines = new String[] { vsString };
		int[] vlengths = new int[] { vlines[0].length() };
		gl.glShaderSource(vertexShader, vlines.length, vlines, vlengths, 0);
		gl.glCompileShader(vertexShader);

		// Check compile status.
		int[] compiled = new int[1];
		gl.glGetShaderiv(vertexShader, GL3.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] != 0) {
			System.out.println("Vertex shader compiled");
		} else {
			int[] logLength = new int[1];
			gl.glGetShaderiv(vertexShader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(vertexShader, logLength[0], (int[]) null, 0, log, 0);

			System.err.println("Error compiling the vertex shader: " + new String(log));
			System.exit(1);
		}

		// Compile the fragmentShader String into a program.
		String[] flines = new String[] { fsString };
		int[] flengths = new int[] { flines[0].length() };
		gl.glShaderSource(fragmentShader, flines.length, flines, flengths, 0);
		gl.glCompileShader(fragmentShader);

		// Check compile status.
		gl.glGetShaderiv(fragmentShader, GL3.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] != 0) {
			System.out.println("Fragment shader compiled");
		} else {
			int[] logLength = new int[1];
			gl.glGetShaderiv(fragmentShader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(fragmentShader, logLength[0], (int[]) null, 0, log, 0);

			System.err.println("Error compiling the fragment shader: " + new String(log));
			System.exit(1);
		}

		// Each shaderProgram must have
		// one vertex shader and one fragment shader.
		shaderProgram = gl.glCreateProgram();
		gl.glAttachShader(shaderProgram, vertexShader);
		gl.glAttachShader(shaderProgram, fragmentShader);

		// Associate attribute ids with the attribute names inside
		// the vertex shader.
		//gl.glBindAttribLocation(shaderProgram, 0, "attribute_Position");
		//gl.glBindAttribLocation(shaderProgram, 1, "attribute_Color");

		gl.glLinkProgram(shaderProgram);

		// Get a id number to the uniform_Projection matrix
		// so that we can update it.
		ModelLocation = gl.glGetUniformLocation(shaderProgram, "model");
		ViewLocation = gl.glGetUniformLocation(shaderProgram, "view");
		ProjectionLocation = gl.glGetUniformLocation(shaderProgram, "projection");
		
		gl.glEnable(GL3.GL_CULL_FACE);
		
		
		
		for (Mesh mesh : ImbaCAD.meshes) {
			mesh.init(drawable);
		}
	}
	
	

	@Override
	public void display(GLAutoDrawable drawable) {
		
		/*
		 * Events
		 */
		events.process();
		
		if (events.getKey(KeyEvent.VK_ESCAPE)) {
			//this.dispose();
			// TODO: dispose?
			return;
		}
		
		
		/*
		 * Drawing
		 */
		GL3 gl = drawable.getGL().getGL3();
		
		float[] projection;
		float[] view;
		float[] model;
		
		
		// Clear screen
		gl.glClearColor(0, 0, 0, 1.0f); 
		gl.glClear(GL3.GL_STENCIL_BUFFER_BIT | GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

		// Use the shaderProgram that got linked during the init part.
		gl.glUseProgram(shaderProgram);
		
		// set up projection matrix
		projection = Glm.diag(1.0f);
		projection = Glm.perspective((float)(0.5f * Math.PI), (float)width / height, 0.1f, 100.0f);
		
		// set up view matrix
		view = Glm.diag(1.0f);
		view = Glm.rotate(view, (float)(events.getPolarAngle() - Math.PI), Glm.vec3(1.0f, 0.0f, 0.0f));
		view = Glm.rotate(view, (float)(Math.PI / 2.0f - events.getAzimuthAngle()), Glm.vec3(0.0f, 0.0f, 1.0f));
		view = Glm.translate(view, events.getPosition().toArray());
		
		gl.glUniformMatrix4fv(ViewLocation, 1, false,	view, 0);
		gl.glUniformMatrix4fv(ProjectionLocation, 1, false,	projection, 0);
		
		// set up model matrix
		for (Mesh mesh : ImbaCAD.meshes) {
			
			if (mesh == ImbaCAD.meshes.getLast()) {
				alpha += 0.001f;
				mesh.getRotation().setZ(alpha);
			}
			
			model = Glm.diag(1.0f);
			model = Glm.translate(model, (mesh.getPosition().mul(-1.0f)).toArray());
			model = Glm.rotate(model, mesh.getRotation().getX(), Glm.vec3(1.0f, 0.0f, 0.0f));
			model = Glm.rotate(model, mesh.getRotation().getZ(), Glm.vec3(0.0f, 0.0f, 1.0f));
			
			gl.glUniformMatrix4fv(ModelLocation, 1, false,	model, 0);
			
			mesh.draw(drawable, shaderProgram);
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("cleanup");
		
		GL3 gl = drawable.getGL().getGL3();
		
		//mesh.dispose(drawable);
		for (Mesh mesh : ImbaCAD.meshes) {
			mesh.dispose(drawable);
		}
		
		gl.glUseProgram(0);
		
		gl.glDetachShader(shaderProgram, vertexShader);
		gl.glDeleteShader(vertexShader);
		gl.glDetachShader(shaderProgram, fragmentShader);
		gl.glDeleteShader(fragmentShader);
		gl.glDeleteProgram(shaderProgram);
	}



	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int z, int h) {
		System.out.println("Window resized to width=" + z + " height=" + h);
		width = z;
		height = h;

		// Get gl
		GL3 gl = drawable.getGL().getGL3();

		// perspectiveGL((GL2)gl, 90.0, (double)width / height, 0.1, 100.0);

		gl.glViewport(0, 0, width, height);
	}


}
