package imbacad.model.mesh.vertex;

import imbacad.model.Vec3;

public abstract class Vertex<T> {
	
	protected float[] data;
	
	public Vertex(int stride) {
		data = new float[stride];
	}
	
	

	public Vec3 getPosition() {
		return new Vec3(data[0], data[1], data[2]);
	}

	public void setPosition(Vec3 position) {
		data[0] = position.getX();
		data[1] = position.getY();
		data[2] = position.getZ();
	}
	
	/**
	 * Returns the vertex data as float array.
	 * @return
	 */
	public float[] getData() {
		return data;
	}
	
	/**
	 * Creates a copy of an instance of type T.
	 * @return
	 */
	public abstract T copy();
}
