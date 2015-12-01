package imbacad.model.mesh;

import java.util.ArrayList;

public class VertexArray2<T extends Vertex2> extends ArrayList<T> {
	
	private static final long serialVersionUID = -2014915524213423510L;
	
	private int stride;
	
	public VertexArray2() {
		super();
	}
	
	public VertexArray2(float[] vertices, int stride, VertexFactory<T> factory) {
		super();
		
		if (vertices.length % stride != 0) throw new IllegalStateException("Each vertex has to consist of " + stride + " floats.");
		
		for (int k = 0; k < vertices.length; k += stride) {
			this.add(factory.create(vertices, k));
		}
		
		this.stride = stride;
	}
	
	public float[] toFloats() {
		float[] result = new float[this.size() * stride];
		
		int i = 0;
		for (T v: this) {
			float[] vertexFloats = v.toFloats();
			
			for (int j = 0; j < vertexFloats.length; ++j) {
				result[i+j] = vertexFloats[j];
			}
				
			i += stride;
		}
		
		return result;
	}
	
	
}
