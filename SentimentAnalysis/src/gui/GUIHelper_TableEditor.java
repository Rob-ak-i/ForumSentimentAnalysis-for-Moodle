package gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import common.CommonData;

public class GUIHelper_TableEditor {
	public static void initTableEditorWindow() {

		JFrame frame2=new JFrame(common.Lang.InnerTable.Item.window2Name);
		CommonData.frame2=frame2;
		frame2.setSize(CommonData.WIDTH,CommonData.HEIGHT);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//frame2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame2.setResizable(true);
		//frame2.setLayout(null);
		frame2.setVisible(true);
		frame2.setFocusable(true);
		frame2.setBounds(100,100,800,600);
		
		GridBagLayout layout = new GridBagLayout();
		frame2.setLayout(layout);
		

		addTableName(layout,frame2);
		addTableNameButton(layout,frame2);
		addTable(layout,frame2);
		JTextField tableName = new JTextField("",5);
		
	}
	private static void addTableNameButton(GridBagLayout layout, JFrame frame) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor=GridBagConstraints.WEST;
		constraints.fill=GridBagConstraints.NONE;
		constraints.gridheight=1;
		constraints.gridwidth=0;
		constraints.gridx=GridBagConstraints.RELATIVE;
		constraints.gridy=GridBagConstraints.RELATIVE;
		constraints.insets=new Insets(50,0,0,0);
		constraints.ipadx=0;
		constraints.ipady=0;
		constraints.weightx=0;
		constraints.weighty=0;
		
		JButton tableNameButton = new JButton();
		//tableNameButton.set
		tableNameButton.setText("OK");
		
		layout.setConstraints(tableNameButton, constraints);
		frame.add(tableNameButton);
		
	}
	private static void addTableName(GridBagLayout layout, JFrame frame) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor=GridBagConstraints.NORTH;
		constraints.fill=GridBagConstraints.NONE;
		constraints.gridheight=1;
		constraints.gridwidth=1;
		constraints.gridx=GridBagConstraints.RELATIVE;
		constraints.gridy=GridBagConstraints.RELATIVE;
		constraints.insets=new Insets(50,0,0,0);
		constraints.ipadx=0;
		constraints.ipady=0;
		constraints.weightx=0;
		constraints.weighty=0;
		
		JComboBoxExt tableName = new JComboBoxExt();
		//CommonData.GUI2_identifiersComboBox=tableName;
		CommonData.dataManager.addControlledComboBox(tableName);
		
		tableName.setEditable(true);
		tableName.setSize(80, 20);
		
		layout.setConstraints(tableName, constraints);
		frame.add(tableName);
		
	}
	private static void addTable(GridBagLayout layout, JFrame frame) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor=GridBagConstraints.NORTH;
		constraints.fill=GridBagConstraints.NONE;
		constraints.gridheight=1;
		constraints.gridwidth=GridBagConstraints.REMAINDER;
		constraints.gridx=GridBagConstraints.RELATIVE;
		constraints.gridy=GridBagConstraints.RELATIVE;
		constraints.insets=new Insets(40,0,0,0);
		constraints.ipadx=0;
		constraints.ipady=0;
		constraints.weightx=0;
		constraints.weighty=0;
		
		String[] colNames= {"userID", "userName", "postID", "parentPostID", "messageText"};
		Object[][] data= {{new Integer(34),"Robert",0,0,"hello, everyone!"},{0,"",0,0,""},{21,"Dilovar",1,0,"hello!!!"}};
		JTable table = new JTable(data,colNames); 

		table.setFillsViewportHeight(true);
		
		layout.setConstraints(table, constraints);
		frame.add(table);
		
	}
	public static void main(String[] args) {
		common.Settings.initialize();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUIHelper_TableEditor.initTableEditorWindow();
			}
		});
	}
}
