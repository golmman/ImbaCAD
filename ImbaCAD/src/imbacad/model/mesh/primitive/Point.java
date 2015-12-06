package imbacad.model.mesh.primitive;

import com.jogamp.opengl.GL;

import imbacad.model.CopyFactory;

public class Point extends Primitive {
	
	
	public static final CopyFactory<Point> COPY = new CopyFactory<Point>() {
		@Override
		public Point copy(Point type) {
			return new Point(type);
		}
	};
	
	
	public Point(Point p) {
		super(p.id);
	}
	
	
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