package imbacad.model.mesh.vertex;

import imbacad.model.Vec2;
import imbacad.model.Vec3;
import imbacad.model.Vec4;

public class TextureVertex extends Vertex<TextureVertex> {
	
	public static final int STRIDE = 12;
	
	public TextureVertex(float[] f, int offset) {
		super(STRIDE);
		
		data[0] = f[offset+0];
		data[1] = f[offset+1];
		data[2] = f[offset+2];
		
		data[3] = f[offset+3];
		data[4] = f[offset+4];
		
		data[5] = f[offset+5];
		data[6] = f[offset+6];
		data[7] = f[offset+7];
		
		data[8] = f[offset+8];
		data[9] = f[offset+9];
		data[10] = f[offset+10];
		data[11] = f[offset+11];
	}
	
	
	public TextureVertex(TextureVertex vertex) {
		super(STRIDE);
		
		data[0] = vertex.data[0];
		data[1] = vertex.data[1];
		data[2] = vertex.data[2];
		
		data[3] = vertex.data[3];
		data[4] = vertex.data[4];
		
		data[5] = vertex.data[5];
		data[6] = vertex.data[6];
		data[7] = vertex.data[7];
		
		data[8] = vertex.data[8];
		data[9] = vertex.data[9];
		data[10] = vertex.data[10];
		data[11] = vertex.data[11];
	}
	
	public TextureVertex(Vec3 position, Vec2 texture, Vec3 normal, Vec4 color) {
		super(STRIDE);
		
		data[0] = position.getX();
		data[1] = position.getY();
		data[2] = position.getZ();
		
		data[3] = texture.getU();
		data[4] = texture.getV();
		
		data[5] = normal.getX();
		data[6] = normal.getY();
		data[7] = normal.getZ();
		
		data[8] = color.getR();
		data[9] = color.getG();
		data[10] = color.getB();
		data[11] = color.getA();
	}

	public TextureVertex(Vec3 position) {
		super(STRIDE);
		
		data[0] = position.getX();
		data[1] = position.getY();
		data[2] = position.getZ();
		
		data[3] = 0.0f;
		data[4] = 0.0f;
		
		data[5] = 0.0f;
		data[6] = 0.0f;
		data[7] = 0.0f;
		
		data[8] = 0.0f;
		data[9] = 0.0f;
		data[10] = 0.0f;
		data[11] = 0.0f;
	}
	
	public TextureVertex(float posx, float posy, float posz) {
		super(STRIDE);
		
		data[0] = posx;
		data[1] = posy;
		data[2] = posz;
		
		data[3] = 0.0f;
		data[4] = 0.0f;
		
		data[5] = 0.0f;
		data[6] = 0.0f;
		data[7] = 0.0f;
		
		data[8] = 0.0f;
		data[9] = 0.0f;
		data[10] = 0.0f;
		data[11] = 0.0f;
	}
	
	public TextureVertex(
			float posx, float posy, float posz, 
			float texu, float texv, 
			float norx, float nory, float norz,
			float colr, float colg, float colb, float cola) {
		super(STRIDE);
		
		data[0] = posx;
		data[1] = posy;
		data[2] = posz;
		
		data[3] = texu;
		data[4] = texv;
		
		data[5] = norx;
		data[6] = nory;
		data[7] = norz;
		
		data[8] = colr;
		data[9] = colg;
		data[10] = colb;
		data[11] = cola;
	}
	
	public TextureVertex() {
		super(STRIDE);

		data[0] = 0.0f;
		data[1] = 0.0f;
		data[2] = 0.0f;
		
		data[3] = 0.0f;
		data[4] = 0.0f;
		
		data[5] = 0.0f;
		data[6] = 0.0f;
		data[7] = 0.0f;
		
		data[8] = 0.0f;
		data[9] = 0.0f;
		data[10] = 0.0f;
		data[11] = 0.0f;
	}
	
	

	public Vec2 getTexture() {
		return new Vec2(data[3], data[4]);
	}


	public void setTexture(Vec2 texture) {
		data[3] = texture.getU();
		data[4] = texture.getV();
	}


	public Vec3 getNormal() {
		return new Vec3(data[5], data[6], data[7]);
	}


	public void setNormal(Vec3 normal) {
		data[5] = normal.getX();
		data[6] = normal.getY();
		data[7] = normal.getZ();
	}
	
	public Vec4 getColor() {
		return new Vec4(data[8], data[9], data[10], data[11]);
	}
	
	public void setColor(Vec4 color) {
		data[8] = color.getR();
		data[9] = color.getG();
		data[10] = color.getB();
		data[11] = color.getA();
	}


	@Override
	public TextureVertex copy() {
		return new TextureVertex(this);
	}

}
