package imbacad;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.Animator;
import com.jogamp.common.nio.Buffers;

import imbacad.model.Glm;
import imbacad.model.Mesh;
import imbacad.model.Vec3;

import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public class ImbaCAD_old implements GLEventListener, KeyListener, MouseListener {

	private float vertices[] = {
			 0.5f,  0.5f, -0.5f,  1.0f,  0.0f,
			-0.5f,  0.5f, -0.5f,  0.0f,  0.0f, 
			-0.5f, -0.5f, -0.5f,  0.0f,  1.0f, 
			 0.5f, -0.5f, -0.5f,  1.0f,  1.0f, 
		};
	
	private int[] indices = {
			0, 1, 2, 0, 2, 3
	};
	
	private static GLWindow glWindow;
	private static GLWindow glWindow2;
	private static Animator animator;
	
	private Mesh mesh = null;
	
	private static int width = 800;
	private static int height = 600;

	private int shaderProgram;
	private int vertShader;
	private int fragShader;
	
	private int ModelviewLocation;
	private int ProjectionLocation;

	private static final int VERTICES_IDX = 0;
	private static final int COLOR_IDX = 1;
	
	private int[] vboHandles;
	
	
	
	
	private boolean[] keys = new boolean[256]; 
	private boolean[] buttons = new boolean[32];
	private int mouseX = 0;
	private int mouseY = 0;
	private int mouseDx = 0;
	private int mouseDy = 0;
	
	private float polarAngle = (float)Math.PI;
	private float azimuthAngle = 0.0f;
	
	private Vec3 pos = new Vec3();
	
	private float velocity = 0.001f;
	
	
	
	


	public static void main(String[] s) {
		Glm.init();
		
		ImbaCAD_old icad = new ImbaCAD_old();
		
		GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL3));
		// We may at this point tweak the caps and request a translucent drawable
		caps.setBackgroundOpaque(false);
		
		
		glWindow = GLWindow.create(caps);

		glWindow.setTitle("ImbaCAD");
		glWindow.setSize(width, height);
		glWindow.setUndecorated(false);
		glWindow.setPointerVisible(true);
		glWindow.setRealized(true);
		glWindow.setVisible(true);
		
		glWindow.addKeyListener(icad);
		glWindow.addMouseListener(icad);
		glWindow.addGLEventListener(icad);
		
		
		glWindow2 = GLWindow.create(caps);

		glWindow2.setTitle("ImbaCAD");
		glWindow2.setPosition(400, 400);
		glWindow2.setSize(width, height);
		glWindow2.setUndecorated(false);
		glWindow2.setPointerVisible(true);
		glWindow2.setRealized(true);
		glWindow2.setVisible(true);
		
		glWindow2.addKeyListener(icad);
		glWindow2.addMouseListener(icad);
		glWindow2.addGLEventListener(icad);
		
		
		
		animator = new Animator();
		animator.add(glWindow);
		animator.add(glWindow2);
		animator.start();
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
		ModelviewLocation = gl.glGetUniformLocation(shaderProgram, "modelview");
		ProjectionLocation = gl.glGetUniformLocation(shaderProgram, "projection");

		vboHandles = new int[2];
		gl.glGenBuffers(2, vboHandles, 0);
		
		gl.glEnable(GL3.GL_CULL_FACE);
		
		mesh = new Mesh("test2.jpg", vertices, indices);
		mesh.init(drawable);
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

	//private float distance = 0.0f;
	//private float sign = 1.0f;
	
	@Override
	public void display(GLAutoDrawable drawable) {
		//distance += sign * 0.0005;
//		if (distance >= 1.0f) {
//			distance = 1.0f;
//			sign = -1.0f;
//		} else if (distance <= -1.0f) {
//			distance = -1.0f;
//			sign = 1.0f;
//		}
		//System.out.println(distance);
		
		
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
		
		if (keys[KeyEvent.VK_W]) {
			pos = pos.sub(lookingAt.mul(velocity));
		}
		if (keys[KeyEvent.VK_A]) {
			pos = pos.sub(Vec3.AXIS_Z.cross(lookingAt2.mul(velocity)));
		}
		if (keys[KeyEvent.VK_S]) {
			pos = pos.add(lookingAt.mul(velocity));
		}
		if (keys[KeyEvent.VK_D]) {
			pos = pos.add(Vec3.AXIS_Z.cross(lookingAt2.mul(velocity)));
		}
		if (keys[KeyEvent.VK_SPACE]) {
			pos = pos.sub(Vec3.AXIS_Z.mul(velocity));
		}
		if (keys[KeyEvent.VK_SHIFT]) {
			pos = pos.add(Vec3.AXIS_Z.mul(velocity));
		}
		
		if (keys[KeyEvent.VK_ESCAPE]) {
			animator.remove(glWindow);
			animator.remove(glWindow2);
			glWindow.destroy();
			glWindow2.destroy();
			return;
		}
		
		
		if (buttons[MouseEvent.BUTTON3]) {
			azimuthAngle -= 0.003f * mouseDx;
			polarAngle += 0.003f * mouseDy;
		}

		
		if (azimuthAngle >= 2.0f * Math.PI) {
			float d = (float)(azimuthAngle / (2.0f * Math.PI));
			azimuthAngle = d - (float)Math.floor(d);
		} else if (azimuthAngle < 0.0f) {
			float d = (float)(-azimuthAngle / (2.0f * Math.PI));
			azimuthAngle = (float)(2.0f * Math.PI - (d - (float)Math.floor(d)));
		}
		
		mouseDx = 0;
		mouseDy = 0;
		
		
		
		/*
		 * Drawing
		 */

		GL3 gl = drawable.getGL().getGL3();
		
		// Clear screen
		gl.glClearColor(0, 0, 0, 1.0f); 
		gl.glClear(GL3.GL_STENCIL_BUFFER_BIT | GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);

		// Use the shaderProgram that got linked during the init part.
		gl.glUseProgram(shaderProgram);
		
		
		// set up matrices
		float[] projection = Glm.diag(1.0f);
		float[] modelview = Glm.diag(1.0f);
		Vec3 camStart = new Vec3(0.0f, 0.0f, -1.5f);
		
		projection = Glm.perspective((float)(0.5f * Math.PI), (float)width / height, 0.1f, 100.0f);
		
		modelview = Glm.rotate(modelview, (float)(polarAngle - Math.PI), Glm.vec3(1.0f, 0.0f, 0.0f));
		modelview = Glm.rotate(modelview, (float)(Math.PI / 2.0f - azimuthAngle), Glm.vec3(0.0f, 0.0f, 1.0f));
		
		modelview = Glm.translate(modelview, pos.add(camStart).toArray());
		
		//modelview = Glm.rotate(modelview, distance, Glm.vec3(0.0f, 0.0f, 1.0f));
		
		gl.glUniformMatrix4fv(ModelviewLocation, 1, false,	modelview, 0);
		gl.glUniformMatrix4fv(ProjectionLocation, 1, false,	projection, 0);
			
		
		// set up triangle
		float[] vertices = { 0.0f, 0.5f, 0.0f, 1.0f, // Top
							-0.5f, -0.5f, 0.0f, 1.0f, // Bottom Left
							0.5f, -0.5f, 0.0f, 1.0f // Bottom Right
		};
		

		FloatBuffer fbVertices = Buffers.newDirectFloatBuffer(vertices);

		// Select the VBO, GPU memory data, to use for vertices
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboHandles[VERTICES_IDX]);

		// transfer data to VBO, this perform the copy of data from CPU -> GPU
		// memory
		int numBytes = vertices.length * 4;
		gl.glBufferData(GL.GL_ARRAY_BUFFER, numBytes, fbVertices, GL.GL_STATIC_DRAW);
		fbVertices = null; // It is OK to release CPU vertices memory after
							// transfer to GPU

		// Associate Vertex attribute 0 with the last bound VBO
		gl.glVertexAttribPointer(0, 4, GL3.GL_FLOAT, false, 0, 0);

		// VBO
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0); 
		// You can unbind the VBO after it have been associated using glVertexAttribPointer
		gl.glEnableVertexAttribArray(0);

		float[] colors = { 
				1.0f, 0.0f, 0.0f, 1.0f, // Top color (red)
				0.0f, 0.0f, 0.0f, 1.0f, // Bottom Left color (black)
				1.0f, 1.0f, 0.0f, 0.9f  // Bottom Right color (yellow) with 10% transparency
									
		};

		FloatBuffer fbColors = Buffers.newDirectFloatBuffer(colors);

		// Select the VBO, GPU memory data, to use for colors
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vboHandles[COLOR_IDX]);

		numBytes = colors.length * 4;
		gl.glBufferData(GL.GL_ARRAY_BUFFER, numBytes, fbColors,	GL.GL_STATIC_DRAW);
		fbColors = null; // It is OK to release CPU color memory after transfer to GPU

		// Associate Vertex attribute 1 with the last bound VBO
		gl.glVertexAttribPointer(1, 4, GL3.GL_FLOAT, false, 0, 0);

		gl.glEnableVertexAttribArray(1);

		// draw triangle
		gl.glDrawArrays(GL3.GL_TRIANGLES, 0, 3); // Draw the vertices as triangle

		gl.glDisableVertexAttribArray(0); // Allow release of vertex position memory
		gl.glDisableVertexAttribArray(1); // Allow release of vertex color memory
		
		// draw mesh
		mesh.draw(drawable, shaderProgram);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("cleanup");
		
		GL3 gl = drawable.getGL().getGL3();
		
		mesh.dispose(drawable);
		
		gl.glUseProgram(0);
		gl.glDeleteBuffers(2, vboHandles, 0); // Release VBO, color and vertices, buffer GPU memory.
		vboHandles = null;
		
		gl.glDetachShader(shaderProgram, vertShader);
		gl.glDeleteShader(vertShader);
		gl.glDetachShader(shaderProgram, fragShader);
		gl.glDeleteShader(fragShader);
		gl.glDeleteProgram(shaderProgram);
		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!e.isAutoRepeat()) {
			keys[e.getKeyCode()] = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!e.isAutoRepeat()) {
			keys[e.getKeyCode()] = false;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseDx = e.getX() - mouseX;
		mouseDy = e.getY() - mouseY;
		mouseX = e.getX();
		mouseY = e.getY();
		
		//System.out.println("azi: " + azimuthAngle + ", pol: " + polarAngle);
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
	public void mouseMoved(MouseEvent e) {
		mouseDx = e.getX() - mouseX;
		mouseDy = e.getY() - mouseY;
		mouseX = e.getX();
		mouseY = e.getY();
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
	public void mouseWheelMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
