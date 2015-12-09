package imbacad.model.mesh.primitive;


public class Point extends Primitive<Point> {
	
	public static final int STRIDE = 1;
	
	
	public Point(Point p) {
		super(STRIDE, p.id);
	}
	
	
	public Point(long id) {
		super(STRIDE, id);
	}


	@Override
	public Point copy() {
		return new Point(this);
	}
}