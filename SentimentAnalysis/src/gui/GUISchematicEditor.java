package gui;


import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import common.CommonData;
import common.EntityEditorMouseListener;
import common.EntityRenderer;
import parts.SchematicManager;

/**graphic part of GUI*/
public class GUISchematicEditor extends JFrame{
	/**was set by random seed*/
	private static final long serialVersionUID = 1189687322447209901L;
	public JPanel panel;
	public JToolBar toolbar;
	public JMenuBar menuBar;
	public JTextPane statementPanel;
	public Canvas canvas;
	public GUISchematicEditorContextMenu contextMenu;
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
		panel.setLayout(new BorderLayout());
		this.setContentPane(panel);
		toolbar = new  JToolBar("Toolbar", JToolBar.HORIZONTAL);
		GUIHelper.toolbarButtonsAdd(toolbar);
		GUIHelper.toolbar_PartsButtonsAdd(toolbar);
		toolbar.setPreferredSize(new Dimension(CommonData.WIDTH, 30));
		this.add(toolbar, BorderLayout.NORTH);
		

		//postLoad
		canvas = new Canvas();
		CommonData.canvas=canvas;
        canvas.setBackground(Color.white);
        canvas.setPreferredSize(new Dimension(CommonData.WIDTH, CommonData.HEIGHT-100));
        //canvas.setMinimumSize(new Dimension(100, 100));
        //canvas.setMaximumSize(new Dimension(2000, 2000));
        this.add(canvas, BorderLayout.CENTER);
		addListeners();
		
		statementPanel = new JTextPane(); 
		statementPanel.setBackground(new Color(0xeFeFf7));
		statementPanel.setEditable(false);
		statementPanel.setPreferredSize(new Dimension(CommonData.WIDTH, 20));
        this.add(statementPanel, BorderLayout.SOUTH);
		this.pack();
		
		
		EntityEditorMouseListener listener = new EntityEditorMouseListener(this.toolbar, this.menuBar, this, this.panel);
		EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_OBSERVE);
		CommonData.listener=listener;
		CommonData.renderer = new EntityRenderer(this, this.panel, this.canvas, 0);
		CommonData.renderer.createCanvasComponents(false);
		CommonData.renderer.start();

		contextMenu = new GUISchematicEditorContextMenu();
		
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

    public void clearColoredText() {
    	statementPanel.setText("");
    }
	public void addColoredText(String text, Color color) {
		StyledDocument doc = statementPanel.getStyledDocument();
		Style style = statementPanel.addStyle("Color Style", null);
		StyleConstants.setForeground(style, color);
		try {
			doc.insertString(doc.getLength(), text, style);
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}