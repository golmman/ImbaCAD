package imbacad;

import java.util.LinkedList;

import imbacad.model.Glm;
import imbacad.model.Mesh;
import imbacad.view.MainWindow;


public class ImbaCAD {

	
	public static final String PROJECT_NAME = "ImbaCAD";
	
	public static LinkedList<Mesh> meshes = new LinkedList<Mesh>();


	public static void main(String[] s) {
		Glm.init();
		
		
		new MainWindow(PROJECT_NAME);
	}
	

}
