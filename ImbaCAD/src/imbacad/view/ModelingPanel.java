package imbacad.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import imbacad.ImbaCAD;
import imbacad.control.RenderingEventAdapter;
import imbacad.model.DefaultRenderer;
import imbacad.model.Glm;
import imbacad.model.Mesh;
import imbacad.model.Vec3;
import imbacad.model.camera.Camera;
import imbacad.model.camera.LevitateUpdater;
import imbacad.model.camera.OrbitUpdater;
import imbacad.model.camera.PanUpdater;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.Animator;

/**
 * 
 * A Container on which all the display action happens.
 * @author Dirk Kretschmann
 *
 */
public class ModelingPanel extends JPanel implements ComponentListener, ItemListener {

	private static final long serialVersionUID = -2636383013866025654L;
	
	private DefaultRenderer renderer = null;
	private RenderingEventAdapter events = null;
	
	private Animator animator = null;
	
	private JPanel panelControl = new JPanel();
	private GLJPanel panelRendering = null;
	
	private JRadioButton buttonLevitate = new JRadioButton("Levitate");
	private JRadioButton buttonOrbit = new JRadioButton("Orbit");
	private JRadioButton buttonPan = new JRadioButton("Pan");
	
	
	public static void main(String[] args) {
		Glm.init();
		
		Mesh mesh1 = new Mesh("test2.jpg", MainWindow.testVertices, MainWindow.testIndices);
		Mesh mesh2 = new Mesh("test.bmp", MainWindow.testVertices, MainWindow.testIndices);
		mesh2.setPosition(new Vec3(0.5f, -1.5f, 0.0f));
		ImbaCAD.meshes.add(mesh1);
		ImbaCAD.meshes.add(mesh2);
		
		
		Animator ani = new Animator();
		
		JFrame frame = new JFrame("Test");
		frame.setBounds(100, 100, 400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1, 1));
		
		ModelingPanel mw = new ModelingPanel(ani);
		
		frame.add(mw);
		
		frame.setVisible(true);
		
		
		ani.start();
	}
	
	
	
	
	
	public ModelingPanel(Animator animator) {
		super();
		
		System.out.println("new ModelingWindow");		
		
		GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL3));
		caps.setBackgroundOpaque(false);
		
		
		Camera camera = new Camera();
		camera.setPosition(new Vec3(-3.0f, 1.0f, 1.5f));
		camera.setAzimuthAngle(0.0f);
		camera.setPolarAngle((float)(0.6f * Math.PI));
		camera.setFov((float)(0.5f * Math.PI));
		camera.setVelocity(0.01f);
		
		camera.lookAt(new Vec3());
		
		events = new RenderingEventAdapter();
		renderer = new DefaultRenderer(events, camera, new LevitateUpdater());
		
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(buttonLevitate);
		bg.add(buttonOrbit);
		bg.add(buttonPan);
		buttonLevitate.addItemListener(this); 
		buttonOrbit.addItemListener(this);
		buttonPan.addItemListener(this);
		
		buttonLevitate.setSelected(true);
		
		JPanel panelControlWest = new JPanel(); 
		panelControlWest.setLayout(new GridLayout(1, 3));
		panelControlWest.add(buttonLevitate);
		panelControlWest.add(buttonOrbit);
		panelControlWest.add(buttonPan);
		panelControl.setLayout(new BorderLayout());
		panelControl.add(panelControlWest, BorderLayout.WEST);

		
		panelRendering = new GLJPanel(caps);
		panelRendering.addGLEventListener(renderer);
		panelRendering.addKeyListener(events);
		panelRendering.addMouseListener(events);
		panelRendering.addMouseMotionListener(events);
		panelRendering.addFocusListener(events);
		
		
		this.setLayout(null);
		
		this.addComponentListener(this);
		
		this.add(panelControl);
		this.add(panelRendering);
		
		this.animator = animator;
		this.animator.add(panelRendering);
	}





	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentMoved(ComponentEvent e) {}


	@Override
	public void componentResized(ComponentEvent e) {
		int w = this.getWidth();
		int h = this.getHeight();
		final int CONTROL_HEIGHT = 25; 
		
		panelControl.setBounds(0, 0, w, CONTROL_HEIGHT);
		panelRendering.setBounds(0, CONTROL_HEIGHT, w, h - CONTROL_HEIGHT);
	}

	@Override
	public void componentShown(ComponentEvent e) {}


	
	@Override
	public void itemStateChanged(ItemEvent e) {
		
		if (e.getStateChange() == ItemEvent.SELECTED) {
			JToggleButton button = (JToggleButton)e.getItem();
			
			if (button == buttonLevitate) {
				renderer.setProcessor(new LevitateUpdater());
				System.out.println("Levitate");
			} else if (button == buttonOrbit) {
				renderer.setProcessor(new OrbitUpdater());
				System.out.println("Orbit");
			} else if (button == buttonPan) {
				renderer.setProcessor(new PanUpdater());
				System.out.println("Pan");
			}
		}
	}
}










