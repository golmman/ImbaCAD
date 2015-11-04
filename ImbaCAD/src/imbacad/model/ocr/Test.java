package imbacad.model.ocr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

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
		
		
		int[] data = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
		
		
		//testWindow(data, image.getWidth(), image.getHeight());
		
		Tesseract.init("C:/Program Files (x86)/tesseract-vs2012-master", "eng");
		
		String text = Tesseract.getUTF8Text(data, image.getWidth(), image.getHeight());
		
		System.out.println(text);
		
		
		
		Tesseract.end();
	}
	
	
	
	@SuppressWarnings("unused")
	private static void testWindow(int[] data, int w, int h) {
		JFrame frame = new JFrame("ttt") {
			private static final long serialVersionUID = 8239619180528047017L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				g.setColor(Color.RED);
				g.drawLine(50, 50, 50, 50);
				
				for (int y = 0; y < h; ++y) {
					for (int x = 0; x < w; ++x) {
						g.setColor(new Color(data[x + w * y]));
						
						g.drawLine(x + 50, y + 50, x + 50, y + 50);
					}	
				}
				
				
			}
		};
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 800, 600);
		
		
		
		
		
		frame.setVisible(true);
	}

}
