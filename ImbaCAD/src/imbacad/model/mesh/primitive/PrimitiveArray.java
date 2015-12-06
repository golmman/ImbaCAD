package imbacad.model.mesh.primitive;

import java.util.ArrayList;

import imbacad.model.CopyFactory;

public class PrimitiveArray<P extends Primitive> extends ArrayList<P> {
	
	private static final long serialVersionUID = -8330805264678691468L;
	
	private int stride = 0;
	
	public PrimitiveArray() {
		super();
	}
	
	public PrimitiveArray(PrimitiveArray<P> primitives, CopyFactory<P> copyFactory) {
		super(primitives.size());
		this.stride = primitives.stride;
		
		for (P vertex: primitives) {
			this.add(copyFactory.copy(vertex));
		}
	}

	public PrimitiveArray(P[] primitives) {
		super(primitives.length);
		
		if (primitives.length == 0) return;
		
		stride = primitives[0].getIndices().length;
		
		for (int k = 0; k < primitives.length; ++k) {
			this.add(primitives[k]);
		}
	}
	
	@Override
	public boolean add(P e) {
		if (stride == 0) {
			stride = e.getIndices().length;
		}
		return super.add(e);
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
	
	public int getStride() {
		return stride;
	}
	
	public int getTotalBytes() {
		return 4 * stride * this.size();
	}

}
