package imbacad.model.camera;


import com.jogamp.opengl.GLAutoDrawable;

import imbacad.model.Vec3;

/**
 * 
 * Mathematical spherical coordinate conventions are realized by a GLAutoDrawables
 * {@link GLAutoDrawable#display(com.jogamp.opengl.GLAutoDrawable) display} method. <br>
 * See <a href="https://upload.wikimedia.org/wikipedia/commons/4/4f/3D_Spherical.svg">
 * https://upload.wikimedia.org/wikipedia/commons/4/4f/3D_Spherical.svg</a> <br>
 * (polarAngle = theta, azimuthAngle = phi) <br>
 * Example: setting position to Vec3(-3.0f, 1.0f, 2.0f) and polarAngle to pi / 2
 * makes it so that the camera is looking at Vec3(0.0f, 1.0f, 2.0f).
 * 
 * @author Dirk Kretschmann
 *
 */
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
	
	/**
	 * Calculates the unit normal vector of the camera plane.
	 * @return
	 */
	public Vec3 getLookingAt() {
		return new Vec3(
				(float)(Math.sin(polarAngle) * Math.cos(azimuthAngle)),
				(float)(Math.sin(polarAngle) * Math.sin(azimuthAngle)),	
				(float)(Math.cos(polarAngle)));	
	}
	
	/**
	 * Modifies azimuth and polar angle so that the camera then looks at the given coordinate.
	 * @param lookAt
	 */
	public void lookAt(Vec3 lookAt) {
		Vec3 v = lookAt.sub(this.position);
		
		azimuthAngle = getLookAtAzimuth(lookAt);
		
		if (v.getZ() >= 0.0f) {
			polarAngle = (float)(Math.asin(Vec3.AXIS_Z.cross(v).norm() / v.norm()));
		} else {
			polarAngle = (float)(Math.PI - (Math.asin(Vec3.AXIS_Z.cross(v).norm() / v.norm())));
		}
	}
	
	private float getLookAtAzimuth(Vec3 lookAt) {
		Vec3 v = lookAt.sub(this.position);
		float x = v.getX();
		float y = v.getY();
		
		if (x == 0.0f && y == 0.0f) return azimuthAngle;	// nothing to look at
		
		if (y == 0.0f && x >  0.0f) return 0.0f;					// looking down the positive x-axis
		if (y >  0.0f && x == 0.0f) return 0.5f * (float)Math.PI;	// looking down the positive y-axis
		if (y == 0.0f && x <  0.0f) return 1.0f * (float)Math.PI;	// looking down the negative x-axis
		if (y <  0.0f && x == 0.0f) return 1.5f * (float)Math.PI;	// looking down the negative y-axis
		
		if (x > 0.0f && y > 0.0f) return (float) Math.atan( y / x);						// 1st quadrant
		if (x < 0.0f && y > 0.0f) return (float)(Math.atan(-x / y) + 0.5f * Math.PI);	// 2nd quadrant
		if (x < 0.0f && y < 0.0f) return (float)(Math.atan( y / x) + 1.0f * Math.PI);	// 3rd quadrant
		if (x > 0.0f && y < 0.0f) return (float)(Math.atan(-x / y) + 1.5f * Math.PI);	// 4th quadrant
		
		return azimuthAngle;
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
