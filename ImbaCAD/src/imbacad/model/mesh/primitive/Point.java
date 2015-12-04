package imbacad.model.mesh.primitive;

import com.jogamp.opengl.GL;

public class Point extends Primitive {
	
	public Point(PrimitiveID id) {
		super(id);
	}

	@Override
	public int[] getIndices() {
		return null;
	}

	@Override
	public void setIndices(int[] indices) {
		
	}
	
	@Override
	public int getDrawMode() {
		return GL.GL_POINTS;
	}
}