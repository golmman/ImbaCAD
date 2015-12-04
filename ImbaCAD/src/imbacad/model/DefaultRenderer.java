package imbacad.model;

import imbacad.ImbaCAD;
import imbacad.control.RenderingEventAdapter;
import imbacad.model.camera.Camera;
import imbacad.model.camera.CameraUpdater;
import imbacad.model.camera.LevitateUpdater;
import imbacad.model.mesh.ColorMesh;
import imbacad.model.mesh.Mesh;
import imbacad.model.mesh.TextureMesh;
import imbacad.model.shader.Shader;
import imbacad.model.shader.UniformMatrix4;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.HashSet;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * Renders
 * @author Dirk Kretschmann
 *
 */
public class DefaultRenderer implements GLEventListener {
	
	RenderingEventAdapter events = null;
	private Camera camera = null;
	private CameraUpdater cameraUpdater = null;
	
	private int width = 800;
	private int height = 600;
	
	private FrameBuffer frameBuffer = new FrameBuffer();
	
	private Shader texShader = null;
	private Shader colShader = null;
	
	private UniformMatrix4 uniformTexProj = null;
	private UniformMatrix4 uniformTexView = null;
	private UniformMatrix4 uniformTexModel = null;
	
	private UniformMatrix4 uniformColProj = null;
	private UniformMatrix4 uniformColView = null;
	private UniformMatrix4 uniformColModel = null;
	
	private TextureMesh selectedMesh = null;
	private int selectedVertex = -1;
	
	private HashSet<TextureMesh> textureMeshes = new HashSet<TextureMesh>();
	private HashSet<ColorMesh> colorMeshes = new HashSet<ColorMesh>();
	
