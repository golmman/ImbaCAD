package imbacad.model.mesh.primitive;

import com.jogamp.opengl.GL;

public class Line extends Primitive {
	
	private int i0, i1;
	
	public Line(int i0, int i1, PrimitiveID id) {
		super(id);
		this.i0 = i0;
		this.i1 = i1;
	}

	@Override
	public int[] getIndices() {
		int[] result = {i0, i1};
		return result;
	}

	@Override
	public void setIndices(int[] indices) {
		i0 = indices[0];
		i1 = indices[1];
	}

	@Override
	public int getDrawMode() {
		return GL.GL_LINES;
	}
}