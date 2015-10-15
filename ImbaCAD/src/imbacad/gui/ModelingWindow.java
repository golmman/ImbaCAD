package imbacad.gui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileReader;
import java.io.IOException;

import imbacad.ImbaCAD;
import imbacad.Mesh;
import imbacad.event.LevitateEvents;
import imbacad.event.MKEvents;
import imbacad.util.Glm;
import imbacad.util.Vec3;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

public class ModelingWindow extends JDialog implements GLEventListener, WindowListener {

	private static final long serialVersionUID = -2636383013866025654L;
	
	private MKEvents events;
	
	private Animator animator;
	
	private GLCanvas glCanvas;
	
	private static int width = 800;
	private static int height = 600;

	private int shaderProgram;
	private int vertShader;
	private int fragShader;
	
	private int ModelLocation;
	private int ViewLocation;
	private int ProjectionLocation;
	
	private float polarAngle = (float)(0.6f * Math.PI);
	private float azimuthAngle = 0.0f;
	private Vec3 camStart = new Vec3(3.0f, -1.0f, -1.5f);
	
	private Vec3 pos = new Vec3();
	
	private float velocity = 0.001f;
	
	
	// TODO: only for debug
	private float alpha = 0.0f;
	
	
	
	public static void main(String[] args) {
		Glm.init();
		
		Animator ani = new Animator();
		
		new ModelingWindow(null, ani);
		
		ani.start();
	}
	
	
	
	
	
