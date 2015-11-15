package imbacad.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jogamp.opengl.GL3;

import imbacad.ImbaCAD;
import imbacad.model.shader.HasUniforms;
import imbacad.model.shader.Shader;
import imbacad.model.shader.UniformFloat;
import imbacad.model.shader.UniformInt;
import imbacad.model.shader.UniformVec3;

public class Light implements HasUniforms {

	private static int maxLights = 0;
	
	private UniformInt uniformNum = null;	// TODO: only send this once?!
	
	private UniformInt uniformDir = null;
	private UniformVec3 uniformPos = null;
	private UniformVec3 uniformCol = null;
	private UniformFloat uniformAtt = null;
	private UniformFloat uniformAmb = null;
	
	private boolean isDirectional = false;
	private Vec3 position = null;
	private Vec3 color = null;
	private float attenuation = 0.0f;
	private float ambient = 0.0f;
	
	
	public Light(File fragmentShaderFile, boolean isDirectional, Vec3 position, Vec3 color, float attenuation, float ambient) {
		this.isDirectional = isDirectional;
		this.position = position;
		this.color = color;
		this.attenuation = attenuation;
		this.ambient = ambient;
		
		// update MAX_LIGHTS
		if (maxLights == 0) {
			try {
				FileReader fr = new FileReader(fragmentShaderFile);
				
				int c = 0;
				StringBuilder sb = new StringBuilder();
				while ((c = fr.read()) != -1) {
					sb.append((char)c);
				}
				fr.close();
				
				String text = sb.toString();
				
				Pattern pattern = Pattern.compile("(.*?)const int MAX_LIGHTS = (\\d*);");
				Matcher matcher = pattern.matcher(text);
				
				if (matcher.find()) {
					maxLights = Integer.parseInt(matcher.group(2));
				} else {
					throw new IllegalStateException("No 'const int MAX_LIGHTS' defined in " + fragmentShaderFile.getName() + ".");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (ImbaCAD.lights.size() >= maxLights){
			throw new IllegalStateException("Exceeding MAX_LIGHTS limit of" + maxLights + ".");
		}
	}
	
	@Override
	public void updateUniforms(GL3 gl, Shader shader) {
		
		if (uniformAmb == null) {
			uniformNum = new UniformInt(gl, shader, "numLights");
			
			int i = ImbaCAD.lights.indexOf(this);
			
			uniformDir = new UniformInt(gl, shader, "light[" + i + "].isDirectional");
			uniformPos = new UniformVec3(gl, shader, "light[" + i + "].position");
			uniformCol = new UniformVec3(gl, shader, "light[" + i + "].color");
			uniformAtt = new UniformFloat(gl, shader, "light[" + i + "].attenuation");
			uniformAmb = new UniformFloat(gl, shader, "light[" + i + "].ambient");
		}
		
		uniformNum.update(gl, ImbaCAD.lights.size());
		
		uniformDir.update(gl, isDirectional ? 1 : 0);
		uniformPos.update(gl, position);
		uniformCol.update(gl, color);
		uniformAtt.update(gl, attenuation);
		uniformAmb.update(gl, ambient);
	}
	

	public Vec3 getPosition() {
		return position;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public Vec3 getIntensities() {
		return color;
	}

	public void setIntensities(Vec3 intensities) {
		this.color = intensities;
	}

	public float getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(float attenuation) {
		this.attenuation = attenuation;
	}

	public float getAmbientCoefficient() {
		return ambient;
	}

	public void setAmbientCoefficient(float ambientCoefficient) {
		this.ambient = ambientCoefficient;
	}

	public static int getMaxLights() {
		return maxLights;
	}

}
