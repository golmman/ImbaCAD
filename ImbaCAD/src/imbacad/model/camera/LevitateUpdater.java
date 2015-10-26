package imbacad.model.camera;


import imbacad.control.RenderingEventAdapter;
import imbacad.model.Vec3;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


/**
 * Enables free Flying
 * @author Dirk Kretschmann
 *
 */
public class LevitateUpdater implements CameraUpdater {
	

	public LevitateUpdater() {}
	
	@Override
	public void update(Camera camera, RenderingEventAdapter events) {
		Vec3 lookingAt = camera.getLookingAt();	
		
		// clear z and normalize, used in strafe movement
		Vec3 lookingAt2 = new Vec3(lookingAt);
		lookingAt2.setZ(0.0f);
		lookingAt2 = lookingAt2.mul(1.0f / lookingAt2.norm());
		
		Vec3 newPos = camera.getPosition();
		float newAzi = camera.getAzimuthAngle();
		float newPol = camera.getPolarAngle();
		
		if (events.getKey(KeyEvent.VK_W)) {
			newPos = newPos.add(lookingAt.mul(camera.getVelocity()));
		}
		if (events.getKey(KeyEvent.VK_A)) {
			newPos = newPos.add(Vec3.AXIS_Z.cross(lookingAt2.mul(camera.getVelocity())));
		}
		if (events.getKey(KeyEvent.VK_S)) {
			newPos = newPos.sub(lookingAt.mul(camera.getVelocity()));
		}
		if (events.getKey(KeyEvent.VK_D)) {
			newPos = newPos.sub(Vec3.AXIS_Z.cross(lookingAt2.mul(camera.getVelocity())));
		}
		if (events.getKey(KeyEvent.VK_SPACE)) {
			newPos = newPos.add(Vec3.AXIS_Z.mul(camera.getVelocity()));
		}
		if (events.getKey(KeyEvent.VK_SHIFT)) {
			newPos = newPos.sub(Vec3.AXIS_Z.mul(camera.getVelocity()));
		}
		
		
		
		if (events.getButton(MouseEvent.BUTTON3)) {
			newAzi -= 0.003f * events.getMouseDx();
			newPol += 0.003f * events.getMouseDy();
		}

		
		if (newAzi >= 2.0f * Math.PI) {
			float d = (float)(newAzi / (2.0f * Math.PI));
			newAzi = d - (float)Math.floor(d);
		} else if (newAzi < 0.0f) {
			float d = (float)(-newAzi / (2.0f * Math.PI));
			newAzi = (float)(2.0f * Math.PI - (d - (float)Math.floor(d)));
		}
		
		
		camera.setPosition(newPos);
		camera.setAzimuthAngle(newAzi);
		camera.setPolarAngle(newPol);
		
		events.reset();
	}


	


}
