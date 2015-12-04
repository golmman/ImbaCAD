package imbacad.model.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import imbacad.model.Vec4;
import imbacad.model.mesh.vertex.ColorVertex;
import imbacad.model.mesh.vertex.VertexArray;
import imbacad.model.shader.Shader;

/**
 * Represents a mesh using a simplyfied shader which only outputs colors
 * @author Dirk Kretschmann
 *
 */
public class ColorMesh extends Mesh<ColorVertex> {
	
	private int drawMode = GL.GL_LINES;
	
	private ColorMesh(int drawMode, VertexArray<ColorVertex> vertices, int[] indices, String name) {
		super(vertices, indices, name);
		this.drawMode = drawMode;
	}
	
	
	/**
	 * Creates a mesh where vertex colours are interpolated for each fragment.
	 * @param vertices
	 * @param colors
	 * @param drawMode
	 * @param name
	 * @return
	 */
	public static ColorMesh createColorGradientMesh(int drawMode, VertexArray<ColorVertex> vertices, int[] indices, String name) {
		return new ColorMesh(drawMode, vertices, indices, name);
	}
	
	
	/**
	 * Creates a mesh where the provoking vertex (first vertex of each primitive) 
	 * determines the colour of the entire primitive.
	 * @param vertices
	 * @param colors
	 * @param drawMode
	 * @param name
	 * @return
	 */
	public static ColorMesh createColorFlatMesh(int drawMode, VertexArray<ColorVertex> vertices, int[] indices, String name) {
		ColorMesh mesh = new ColorMesh(drawMode, vertices, indices, name);
		
		if (drawMode == GL.GL_LINES) {
			for (int k = 1; k < mesh.vertices.size(); k += 2) {
				mesh.vertices.get(k).color = new Vec4(mesh.vertices.get(k-1).color);
			}
		} else if (drawMode == GL.GL_TRIANGLES) {
			
		}
		
		return mesh;
	}
	

	@Override
	public void draw(GL3 gl, Shader shader) {
		gl.glBindVertexArray(vao[0]);
		
		if (indices == null) {
			gl.glDrawArrays(drawMode, 0, vertices.getTotalBytes());
		} else {
			gl.glDrawElements(drawMode, indices.length, GL.GL_UNSIGNED_INT, 0);
		}
		
		
		gl.glBindVertexArray(0);
	}


	@Override
	public void init(GL3 gl) {
		gl.glGenVertexArrays(1, vao, 0);
		gl.glGenBuffers(1, vbo, 0);
		gl.glGenBuffers(1, ibo, 0);
		
		
		/*
		 * create texture vertex array
		 */
		gl.glBindVertexArray(vao[0]);
		
		FloatBuffer vertexBuf = Buffers.newDirectFloatBuffer(vertices.toFloats());
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.getTotalBytes(), vertexBuf, GL.GL_STATIC_DRAW);
		
		if (indices != null) {
			IntBuffer indexBuf = Buffers.newDirectIntBuffer(indices);
			gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
			gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, indexBuf, GL.GL_STATIC_DRAW);
		}

		// Set the vertex attribute pointers
		// vertex positions
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, vertices.getStrideBytes(), 0);
		
		// vertex colors
		gl.glEnableVertexAttribArray(1);
		gl.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, vertices.getStrideBytes(), 12);

		gl.glBindVertexArray(0);
		
	}

	@Override
	public void dispose(GL3 gl) {
		gl.glDeleteVertexArrays(1, vao, 0);
		gl.glDeleteBuffers(1, vbo, 0);
		gl.glDeleteBuffers(1, ibo, 0);
	}
	


	public int getDrawMode() {
		return drawMode;
	}




}
