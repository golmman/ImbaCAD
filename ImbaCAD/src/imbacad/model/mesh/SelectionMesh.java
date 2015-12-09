package imbacad.model.mesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import com.jogamp.opengl.GL;

import imbacad.model.Vec4;
import imbacad.model.mesh.primitive.Primitive;
import imbacad.model.mesh.primitive.PrimitiveArray;
import imbacad.model.mesh.vertex.ColorVertex;
import imbacad.model.mesh.vertex.Vertex;
import imbacad.model.mesh.vertex.VertexArray;

public class SelectionMesh<P extends Primitive<P>> extends ColorMesh<P> {
	
	private static long colorCount = 0L;
	private static HashMap<Long, ArrayList<Vertex<?>>> colorMap = new HashMap<Long, ArrayList<Vertex<?>>>();
	
	
	protected SelectionMesh(int drawMode, VertexArray<ColorVertex> vertices, PrimitiveArray<P> primitives, String name) {
		super(drawMode, vertices, primitives, name);
	}
	
	
	/**
	 * 
	 * @param mesh
	 * @return
	 */
	public static <P extends Primitive<P>> SelectionMesh<P> createSelectionMesh(Mesh<?, P> mesh) {
		
		if (!mesh.isFlat()) {
			throw new UnsupportedOperationException("Input mesh has to be flat.");
		}
		
		// determine draw mode
		int drawMode = -1;
		if (mesh instanceof ColorMesh<?>) {
			drawMode = ((ColorMesh<?>)mesh).getDrawMode();
		} else if (mesh instanceof TextureMesh) {
			drawMode = GL.GL_TRIANGLES;
		}
		
		// copy primitive data
		PrimitiveArray<P> primitives = new PrimitiveArray<P>(mesh.primitives);
		primitives.sort(new Comparator<P>() {
			@Override
			public int compare(P o1, P o2) {
				return o1.getID() < o2.getID() ? -1 :
					   o1.getID() > o2.getID() ?  1 : 0;
			}
		});
		
		// copy position data
		VertexArray<ColorVertex> vertices = new VertexArray<ColorVertex>();
		for (Vertex<?> vertex: mesh.vertices) {
			vertices.add(new ColorVertex(vertex.getPosition(), new Vec4()));
		}
		
		
		// add colours
		int[] ind;
		long currentID = 0L, lastID = 933466737971350058L; // should not use that number as id...
		long key;
		Vec4 color = null;
		
		for (P prim: primitives) {
			
			// change colours if ids change (note that primitives is sorted)
			currentID = prim.getID();
			if (currentID != lastID) {
				//color = nextColor();
				color = nextRandomColor();
			}
			
			// get the current colorMap key
			key = getKey(color);
			
			// assign colour to first vertex of each primitive
			ind = prim.getData();
			vertices.get(ind[0]).setColor(color);
			
			// add vertex to colorMap
			if (colorMap.get(key) == null) {
				colorMap.put(key, new ArrayList<Vertex<?>>());
			}
			colorMap.get(key).add(mesh.vertices.get(ind[0]));
			
			// remember last id
			lastID = currentID;
		}
		
		SelectionMesh<P> result = new SelectionMesh<P>(drawMode, vertices, primitives, mesh.name + "_SELECTION");
		result.setFlat(true);
		
		return result;
	}
	
	
	
	
	protected static Vec4 nextColor() {
		long r = (colorCount & 0xFF000000) >> 24;
		long g = (colorCount & 0xFF0000) >> 16;
		long b = (colorCount & 0xFF00) >> 8;
		long a = colorCount & 0xFF;
		
		float f = 1.0f / 255.0f;
		
		++SelectionMesh.colorCount;
		
		return new Vec4(f * r, f * g, f * b, f * a);
	}
	
	protected static Vec4 nextRandomColor() {
		Random r = new Random();		
		return new Vec4(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat());
	}
	
	protected static long getKey(Vec4 color) {
		long r = (long)(255.0f * color.getR()) << 24;
		long g = (long)(255.0f * color.getG()) << 16;
		long b = (long)(255.0f * color.getB()) << 8;
		long a = (long)(255.0f * color.getA());
		
		return r | g | b | a;
	}
	
	
	public static ArrayList<Vertex<?>> getVertices(Vec4 color) {
		return colorMap.get(getKey(color));
	}
}














