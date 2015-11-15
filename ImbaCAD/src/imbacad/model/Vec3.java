package imbacad.model;

public class Vec3 {
	
	public static final Vec3 AXIS_X = new Vec3(1.0f, 0.0f, 0.0f);
	public static final Vec3 AXIS_Y = new Vec3(0.0f, 1.0f, 0.0f);
	public static final Vec3 AXIS_Z = new Vec3(0.0f, 0.0f, 1.0f);
	
	private float[] vec = new float[3];
	
	public Vec3() {
		this.vec[0] = 0.0f;
		this.vec[1] = 0.0f;
		this.vec[2] = 0.0f;
	}
	
	public Vec3(float xyz) {
		this.vec[0] = xyz;
		this.vec[1] = xyz;
		this.vec[2] = xyz;
	}
	
	public Vec3(float x, float y, float z) {
		this.vec[0] = x;
		this.vec[1] = y;
		this.vec[2] = z;
	}
	
	public Vec3(float[] v) {
		this.vec[0] = v[0];
		this.vec[1] = v[1];
		this.vec[2] = v[2];
	}
	
	public Vec3(Vec3 v) {
		this.vec[0] = v.vec[0];
		this.vec[1] = v.vec[1];
		this.vec[2] = v.vec[2];
	}
	
	public Vec3 add(Vec3 v) {
		return new Vec3(
				this.vec[0] + v.vec[0],
				this.vec[1] + v.vec[1],
				this.vec[2] + v.vec[2]);		
	}
	
	public Vec3 sub(Vec3 v) {
		return new Vec3(
				this.vec[0] - v.vec[0],
				this.vec[1] - v.vec[1],
				this.vec[2] - v.vec[2]);		
	}
	
	public float dot(Vec3 v) {
		return 	  this.vec[0] * v.vec[0]
				+ this.vec[1] * v.vec[1]
				+ this.vec[2] * v.vec[2];		
	}
	
	public Vec3 mul(float t) {
		return new Vec3(
				t * this.vec[0],
				t * this.vec[1],
				t * this.vec[2]);
	}
	
	public Vec3 cross(Vec3 v) {
		return new Vec3(
				this.vec[1] * v.vec[2] - this.vec[2] * v.vec[1],
				this.vec[2] * v.vec[0] - this.vec[0] * v.vec[2],
				this.vec[0] * v.vec[1] - this.vec[1] * v.vec[0]);		
	}
	
	public float norm() {
		return (float)(Math.sqrt(
				  this.vec[0] * this.vec[0] 
				+ this.vec[1] * this.vec[1] 
				+ this.vec[2] * this.vec[2]));
	}
	
	public Vec3 normalised() {
		return this.mul(1.0f / this.norm());
	}
	
	
	public Vec3 rotateX(float a) {
		return new Vec3(
				this.vec[0],
				(float)(this.vec[1] * Math.cos(a) - this.vec[2] * Math.sin(a)),
				(float)(this.vec[1] * Math.sin(a) + this.vec[2] * Math.cos(a)));
	}
	
	public Vec3 rotateY(float a) {
		return new Vec3(
				(float)(this.vec[0] * Math.cos(a) + this.vec[2] * Math.sin(a)),
				this.vec[1],
				(float)(-this.vec[0] * Math.sin(a) + this.vec[2] * Math.cos(a)));
	}
	
	public Vec3 rotateZ(float a) {
		return new Vec3(
				(float)(this.vec[0] * Math.cos(a) - this.vec[1] * Math.sin(a)),
				(float)(this.vec[0] * Math.sin(a) + this.vec[1] * Math.cos(a)),
				this.vec[2]);
	}
	
	public float getX() {
		return this.vec[0];
	}
	
	public float getY() {
		return this.vec[1];
	}
	
	public float getZ() {
		return this.vec[2];
	}
	
	public void setX(float x) {
		this.vec[0] = x;
	}
	
	public void setY(float y) {
		this.vec[1] = y;
	}
	
	public void setZ(float z) {
		this.vec[2] = z;
	}
	
	public float[] toArray() {
		return this.vec;
	}
	
	@Override
	public String toString() {
		return "(" + this.vec[0] + ", " + this.vec[1] + ", " + this.vec[2] + ")";
	}
	
	public String toStringCopyable() {
		return "new " + this.getClass().getSimpleName() + "("
				+ this.vec[0] + "f, "
				+ this.vec[1] + "f, "
				+ this.vec[2] + "f"
				+");";
	}
}












