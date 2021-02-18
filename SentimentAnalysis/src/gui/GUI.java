package gui;
import  java.awt.*;
import  java.io.*;

import  javax.swing.*;

import  java.awt.image.*;

import common.EntityEditor;
import parts.PartBasic;
import common.CommonData;
import common.EntityEditorMouseListener;
import common.EntityRenderer;

public class GUI {


	JFrame frame;
	JPanel panel;//MyPanel panel;
	JToolBar toolbar;
	JToolBar toolbarPartEdit;
	
	

	
	
	String dataSubDir = CommonData.dataSubDir;

	public GUI() {
		new GUIHelper_TableSelector();
		frame=new JFrame(common.Lang.InnerTable.Item.windowName);
		frame.setSize(CommonData.WIDTH,CommonData.HEIGHT+CommonData.HEIGHTFRAMEDIFFERENCE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar = new  JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.setBounds(0,0,350,30);
		GUIHelper_MainMenu.addActionsToMenuBar(menuBar);
		
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setFocusable(true);
		
		
		panel = new  JPanel();//panel = new  MyPanel();
		panel.setBounds(0, 30, CommonData.WIDTH, CommonData.HEIGHT);
		//panel.setLayout(new FlowLayout());
		frame.setContentPane(panel);
		toolbar = new  JToolBar("Toolbar", JToolBar.HORIZONTAL);
		GUIHelper.toolbarButtonsAdd(toolbar);
		GUIHelper.toolbar_PartsButtonsAdd(toolbar);
		toolbar.setBounds(0, 0, CommonData.WIDTH, 30);
		frame.add(toolbar);
		//postLoad
		Canvas canvas = new Canvas();
		CommonData.canvas=canvas;
		CommonData.scheme = new EntityEditor(CommonData.WIDTH, CommonData.HEIGHT);
		CommonData.panel=panel;
		CommonData.frame=frame;
		EntityEditorMouseListener listener = new EntityEditorMouseListener(toolbar, menuBar, frame, panel);
		CommonData.listener=listener;
		EntityRenderer renderer = new EntityRenderer(frame, panel, canvas, 0);
		renderer.createCanvasComponents(false);
		renderer.start();
		CommonData.renderer=renderer;
		GUIHelper_Listeners.addListeners(frame);
		
		canvas.addMouseListener(CommonData.listener);
		canvas.addMouseMotionListener(CommonData.listener);
		canvas.addMouseWheelListener(EntityEditorMouseListener.mouseWheelListener);
		canvas.addKeyListener(EntityEditorMouseListener.keyAdapter);
		//panel.addMouseListener(listener);
		//panel.addMouseMotionListener(listener);
		GUIHelper_TableEditor.initTableEditorWindow();
	}

}