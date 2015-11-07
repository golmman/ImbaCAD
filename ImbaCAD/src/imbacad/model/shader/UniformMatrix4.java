package imbacad.model.shader;

import com.jogamp.opengl.GL3;

/**
 * TODO: Change generic type to Matrix4
 * @author Dirk Kretschmann
 *
 */
public class UniformMatrix4 extends UniformObject<float[]> {

	public UniformMatrix4(GL3 gl, Shader shader, String name) {
		super(gl, shader, name);
	}

	@Override
	public void update(GL3 gl) {
		gl.glUniformMatrix4fv(location, 1, false, value, 0);
	}

}
