package imbacad.model.dwg;


public class DWG {
	
	public static final String TEST_DIR = "C:/Users/Johannes/Documents/visual studio 2012/Projects/dwg/dwg/dwg2000/";
	
	private static final DWG INSTANCE = new DWG();
	
	private DWG() {
		System.loadLibrary("jdwg");
	}
	
	public static void main(String[] args) {
		
		
		double[] lines = readLines(TEST_DIR + "ex0.dwg");
		
		for (int k = 0; k < lines.length; ++k) {
			System.out.println(lines[k]);
		}
	}

	
	public static native double[] readLines(String filename);
}
