package imbacad.model.mesh.primitive;


public class Triangle extends Primitive<Triangle> {
	
	public static final int STRIDE = 3;
	
	
	public Triangle(Triangle t) {
		super(STRIDE, t.id);
		
		data[0] = t.data[0];
		data[1] = t.data[1];
		data[2] = t.data[2];
	}
	
	public Triangle(int i0, int i1, int i2, long id) {
		super(STRIDE, id);
		
		data[0] = i0;
		data[1] = i1;
		data[2] = i2;
	}

	@Override
	public Triangle copy() {
		return new Triangle(this);
	}
}