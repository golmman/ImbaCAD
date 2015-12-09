package imbacad.model.mesh.vertex;

import imbacad.model.Vec3;
import imbacad.model.Vec4;

public class ColorVertex extends Vertex<ColorVertex> {

	public static final int STRIDE = 7;
	
	public ColorVertex(ColorVertex vertex) {
		super(STRIDE);
		
		data[0] = vertex.data[0];
		data[1] = vertex.data[1];
		data[2] = vertex.data[2];
		
		data[3] = vertex.data[3];
		data[4] = vertex.data[4];
		data[5] = vertex.data[5];
		data[6] = vertex.data[6];
	}
	
	public ColorVertex(float[] f, int offset) {
		super(STRIDE);
		
		data[0] = f[offset+0];
		data[1] = f[offset+1];
		data[2] = f[offset+2];
		
		data[3] = f[offset+3];
		data[4] = f[offset+4];
		data[5] = f[offset+5];
		data[6] = f[offset+6];
	}
	
	public ColorVertex(float posx, float posy, float posz, float r, float g, float b, float a) {
		super(STRIDE);
		
		data[0] = posx;
		data[1] = posy;
		data[2] = posz;
		
		data[3] = r;
		data[4] = g;
		data[5] = b;
		data[6] = a;
	}
	
	public ColorVertex(Vec3 position, Vec4 color) {
		super(STRIDE);
		
		data[0] = position.getX();
		data[1] = position.getY();
		data[2] = position.getZ();
		
		data[3] = color.getR();
		data[4] = color.getG();
		data[5] = color.getB();
		data[6] = color.getA();
	}

	

	public Vec4 getColor() {
		return new Vec4(data[3], data[4], data[5], data[6]);
	}

	public void setColor(Vec4 color) {
		data[3] = color.getR();
		data[4] = color.getG();
		data[5] = color.getB();
		data[6] = color.getA();
	}

	@Override
	public ColorVertex copy() {
		return new ColorVertex(this);
	}

}
