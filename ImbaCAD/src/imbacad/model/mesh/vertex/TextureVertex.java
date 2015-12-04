package imbacad.model.mesh.vertex;

import imbacad.model.Vec2;
import imbacad.model.Vec3;

public class TextureVertex extends Vertex {
	
	public Vec3 position = null;
	public Vec2 texture = null;
	public Vec3 normal = null;
	
	public TextureVertex(float[] f, int offset) {
		position = new Vec3(f[offset+0], f[offset+1], f[offset+2]);
		texture  = new Vec2(f[offset+3], f[offset+4]);
		normal   = new Vec3(f[offset+5], f[offset+6], f[offset+7]);
	}
	
	
	public TextureVertex(TextureVertex vertex) {
		this.position = new Vec3(vertex.position);
		this.texture = new Vec2(vertex.texture);
		this.normal = new Vec3(vertex.normal);
	}
	
	public TextureVertex(Vec3 position, Vec2 texture, Vec3 normal) {
		this.position = position;
		this.texture = texture;
		this.normal = normal;
	}

	public TextureVertex(Vec3 position) {
		this.position = position;
		this.texture = new Vec2();
		this.normal = new Vec3();
	}
	
	public TextureVertex(float posx, float posy, float posz) {
		this.position = new Vec3(posx, posy, posz);
		this.texture = new Vec2();
		this.normal = new Vec3();
	}
	
	public TextureVertex(float posx, float posy, float posz, float texu, float texv, float norx, float nory, float norz) {
		this.position = new Vec3(posx, posy, posz);
		this.texture = new Vec2(texu, texv);
		this.normal = new Vec3(norx, nory, norz);
	}
	
	public TextureVertex() {
		this.position = new Vec3();
		this.texture = new Vec2();
		this.normal = new Vec3();
	}
	

	@Override
	public float[] toFloats() {
		float[] floats = {
				position.getX(), position.getY(), position.getZ(),
				texture.getX(), texture.getY(),
				normal.getX(), normal.getY(), normal.getZ()};
		return floats;
	}
	

}
