package imbacad.model;


public class Glm {
	
	private float[] values = new float[16];
	
	private Glm() {}
	
	public static void init() {
		System.loadLibrary("javaglm");
	}
	
	public static void main(String[] args) {
		System.loadLibrary("javaglm");
		float[] d1 = { 	 1, 2,  1,  3,
						 1, 1,  1,  1,
						-1, 2, -1,  1,
						 0, 0,  2, -2};
		float[] d2 = { 	 1, 0,  1,  0,
						-1, 2,  0,  2,
						-1, 0, -1,  0,
						 1, 1,  0, -1};
		float[] v = {1, 2, 3};
		
		float[] m = mul(d1, d2);
		m = translate(d1, v);
		m = rotate(d1, 0.5f, v);
		m = perspective((float)Math.PI / 2, 4.0f/3, 0.01f, 100.0f);
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(m[4 * i + j] + "  ");
			}
			System.out.println();
		}
		
	}
	
	public static float[] vec3(float x, float y, float z) {
		float[] v = new float[3];
		v[0] = x;
		v[1] = y;
		v[2] = z;
		return v;
	}
	
	public static float[] diag(float d) {
		float[] m = new float[16];
		m[0] = d;
		m[5] = d;
		m[10] = d;
		m[15] = d;
		return m;
	}
	
	public static native float[] mul(float[] A, float[] B);
	public static native float[] translate(float[] M, float[] v);
	public static native float[] rotate(float[] M, float a, float[] v);
	public static native float[] perspective(float fovy, float aspect, float zNear, float zFar);
}
