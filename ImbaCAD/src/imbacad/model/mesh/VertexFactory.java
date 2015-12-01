package imbacad.model.mesh;

import java.util.ArrayList;

public interface VertexFactory<T> {
	T create(ArrayList<Float> f, int offset);
	T create(float[] f, int offset);
}
