package imbacad.model.mesh.vertex;

import imbacad.model.Vec3;

public abstract class Vertex {
	
	public Vec3 position;
	
	public Vertex() {
		
	}
	
	public abstract float[] toFloats();

}
