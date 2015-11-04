package imbacad;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import imbacad.model.Vec3;

public class VertexNormals {

	public VertexNormals() {}

	public static void main(String[] args) {
		
		ArrayList<Vec3> vectors = new ArrayList<Vec3>();
		vectors.add(new Vec3(-0.2f, 0.5f, -0.5f));
		vectors.add(new Vec3(-0.2f, -0.5f, -0.5f));
		vectors.add(new Vec3(1.0f, 0.0f, 0.0f));
		
		System.out.println(getNormal(vectors).toStringCopyable());
	}
	
	
	/**
	 * Calculates the normalised vertex normal of the origin given neighbour surfaces spanned by input vectors.
	 * The order of the input array determines the orientation of the vector.
	 * For example if the input is z,y,x-axis in this order the output is 1/sqrt(3) * (-1, -1, -1).
	 * If the input is x,y,y-axis in this order the output is 1/sqrt(3) * (1, 1, 1).
	 * Input has to be at least three vectors.
	 * @param vectors
	 * @return
	 */
	public static Vec3 getNormal(ArrayList<Vec3> vectors) {
		if (vectors.size() < 3) throw new IllegalArgumentException("Three or more input vectors required.");
		
		Vec3 result = new Vec3();
		
		vectors.add(vectors.get(0));
		
		for (int k = 0; k < vectors.size() - 1; ++k) {
			result = result.add(vectors.get(k).cross(vectors.get(k+1)).normalised());
			System.out.println("miaus");
		}
		
		
		return result.normalised();
	}

}
