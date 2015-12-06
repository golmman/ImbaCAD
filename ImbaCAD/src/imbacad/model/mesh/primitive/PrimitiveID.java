package imbacad.model.mesh.primitive;

import java.util.HashMap;

import imbacad.model.Vec4;

public class PrimitiveID {
	
	private static HashMap<Long, PrimitiveID> pridMap = new HashMap<Long, PrimitiveID>();
	
	private static long count = 0;
	private static PrimitiveID last = null;
	
	private long id;
	
	
	public static void main(String[] args) {
		PrimitiveID.count = 0xFFAE1BC4L;
		PrimitiveID prid0 = new PrimitiveID();
		
		PrimitiveID.count = 0xF102FBA6L;
		PrimitiveID prid1 = new PrimitiveID();
		
		
		System.out.printf("%X %X\n", PrimitiveID.toID(toColor(prid0)).id, prid0.id);
		System.out.printf("%X %X\n", PrimitiveID.toID(toColor(prid1)).id, prid1.id);
	}
	
	
	public PrimitiveID() {
		
		this.id = PrimitiveID.count;
		PrimitiveID.pridMap.put(this.id, this);
		
		PrimitiveID.last = this;
		++PrimitiveID.count;
	}
	
	public boolean equals(PrimitiveID prid) {
		return this.id == prid.id;
	}
	
	/**
	 * Returns the last created PrimitiveID object.
	 * @return
	 */
	public static PrimitiveID getLast() {
		return PrimitiveID.last;
	}
	
	public static Vec4 toColor(PrimitiveID prid) {
		long r = (prid.id & 0xFF000000) >> 24;
		long g = (prid.id & 0xFF0000) >> 16;
		long b = (prid.id & 0xFF00) >> 8;
		long a = prid.id & 0xFF;
		
		float f = 1.0f / 255.0f;
		
		return new Vec4(f * r, f * g, f * b, f * a);
	}
	
	
	public static PrimitiveID toID(Vec4 color) {
		long r = (long)(255.0f * color.getR()) << 24;
		long g = (long)(255.0f * color.getG()) << 16;
		long b = (long)(255.0f * color.getB()) << 8;
		long a = (long)(255.0f * color.getA());
		
		return pridMap.get(r | g | b | a);
	}
	
	public static PrimitiveID toID(int cr, int cg, int cb, int ca) {
		long r = (long)(cr) << 24;
		long g = (long)(cg) << 16;
		long b = (long)(cb) << 8;
		long a = (long)(ca);
		
		return pridMap.get(r | g | b | a);
	}
}
