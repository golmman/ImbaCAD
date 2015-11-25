package imbacad.model.mesh;

import imbacad.model.Vec2;
import imbacad.model.Vec3;


/**
 * 		  topL_____________topR
 * 		     /            /
 * 		    /    ^       /
 * 		   /     |      /
 * 		  /   normal   /
 * 		 /            /
 * 		/____________/
 *   botL            botR
 *   
 * @author Dirk Kretschmann
 *
 */
public class Plaster {
	
	private Vec3 topR;
	private Vec3 topL;
	private Vec3 botL;
	private Vec3 botR;
	
	public Plaster(Vec3 topR, Vec3 topL, Vec3 botL, Vec3 botR) {
		this.topR = topR;
		this.topL = topL;
		this.botL = botL;
		this.botR = botR;
	}
	
	/**
	 * Creates an even plaster, i.e. the plaster plane is parallel to the z-axis
	 * and the top and bottom edge are parallel to the xy-plane.
	 * @param left
	 * @param right
	 * @param bot
	 * @param top
	 */
	public Plaster(Vec2 left, Vec2 right, float top, float bot) {
		topR = new Vec3(right.getX(), right.getY(), top);
		topL = new Vec3( left.getX(),  left.getY(), top);
		botL = new Vec3( left.getX(),  left.getY(), bot);
		botR = new Vec3(right.getX(), right.getY(), bot);
	}
	
	public Plaster(float leftx, float lefty, float rightx, float righty, float top, float bot) {
		topR = new Vec3(rightx, righty, top);
		topL = new Vec3( leftx,  lefty, top);
		botL = new Vec3( leftx,  lefty, bot);
		botR = new Vec3(rightx, righty, bot);
	}
	
	public Plaster() {
		this.topR = new Vec3();
		this.topL = new Vec3();
		this.botL = new Vec3();
		this.botR = new Vec3();
	}
	
	
	

	public Vec3 getTopR() {
		return topR;
	}

	public void setTopR(Vec3 topR) {
		this.topR = topR;
	}

	public Vec3 getTopL() {
		return topL;
	}

	public void setTopL(Vec3 topL) {
		this.topL = topL;
	}

	public Vec3 getBotL() {
		return botL;
	}

	public void setBotL(Vec3 botL) {
		this.botL = botL;
	}

	public Vec3 getBotR() {
		return botR;
	}

	public void setBotR(Vec3 botR) {
		this.botR = botR;
	}

}
