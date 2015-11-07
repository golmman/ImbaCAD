package imbacad.model.shader;

import com.jogamp.opengl.GL3;

public interface HasUniforms {
	public void updateUniforms(GL3 gl, Shader shader);
}
