package imbacad.model.mesh.primitive;

import java.util.ArrayList;

public class PrimitiveArray<T extends Primitive> extends ArrayList<T> {
	
	private static final long serialVersionUID = -8330805264678691468L;
	
	private int stride = 0;

	public PrimitiveArray(T[] primitives) {
		super(primitives.length);
		
		if (primitives.length == 0) return;
		
		stride = primitives[0].getIndices().length;
		
		for (int k = 0; k < primitives.length; ++k) {
			this.add(primitives[k]);
		}
	}
	
	public int[] toInts() {
		int[] result = new int[this.size() * stride];
		int[] indices;
		
		for (int i = 0; i < this.size(); ++i) {
			indices = this.get(i).getIndices();
			for (int j = 0; j < stride; ++j) {
				result[i * stride + j] = indices[j];
			}
		}
		
		return result;
	}

}
