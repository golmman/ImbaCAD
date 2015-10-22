package imbacad.event;


import imbacad.util.Vec3;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class LevitateEvents extends MKEvents {

	private Vec3 position = new Vec3();
	private float azimuthAngle = 0.0f;
	private float polarAngle = 0.0f;
	private float velocity = 0.001f;

	public LevitateEvents() {
		position = new Vec3();
		azimuthAngle = 0.0f;
		polarAngle = 0.0f;
		velocity = 0.001f;
	}
	
	public LevitateEvents(Vec3 position, float azimuthAngle, float polarAngle, float velocity) {
		super();
		this.position = position;
		this.polarAngle = polarAngle;
		this.azimuthAngle = azimuthAngle;
		this.velocity = velocity;
	}

	
	
	@Override
	public void process() {
		// calculate normalized direction the camera is looking at
		Vec3 lookingAt = new Vec3(
			(float)(Math.sin(polarAngle) * Math.cos(azimuthAngle)),
			(float)(Math.sin(polarAngle) * Math.sin(azimuthAngle)),	
			(float)(Math.cos(polarAngle)));	
		
		// clear z and normalize, used in strafe movement
		Vec3 lookingAt2 = new Vec3(lookingAt);
		lookingAt2.setZ(0.0f);
		lookingAt2 = lookingAt2.mul(1.0f / lookingAt2.norm());
		
		if (keys[KeyEvent.VK_W]) {
			position = position.sub(lookingAt.mul(velocity));
		}
		if (keys[KeyEvent.VK_A]) {
			position = position.sub(Vec3.AXIS_Z.cross(lookingAt2.mul(velocity)));
		}
		if (keys[KeyEvent.VK_S]) {
			position = position.add(lookingAt.mul(velocity));
		}
		if (keys[KeyEvent.VK_D]) {
			position = position.add(Vec3.AXIS_Z.cross(lookingAt2.mul(velocity)));
		}
		if (keys[KeyEvent.VK_SPACE]) {
			position = position.sub(Vec3.AXIS_Z.mul(velocity));
		}
		if (keys[KeyEvent.VK_SHIFT]) {
			position = position.add(Vec3.AXIS_Z.mul(velocity));
		}
		
		
		if (buttons[MouseEvent.BUTTON3]) {
			azimuthAngle -= 0.003f * mouseDx;
			polarAngle += 0.003f * mouseDy;
		}

		
		if (azimuthAngle >= 2.0f * Math.PI) {
			float d = (float)(azimuthAngle / (2.0f * Math.PI));
			azimuthAngle = d - (float)Math.floor(d);
		} else if (azimuthAngle < 0.0f) {
			float d = (float)(-azimuthAngle / (2.0f * Math.PI));
			azimuthAngle = (float)(2.0f * Math.PI - (d - (float)Math.floor(d)));
		}
		
		mouseDx = 0;
		mouseDy = 0;
	}

	@Override
	public void keyTyped(KeyEvent e) {}


	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	

	public Vec3 getPosition() {
		return position;
	}



	public void setPosition(Vec3 position) {
		this.position = position;
	}



	public float getPolarAngle() {
		return polarAngle;
	}



	public void setPolarAngle(float polarAngle) {
		this.polarAngle = polarAngle;
	}



	public float getAzimuthAngle() {
		return azimuthAngle;
	}



	public void setAzimuthAngle(float azimuthAngle) {
		this.azimuthAngle = azimuthAngle;
	}



	public float getVelocity() {
		return velocity;
	}



	public void setVelocity(float velocity) {
		this.velocity = velocity;
	}





}
