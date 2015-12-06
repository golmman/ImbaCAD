package imbacad.model.mesh;

import imbacad.model.mesh.primitive.Primitive;
import imbacad.model.mesh.vertex.Vertex;

public class Entity<V extends Vertex, P extends Primitive> {
	
	private Mesh<V, P> displayMesh = null;
	private ColorMesh<P> selectionMesh = null;
	
	public Entity(Mesh<V, P> displayMesh) {
		this.displayMesh = displayMesh;
	}

}
