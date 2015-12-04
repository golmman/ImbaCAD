package imbacad.model.mesh.primitive;

public class PrimitiveID {
	
	private static int count = 0;
	private static PrimitiveID last = null;
	
	private int id;
	
	
	
	public PrimitiveID() {
		this.id = PrimitiveID.count;
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
}
