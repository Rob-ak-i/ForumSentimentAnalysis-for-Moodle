package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import common.CommonData;
import util.AdditionalDataObjects;

@SuppressWarnings("serial")
public class GUISchematicEditorContextMenu extends JFrame{
	private Container contentPane;
	private ArrayList<Component> components;
	public static GUISchematicEditorContextMenu main; 
	public GUISchematicEditorContextMenu() {
		super("dtfh");
		this.setBounds(600,300,400,400);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setUndecorated(true);
		contentPane=this.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		this.setVisible(false);
		
	}
	public void compileMenu() {
		JMenuItem menuItem;
		menuItem=new JMenuItem(basicAction);
		menuItem.setAlignmentX(LEFT_ALIGNMENT);
		contentPane.add(menuItem);
	}
	Action basicAction = new  AbstractAction("someText") {
		public void actionPerformed(ActionEvent event) {
			
		}
	};
}
