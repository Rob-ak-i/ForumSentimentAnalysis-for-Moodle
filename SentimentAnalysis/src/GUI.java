import  java.awt.*;
import  java.io.*;
import  javax.swing.*;
import  java.awt.image.*;

import common.EntityEditor;
import parts.PartBasic;
import common.CommonData;
import common.EntityEditor_Helper;
import common.EntityRenderer;

public class GUI {


	JFrame frame;
	JPanel panel;//MyPanel panel;
	JToolBar toolbar;
	JToolBar toolbarPartEdit;
	
	

	
	
	String dataSubDir = CommonData.dataSubDir;

	public GUI() {
		
		frame=new JFrame(common.Lang.InnerTable.Item.windowName);
		frame.setSize(CommonData.WIDTH,CommonData.HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menuBar = new  JMenuBar();
		frame.setJMenuBar(menuBar);
		menuBar.setBounds(0,0,350,30);
		GUIHelper_MainMenu.addActionsToMenuBar(menuBar);
		
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setVisible(true);
		
		panel = new  JPanel();//panel = new  MyPanel();
		panel.setBounds(0, 30, CommonData.WIDTH, CommonData.HEIGHT);
		toolbar = new  JToolBar("Toolbar", JToolBar.HORIZONTAL);
		GUIHelper.toolbarButtonsAdd(toolbar);
		GUIHelper.toolbar_PartsButtonsAdd(toolbar);
		EntityRenderer renderer = new EntityRenderer(frame, panel, 0);
		renderer.start();
		toolbar.setBounds(0, 0, CommonData.WIDTH, 30);
		frame.add(toolbar);
		
		//panel.repaint();
		
		//postLoad
		CommonData.scheme = new EntityEditor(CommonData.WIDTH, CommonData.HEIGHT);
		EntityEditor_Helper.panel=panel;
		EntityEditor_Helper.frame=frame;
		EntityEditor_Helper.renderer=renderer;
		
		//afterLoad
		EntityEditor_Helper.addListeners();
	}
/*
	@SuppressWarnings("serial")
	class MyPanel extends JPanel {
			public MyPanel() { }
			
			public void paintComponent (Graphics g) {
				if(CommonGraphic.imag==null) {
					CommonGraphic.imag = new  BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
						Graphics d = CommonGraphic.imag.createGraphics();
					Graphics2D d2 = (Graphics2D) d;
					d2.setColor(Color.white);
					d2.fillRect(0, 0, this.getWidth(), this.getHeight());
						CommonGraphic.graphics=d;
						CommonGraphic.graphics2d=d2;
						PartBasic.g=CommonGraphic.graphics;
				}
				 super.paintComponent(g);
				 g.drawImage(CommonGraphic.imag, 0, 0,this);      
			}
	}
*/

}