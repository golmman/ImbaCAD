package imbacad.model.shader;

import com.jogamp.opengl.GL3;

public abstract class UniformObject<T> {
	
	protected Shader shader;
	protected String name;
	protected T value;
	protected int location;
	
	public UniformObject(GL3 gl, Shader shader, String name) {
		this.shader = shader;
		this.name = name;
		this.value = null;
		
		this.location = shader.getUniformLocation(gl, name);
	}

	public abstract void update(GL3 gl);
	
	public void update(GL3 gl, T value) {
		set(value);
		update(gl);
	}


	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
	}



}
