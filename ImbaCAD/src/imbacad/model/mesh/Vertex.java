package imbacad.model.mesh;

import imbacad.model.Vec2;
import imbacad.model.Vec3;

public class Vertex {
	
	public static final int FLOATS_PER = 8;
	public static final int SIZE_OF = 4 * FLOATS_PER;
	
	public Vec3 position;
	public Vec2 texture;
	public Vec3 normal;
	
	public Vertex(Vertex vertex) {
		this.position = new Vec3(vertex.position);
		this.texture = new Vec2(vertex.texture);
		this.normal = new Vec3(vertex.normal);
	}
	
	
	public Vertex(Vec3 position, Vec2 texture, Vec3 normal) {
		this.position = position;
		this.texture = texture;
		this.normal = normal;
	}


	public Vertex(float posx, float posy, float posz) {
		this.position = new Vec3(posx, posy, posz);
		this.texture = new Vec2();
		this.normal = new Vec3();
	}
	
	public Vertex(float posx, float posy, float posz, float texu, float texv, float norx, float nory, float norz) {
		this.position = new Vec3(posx, posy, posz);
		this.texture = new Vec2(texu, texv);
		this.normal = new Vec3(norx, nory, norz);
	}
	
	public Vertex() {
		this.position = new Vec3();
		this.texture = new Vec2();
		this.normal = new Vec3();
	}

}
