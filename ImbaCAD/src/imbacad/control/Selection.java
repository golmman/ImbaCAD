package imbacad.control;

import imbacad.model.Vec4;
import imbacad.model.mesh.Mesh;
import imbacad.model.mesh.primitive.Primitive;

public class Selection {
	
	private Mesh<?, ?> mesh;
	private Primitive<?> primitive;
	private Vec4 oldColor;
	

	public Selection(Mesh<?, ?> mesh, Primitive<?> primitive, Vec4 oldColor) {
		super();
		this.mesh = mesh;
		this.primitive = primitive;
		this.oldColor = oldColor;
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

	public Vec4 getOldColor() {
		return oldColor;
	}

	public void setOldColor(Vec4 oldColor) {
		this.oldColor = oldColor;
	}
}
