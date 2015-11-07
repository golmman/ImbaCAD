package imbacad.model.shader;

import com.jogamp.opengl.GL3;

public class UniformFloat extends UniformObject<Float> {

	public UniformFloat(GL3 gl, Shader shader, String name) {
		super(gl, shader, name);
	}

	
	@Override
	public void update(GL3 gl) {
		gl.glUniform1f(location, value);
	}

}
