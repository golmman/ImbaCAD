package imbacad.event;

import java.awt.event.MouseEvent;

import imbacad.rendering.Camera;
import imbacad.util.Vec3;

public class PanProcessor extends RenderingEventProcessor {


	public PanProcessor(RenderingEventAdapter events) {
		super(events);
	}

	@Override
	public void process(Camera camera) {
		// calculate normalized direction the camera is looking at
		Vec3 n = new Vec3(
			(float)(Math.sin(camera.getPolarAngle()) * Math.cos(camera.getAzimuthAngle())),
			(float)(Math.sin(camera.getPolarAngle()) * Math.sin(camera.getAzimuthAngle())),	
			(float)(Math.cos(camera.getPolarAngle())));	
		
		Vec3 n_xy = new Vec3(n.getX(), n.getY(), 0.0f);
		//Vec3 n_z = new Vec3(0, 0, n.getZ());
		
		float a = n.norm() / n_xy.norm();
		float b = -n.getZ() / (n.norm() * n_xy.norm());
		
		Vec3 up = Vec3.AXIS_Z.mul(a).add(n.mul(b));
		Vec3 right = n.cross(up);
		
		
		Vec3 newPos = camera.getPosition();
		
		if (events.getButton(MouseEvent.BUTTON3)) {
			newPos = newPos.add(right.mul(camera.getVelocity()* events.getMouseDx()));
			newPos = newPos.add(up.mul(camera.getVelocity() * -events.getMouseDy()));
		}
		
		
		camera.setPosition(newPos);
		
		events.resetMouseDelta();
	}

}
