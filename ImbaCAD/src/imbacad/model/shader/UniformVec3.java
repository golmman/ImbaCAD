package imbacad.model.shader;

import com.jogamp.opengl.GL3;

import imbacad.model.Vec3;

public class UniformVec3 extends UniformObject<Vec3> {

	public UniformVec3(GL3 gl, Shader shader, String name) {
		super(gl, shader, name);
	}

	@Override
	public void update(GL3 gl) {
		gl.glUniform3fv(location, 1, value.toArray(), 0);
	}
	

}
