package imbacad.model.mesh;

import java.nio.FloatBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import imbacad.model.Vec3;
import imbacad.model.Vec4;
import imbacad.model.shader.Shader;

public class ColorMesh extends Mesh {
	
	Vec3[] vertices = null;
	Vec4[] colors = null;
	
	private int[] colVAO = new int[1];
	private int[] vertexBuffer = new int[1];
	private int[] colorBuffer = new int[1];
	
	private int drawMode = GL.GL_LINES;
	
	public ColorMesh(Vec3[] vertices, Vec4[] colors, String name) {
		super(name);
		
		if (vertices.length != colors.length) {
			throw new IllegalStateException("Number of colors and vertices must be equal.");
		}
		
		this.vertices = vertices;
		this.colors = colors;
		
	}
	
	public ColorMesh(Vec3[] vertices, Vec4 color, String name) {
		super(name);
		
		
		this.vertices = vertices;
		this.colors = new Vec4[vertices.length];
		
		for (int k = 0; k < vertices.length; ++k) {
			this.colors[k] = new Vec4(color);
		}
		
	}
	

	@Override
	public void draw(GL3 gl, Shader shader) {
		gl.glBindVertexArray(colVAO[0]);
		gl.glDrawArrays(drawMode, 0, vertices.length);
		gl.glBindVertexArray(0);
	}


	@Override
	public void init(GL3 gl) {
		gl.glGenVertexArrays(1, colVAO, 0);
		gl.glGenBuffers(1, vertexBuffer, 0);
		gl.glGenBuffers(1, colorBuffer, 0);
		
		float[] vertexFloats = new float[3 * vertices.length];
		float[] colorFloats = new float[4 * colors.length];
		for (int k = 0; k < vertices.length; ++k) {
			vertexFloats[3*k]   = vertices[k].getX();
			vertexFloats[3*k+1] = vertices[k].getY();
			vertexFloats[3*k+2] = vertices[k].getZ();
			
			colorFloats[4*k]   = colors[k].getX();
			colorFloats[4*k+1] = colors[k].getY();
			colorFloats[4*k+2] = colors[k].getZ();
			colorFloats[4*k+3] = colors[k].getW();
		}
		
		FloatBuffer vertexBuf = Buffers.newDirectFloatBuffer(vertexFloats);
		FloatBuffer colorBuf = Buffers.newDirectFloatBuffer(colorFloats);
		
		
		gl.glBindVertexArray(colVAO[0]);
		
		// vertices
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBuffer[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertexFloats.length * 4, vertexBuf, GL.GL_STATIC_DRAW);

		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 0, 0);

		// colors
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, colorBuffer[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, colorFloats.length * 4, colorBuf, GL.GL_DYNAMIC_DRAW);
		
		gl.glEnableVertexAttribArray(1);
		gl.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, 0, 0);

		gl.glBindVertexArray(0);
		
	}


	@Override
	public void dispose(GL3 gl) {
		// TODO Auto-generated method stub
		
	}


	public int getDrawMode() {
		return drawMode;
	}


	public void setDrawMode(int drawMode) {
		this.drawMode = drawMode;
	}


}
