package imbacad.model.mesh.vertex;

import imbacad.model.Vec3;
import imbacad.model.Vec4;

public class ColorVertex extends Vertex {

	
	public Vec3 position;
	public Vec4 color;
	
	public ColorVertex(float[] f, int offset) {
		position = new Vec3(f[offset+0], f[offset+1], f[offset+2]);
		color  = new Vec4(f[offset+3], f[offset+4], f[offset+5], f[offset+6]);
	}
	
	public ColorVertex(float posx, float posy, float posz, float r, float g, float b, float a) {
		this.position = new Vec3(posx, posy, posz);
		this.color = new Vec4(r, g, b, a);
	}

	@Override
	public float[] toFloats() {
		float[] floats = {
				position.getX(), position.getY(), position.getZ(),
				color.getR(), color.getG(), color.getB(), color.getA()};
		return floats;
	}

}
