package imbacad.model;

import com.jogamp.opengl.GL3;

import imbacad.model.shader.HasUniforms;
import imbacad.model.shader.Shader;
import imbacad.model.shader.UniformFloat;
import imbacad.model.shader.UniformInt;
import imbacad.model.shader.UniformVec3;

public class Material implements HasUniforms {
	
	private UniformFloat uniformTex = null;
	private UniformFloat uniformShi = null;
	private UniformVec3 uniformSpeCol = null;
	private UniformInt uniformFlatNor = null;
	
	private float texture = 0.0f;
	private float shininess = 200.0f;
	private Vec3 specularColor = new Vec3(1.0f, 1.0f, 1.0f);
	private boolean flatNormals = false;
	
	
	
	public Material() {}

	@Override
	public void updateUniforms(GL3 gl, Shader shader) {
		if (uniformTex == null) {
			uniformTex = new UniformFloat(gl, shader, "material.texture");
			uniformShi = new UniformFloat(gl, shader, "material.shininess");
			uniformSpeCol = new UniformVec3(gl, shader, "material.specularColor");
			uniformFlatNor = new UniformInt(gl, shader, "material.flatNormals");
		}
		
		uniformTex.update(gl, texture);
		uniformShi.update(gl, shininess);
		uniformSpeCol.update(gl, specularColor);
		uniformFlatNor.update(gl, flatNormals ? 1 : 0);
	}

	public float getTexture() {
		return texture;
	}

	public void setTexture(float texture) {
		this.texture = texture;
	}

	public float getShininess() {
		return shininess;
	}

	public void setShininess(float shininess) {
		this.shininess = shininess;
	}

	public Vec3 getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Vec3 specularColor) {
		this.specularColor = specularColor;
	}

	public boolean isFlatNormals() {
		return flatNormals;
	}

	public void setFlatNormals(boolean flatNormals) {
		this.flatNormals = flatNormals;
	}

}
