package imbacad.model.mesh.vertex;

import java.util.ArrayList;

public class VertexArray<T extends Vertex> extends ArrayList<T> {
	
	private static final long serialVersionUID = -2014915524213423510L;
	
	private int stride = 0;
	
	
	public VertexArray() {
		super();
	}
	
	
	public VertexArray(T[] vertices) {
		super(vertices.length);
		
		if (vertices.length == 0) return;
		
		stride = vertices[0].toFloats().length;
		
		for (int k = 0; k < vertices.length; ++k) {
			this.add(vertices[k]);
		}
	}
	
	@Override
	public boolean add(T e) {
		if (stride == 0) {
			stride = e.toFloats().length;
		}
		return super.add(e);
	}
	
	public float[] toFloats() {
		float[] result = new float[this.size() * stride];
		float[] vertexFloats;
		
		int i = 0;
		for (T v: this) {
			vertexFloats = v.toFloats();
			
			for (int j = 0; j < vertexFloats.length; ++j) {
				result[i+j] = vertexFloats[j];
			}
			i += stride;
		}
		
		return result;
	}
	
	public int getTotalBytes() {
		return 4 * stride * this.size();
	}
	
	public int getStrideBytes() {
		return 4 * stride;
	}


	public int getStride() {
		return stride;
	}
	
	
}
