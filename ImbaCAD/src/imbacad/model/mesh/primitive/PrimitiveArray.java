package imbacad.model.mesh.primitive;

import java.util.ArrayList;


public class PrimitiveArray<P extends Primitive<P>> extends ArrayList<P> {
	
	private static final long serialVersionUID = -8330805264678691468L;
	
	private int stride = 0;
	
	public PrimitiveArray() {
		super();
	}
	
	public PrimitiveArray(PrimitiveArray<P> primitives) {
		super(primitives.size());
		this.stride = primitives.stride;
		
		for (P primitive: primitives) {
			this.add(primitive.copy());
		}
	}

	public PrimitiveArray(P[] primitives) {
		super(primitives.length);
		
		if (primitives.length == 0) return;
		
		stride = primitives[0].getData().length;
		
		for (int k = 0; k < primitives.length; ++k) {
			this.add(primitives[k].copy());
		}
	}
	
	@Override
	public boolean add(P e) {
		if (stride == 0) {
			stride = e.getData().length;
		}
		return super.add(e.copy());
	}
	
	public int[] toInts() {
		int[] result = new int[this.size() * stride];
		int[] indices;
		
		for (int i = 0; i < this.size(); ++i) {
			indices = this.get(i).getData();
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
