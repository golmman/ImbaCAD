package imbacad.model.mesh;

import com.jogamp.opengl.GL3;

import imbacad.model.CopyFactory;
import imbacad.model.Vec3;
import imbacad.model.mesh.primitive.Primitive;
import imbacad.model.mesh.primitive.PrimitiveArray;
import imbacad.model.mesh.vertex.Vertex;
import imbacad.model.mesh.vertex.VertexArray;
import imbacad.model.shader.Shader;

public abstract class Mesh<V extends Vertex, P extends Primitive> {
	
	protected int[] vao = new int[1];	// name for vertex array object
	protected int[] vbo = new int[1];	// name for vertex buffer object
	protected int[] ibo = new int[1];	// name for index buffer object
	
	protected VertexArray<V> originalVertices;
	protected PrimitiveArray<P> originalPrimitives;
	protected VertexArray<V> vertices;
	protected PrimitiveArray<P> primitives;
	protected String name = null;
	protected CopyFactory<V> vertexCopyFactory;
	protected CopyFactory<P> primitveCopyFactory;
	
	protected Vec3 position = new Vec3();
	protected Vec3 rotation = new Vec3();
	
	
	protected ColorMesh<P> selectionMesh;
	
	/**
	 * Creates copies of vertices and primitives for further use.
	 * @param vertices
	 * @param primitives
	 * @param name
	 * @param vcf
	 * @param pcf
	 */
	protected Mesh(VertexArray<V> vertices, PrimitiveArray<P> primitives, String name, CopyFactory<V> vcf, CopyFactory<P> pcf) {
		this.originalVertices = vertices;
		this.originalPrimitives = primitives;
		this.vertices = new VertexArray<V>(vertices, vcf);
		this.primitives = new PrimitiveArray<P>(primitives, pcf);
		this.name = name;
		this.vertexCopyFactory = vcf;
		this.primitveCopyFactory = pcf;
	}
	
	protected Mesh(Mesh<V, P> mesh) {
		this.originalVertices = mesh.originalVertices;
		this.originalPrimitives = mesh.originalPrimitives;
		this.vertices = new VertexArray<V>(mesh.vertices, mesh.vertexCopyFactory);
		this.primitives = new PrimitiveArray<P>(mesh.primitives, mesh.primitveCopyFactory);
		this.name = mesh.name;
		this.vertexCopyFactory = mesh.vertexCopyFactory;
		this.primitveCopyFactory = mesh.primitveCopyFactory;
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
}
