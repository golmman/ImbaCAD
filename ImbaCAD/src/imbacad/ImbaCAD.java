package imbacad;

import java.util.LinkedList;

import imbacad.model.Glm;
import imbacad.model.Light;
import imbacad.model.mesh.Mesh;
import imbacad.view.MainWindow;


/*
 * 
 * Shader highlighting: http://sourceforge.net/projects/webglsl/
 * 
 */



public class ImbaCAD {

	
	public static final String PROJECT_NAME = "ImbaCAD";
	
	public static LinkedList<Mesh<?, ?>> meshes = new LinkedList<Mesh<?, ?>>();
	public static LinkedList<Light> lights = new LinkedList<Light>();


	public static void main(String[] s) {
		Glm.init();
		
		
		new MainWindow(PROJECT_NAME);
	}
	

}
