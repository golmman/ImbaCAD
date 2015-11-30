package imbacad.model.mesh;

import java.io.File;
import java.util.ArrayList;

public class Mesh2D {
	
	private Plaster[] plasters;
	private String name;
	
	/**
	 * Conventions: 
	 * The z component of a vertex represents the top coordinate of the wall at that vertex.
	 * The w component of a vertex represents the bottom coordinate of the wall at that vertex.
	 * A 2D-vectors normal points to the left, e.g. the normal of the x-axis is the y-axis. 
	 * @param vertices
	 * @param indices
	 */
	public Mesh2D(Plaster[] plasters, String name) {
		this.plasters = plasters;
		this.name = name;
	}
	
	
	public TextureMesh to3D() {
		VertexArray vertices3D = new VertexArray();
		ArrayList<Integer> indices3D = new ArrayList<Integer>();
		
		
		int i0;
		for (int k = 0; k < plasters.length; ++k) {
			i0 = vertices3D.size();
			
			indices3D.add(i0+0);
			indices3D.add(i0+1);
			indices3D.add(i0+2);
			
			indices3D.add(i0+0);
			indices3D.add(i0+2);
			indices3D.add(i0+3);
			
			vertices3D.add(new Vertex(plasters[k].getTopR()));
			vertices3D.add(new Vertex(plasters[k].getTopL()));
			vertices3D.add(new Vertex(plasters[k].getBotL()));
			vertices3D.add(new Vertex(plasters[k].getBotR()));
		}
		
		
		int[] intIndices3D = new int[indices3D.size()];
		for (int k = 0; k < intIndices3D.length; ++k) {
			intIndices3D[k] = indices3D.get(k);
		}
		
		return TextureMesh.createFlatShadedMesh(new File("white.bmp"), vertices3D, intIndices3D, this.name + "3D");
	}
	
	
//	public Mesh to3D() {
//		VertexArray vertices3D = new VertexArray();
//		ArrayList<Integer> indices3D = new ArrayList<Integer>();
//		
//		
//		Vec4 v0, v1;
//		int i0;
//		for (int k = 0; k < indices.length; k += 2) {
//			v0 = vertices[indices[k]];
//			v1 = vertices[indices[k+1]];
//			
//			i0 =  vertices3D.size();
//			
//			// TODO: reuse created vertices
//			vertices3D.add(new Vertex(v0.getX(), v0.getY(), v0.getW()));
//			vertices3D.add(new Vertex(v1.getX(), v1.getY(), v1.getW()));
//			vertices3D.add(new Vertex(v0.getX(), v0.getY(), v0.getZ()));
//			vertices3D.add(new Vertex(v1.getX(), v1.getY(), v1.getZ()));
//			
//			indices3D.add(i0+0);
//			indices3D.add(i0+3);
//			indices3D.add(i0+1);
//			
//			indices3D.add(i0+0);
//			indices3D.add(i0+2);
//			indices3D.add(i0+3);
//		}
//		
//		
//		
//		int[] intIndices3D = new int[indices3D.size()];
//		for (int k = 0; k < intIndices3D.length; ++k) {
//			intIndices3D[k] = indices3D.get(k);
//		}
//		
//		return Mesh.createFlatShadedMesh(new File("white.bmp"), vertices3D, intIndices3D, "");
//	}

}