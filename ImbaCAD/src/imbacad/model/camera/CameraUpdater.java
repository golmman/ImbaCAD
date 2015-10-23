package imbacad.model.camera;

import imbacad.control.RenderingEventAdapter;

/**
 * 
 * Updates a camera based on the given events.
 * @author Dirk Kretschmann
 *
 */
public interface CameraUpdater {
	
	/**
	 * Updates the camera
	 * @param camera
	 * @param events
	 */
	public void update(Camera camera, RenderingEventAdapter events);
}
