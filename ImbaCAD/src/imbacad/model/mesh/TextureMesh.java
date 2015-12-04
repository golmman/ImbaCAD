package imbacad.model.mesh;

import imbacad.model.Material;
import imbacad.model.Vec3;
import imbacad.model.mesh.vertex.TextureVertex;
import imbacad.model.mesh.vertex.VertexArray;
import imbacad.model.shader.Shader;

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

/**
 * Represents a mesh which uses an advanced shader with lighting, textures etc. .
 * @author Dirk Kretschmann
 *
 */
public class TextureMesh extends Mesh<TextureVertex> {
	
	private Material material = new Material();
	
	private File textureFile;
	
	private int[] texture = new int[1];			// texture id

	
//	private int[] colVAO = new int[1];
//	private int[] colorBuffer = new int[1];
	
	

	private TextureMesh(File textureFile, VertexArray<TextureVertex> vertices, int[] indices, String name) {
		super(vertices, indices, name);
		
		if (indices.length % 3 != 0) throw new IllegalStateException("The index array has to be divisible by 3.");
		
		this.textureFile = textureFile;
		this.vertices = vertices;
		this.indices = indices;
	}
	
	
	/**
	 * Creates a new Mesh from given data.<br>
	 * @param textureFile
	 * @param vertices
	 * @param indices
	 * @param name
	 */
	public static TextureMesh createMesh(File textureFile, VertexArray<TextureVertex> vertices, int[] indices, String name) {
		return new TextureMesh(textureFile, vertices, indices, name);
	}
	
	/**
	 * Creates a new Mesh from given data and sets normals per face to enable flat shading.<br>
	 * TODO: vertex texture coordinates are currently ignored.
	 * @param textureFile
	 * @param vertices
	 * @param indices
	 * @param name
	 * @return
	 */
	public static TextureMesh createFlatShadedMesh(File textureFile, VertexArray<TextureVertex> vertices, int[] indices, String name) {
		if (indices.length % 3 != 0) throw new IllegalStateException("The index array has to be divisible by 3.");
		
		for (int k = 0; k < indices.length; k += 3) {
			TextureVertex vertex0 = new TextureVertex(vertices.get(indices[k]));
			TextureVertex vertex1 = new TextureVertex(vertices.get(indices[k+1]));
			TextureVertex vertex2 = new TextureVertex(vertices.get(indices[k+2]));
			
			// get normal, add vertex
			Vec3 v1 = new Vec3(vertex1.position.sub(vertex0.position));
			Vec3 v2 = new Vec3(vertex2.position.sub(vertex0.position));
			vertex0.normal = v1.cross(v2).normalised();
			vertices.add(vertex0);
			
			// set starting index to newly added vertex
			indices[k] = vertices.size() - 1;
		}
		
		TextureMesh result = new TextureMesh(textureFile, vertices, indices, name);
		result.material.setFlatNormals(true);
		
		return result;
	}
	
	/**
	 * Creates a new Mesh from given data and sets vertex normals as mean adjacent face normals.<br>
	 * @param textureFile
	 * @param vertices
	 * @param indices
	 * @param name
	 * @return
	 */
	public static TextureMesh createPhongShadedMesh(File textureFile, VertexArray<TextureVertex> vertices, int[] indices, String name) {
		if (indices.length % 3 != 0) throw new IllegalStateException("The index array has to be divisible by 3.");
		
		Vec3 v0 = null, v1 = null, v2 = null;
		Vec3 a1, a2;
		Vec3 normals;
		
		for (int k = 0; k < vertices.size(); ++k) {
			
			normals = new Vec3();
			
			// find all the triangles which use vertex k
			for (int i = 0; i < indices.length; ++i) {
				if (k == indices[i]) {
					// preserve orientation
					if (i % 3 == 0) {
						v0 = vertices.get(indices[i+0]).position;
						v1 = vertices.get(indices[i+1]).position;
						v2 = vertices.get(indices[i+2]).position;
					} else if (i % 3 == 1) {
						v0 = vertices.get(indices[i-1]).position;
						v1 = vertices.get(indices[i+0]).position;
						v2 = vertices.get(indices[i+1]).position;
					} else if (i % 3 == 2) {
						v0 = vertices.get(indices[i-2]).position;
						v1 = vertices.get(indices[i-1]).position;
						v2 = vertices.get(indices[i+0]).position;
					}
					
					// add face normal
					a1 = v1.sub(v0);
					a2 = v2.sub(v0);
					normals = normals.add( a1.cross(a2) );
					
					System.out.println(normals);
				}
			}
			
			// update normal
			vertices.get(k).normal = normals.normalised();
			
			System.out.println(k + " " + vertices.get(k).normal);
		}
		
		TextureMesh result = new TextureMesh(textureFile, vertices, indices, name);
		
		return result;
	}
	
