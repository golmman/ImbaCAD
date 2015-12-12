package imbacad.control;

import imbacad.model.mesh.Mesh;
import imbacad.model.mesh.primitive.Primitive;

public class Selection {
	
	private Mesh<?, ?> mesh;
	private Primitive<?> primitive;
	
	public Selection(Mesh<?, ?> mesh, Primitive<?> primitive) {
		super();
		this.mesh = mesh;
		this.primitive = primitive;
	}
	
	
	

	public Mesh<?, ?> getMesh() {
		return mesh;
	}

	public void setMesh(Mesh<?, ?> mesh) {
		this.mesh = mesh;
	}

	public Primitive<?> getPrimitive() {
		return primitive;
	}

	public void setPrimitive(Primitive<?> primitive) {
		this.primitive = primitive;
	}
}
