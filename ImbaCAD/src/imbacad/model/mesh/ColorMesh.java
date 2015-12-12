package imbacad.model.mesh;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

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
	public static <P extends Primitive<P>> ColorMesh<P> createGradientColorMesh(
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
	public static <P extends Primitive<P>> ColorMesh<P> createFlatColorMesh(
			int drawMode, VertexArray<ColorVertex> vertices, PrimitiveArray<P> primitives, String name) {
		
		
		ColorMesh<P> result = new ColorMesh<P>(drawMode, vertices, primitives, name);
		
		result.makeFlatData();
		
		return result;
	}
	
	
//	public static <V extends Vertex<V>, P extends Primitive<P>> ColorMesh<P> 
//		createSelectionMesh(Mesh<V, P> mesh) {
//		
//		// determine draw mode
//		int drawMode = -1;
//		if (mesh instanceof ColorMesh<?>) {
//			drawMode = ((ColorMesh<?>)mesh).drawMode;
//		} else if (mesh instanceof TextureMesh) {
//			drawMode = GL.GL_TRIANGLES;
//		}
//		
//		// copy primitive data
//		PrimitiveArray<P> primitives = new PrimitiveArray<P>(mesh.originalPrimitives);
//		
//		// copy position data
//		VertexArray<ColorVertex> vertices = new VertexArray<ColorVertex>();
//		for (V vertex: mesh.originalVertices) {
//			vertices.add(new ColorVertex(
//					vertex.getPosition().getX(), 
//					vertex.getPosition().getY(), 
//					vertex.getPosition().getZ(), 
//					0.0f, 0.0f, 0.0f, 0.0f));
//		}
//		
//		// add colours
//		int[] indices;
//		for (P prim: primitives) {
//			
//			indices = prim.getData();
//			for (int k = 0; k < indices.length; ++k) {
//				
//			}
//			
//		}
//		
//		return new ColorMesh<P>(drawMode, vertices, primitives, mesh.name + "_SELECTION");
//	}
	

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
