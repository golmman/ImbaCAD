package imbacad.model;

public class Vec2 {
	
	private float[] vec = new float[2];
	
	public Vec2(float x, float y) {
		vec[0] = x;
		vec[1] = y;
	}
	
	public Vec2(Vec2 v) {
		this.vec[0] = v.vec[0];
		this.vec[1] = v.vec[1];
	}
	
	public Vec2() {
		this.vec[0] = 0.0f;
		this.vec[1] = 0.0f;
	}
	
	public float getU() {
		return vec[0];
	}
	
	public float getV() {
		return vec[1];
	}
	
	public float getX() {
		return vec[0];
	}
	
	public float getY() {
		return vec[1];
	}
}
