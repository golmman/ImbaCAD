package imbacad.model.mesh.primitive;


public class Line extends Primitive<Line> {
	
	public static final int STRIDE = 2;
	
	public Line(Line l) {
		super(STRIDE, l.id);
		
		data[0] = l.data[0];
		data[1] = l.data[1];
	}
	
	public Line(int i0, int i1, long id) {
		super(STRIDE, id);
		
		data[0] = i0;
		data[1] = i1;
	}

	@Override
	public Line copy() {
		return new Line(this);
	}
	
	
	@Override
	public String toString() {
		return "(" + data[0] + ", " + data[1] + "), " + id;
	}
}