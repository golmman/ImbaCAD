package imbacad.model.ocr;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {
	
	
	
	public Test() {}

	public static void main(String[] args) {
		System.loadLibrary("tessx64");
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("miau3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		int[] data = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth() * 4);
		
		initTesseract("C:/Program Files (x86)/tesseract-vs2012-master", "eng");
		
		String text = getUTF8Text(data, image.getWidth(), image.getHeight());
		
		
		for (int k = 0; k < data.length; ++k) {
			System.out.print(data[k]);
		}
		
		System.out.println(text);
		
	}
	
	
	
	public static native void initTesseract(String filename, String language);
	
	public static native String getUTF8Text(int[] data, int width, int height);

}
