package imbacad.model.mesh;

import java.util.ArrayList;

import imbacad.model.Vec2;
import imbacad.model.Vec3;

public class TestVertex extends Vertex2 {
	
	public static final int STRIDE = 8;
	
	public static final VertexFactory<TestVertex> FACTORY = new VertexFactory<TestVertex>() {
		@Override
		public TestVertex create(ArrayList<Float> f, int offset) {
			return new TestVertex(f, offset);
		}

		@Override
		public TestVertex create(float[] f, int offset) {
			return new TestVertex(f, offset);
		}
	};
	
	public Vec3 position = null;
	public Vec2 texture = null;
	public Vec3 normal = null;
	
	public TestVertex(ArrayList<Float> f, int offset) {
		position = new Vec3(f.get(offset+0), f.get(offset+2), f.get(offset+2));
		texture  = new Vec2(f.get(offset+3), f.get(offset+4));
		normal   = new Vec3(f.get(offset+5), f.get(offset+6), f.get(offset+7));
	}
	
	public TestVertex(float[] f, int offset) {
		position = new Vec3(f[offset+0], f[offset+2], f[offset+2]);
		texture  = new Vec2(f[offset+3], f[offset+4]);
		normal   = new Vec3(f[offset+5], f[offset+6], f[offset+7]);
	}
	
	
	public TestVertex(TestVertex vertex) {
		this.position = new Vec3(vertex.position);
		this.texture = new Vec2(vertex.texture);
		this.normal = new Vec3(vertex.normal);
	}
	
	public TestVertex(Vec3 position, Vec2 texture, Vec3 normal) {
		this.position = position;
		this.texture = texture;
		this.normal = normal;
	}

	public TestVertex(Vec3 position) {
		this.position = position;
		this.texture = new Vec2();
		this.normal = new Vec3();
	}
	
	public TestVertex(float posx, float posy, float posz) {
		this.position = new Vec3(posx, posy, posz);
		this.texture = new Vec2();
		this.normal = new Vec3();
	}
	
	public TestVertex(float posx, float posy, float posz, float texu, float texv, float norx, float nory, float norz) {
		this.position = new Vec3(posx, posy, posz);
		this.texture = new Vec2(texu, texv);
		this.normal = new Vec3(norx, nory, norz);
	}
	
	public TestVertex() {
		this.position = new Vec3();
		this.texture = new Vec2();
		this.normal = new Vec3();
	}
	

	@Override
	public float[] toFloats() {
		
		return null;
	}
	

}
