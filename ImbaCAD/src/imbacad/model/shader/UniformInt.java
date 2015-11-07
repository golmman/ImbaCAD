package imbacad.model.shader;

import com.jogamp.opengl.GL3;

public class UniformInt extends UniformObject<Integer> {

	public UniformInt(GL3 gl, Shader shader, String name) {
		super(gl, shader, name);
	}

	@Override
	public void update(GL3 gl) {
		gl.glUniform1i(location, value);
	}

}
