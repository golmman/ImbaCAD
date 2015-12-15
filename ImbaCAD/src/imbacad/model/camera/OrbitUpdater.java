package imbacad.model.camera;

import java.awt.event.MouseEvent;

import imbacad.control.RenderingEventAdapter;
import imbacad.model.Vec3;

/**
 * Enables orbiting a point with a camera
 * @author Dirk Kretschmann
 *
 */
public class OrbitUpdater implements CameraUpdater {
	
	public OrbitUpdater() {}

	@Override
	public void update(Camera camera, RenderingEventAdapter events) {
		
		
		
		camera.lookAt(new Vec3());
		
		Vec3 newPos = camera.getPosition();
		float newAzi = camera.getAzimuthAngle();
		float newPol = camera.getPolarAngle();
		
		
		if (events.getButton(MouseEvent.BUTTON3)) {
			float relX = 0.003f * events.getMouseDx();
			float relY = 0.003f * events.getMouseDy();
			
			newPos = newPos.rotateZ(-newAzi);
			newPos = newPos.rotateY(-newPol);
			
			newPol += relY;
			newAzi -= relX;
			
			newPol = Camera.correctPol(newPol);
			newAzi = Camera.correctAzi(newAzi);
			
			newPos = newPos.rotateY(newPol);
			newPos = newPos.rotateZ(newAzi);
			
		}
		
		if (events.getMouseWheel() != 0) {
			newPos = newPos.add(camera.getLookingAt().mul(
					-0.05f * events.getMouseWheel() * camera.getPosition().norm()));
		}
		
		
		
		camera.setPosition(newPos);
		camera.setAzimuthAngle(newAzi);
		camera.setPolarAngle(newPol);
		
		events.reset();
	}

}
