package imbacad.model.mesh.primitive;

/**
 * 
 * @author Dirk Kretschmann
 *
 */
public abstract class Primitive<T> {
	
	protected long id;
	protected int[] data;
	
	
	protected Primitive(int stride, long id) {
		this.id = id;
		this.data = new int[stride];
	}
	
	
	/**
	 * Returns integer array of indices.
	 * @return
	 */
	public int[] getData() {
		return data;
	}
	
	/**
	 * Sets indices.
	 * @param data
	 */
	public void setData(int[] data) {
		this.data = data;
	}


	public long getID() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Creates a copy of an instance of type T.
	 * @return
	 */
	public abstract T copy();
	
	@Override
	public String toString() {
		String s = "";
		for (int k = 0; k < data.length; ++k) {
			s += (data[k] + " ");
		}
		return s;
	}
}
