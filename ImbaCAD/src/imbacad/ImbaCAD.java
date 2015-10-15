package imbacad;

import java.util.LinkedList;

import imbacad.gui.MainWindow;
import imbacad.util.Glm;


public class ImbaCAD {

	
	public static final String PROJECT_NAME = "ImbaCAD";
	
	public static LinkedList<Mesh> meshes = new LinkedList<Mesh>();


	public static void main(String[] s) {
		Glm.init();
		
		
		new MainWindow(PROJECT_NAME);
	}
	

}
