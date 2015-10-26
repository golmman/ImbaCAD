package imbacad.model;

import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;

public class Mesh {

	public static final int SIZEOF_VERTEX = 20;

	private String textureFilename;
	private float[] vertices;
	private int[] indices;
	
	private int[] texture = new int[1];			// texture id

	private int[] VAO = new int[1]; // Vertex Array Object - holds the set of "everything that needs rendering"
	private int[] VBO = new int[1]; // Vertex Buffer Object - holds vertices
	private int[] EBO = new int[1]; // Element Buffer Object - holds indices
	
	
	private Vec3 position = new Vec3();
	private Vec3 rotation = new Vec3();

	public Mesh(String textureFilename, float[] vertices, int[] indices) {
		this.textureFilename = textureFilename;
		this.vertices = vertices;
		this.indices = indices;
	}
	
	public void init(GLAutoDrawable drawable) {

		FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
		IntBuffer indexBuffer = Buffers.newDirectIntBuffer(indices);

		GL3 gl = drawable.getGL().getGL3();

		// Create buffers/arrays
		gl.glGenVertexArrays(1, VAO, 0);
		gl.glGenBuffers(1, VBO, 0);
		gl.glGenBuffers(1, EBO, 0);

		gl.glBindVertexArray(VAO[0]);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, VBO[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.length * 4, vertexBuffer, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, EBO[0]);
		gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, indexBuffer, GL.GL_STATIC_DRAW);

		// Set the vertex attribute pointers
		// Vertex Positions
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, SIZEOF_VERTEX, 0);

		// Vertex Texture Coords
		gl.glEnableVertexAttribArray(3);
		gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, SIZEOF_VERTEX, 12);

		gl.glBindVertexArray(0);
		
		loadTexture(drawable, textureFilename);
	}

	private void loadTexture(GLAutoDrawable drawable, String filename) {
		GL3 gl = drawable.getGL().getGL3();

		// Generate texture ID and load texture data
		int[] textureID = new int[1];
		gl.glGenTextures(1, textureID, 0);
		byte[] imageBytes;
		
		
		
		
		// Load image and get height and width for raster.
		// Image img = Toolkit.getDefaultToolkit().createImage(filename);
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int imgHeight = img.getHeight(null);
		int imgWidth = img.getWidth(null);

		// Create a raster with correct size,
		// and a colorModel and finally a bufImg.
		WritableRaster raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, imgWidth, imgHeight, 4, null);
		ComponentColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), 
				new int[] { 8, 8, 8, 8 }, true, false, ComponentColorModel.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);
		BufferedImage bufImg = new BufferedImage(colorModel, raster, false, null);
		
		

		
		// Filter img into bufImg and perform
		// Coordinate Transformations on the way.
		Graphics2D g = bufImg.createGraphics();
//		AffineTransform gt = new AffineTransform();		// TODO: Original code has this uncommented
//		gt.translate(0, imgHeight);						//       but it results in simply flipping
//		gt.scale(1, -1d);								//       the image upside down. WHY?!
//		g.transform(gt);
		g.drawImage(img, null, null);
		
		// Retrieve underlying byte array (imgBuf)
		// from bufImg.
		DataBufferByte imgBuf = (DataBufferByte) raster.getDataBuffer();
		imageBytes = imgBuf.getData();
		g.dispose();
		
		
		ByteBuffer imageBuffer = Buffers.newDirectByteBuffer(imageBytes);

		// Assign texture to ID
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, imgWidth, imgHeight, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, imageBuffer);
		gl.glGenerateMipmap(GL.GL_TEXTURE_2D);

		// Parameters
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glBindTexture(GL.GL_TEXTURE_2D, 0);

		this.texture = textureID;
	}

	public void draw(GLAutoDrawable drawable, int shader) {
		GL3 gl = drawable.getGL().getGL3();
		
		
		
		// Active proper texture unit before binding
		gl.glActiveTexture(GL.GL_TEXTURE0); 

		// Now set the sampler to the correct texture unit
		gl.glUniform1f(gl.glGetUniformLocation(shader, "texDiffuse"), 0);
		// And finally bind the texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0]);

		gl.glActiveTexture(GL.GL_TEXTURE0); // Always good practice to set
											// everything back to defaults once configured.
		
		
		// Draw mesh
		gl.glBindVertexArray(VAO[0]);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
		gl.glDrawElements(GL.GL_POINTS, indices.length, GL.GL_UNSIGNED_INT, 0);
		gl.glBindVertexArray(0);
		
		
		
		

		
		
	}
	
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("Mesh.dispose()");
		
		GL3 gl = drawable.getGL().getGL3();
		gl.glDeleteVertexArrays(1, VAO, 0);
		gl.glDeleteBuffers(1, VBO, 0);
		gl.glDeleteBuffers(1, EBO, 0);
		
		gl.glDeleteTextures(1, texture, 0);
	}

	public float[] getVertices() {
		return vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public Vec3 getPosition() {
		return position;
	}

	public void setPosition(Vec3 position) {
		this.position = position;
	}

	public Vec3 getRotation() {
		return rotation;
	}

	public void setRotation(Vec3 rotation) {
		this.rotation = rotation;
	}

	public int[] getVAO() {
		return VAO;
	}

	public int[] getVBO() {
		return VBO;
	}

	public int[] getEBO() {
		return EBO;
	}
	
}
