package imbacad.model.mesh;

import java.util.ArrayList;

public class VertexArray extends ArrayList<Vertex> {
	
	private static final long serialVersionUID = -199773943999337929L;

	public VertexArray() {
		super();
	}
	
	public VertexArray(float[] vertices) {
		super();
		
		if (vertices.length % Vertex.FLOATS_PER != 0) throw new IllegalStateException("Each vertex has to consist of 8 floats.");
		
		for (int k = 0; k < vertices.length; k += Vertex.FLOATS_PER) {
			this.add(new Vertex(
					vertices[k+0], vertices[k+1], vertices[k+2],
					vertices[k+3], vertices[k+4],
					vertices[k+5], vertices[k+6], vertices[k+7]));
		}
	}
	
	
	public float[] toFloats() {
		float[] result = new float[this.size() * Vertex.FLOATS_PER];
		
		int k = 0;
		for (Vertex v: this) {
			result[k+0] = v.position.getX();
			result[k+1] = v.position.getY();
			result[k+2] = v.position.getZ();
			result[k+3] = v.texture.getU();
			result[k+4] = v.texture.getV();
			result[k+5] = v.normal.getX();
			result[k+6] = v.normal.getY();
			result[k+7] = v.normal.getZ();
					
			k += Vertex.FLOATS_PER;
		}
		
		return result;
	}
}
