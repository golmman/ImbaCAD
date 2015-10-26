package imbacad.model.camera;

import java.awt.event.MouseEvent;

import imbacad.control.RenderingEventAdapter;
import imbacad.model.Vec3;

/**
 * 
 * Enables camera panning
 * @author Dirk Kretschmann
 *
 */
public class PanUpdater implements CameraUpdater {


	public PanUpdater() {}

	@Override
	public void update(Camera camera, RenderingEventAdapter events) {
		Vec3 n = camera.getLookingAt();
		
		float z0 = 1.0f - n.getZ() * n.getZ();
		float z1 = 1.0f / (float)Math.sqrt(z0);
		
		Vec3 up = new Vec3(-z1 * n.getX() * n.getZ(), -z1 * n.getY() * n.getZ(), z1 * z0);
		Vec3 right = n.cross(up);
		
		
		Vec3 newPos = camera.getPosition();
		
		if (events.getButton(MouseEvent.BUTTON3)) {
			newPos = newPos.sub(right.mul(camera.getVelocity()* events.getMouseDx()));
			newPos = newPos.add(up.mul(camera.getVelocity() * events.getMouseDy()));
		}
		
		
		camera.setPosition(newPos);
		
		events.reset();
	}

}
