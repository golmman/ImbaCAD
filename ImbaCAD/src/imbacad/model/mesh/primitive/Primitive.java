package imbacad.model.mesh.primitive;

/**
 * 
 * @author Dirk Kretschmann
 *
 */
public abstract class Primitive {
	
	private PrimitiveID id;
	
	protected Primitive(PrimitiveID id) {
		this.id = id;
	}
	
	
	public abstract int[] getIndices();
	public abstract void setIndices(int[] indices);
	public abstract int getDrawMode();


	public PrimitiveID getId() {
		return id;
	}


	public void setId(PrimitiveID id) {
		this.id = id;
	}
	

}
