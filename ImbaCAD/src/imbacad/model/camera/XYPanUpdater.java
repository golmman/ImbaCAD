package imbacad.model.camera;

import java.awt.event.MouseEvent;

import imbacad.control.RenderingEventAdapter;
import imbacad.model.Vec3;

public class XYPanUpdater implements CameraUpdater {

	public XYPanUpdater() {}

	@Override
	public void update(Camera camera, RenderingEventAdapter events) {
		Vec3 newPos = camera.getPosition();
		float newAzi = camera.getAzimuthAngle();
		float newPol = camera.getPolarAngle();
		
		
		if (events.getMouseWheel() != 0) {
			newPos = newPos.add(camera.getLookingAt().mul(
					-0.05f * events.getMouseWheel() * camera.getPosition().getZ()));
		}
		
		
		if (events.getButton(MouseEvent.BUTTON1)) {
			Vec3 move = new Vec3((float)events.getMouseDy(), (float)events.getMouseDx(), 0.0f);
			move = move.rotateZ(newAzi);
			newPos = newPos.add(move.mul(0.003f * Math.abs(camera.getPosition().getZ())));
		}
		
		
		if (events.getButton(MouseEvent.BUTTON3)) {
			newAzi -= 0.003f * events.getMouseDx();
			newPol += 0.003f * events.getMouseDy();
		}
		
		newPol = Camera.correctPol(newPol);
		newAzi = Camera.correctAzi(newAzi);
		
		camera.setPosition(newPos);
		camera.setAzimuthAngle(newAzi);
		camera.setPolarAngle(newPol);
		
		events.reset();

	}

}
