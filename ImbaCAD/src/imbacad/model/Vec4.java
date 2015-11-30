package imbacad.model;

public class Vec4 {
	
	private float[] vec = new float[4];
	
	public Vec4() {
		vec[0] = 0.0f;
		vec[1] = 0.0f;
		vec[2] = 0.0f;
		vec[3] = 0.0f;
	}
	
	public Vec4(Vec4 v) {
		vec[0] = v.vec[0];
		vec[1] = v.vec[1];
		vec[2] = v.vec[2];
		vec[3] = v.vec[3];
	}
	
	public Vec4(float x, float y, float z, float w) {
		vec[0] = x;
		vec[1] = y;
		vec[2] = z;
		vec[3] = w;
	}
	
	
	public float getX() {
		return vec[0];
	}
	
	public float getY() {
		return vec[1];
	}
	
	public float getZ() {
		return vec[2];
	}
	
	public float getW() {
		return vec[3];
	}

}
