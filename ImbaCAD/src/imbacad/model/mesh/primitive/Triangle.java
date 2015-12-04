package imbacad.model.mesh.primitive;

import com.jogamp.opengl.GL;

public class Triangle extends Primitive {
	
	private int i0, i1, i2;
	
	public Triangle(int i0, int i1, int i2, PrimitiveID id) {
		super(id);
		this.i0 = i0;
		this.i1 = i1;
		this.i2 = i2;
	}

	@Override
	public int[] getIndices() {
		int[] result = {i0, i1, i2};
		return result;
	}

	@Override
	public void setIndices(int[] indices) {
		i0 = indices[0];
		i1 = indices[1];
		i2 = indices[2];
	}
	
	@Override
	public int getDrawMode() {
		return GL.GL_TRIANGLES;
	}
}