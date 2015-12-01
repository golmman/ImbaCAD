package imbacad.model;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

public class FrameBuffer {

	private int[] fbo = new int[1];					// Framebuffer Object
	private int[] rboColor = new int[1];			// Renderbuffer Object
	private int[] rboDepthStencil = new int[1];
	private int[] pbo = new int[1];					// Pixelbuffer Object
	
	public FrameBuffer() {}
	
	public void bind(GL3 gl) {
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo[0]);
	}
	
	public void unbind(GL3 gl) {
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
	}
	
	public void init(GL3 gl, int width, int height) {
		// create and set up color renderbuffer object
		gl.glGenRenderbuffers(1, rboColor, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, rboColor[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL3.GL_RGBA8, width, height);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
		
		// create and set up depth and stencil renderbuffer object
		gl.glGenRenderbuffers(1, rboDepthStencil, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, rboDepthStencil[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL3.GL_DEPTH_STENCIL, width, height);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, 0);
		
		// create framebuffer object
		gl.glGenFramebuffers(1, fbo, 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo[0]);
		
		// attach rbos to the fbo
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL3.GL_DEPTH_STENCIL_ATTACHMENT, GL.GL_RENDERBUFFER, rboDepthStencil[0]);
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL3.GL_COLOR_ATTACHMENT0, GL.GL_RENDERBUFFER, rboColor[0]);
		
		// check status
		int fbstatus = gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER);
		if (fbstatus != GL.GL_FRAMEBUFFER_COMPLETE) {
			System.err.println("Framebuffer check failed, status: " + fbstatus);
		}
		
		// unbind fbo, i.e. rebind window framebuffer
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
		
		// create and set up pixelbuffer object
		gl.glGenBuffers(1, pbo, 0);
		gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, pbo[0]);
		gl.glBufferData(GL3.GL_PIXEL_PACK_BUFFER, 4, null, GL3.GL_DYNAMIC_READ);
		gl.glBindBuffer(GL3.GL_PIXEL_PACK_BUFFER, 0);
	}
	
	public void dispose(GL3 gl) {
		gl.glDeleteRenderbuffers(1, rboColor, 0);
		gl.glDeleteRenderbuffers(1, rboDepthStencil, 0);
		gl.glDeleteFramebuffers(1, fbo, 0);
		gl.glDeleteBuffers(1, pbo, 0);
	}

	public void reshape(GL3 gl, int width, int height) {
		dispose(gl);
		init(gl, width, height);
	}
}
