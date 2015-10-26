package imbacad.model;

import imbacad.ImbaCAD;
import imbacad.control.RenderingEventAdapter;
import imbacad.model.camera.Camera;
import imbacad.model.camera.CameraUpdater;

import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * Renders
 * @author Dirk Kretschmann
 *
 */
public class DefaultRenderer implements GLEventListener {
	
	RenderingEventAdapter events = null;
	private Camera camera = null;
	private CameraUpdater cameraUpdater = null;
	
	private int width = 800;
	private int height = 600;

	private int shaderProgram;
	private int vertexShader;
	private int fragmentShader;
	
	private int ModelLocation;
	private int ViewLocation;
	private int ProjectionLocation;
	
	// TODO: only for debug
	private float alpha = 0.0f;
	
	public DefaultRenderer(RenderingEventAdapter events, Camera camera, CameraUpdater updater) {
		this.events = events;
		this.cameraUpdater = updater;
		this.camera = camera;
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
		gl.glEnable(GL3.GL_PROGRAM_POINT_SIZE);
		
		
		
		for (Mesh mesh : ImbaCAD.meshes) {
			mesh.init(drawable);
		}
	}
	
	

	@Override
	public void display(GLAutoDrawable drawable) {
		
		/*
		 * Events
		 */
		cameraUpdater.update(camera, events);
		
//		if (processor.getKey(KeyEvent.VK_ESCAPE)) {
//			//this.dispose();
//			// TODO: dispose?
//			return;
//		}
		
		
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
		projection = Glm.perspective(camera.getFov(), (float)width / height, 0.1f, 100.0f);
		
		// set up view matrix
		view = Glm.diag(1.0f);
		view = Glm.rotate(view, (float)(camera.getPolarAngle() - Math.PI), Glm.vec3(1.0f, 0.0f, 0.0f));
		view = Glm.rotate(view, (float)(Math.PI / 2.0f - camera.getAzimuthAngle()), Glm.vec3(0.0f, 0.0f, 1.0f));
		view = Glm.translate(view, camera.getPosition().mul(-1.0f).toArray());
		
		gl.glUniformMatrix4fv(ViewLocation, 1, false,	view, 0);
		gl.glUniformMatrix4fv(ProjectionLocation, 1, false,	projection, 0);
		
		// set up model matrix
		for (Mesh mesh : ImbaCAD.meshes) {
			
			if (mesh == ImbaCAD.meshes.getLast()) {
				// rotate last
				alpha += 0.01f;
				mesh.getRotation().setZ(alpha);
			}
			
			model = Glm.diag(1.0f);
			model = Glm.translate(model, (mesh.getPosition().mul(-1.0f)).toArray());
			model = Glm.rotate(model, mesh.getRotation().getX(), Glm.vec3(1.0f, 0.0f, 0.0f));
			model = Glm.rotate(model, mesh.getRotation().getZ(), Glm.vec3(0.0f, 0.0f, 1.0f));
			
			gl.glUniformMatrix4fv(ModelLocation, 1, false,	model, 0);
			
			mesh.draw(drawable, shaderProgram);
			
			
			
			if (mesh == ImbaCAD.meshes.getFirst()) {
				// calculate screen coordinates for last mesh
				
				
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, mesh.getVBO()[0]);
				
				
				// get object coordinates, equal to mesh.getVertices()[0];
				FloatBuffer vertices = Buffers.newDirectFloatBuffer(20);
				gl.glGetBufferSubData(GL.GL_ARRAY_BUFFER, 0, 20 * 4, vertices);
				float objX = vertices.get(0); 		
				float objY = vertices.get(1);
				float objZ = vertices.get(2);
				float objW = 1.0f;
				
				// get clip coordinates, OpenGL uses column-major order
				float[] mvp = Glm.mul(Glm.mul(projection, view), model);
				float clipX = objX * mvp[ 0] + objY * mvp[ 4] + objZ * mvp[ 8] + objW * mvp[12];
				float clipY = objX * mvp[ 1] + objY * mvp[ 5] + objZ * mvp[ 9] + objW * mvp[13];
				float clipZ = objX * mvp[ 2] + objY * mvp[ 6] + objZ * mvp[10] + objW * mvp[14];
				float clipW = objX * mvp[ 3] + objY * mvp[ 7] + objZ * mvp[11] + objW * mvp[15];
				
				// get normalized device coordinates
				float ndcX = clipX / clipW;
				float ndcY = clipY / clipW;
				float ndcZ = clipZ / clipW;
				
				// get screen coordinates
				IntBuffer viewport = Buffers.newDirectIntBuffer(4);
				gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
				FloatBuffer depthRange = Buffers.newDirectFloatBuffer(2);
				gl.glGetFloatv(GL.GL_DEPTH_RANGE, depthRange);
				float scX = 0.5f * viewport.get(2) * (ndcX + 1) + viewport.get(0);
				float scY = viewport.get(3) - 0.5f * viewport.get(3) * (ndcY + 1) + viewport.get(1);
				float scZ = 0.5f * ((depthRange.get(1) - depthRange.get(0)) * ndcZ + depthRange.get(1) + depthRange.get(0));
				
				// get distance to mouse pointer
				float dx = scX - events.getMouseX();
				float dy = scY - events.getMouseY();
				
				if (Math.sqrt(dx * dx + dy * dy) < 10.0) {
					System.out.println("top left corner! " + Math.sqrt(dx * dx + dy * dy));
				}
				
				//System.out.println(scX + " " + scY + " " + scZ);
			}
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
		
		width = z;
		height = h;

		// Get gl
		GL3 gl = drawable.getGL().getGL3();

		// perspectiveGL((GL2)gl, 90.0, (double)width / height, 0.1, 100.0);

		gl.glViewport(0, 0, width, height);
		
		System.out.println("Window resized to width=" + z + " height=" + h);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public CameraUpdater getProcessor() {
		return cameraUpdater;
	}

	public void setProcessor(CameraUpdater processor) {
		this.cameraUpdater = processor;
	}

}
