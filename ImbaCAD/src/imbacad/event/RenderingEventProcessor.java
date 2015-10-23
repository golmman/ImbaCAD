package imbacad.event;

import imbacad.rendering.Camera;

public abstract class RenderingEventProcessor {
	
	protected RenderingEventAdapter events;
	
	public RenderingEventProcessor(RenderingEventAdapter events) {
		this.events = events;
	}
	
	public abstract void process(Camera camera);
}
