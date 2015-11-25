package imbacad.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;

import imbacad.ImbaCAD;
import imbacad.control.RenderingEventAdapter;
import imbacad.model.DefaultRenderer;
import imbacad.model.Vec3;
import imbacad.model.camera.Camera;
import imbacad.model.camera.LevitateUpdater;
import imbacad.model.camera.OrbitUpdater;
import imbacad.model.camera.PanUpdater;
import imbacad.model.mesh.Mesh;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
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
	
	class MenuMesh extends JCheckBoxMenuItem {
		private static final long serialVersionUID = 3058433128285873718L;
		public Mesh mesh = null;
		public MenuMesh(Mesh mesh) {
			super(mesh.getName());
			this.mesh = mesh;
		}
	}
	
	private static final long serialVersionUID = -2636383013866025654L;
	
	private DefaultRenderer renderer = null;
	private RenderingEventAdapter events = null;
	
	private Animator animator = null;
	
	private JPanel panelControl = new JPanel();
	private GLJPanel panelRendering = null;
	
	private JRadioButton buttonLevitate = new JRadioButton("Levitate");
	private JRadioButton buttonOrbit = new JRadioButton("Orbit");
	private JRadioButton buttonPan = new JRadioButton("Pan");
	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu menuShow = new JMenu("Show Mesh");
	private LinkedList<MenuMesh> menuMeshes = new LinkedList<MenuMesh>();
	
	
	
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
		renderer = new DefaultRenderer(events, camera);
		
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(buttonLevitate);
		bg.add(buttonOrbit);
		bg.add(buttonPan);
		buttonLevitate.addItemListener(this); 
		buttonOrbit.addItemListener(this);
		buttonPan.addItemListener(this);
		
		buttonOrbit.setSelected(true);
		
		menuBar.add(menuShow);
		for (Mesh m: ImbaCAD.meshes) {
			MenuMesh menuItem = new MenuMesh(m);
			menuItem.addItemListener(this);
			menuItem.setSelected(true);	
			menuMeshes.add(menuItem);
			menuShow.add(menuItem);
		}
		
		
		JPanel panelControlWest = new JPanel(); 
		panelControlWest.setLayout(new GridLayout(1, 3));
		panelControlWest.add(menuBar);
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
		panelRendering.addMouseWheelListener(events);
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
		
		if (e.getSource() instanceof MenuMesh) {
			MenuMesh menu = (MenuMesh)e.getSource();
			
			if (e.getStateChange() == ItemEvent.SELECTED) {
				renderer.getMeshMap().add(menu.mesh);
			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				renderer.getMeshMap().remove(menu.mesh);
			}
			
			
			// re-draw
			if (panelRendering != null) {
				panelRendering.display();
			}
			
			
		} else if (e.getSource() instanceof JToggleButton) {
			JToggleButton button = (JToggleButton)e.getItem();
			
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (button == buttonLevitate) {
					renderer.setCameraUpdater(new LevitateUpdater());
					System.out.println("Levitate");
				} else if (button == buttonOrbit) {
					renderer.setCameraUpdater(new OrbitUpdater());
					System.out.println("Orbit");
				} else if (button == buttonPan) {
					renderer.setCameraUpdater(new PanUpdater());
					System.out.println("Pan");
				}
			}
		}
	}


}










