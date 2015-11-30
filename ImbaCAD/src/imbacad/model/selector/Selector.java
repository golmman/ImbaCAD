package imbacad.model.selector;

import java.util.Collection;

import com.jogamp.opengl.GLAutoDrawable;

import imbacad.control.RenderingEventAdapter;
import imbacad.model.mesh.TextureMesh;

public abstract class Selector {
	
//	private Mesh selectedMesh;
//	private Selectable selection;
	
	
	public Selector() {}
	
	
	public abstract void update(GLAutoDrawable drawable, Collection<TextureMesh> meshes, float[] mvp, RenderingEventAdapter events);
}
