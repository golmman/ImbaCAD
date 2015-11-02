package imbacad.model;

import imbacad.ImbaCAD;
import imbacad.control.RenderingEventAdapter;
import imbacad.model.camera.Camera;
import imbacad.model.camera.CameraUpdater;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

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
	
	private Shader texShader = null;
	private Shader colShader = null;
	
	private Mesh selectedMesh = null;
	private int selectedVertex = -1;
	
	// TODO: only for debug
	private float alpha = 0.0f;
	
	public DefaultRenderer(RenderingEventAdapter events, Camera camera, CameraUpdater updater) {
		this.events = events;
		this.cameraUpdater = updater;
		this.camera = camera;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL3 gl = drawable.getGL().getGL3();
		
		this.texShader = new Shader(drawable, new File("shader/texVertex.shader"), new File("shader/texFragment.shader"));
		this.colShader = new Shader(drawable, new File("shader/colVertex.shader"), new File("shader/colFragment.shader"));
		
		gl.glEnable(GL3.GL_CULL_FACE);
		gl.glEnable(GL3.GL_PROGRAM_POINT_SIZE);
		
		
		
		for (Mesh mesh : ImbaCAD.meshes) {
			mesh.init(drawable);
		}
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
		gl.glUniformMatrix4fv(texShader.getView(), 1, false,	view, 0);
		gl.glUniformMatrix4fv(texShader.getProjection(), 1, false,	projection, 0);
		
		for (Mesh mesh : ImbaCAD.meshes) {
			
			if (mesh == ImbaCAD.meshes.getLast()) {
				// rotate last
				alpha += 0.01f;
				mesh.getRotation().setZ(alpha);
			}
			
			model = Glm.diag(1.0f);
			model = Glm.translate(model, (mesh.getPosition().mul(-1.0f)).toArray());
			model = Glm.rotate(model, mesh.getRotation().getX(), Glm.vec3(1.0f, 0.0f, 0.0f));
			model = Glm.rotate(model, mesh.getRotation().getZ(), Glm.vec3(0.0f, 0.0f, 1.0f));
			
			gl.glUniformMatrix4fv(texShader.getModel(), 1, false,	model, 0);
			
			mesh.draw(drawable, texShader.getProgram());
			
		}
		
		/*
		 * draw selection TODO: Create class which handles that
		 */
		gl.glUseProgram(colShader.getProgram());
		gl.glUniformMatrix4fv(colShader.getView(), 1, false,	view, 0);
		gl.glUniformMatrix4fv(colShader.getProjection(), 1, false,	projection, 0);
		
		// remove last selection
		if (selectedMesh != null) {
			float[] col = {1.0f, 0.0f, 0.0f, 1.0f};
			FloatBuffer colBuf = Buffers.newDirectFloatBuffer(col);
			
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, selectedMesh.getColorBuffer());
			gl.glBufferSubData(GL.GL_ARRAY_BUFFER, selectedVertex * 16, 16, colBuf);
			
			selectedMesh = null;
			selectedVertex = -1;
		}
		
		// draw new selection
		for (Mesh mesh : ImbaCAD.meshes) {
			
			model = Glm.diag(1.0f);
			model = Glm.translate(model, (mesh.getPosition().mul(-1.0f)).toArray());
			model = Glm.rotate(model, mesh.getRotation().getX(), Glm.vec3(1.0f, 0.0f, 0.0f));
			model = Glm.rotate(model, mesh.getRotation().getZ(), Glm.vec3(0.0f, 0.0f, 1.0f));
			
			gl.glUniformMatrix4fv(colShader.getModel(), 1, false,	model, 0);
			
			
			
			int floatCount = mesh.getVertexCount() * Mesh.SIZEOF_VERTEX / 4;
			
			// get data from the gpu
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, mesh.getVertexBuffer());
			FloatBuffer vertices = Buffers.newDirectFloatBuffer(floatCount);
			gl.glGetBufferSubData(GL.GL_ARRAY_BUFFER, 0, 4 * floatCount, vertices);
			IntBuffer viewport = Buffers.newDirectIntBuffer(4);
			gl.glGetIntegerv(GL.GL_VIEWPORT, viewport);
			FloatBuffer depthRange = Buffers.newDirectFloatBuffer(2);
			gl.glGetFloatv(GL.GL_DEPTH_RANGE, depthRange);
			
			// get model-view-projection matrix
			float[] mvp = Glm.mul(Glm.mul(projection, view), model);
			
			// calculate each vertex screen coordinate
			for (int k = 0; k < floatCount; k += Mesh.SIZEOF_VERTEX / 4) {
				// get object coordinates, equal to mesh.getVertices()[0];
				float objX = vertices.get(k+0); 		
				float objY = vertices.get(k+1);
				float objZ = vertices.get(k+2);
				float objW = 1.0f;
				
				// get clip coordinates, OpenGL uses column-major order
				float clipX = objX * mvp[ 0] + objY * mvp[ 4] + objZ * mvp[ 8] + objW * mvp[12];
				float clipY = objX * mvp[ 1] + objY * mvp[ 5] + objZ * mvp[ 9] + objW * mvp[13];
				float clipZ = objX * mvp[ 2] + objY * mvp[ 6] + objZ * mvp[10] + objW * mvp[14];
				float clipW = objX * mvp[ 3] + objY * mvp[ 7] + objZ * mvp[11] + objW * mvp[15];
				
				// get normalized device coordinates
				float ndcX = clipX / clipW;
				float ndcY = clipY / clipW;
				float ndcZ = clipZ / clipW;
				
				// get screen coordinates
				float scX = 0.5f * viewport.get(2) * (ndcX + 1) + viewport.get(0);
				float scY = viewport.get(3) - 0.5f * viewport.get(3) * (ndcY + 1) + viewport.get(1);
				float scZ = 0.5f * ((depthRange.get(1) - depthRange.get(0)) * ndcZ + depthRange.get(1) + depthRange.get(0));
				
				// get distance to mouse pointer
				float dx = scX - events.getMouseX();
				float dy = scY - events.getMouseY();
				
				if (Math.sqrt(dx * dx + dy * dy) < 5.0) {
					selectedMesh = mesh;
					selectedVertex = k / (Mesh.SIZEOF_VERTEX / 4);
					
					float[] col = {0.0f, 1.0f, 0.0f, 1.0f};
					FloatBuffer colBuf = Buffers.newDirectFloatBuffer(col);
					
					gl.glBindBuffer(GL.GL_ARRAY_BUFFER, selectedMesh.getColorBuffer());
					gl.glBufferSubData(GL.GL_ARRAY_BUFFER, selectedVertex * 16, 16, colBuf);
					
					//System.out.println(k / (Mesh.SIZEOF_VERTEX / 4) + " " + Math.sqrt(dx * dx + dy * dy));
				}
			}	
			
			
			mesh.drawPoints(drawable, colShader.getProgram());
		}
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		System.out.println("cleanup");
		
		//GL3 gl = drawable.getGL().getGL3();
		
		for (Mesh mesh : ImbaCAD.meshes) {
			mesh.dispose(drawable);
		}
		
		texShader.dispose(drawable);
	}



	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int z, int h) {
		
		width = z;
		height = h;

		// Get gl
		GL3 gl = drawable.getGL().getGL3();

		// perspectiveGL((GL2)gl, 90.0, (double)width / height, 0.1, 100.0);

		gl.glViewport(0, 0, width, height);
		
		System.out.println("Window resized to width=" + z + " height=" + h);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public CameraUpdater getProcessor() {
		return cameraUpdater;
	}

	public void setProcessor(CameraUpdater processor) {
		this.cameraUpdater = processor;
	}

}
