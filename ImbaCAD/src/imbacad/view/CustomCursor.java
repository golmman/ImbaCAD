package imbacad.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.imageio.ImageIO;

public class CustomCursor {
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 32;
	
	private CustomCursor() {}
	
	
	public static Cursor create(File file, Color transparent) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (img.getWidth() != WIDTH || img.getHeight() != HEIGHT) {
			throw new IllegalStateException("Cursor size has to be " + WIDTH + "x" + HEIGHT + " pixels.");
		}
		
		for (int y = 0; y < HEIGHT; ++y) {
			for (int x = 0; x < WIDTH; ++x) {
				if (img.getRGB(x, y) == transparent.getRGB()) {
					img.setRGB(x, y, 0);
				}
			}
		}
		
		return Toolkit.getDefaultToolkit().createCustomCursor(
				img, new Point(0, 0), "CustomCursor" + Calendar.getInstance().getTimeInMillis());
	}
	
	
	public static Cursor createEraser(int w, int h) {
		BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = img.getGraphics();
		
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, w, h);
		
		return Toolkit.getDefaultToolkit().createCustomCursor(
				img, new Point(0, 0), "CustomCursor" + Calendar.getInstance().getTimeInMillis());
	}

}
