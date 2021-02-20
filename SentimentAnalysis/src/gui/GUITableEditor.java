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

public class GUITableEditor extends JFrame{
	/**was set by random seed*/
	private static final long serialVersionUID = 1382316476397385028L;
	public JComboBoxExt tableName;
	public GridBagLayout layout;
	public JButton tableNameButton; 
	
	
	public GUITableEditor () {
		super(common.Lang.InnerTable.Item.window2Name);
		CommonData.frame2=this;
		this.setSize(CommonData.WIDTH,CommonData.HEIGHT);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		//frame2.setLayout(null);
		this.setVisible(true);
		this.setFocusable(true);
		this.setBounds(100,100,800,600);
		
		layout = new GridBagLayout();
		this.setLayout(layout);
		

		addTableName();
		addTableNameButton();
		addTable();
		JTextField tableName = new JTextField("",5);

	}
	private void addTableNameButton() {
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
		
		tableNameButton = new JButton();
		//tableNameButton.set
		tableNameButton.setText("OK");
		
		layout.setConstraints(tableNameButton, constraints);
		this.add(tableNameButton);
		
	}
	private void addTableName() {
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
		
		tableName = new JComboBoxExt();
		CommonData.dataManager.addControlledComboBox(tableName);
		
		tableName.setEditable(true);
		tableName.setSize(80, 20);
		
		layout.setConstraints(tableName, constraints);
		this.add(tableName);
		
	}
	private void addTable() {
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
		this.add(table);
		
	}
	public static void main(String[] args) {
		common.Settings.initialize();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUITableEditor frame=new GUITableEditor();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
