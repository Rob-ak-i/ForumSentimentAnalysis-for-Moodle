package gui;


import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import common.CommonData;
import common.EntityEditorMouseListener;
import common.EntityRenderer;

import parts.GenericPhantom;
import parts.PartBinaryBasic;
import parts.PartNode;
import parts.SchematicManager;
import util.Dot;

public class GUISchematicEditor extends JFrame{
	/**was set by random seed*/
	private static final long serialVersionUID = 1189687322447209901L;
	public JPanel panel;
	public JToolBar toolbar;
	public JMenuBar menuBar;
	public Canvas canvas;
	//public JToolBar toolbarPartEdit;
	/**if we load picture*/
	public static boolean loading=false;
	public GUISchematicEditor() {
		super(common.Lang.InnerTable.Item.windowName);
		this.setSize(CommonData.WIDTH,CommonData.HEIGHT+CommonData.HEIGHTFRAMEDIFFERENCE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuBar = new  JMenuBar();
		this.setJMenuBar(menuBar);
		menuBar.setBounds(0,0,350,30);
		GUIHelper_MainMenu.addActionsToMenuBar(menuBar);
		
		this.setResizable(false);
		this.setLayout(null);
		this.setVisible(true);
		this.setFocusable(true);
		
		
		panel = new  JPanel();//panel = new  MyPanel();
		panel.setBounds(0, 30, CommonData.WIDTH, CommonData.HEIGHT);
		//panel.setLayout(new FlowLayout());
		this.setContentPane(panel);
		toolbar = new  JToolBar("Toolbar", JToolBar.HORIZONTAL);
		GUIHelper.toolbarButtonsAdd(toolbar);
		GUIHelper.toolbar_PartsButtonsAdd(toolbar);
		toolbar.setBounds(0, 0, CommonData.WIDTH, 30);
		this.add(toolbar);
		

		//postLoad
		canvas = new Canvas();
		CommonData.canvas=canvas;
        canvas.setBackground(Color.white);
        canvas.setPreferredSize(new Dimension(CommonData.WIDTH, CommonData.HEIGHT));
        canvas.setMinimumSize(new Dimension(100, 100));
        canvas.setMaximumSize(new Dimension(2000, 2000));
        panel.add(canvas);
		addListeners();

		//panel.setBounds(0,30,CommonData.WIDTH, CommonData.HEIGHT);
		//panel.setBackground(Color.white);
		//panel.setOpaque(true);
		//frame.getContentPane().add(panel);//frame.add(panel);
		
		EntityEditorMouseListener listener = new EntityEditorMouseListener(this.toolbar, this.menuBar, this, this.panel);
		EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_OBSERVE);
		CommonData.listener=listener;
		CommonData.renderer = new EntityRenderer(this, this.panel, this.canvas, 0);
		CommonData.renderer.createCanvasComponents(false);
		CommonData.renderer.start();
		//CommonData.renderer=renderer;
		//GUIHelper_Listeners.addListeners(CommonData.frame);
		
		canvas.addMouseListener(CommonData.listener);
		canvas.addMouseMotionListener(CommonData.listener);
		canvas.addMouseWheelListener(EntityEditorMouseListener.mouseWheelListener);
		canvas.addKeyListener(EntityEditorMouseListener.keyAdapter);
	}
	private void addListeners(){		
		this.addComponentListener(new  ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent evt) {
				/**if loading, then change forms size*/
				/**making this in loading code section(bottom)*/
				if(CommonData.frame==null||CommonData.frame.panel==null)return;
				if(loading==false) {
					loading=true;
					CommonData.WIDTH=CommonData.frame.getWidth();
					CommonData.HEIGHT=CommonData.frame.getHeight()-CommonData.HEIGHTFRAMEDIFFERENCE;
					CommonData.frame.panel.setSize(CommonData.WIDTH, CommonData.HEIGHT);
					CommonData.renderer.updateWindow(true);
					CommonData.renderer.createCanvasComponents(true);
					SchematicManager.resize();
					//CommonData.frame.pack();
				}
				
				loading=false;
			}
		});
	}
}
