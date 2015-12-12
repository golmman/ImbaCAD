package imbacad.model.mesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

import com.jogamp.opengl.GL;

import imbacad.control.Selection;
import imbacad.model.Vec4;
import imbacad.model.mesh.primitive.Primitive;
import imbacad.model.mesh.primitive.PrimitiveArray;
import imbacad.model.mesh.vertex.ColorVertex;
import imbacad.model.mesh.vertex.TextureVertex;
import imbacad.model.mesh.vertex.Vertex;
import imbacad.model.mesh.vertex.VertexArray;

public class SelectionMesh<P extends Primitive<P>> extends ColorMesh<P> {
	
	private static long colorCount = 0L;
	private static HashMap<Long, ArrayList<Selection>> colorMap = new HashMap<Long, ArrayList<Selection>>();
	
	
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
		Vec4 oldColor = null;
		
		for (P prim: primitives) {
			
			ind = prim.getData();
			
			// change colours if ids change (note that primitives are sorted by id)
			currentID = prim.getID();
			if (currentID != lastID) {
				//color = nextColor();
				color = nextRandomColor();
			}
			
			// get the current colorMap key
			key = getKey(color);
			
			// save old colour
			if (mesh instanceof TextureMesh) {
				oldColor = ((TextureVertex)mesh.vertices.get(ind[0])).getColor();
			} else if (mesh instanceof ColorMesh) {
				oldColor = ((ColorVertex)mesh.vertices.get(ind[0])).getColor();
			}
			
			// assign colour to first vertex of each primitive
			vertices.get(ind[0]).setColor(color);
			
			// add vertex to colorMap
			if (colorMap.get(key) == null) {
				colorMap.put(key, new ArrayList<Selection>());
			}
			colorMap.get(key).add(new Selection(mesh, prim, oldColor));
			
			// remember last id
			lastID = currentID;
		}
		
		SelectionMesh<P> result = new SelectionMesh<P>(drawMode, vertices, primitives, mesh.name + "_SELECTION");
		result.setFlat(true);
		
		result.setPosition(mesh.position);
		result.setRotation(mesh.rotation);
		
//		System.out.println("KEYS");
//		for (Long keyz: colorMap.keySet()) {
//			System.out.println(keyR(keyz) + " " + keyG(keyz) + " " + keyB(keyz) + " " + keyA(keyz));
//		}
		
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
		
		//return new Vec4(r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat());
		
		return new Vec4(
				(float)r.nextInt(256) / 255.0f, 
				(float)r.nextInt(256) / 255.0f, 
				(float)r.nextInt(256) / 255.0f, 
				(float)r.nextInt(256) / 255.0f);
	}
	
	public static long getKey(Vec4 color) {
//		long r = (long)(Math.round(255.0f * color.getR())) << 24;
//		long g = (long)(Math.round(255.0f * color.getG())) << 16;
//		long b = (long)(Math.round(255.0f * color.getB())) << 8;
//		long a = (long)(Math.round(255.0f * color.getA()));
		
		long r = (long)(255.0f * color.getR()) << 24;
		long g = (long)(255.0f * color.getG()) << 16;
		long b = (long)(255.0f * color.getB()) << 8;
		long a = (long)(255.0f * color.getA());
		
		return r | g | b | a;
	}
	
	
	public static long getKey(int colr, int colg, int colb, int cola) {
		long r = (long)(colr) << 24;
		long g = (long)(colg) << 16;
		long b = (long)(colb) << 8;
		long a = (long)(cola);
		
		return r | g | b | a;
	}
	
	public static long keyR(long key) {
		return (key >> 24) & 0xFF;
	}
	
	public static long keyG(long key) {
		return (key >> 16) & 0xFF;
	}
	
	public static long keyB(long key) {
		return (key >> 8) & 0xFF;
	}
	
	public static long keyA(long key) {
		return key & 0xFF;
	}
	
	public static ArrayList<Selection> getVertices(long key) {
		return colorMap.get(key);
	}
	
	public static ArrayList<Selection> getVertices(Vec4 color) {
		return colorMap.get(getKey(color));
	}
}














