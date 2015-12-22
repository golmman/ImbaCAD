package imbacad.model.mesh;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import imbacad.model.Vec3;
import imbacad.model.Vec4;
import imbacad.model.dwg.DWG;
import imbacad.model.mesh.primitive.Line;
import imbacad.model.mesh.primitive.Primitive;
import imbacad.model.mesh.primitive.PrimitiveArray;
import imbacad.model.mesh.vertex.ColorVertex;
import imbacad.model.mesh.vertex.VertexArray;
import imbacad.model.shader.HasUniforms;
import imbacad.model.shader.Shader;
import imbacad.model.shader.UniformInt;

/**
 * Represents a mesh using a simplyfied shader which only outputs colors
 * @author Dirk Kretschmann
 *
 */
public class ColorMesh<P extends Primitive<P>> extends Mesh<ColorVertex, P> implements HasUniforms {
	
	public static final List<Integer> SUPPORTED_DRAW_MODES = Arrays.asList(
			GL.GL_POINTS, GL.GL_LINES, GL.GL_TRIANGLES);
	
	private int drawMode = GL.GL_LINES;
	
	
	protected ColorMesh(int drawMode, VertexArray<ColorVertex> vertices, PrimitiveArray<P> primitives, String name) {
		super(vertices, primitives, name);
		
		if (!SUPPORTED_DRAW_MODES.contains(drawMode)) {
			throw new UnsupportedOperationException("unsupported drawmode, check ColorMesh.SUPPORTED_DRAW_MODES");
		}
		
		this.drawMode = drawMode;
	}
	
	
	/**
	 * Creates a mesh where vertex colours are interpolated for each fragment.
	 * @param drawMode
	 * @param vertices
	 * @param primitives
	 * @param name
	 * @return
	 */
	public static final <P extends Primitive<P>> ColorMesh<P> createGradientColorMesh(
			int drawMode, VertexArray<ColorVertex> vertices, PrimitiveArray<P> primitives, String name) {
		
		return new ColorMesh<P>(drawMode, vertices, primitives, name);
	}
	
	
	/**
	 * Creates a mesh where the first vertex of each primitive 
	 * determines the colour of the entire primitive.
	 * @param drawMode
	 * @param vertices
	 * @param primitives
	 * @param name
	 * @return
	 */
	public static final <P extends Primitive<P>> ColorMesh<P> createFlatColorMesh(
			int drawMode, VertexArray<ColorVertex> vertices, PrimitiveArray<P> primitives, String name) {
		
		
		ColorMesh<P> result = new ColorMesh<P>(drawMode, vertices, primitives, name);
		
		result.makeFlatData();
		
		return result;
	}
	
	
	public static final ColorMesh<Line> createDWGMesh(File file, String name) {
		Vec4 color = new Vec4(1.0f, 0.0f, 0.0f, 1.0f);
		
		double[] lines = DWG.readLines(file.getAbsolutePath());
		
		VertexArray<ColorVertex> vertices = new VertexArray<ColorVertex>();
		PrimitiveArray<Line> primitives = new PrimitiveArray<Line>();
		
		for (int k = 0; k < lines.length; k += 4) {
			vertices.add(new ColorVertex(new Vec3((float)lines[k+0], (float)lines[k+1], 0.0f), color));
			vertices.add(new ColorVertex(new Vec3((float)lines[k+2], (float)lines[k+3], 0.0f), color));
			
			primitives.add(new Line(k/2, k/2+1, k/4));
		}
		
		return createFlatColorMesh(GL.GL_LINES, vertices, primitives, name);
	}
	

	@Override
	public void draw(GL3 gl, Shader shader) {
		updateUniforms(gl, shader);		
		
		gl.glBindVertexArray(vao[0]);
		gl.glDrawElements(drawMode, primitives.size() * primitives.getStride(), GL.GL_UNSIGNED_INT, 0);
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
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.getTotalBytes(), vertexBuf, GL.GL_DYNAMIC_DRAW);
		
		IntBuffer indexBuf = Buffers.newDirectIntBuffer(primitives.toInts());
		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
		gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, primitives.getTotalBytes(), indexBuf, GL.GL_STATIC_DRAW);

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


	@Override
	public void updateUniforms(GL3 gl, Shader shader) {
		if (uniformFlat == null) {
			uniformFlat = new UniformInt(gl, shader, "flatColors");
		}
		uniformFlat.update(gl, flat ? 1 : 0);		
	}



}