	@Override
	public void init(GL3 gl) {
		
		
		FloatBuffer vertexBuf = Buffers.newDirectFloatBuffer(vertices.toFloats());
		IntBuffer indexBuf = Buffers.newDirectIntBuffer(indices);
		
		gl.glGenVertexArrays(1, vao, 0);
		gl.glGenBuffers(1, vbo, 0);
		gl.glGenBuffers(1, ibo, 0);
		
		

		/*
		 * create texture vertex array
		 */
		gl.glBindVertexArray(vao[0]);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vbo[0]);
		gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.getTotalBytes(), vertexBuf, GL.GL_STATIC_DRAW);

		gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, ibo[0]);
		gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, indexBuf, GL.GL_STATIC_DRAW);

		// Set the vertex attribute pointers
		// Vertex Positions
		gl.glEnableVertexAttribArray(0);
		gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, vertices.getStrideBytes(), 0);
		
		// Vertex Normals
		gl.glEnableVertexAttribArray(2);
		gl.glVertexAttribPointer(2, 3, GL.GL_FLOAT, false, vertices.getStrideBytes(), 20);

		// Vertex Texture Coords
		gl.glEnableVertexAttribArray(3);
		gl.glVertexAttribPointer(3, 2, GL.GL_FLOAT, false, vertices.getStrideBytes(), 12);

		gl.glBindVertexArray(0);
		
		
		
		loadTexture(gl, textureFile);
	}
	
	private void loadTexture(GL3 gl, File file) {

		// Generate texture ID and load texture data
		int[] textureID = new int[1];
		gl.glGenTextures(1, textureID, 0);
		byte[] imageBytes;
		
		
		
		
		// Load image and get height and width for raster.
		// Image img = Toolkit.getDefaultToolkit().createImage(filename);
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(file);
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
//		gt.scale(1, -1d);								//       the image upside down.
//		g.transform(gt);								//       WHY?!
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

	
	
	@Override
	public void draw(GL3 gl, Shader shader) {
		
		// Active proper texture unit before binding
		gl.glActiveTexture(GL.GL_TEXTURE0); 

		// Now set the sampler to the correct texture unit
		//gl.glUniform1f(gl.glGetUniformLocation(shader, "material.texture"), 0.0f);
		//gl.glUniform1f(gl.glGetUniformLocation(shader, "material.shininess"), 200.0f);
		//gl.glUniform3f(gl.glGetUniformLocation(shader, "material.specularColor"), 1.0f, 1.0f, 1.0f);
		material.updateUniforms(gl, shader);
		
		
		// And finally bind the texture
		gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0]);

		gl.glActiveTexture(GL.GL_TEXTURE0); // Always good practice to set
											// everything back to defaults once configured.
		
		
		// Draw mesh
		gl.glBindVertexArray(vao[0]);
		gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
		gl.glBindVertexArray(0);
	}
	
	
	@Override
	public void dispose(GL3 gl) {
		gl.glDeleteVertexArrays(1, vao, 0);
		gl.glDeleteBuffers(1, vbo, 0);
		gl.glDeleteBuffers(1, ibo, 0);
		
		gl.glDeleteTextures(1, texture, 0);
	}
	
	
}
