package imbacad.model.mesh;

import java.util.ArrayList;
import java.util.LinkedList;

import com.jogamp.opengl.GL3;

import imbacad.model.Vec3;
import imbacad.model.mesh.primitive.Primitive;
import imbacad.model.mesh.primitive.PrimitiveArray;
import imbacad.model.mesh.vertex.Vertex;
import imbacad.model.mesh.vertex.VertexArray;
import imbacad.model.shader.HasUniforms;
import imbacad.model.shader.Shader;

public abstract class Mesh<V extends Vertex<V>, P extends Primitive<P>> implements HasUniforms {
	
	protected int[] vao = new int[1];	// name for vertex array object
	protected int[] vbo = new int[1];	// name for vertex buffer object
	protected int[] ibo = new int[1];	// name for index buffer object
	
	protected VertexArray<V> originalVertices;
	protected PrimitiveArray<P> originalPrimitives;
	protected VertexArray<V> vertices;
	protected PrimitiveArray<P> primitives;
	protected String name = null;
	
	protected Vec3 position = new Vec3();
	protected Vec3 rotation = new Vec3();
	
	protected boolean flat;
	
	protected ColorMesh<P> selectionMesh;
	
	/**
	 * Creates copies of vertices and primitives for further use.
	 * @param vertices
	 * @param primitives
	 * @param name
	 * @param vcf
	 * @param pcf
	 */
	protected Mesh(VertexArray<V> vertices, PrimitiveArray<P> primitives, String name) {
		this.originalVertices = vertices;
		this.originalPrimitives = primitives;
		this.vertices = new VertexArray<V>(vertices);
		this.primitives = new PrimitiveArray<P>(primitives);
		this.name = name;
	}
	
	protected Mesh(Mesh<V, P> mesh) {
		this.originalVertices = mesh.originalVertices;
		this.originalPrimitives = mesh.originalPrimitives;
		this.vertices = new VertexArray<V>(mesh.vertices);
		this.primitives = new PrimitiveArray<P>(mesh.primitives);
		this.name = mesh.name;
	}
	
	/**
	 * Modifies vertices so that each starting index of each primitive is unique,
	 * then flat shading can be applied, e.g. with flat colours or flat normals.
	 */
	protected void makeFlatData() {
		
		int[] ind, ind2;
		
		for (P prim: primitives) {
			ind = prim.getData();
			
			// get normal, add vertex
			vertices.add(vertices.get(ind[0]).copy());
			
			// set starting index to newly added vertex
			int oldIndex = ind[0];
			int newIndex = vertices.size() - 1;
			ind[0] = vertices.size() - 1;
			
			// set all indices - which are not provoking but used the old index - to the new index
			for (int k = 0; k < primitives.size(); ++k) {
				ind2 = primitives.get(k).getData();
				
				for (int i = 1; i < ind2.length; ++i) {
					if (ind2[i] == oldIndex) {
						ind2[i] = newIndex;
					}
				}
			}
		}
		
		clean();
		setFlat(true);
	}
	
	@SuppressWarnings("unused")
	private static <T> void swap(ArrayList<T> a, int i, int j) {
		T tmp = a.get(i);
		a.set(i, a.get(j));
		a.set(j, tmp);
	}
	
	@SuppressWarnings("unused")
	private static boolean contains(int[] a, int n) {
		for (int k = 0; k < a.length; ++k) {
			if (a[k] == n) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Performs left cycles until prefFirst is in front of the array.
	 * @param a
	 * @param prefFirst
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean cycle(int[] a, int prefFirst) {
		// find prefFirst
		int i = -1;
		for (int k = 0; k < a.length; ++k) {
			if (a[k] == prefFirst) {
				i = k;
			}
		}
		if (i == -1) {
			// not found :(
			return false;
		}
		
		// fill before and after
		int[] before = new int[i];
		for (int k = 0; k < i; ++k) {
			before[k] = a[k];
		}
		int[] after  = new int[a.length - i];
		for (int k = i; k < a.length; ++k) {
			after[k-i] = a[k];
		}
		
		// "swap" before and after
		for (int k = 0; k < a.length - i; ++k) {
			a[k] = after[k];
		}
		for (int k = a.length - i; k < a.length; ++k) {
			a[k] = before[k-(a.length-i)];
		}
		
		return true;
	}
	
	
	/**
	 * Removes redundant vertices.
	 */
	protected void clean() {
		int[] ind;
		LinkedList<V> unusedVertices = new LinkedList<V>();
		ArrayList<Integer> unusedVertInd = new ArrayList<Integer>();
		
		
//		System.out.println("BEFORE:");
//		for (V vert: vertices) {
//			System.out.println(vert.getPosition());
//		}
//		for (P prim: primitives) {
//			System.out.println(prim);
//		}
		
		// find unused vertices
		// bucket sort is faster but this costs less memory
		for (int v = 0; v < vertices.size(); ++v) {
			
			boolean used = false;
			
			for (P prim: primitives) {
				ind = prim.getData();
				for (int i = 0; i < ind.length; ++i) {
					if (ind[i] == v) {
						used = true;
						break;
					}
				}
			}
			
			if (!used) {
				unusedVertices.add(vertices.get(v));
				unusedVertInd.add(v);
			}
		}
		
		// correct indices
		for (int v = unusedVertInd.size() - 1; v >= 0; --v) {
			
			for (P prim: primitives) {
				ind = prim.getData();
				for (int i = 0; i < ind.length; ++i) {
					if (ind[i] >= unusedVertInd.get(v)) {
						ind[i] -= 1;
					}
				}
			}
		}
		
		// remove
		vertices.removeAll(unusedVertices);
		
//		System.out.println("AFTER:");
//		for (V vert: vertices) {
//			System.out.println(vert.getPosition());
//		}
//		for (P prim: primitives) {
//			System.out.println(prim);
//		}
		
	}
	
	
	public abstract void draw(GL3 gl, Shader shader);
	public abstract void init(GL3 gl);
	public abstract void dispose(GL3 gl);
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Vec3 getPosition() {
		return position;
	}
	public void setPosition(Vec3 position) {
		this.position = position;
	}
	public Vec3 getRotation() {
		return rotation;
	}
	public void setRotation(Vec3 rotation) {
		this.rotation = rotation;
	}

	public VertexArray<V> getVertices() {
		return vertices;
	}

	public PrimitiveArray<P> getPrimitives() {
		return primitives;
	}

	public boolean isFlat() {
		return flat;
	}

	public void setFlat(boolean flat) {
		this.flat = flat;
	}
}
