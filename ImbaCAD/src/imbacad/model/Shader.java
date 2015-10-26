package imbacad.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;

public class Shader {
	
	private int program;
	
	private int model;
	private int view;
	private int projection;

	public Shader(GLAutoDrawable drawable, File vertFile, File fragFile) {
		GL3 gl = drawable.getGL().getGL3();

		System.out.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
		System.out.println("INIT GL IS: " + gl.getClass().getName());
		System.out.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
		System.out.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
		System.out.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

		if (gl.isGL3core()) {
			System.out.println("GL3 core detected");
		}

		// Create GPU shader handles
		// OpenGL ES retuns a index id to be stored for future reference.
		int vertexShader = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
		int fragmentShader = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
		
		
		// load shaders from disk
		String vsString = null;
		String fsString = null;
		try {
			
			// read vertex shader
			FileReader fr = new FileReader(vertFile);
			StringBuffer sb = new StringBuffer();
			int c = 0;
			
			while ((c = fr.read()) != -1) {
				sb.append((char)c);
			}
			fr.close();
			vsString = sb.toString();
			
			// read vertex shader
			fr = new FileReader(fragFile);
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
		program = gl.glCreateProgram();
		gl.glAttachShader(program, vertexShader);
		gl.glAttachShader(program, fragmentShader);

		gl.glLinkProgram(program);
		
		// Get a id number to the uniform_Projection matrix
		// so that we can update it.
		model = gl.glGetUniformLocation(program, "model");
		view = gl.glGetUniformLocation(program, "view");
		projection = gl.glGetUniformLocation(program, "projection");
	}

	public int getProgram() {
		return program;
	}

	public int getModel() {
		return model;
	}

	public int getView() {
		return view;
	}

	public int getProjection() {
		return projection;
	}

}