	public ModelingWindow(JFrame parent, Animator animator) {
		super(parent, "Title", false);
		
		events = new LevitateEvents();
		
		GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL3));
		caps.setBackgroundOpaque(false);
		
		glCanvas = new GLCanvas(caps);
		glCanvas.setBounds(10, 10, 400, 400);
		glCanvas.addGLEventListener(this);
		glCanvas.addKeyListener(events);
		glCanvas.addMouseListener(events);
		glCanvas.addMouseMotionListener(events);
		
		
		this.setLayout(null);
		this.setTitle("Model Window");
		this.setSize(450, 500);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		this.addWindowListener(this);
		

		this.add(glCanvas);
		
		this.animator = animator;
		this.animator.add(glCanvas);
		
		this.setVisible(true);
		
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
		vertShader = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
		fragShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
		
		
		// load shaders from disk
		String vsString = null;
		String fsString = null;
		try {
			
			// read vertex shader
			FileReader fr = new FileReader("shader/vertex.shader");
			StringBuffer sb = new StringBuffer();
			int c = 0;
			
			while ((c = fr.read()) != -1) {
				sb.append((char)c);
			}
			fr.close();
			vsString = sb.toString();
			
			// read vertex shader
			fr = new FileReader("shader/fragment.shader");
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
		gl.glShaderSource(vertShader, vlines.length, vlines, vlengths, 0);
		gl.glCompileShader(vertShader);

		// Check compile status.
		int[] compiled = new int[1];
		gl.glGetShaderiv(vertShader, GL3.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] != 0) {
			System.out.println("Vertex shader compiled");
		} else {
			int[] logLength = new int[1];
			gl.glGetShaderiv(vertShader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(vertShader, logLength[0], (int[]) null, 0, log, 0);

			System.err.println("Error compiling the vertex shader: " + new String(log));
			System.exit(1);
		}

		// Compile the fragmentShader String into a program.
		String[] flines = new String[] { fsString };
		int[] flengths = new int[] { flines[0].length() };
		gl.glShaderSource(fragShader, flines.length, flines, flengths, 0);
		gl.glCompileShader(fragShader);

		// Check compile status.
		gl.glGetShaderiv(fragShader, GL3.GL_COMPILE_STATUS, compiled, 0);
		if (compiled[0] != 0) {
			System.out.println("Fragment shader compiled");
		} else {
			int[] logLength = new int[1];
			gl.glGetShaderiv(fragShader, GL3.GL_INFO_LOG_LENGTH, logLength, 0);

			byte[] log = new byte[logLength[0]];
			gl.glGetShaderInfoLog(fragShader, logLength[0], (int[]) null, 0, log, 0);

			System.err.println("Error compiling the fragment shader: " + new String(log));
			System.exit(1);
		}

		// Each shaderProgram must have
		// one vertex shader and one fragment shader.
		shaderProgram = gl.glCreateProgram();
		gl.glAttachShader(shaderProgram, vertShader);
		gl.glAttachShader(shaderProgram, fragShader);

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
		// calculate normalized direction the camera is looking at
		Vec3 lookingAt = new Vec3(
			(float)(Math.sin(polarAngle) * Math.cos(azimuthAngle)),
			(float)(Math.sin(polarAngle) * Math.sin(azimuthAngle)),	
			(float)(Math.cos(polarAngle)));	
		
		// clear z and normalize, used in strafe movement
		Vec3 lookingAt2 = new Vec3(lookingAt);
		lookingAt2.setZ(0.0f);
		lookingAt2 = lookingAt2.mul(1.0f / lookingAt2.norm());
		
		if (events.getKey(KeyEvent.VK_W)) {
			pos = pos.sub(lookingAt.mul(velocity));
		}
		if (events.getKey(KeyEvent.VK_A)) {
			pos = pos.sub(Vec3.EZ.cross(lookingAt2.mul(velocity)));
		}
		if (events.getKey(KeyEvent.VK_S)) {
			pos = pos.add(lookingAt.mul(velocity));
		}
		if (events.getKey(KeyEvent.VK_D)) {
			pos = pos.add(Vec3.EZ.cross(lookingAt2.mul(velocity)));
		}
		if (events.getKey(KeyEvent.VK_SPACE)) {
			pos = pos.sub(Vec3.EZ.mul(velocity));
		}
		if (events.getKey(KeyEvent.VK_SHIFT)) {
			pos = pos.add(Vec3.EZ.mul(velocity));
		}
		
		if (events.getKey(KeyEvent.VK_ESCAPE)) {
			this.dispose();
			return;
		}
		
		
		if (events.getButton(MouseEvent.BUTTON3)) {
			azimuthAngle -= 0.003f * events.getMouseDx();
			polarAngle += 0.003f * events.getMouseDy();
		}

		
		if (azimuthAngle >= 2.0f * Math.PI) {
			float d = (float)(azimuthAngle / (2.0f * Math.PI));
			azimuthAngle = d - (float)Math.floor(d);
		} else if (azimuthAngle < 0.0f) {
			float d = (float)(-azimuthAngle / (2.0f * Math.PI));
			azimuthAngle = (float)(2.0f * Math.PI - (d - (float)Math.floor(d)));
		}
		
		events.setMouseDx(0);
		events.setMouseDy(0);
		
		
		
		/*
		 * Drawing
		 */

		GL3 gl = drawable.getGL().getGL3();
		
		// Clear screen
		gl.glClearColor(0, 0, 0, 1.0f); 
		gl.glClear(GL3.GL_STENCIL_BUFFER_BIT | GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

		// Use the shaderProgram that got linked during the init part.
		gl.glUseProgram(shaderProgram);
		
		
		
		float[] projection;
		float[] view;
		float[] model;
		
		// set up projection matrix
		projection = Glm.diag(1.0f);
		projection = Glm.perspective((float)(0.5f * Math.PI), (float)width / height, 0.1f, 100.0f);
		
		// set up view matrix
		view = Glm.diag(1.0f);
		view = Glm.rotate(view, (float)(polarAngle - Math.PI), Glm.vec3(1.0f, 0.0f, 0.0f));
		view = Glm.rotate(view, (float)(Math.PI / 2.0f - azimuthAngle), Glm.vec3(0.0f, 0.0f, 1.0f));
		view = Glm.translate(view, pos.add(camStart).toArray());
		
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
		
		gl.glDetachShader(shaderProgram, vertShader);
		gl.glDeleteShader(vertShader);
		gl.glDetachShader(shaderProgram, fragShader);
		gl.glDeleteShader(fragShader);
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





	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void windowClosing(WindowEvent e) {
		animator.remove(glCanvas);
		glCanvas.destroy();
	}





	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