	public DefaultRenderer(RenderingEventAdapter events, Camera camera) {
		this.events = events;
		this.cameraUpdater = new LevitateUpdater();
		this.camera = camera;
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		
		this.texShader = new Shader(drawable, new File("shader/texture.vsh"), new File("shader/texture.fsh"));
		this.colShader = new Shader(drawable, new File("shader/color.vsh"), new File("shader/color.fsh"));
		
		this.uniformTexProj = new UniformMatrix4(gl, texShader, "projection");
		this.uniformTexView = new UniformMatrix4(gl, texShader, "view");
		this.uniformTexModel = new UniformMatrix4(gl, texShader, "model");
		
		this.uniformColProj = new UniformMatrix4(gl, colShader, "projection");
		this.uniformColView = new UniformMatrix4(gl, colShader, "view");
		this.uniformColModel = new UniformMatrix4(gl, colShader, "model");
		
		for (Mesh<?> mesh : ImbaCAD.meshes) {
			mesh.init(gl);
		}
		
		
		frameBuffer.init(gl, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		
		gl.glEnable(GL3.GL_DEPTH_TEST);
		gl.glDepthFunc(GL3.GL_LESS);
		gl.glEnable(GL3.GL_CULL_FACE);
		gl.glEnable(GL3.GL_PROGRAM_POINT_SIZE);
		
		gl.glProvokingVertex(GL3.GL_FIRST_VERTEX_CONVENTION); 
	}
	
	

	@Override
	public void display(GLAutoDrawable drawable) {
		
		/*
		 * Events
		 */
		cameraUpdater.update(camera, events);
		
//		if (processor.getKey(KeyEvent.VK_ESCAPE)) {
//			//this.dispose();
//			// TODO: dispose?
//			return;
//		}
		
		
		/*
		 * Drawing
		 */
		GL3 gl = drawable.getGL().getGL3();
		
		float[] projection;
		float[] view;
		float[] model;
		
		
		// Clear screen
		gl.glClearColor(0, 0, 0, 1.0f); 
		gl.glClear(GL3.GL_STENCIL_BUFFER_BIT | GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		
		
		
		// set up projection matrix
		projection = Glm.diag(1.0f);
		projection = Glm.perspective(camera.getFov(), (float)width / height, 0.1f, 100.0f);
		
		// set up view matrix
		view = Glm.diag(1.0f);
		view = Glm.rotate(view, (float)(camera.getPolarAngle() - Math.PI), Glm.vec3(1.0f, 0.0f, 0.0f));
		view = Glm.rotate(view, (float)(Math.PI / 2.0f - camera.getAzimuthAngle()), Glm.vec3(0.0f, 0.0f, 1.0f));
		view = Glm.translate(view, camera.getPosition().mul(-1.0f).toArray());
		
		/*
		 * draw meshes
		 */
		gl.glUseProgram(texShader.getProgram());
		
		// send data to gpu
		for (Light light: ImbaCAD.lights) {
			light.updateUniforms(gl, texShader);
		}
		
		camera.updateUniforms(gl, texShader);
		uniformTexView.update(gl, view);
		uniformTexProj.update(gl, projection);
		
		//for (Mesh mesh : ImbaCAD.meshes) {
		for (TextureMesh mesh : textureMeshes) {
			
			if (mesh.getName().equals("test")) {
				mesh.getRotation().setZ(mesh.getRotation().getZ() + 0.01f);
			}
			
			if (mesh.getName().equals("testHouse")) {
				mesh.getRotation().setZ(mesh.getRotation().getZ() - 0.01f);
			}
			
			model = Glm.diag(1.0f);
			model = Glm.translate(model, mesh.getPosition().toArray());
			model = Glm.rotate(model, mesh.getRotation().getX(), Glm.vec3(1.0f, 0.0f, 0.0f));
			model = Glm.rotate(model, mesh.getRotation().getZ(), Glm.vec3(0.0f, 0.0f, 1.0f));
			
			// send data to gpu
			uniformTexModel.update(gl, model);
			
			mesh.draw(gl, texShader);
		}
		
		FloatBuffer data = Buffers.newDirectFloatBuffer(4);
		gl.glReadPixels(
				events.getMouseX(), 
				height - events.getMouseY(), 
				1, 1, GL.GL_RGBA, GL3.GL_FLOAT, data);
		System.out.println(255.0f*data.get(0) + " " + 255.0f*data.get(1) + " " + 255.0f*data.get(2) + " " + 255.0f*data.get(3) + " ");
		
		
		/*
		 * draw selection TODO: Create class which handles that
		 * 
		 * glReadPixels(x, y, 1, 1, GL_DEPTH_COMPONENT, GL_FLOAT, &c);
		 * 
		 */
		gl.glUseProgram(colShader.getProgram());
		
		// send data to gpu
		uniformColView.update(gl, view);
		uniformColProj.update(gl, projection);
		
		for (ColorMesh mesh : colorMeshes) {
			
			model = Glm.diag(1.0f);
			model = Glm.translate(model, mesh.getPosition().toArray());
			model = Glm.rotate(model, mesh.getRotation().getX(), Glm.vec3(1.0f, 0.0f, 0.0f));
			model = Glm.rotate(model, mesh.getRotation().getZ(), Glm.vec3(0.0f, 0.0f, 1.0f));
			
			// send data to gpu
			uniformColModel.update(gl, model);
			
			mesh.draw(gl, colShader);
		}
		
//		
//		// remove last selection
//		if (selectedMesh != null) {
//			float[] col = {1.0f, 0.0f, 0.0f, 1.0f};
//			FloatBuffer colBuf = Buffers.newDirectFloatBuffer(col);
//			
//			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, selectedMesh.getColorBuffer());
//			gl.glBufferSubData(GL.GL_ARRAY_BUFFER, selectedVertex * 16, 16, colBuf);
//			
//			selectedMesh = null;
//			selectedVertex = -1;
//		}
//		
//		// draw new selection
//		//for (Mesh mesh : ImbaCAD.meshes) {
//		for (Mesh mesh : meshMap) {
//			
//			model = Glm.diag(1.0f);
//			model = Glm.translate(model, mesh.getPosition().toArray());
//			model = Glm.rotate(model, mesh.getRotation().getX(), Glm.vec3(1.0f, 0.0f, 0.0f));
//			model = Glm.rotate(model, mesh.getRotation().getZ(), Glm.vec3(0.0f, 0.0f, 1.0f));
//			
//			// send data to gpu
//			uniformColModel.update(gl, model);
//			
//			
//			int floatCount = mesh.getVertexCount() * Vertex.FLOATS_PER;
//			
//			// get data from the gpu
//			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, mesh.getVertexBuffer());
//			FloatBuffer vertices = Buffers.newDirectFloatBuffer(floatCount);
//			gl.glGetBufferSubData(GL.GL_ARRAY_BUFFER, 0, 4 * floatCount, vertices);
//			IntBuffer viewport = Buffers.newDirectIntBuffer(4);
//			gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
//			FloatBuffer depthRange = Buffers.newDirectFloatBuffer(2);
//			gl.glGetFloatv(GL.GL_DEPTH_RANGE, depthRange);
//			
//			// get model-view-projection matrix
//			float[] mvp = Glm.mul(Glm.mul(projection, view), model);
//			
//			// calculate each vertex screen coordinate
//			for (int k = 0; k < floatCount; k += Vertex.FLOATS_PER) {
//				// get object coordinates, equal to mesh.getVertices()[0];
//				float objX = vertices.get(k+0); 		
//				float objY = vertices.get(k+1);
//				float objZ = vertices.get(k+2);
//				float objW = 1.0f;
//				
//				// get clip coordinates, OpenGL uses column-major order
//				float clipX = objX * mvp[ 0] + objY * mvp[ 4] + objZ * mvp[ 8] + objW * mvp[12];
//				float clipY = objX * mvp[ 1] + objY * mvp[ 5] + objZ * mvp[ 9] + objW * mvp[13];
//				float clipZ = objX * mvp[ 2] + objY * mvp[ 6] + objZ * mvp[10] + objW * mvp[14];
//				float clipW = objX * mvp[ 3] + objY * mvp[ 7] + objZ * mvp[11] + objW * mvp[15];
//				
//				// get normalized device coordinates
//				float ndcX = clipX / clipW;
//				float ndcY = clipY / clipW;
//				float ndcZ = clipZ / clipW;
//				
//				// get screen coordinates
//				float scX = 0.5f * viewport.get(2) * (ndcX + 1) + viewport.get(0);
//				float scY = viewport.get(3) - 0.5f * viewport.get(3) * (ndcY + 1) + viewport.get(1);
//				@SuppressWarnings("unused")
//				float scZ = 0.5f * ((depthRange.get(1) - depthRange.get(0)) * ndcZ + depthRange.get(1) + depthRange.get(0));
//				
//				// get distance to mouse pointer
//				float dx = scX - events.getMouseX();
//				float dy = scY - events.getMouseY();
//				
//				if (Math.sqrt(dx * dx + dy * dy) < 5.0) {
//					selectedMesh = mesh;
//					selectedVertex = k / (Vertex.FLOATS_PER);
//					
//					float[] col = {0.0f, 1.0f, 0.0f, 1.0f};
//					FloatBuffer colBuf = Buffers.newDirectFloatBuffer(col);
//					
//					gl.glBindBuffer(GL.GL_ARRAY_BUFFER, selectedMesh.getColorBuffer());
//					gl.glBufferSubData(GL.GL_ARRAY_BUFFER, selectedVertex * 16, 16, colBuf);
//					
//					//System.out.println(k / (Mesh.SIZEOF_VERTEX / 4) + " " + Math.sqrt(dx * dx + dy * dy));
//				}
//			}	
//			
//			
//			mesh.drawPoints(drawable, colShader);
//		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("cleanup");
		
		GL3 gl = drawable.getGL().getGL3();
		
		for (Mesh<?> mesh : ImbaCAD.meshes) {
			mesh.dispose(gl);
		}
		
		frameBuffer.dispose(gl);
		texShader.dispose(gl);
	}



	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int z, int h) {
		
		width = z;
		height = h;

		// Get gl
		GL3 gl = drawable.getGL().getGL3();

		// perspectiveGL((GL2)gl, 90.0, (double)width / height, 0.1, 100.0);

		gl.glViewport(0, 0, width, height);
		
		frameBuffer.reshape(gl, width, height);
		
		System.out.println("Window resized to width=" + z + " height=" + h);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public CameraUpdater getCameraUpdater() {
		return cameraUpdater;
	}

	public void setCameraUpdater(CameraUpdater updater) {
		this.cameraUpdater = updater;
	}

//	public HashSet<TextureMesh> getTextureMeshes() {
//		return textureMeshes;
//	}
//
//	public HashSet<ColorMesh> getColorMeshes() {
//		return colorMeshes;
//	}
	
	public void addMesh(Mesh<?> mesh) {
		if (mesh instanceof TextureMesh) {
			textureMeshes.add((TextureMesh)mesh);
		} else if (mesh instanceof ColorMesh) {
			colorMeshes.add((ColorMesh)mesh);
		}
	}
	
	public void removeMesh(Mesh<?> mesh) {
		if (mesh instanceof TextureMesh) {
			textureMeshes.remove((TextureMesh)mesh);
		} else if (mesh instanceof ColorMesh) {
			colorMeshes.remove((ColorMesh)mesh);
		}
	}

}
