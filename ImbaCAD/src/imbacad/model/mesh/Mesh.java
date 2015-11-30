package imbacad.model.mesh;

import com.jogamp.opengl.GL3;

import imbacad.model.Vec3;
import imbacad.model.shader.Shader;

public abstract class Mesh {
	protected String name = null;
	protected Vec3 position = new Vec3();
	protected Vec3 rotation = new Vec3();
	
	
	public Mesh(String name) {
		this.name = name;
	}
	
	
	public abstract void draw(GL3 gl, Shader shader);
	public abstract void init(GL3 gl);
	public abstract void dispose(GL3 gl);
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Vec3 getPosition() {
		return position;
	}
	public void setPosition(Vec3 position) {
		this.position = position;
	}
	public Vec3 getRotation() {
		return rotation;
	}
	public void setRotation(Vec3 rotation) {
		this.rotation = rotation;
	}
}
