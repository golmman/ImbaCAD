package imbacad.rendering;

import imbacad.util.Vec3;

public class Camera {

	private Vec3 position = new Vec3();
	private float azimuthAngle = 0.0f;
	private float polarAngle = 0.0f;
	private float fov = 0.0f;
	private float velocity = 0.0f;
	
	public Camera() {}
	
	public Camera(Vec3 position, float azimuthAngle, float polarAngle, float fov, float velocity) {
		super();
		this.position = position;
		this.azimuthAngle = azimuthAngle;
		this.polarAngle = polarAngle;
		this.fov = fov;
		this.velocity = velocity;
	}

	public Vec3 getPosition() {
		return position;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public float getAzimuthAngle() {
		return azimuthAngle;
	}

	public void setAzimuthAngle(float azimuthAngle) {
		this.azimuthAngle = azimuthAngle;
	}

	public float getPolarAngle() {
		return polarAngle;
	}

	public void setPolarAngle(float polarAngle) {
		this.polarAngle = polarAngle;
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}

	public float getVelocity() {
		return velocity;
	}

	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}

}
