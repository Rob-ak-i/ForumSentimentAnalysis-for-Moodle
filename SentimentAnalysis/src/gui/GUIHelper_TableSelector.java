package gui;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import common.CommonData;
import common.Lang;
import common.Reports;
import common.Lang.InnerTable;
import common.Lang.InnerTable.Item;
import util.DataTable;

import javax.swing.JComboBox;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class GUIHelper_TableSelector extends JFrame{
	private static final int innerStateDefault=0;
	private static final int innerStateLoad=1;
	private static final int innerStateSave=2;
	private static final int innerStateSaveReport=3;
	private static int reportNumber=0;
	private static int innerState=innerStateDefault;
	public static JComboBoxExt selectorBox;
	public static JLabel label;
	public void prepareToLoad() {
		label.setText(Lang.InnerTable.GUI.TableSelectorLable3);
		innerState=innerStateLoad;
		this.setVisible(true);
	}
	public void prepareToSave() {
		label.setText(Lang.InnerTable.GUI.TableSelectorLable2);
		innerState=innerStateSave;
		this.setVisible(true);
	}
	public void prepareToSaveReport(int reportNumber) {
		label.setText(Lang.InnerTable.GUI.TableSelectorLable1);
		innerState=innerStateSaveReport;
		this.reportNumber=reportNumber;
		this.setVisible(true);
	}
	public GUIHelper_TableSelector() {
		super(common.Lang.InnerTable.Item.windowTableSelectorName);
		CommonData.frame_tableSelector=this;
		this.setSize(CommonData.WIDTH,CommonData.HEIGHT);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);//frame2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		//frame2.setLayout(null);
		this.setVisible(false);
		this.setFocusable(true);
		this.setBounds(300,300,150,100);
		
		//GridBagLayout layout = new GridBagLayout();
		JPanel panel= new JPanel();
		panel.setLayout(new BorderLayout());
		this.setContentPane(panel);
		//this.setLayout(new BorderLayout());
		
		selectorBox = new JComboBoxExt();
		selectorBox.setBounds(0, 25, 80, 20);
		panel.add(selectorBox,BorderLayout.CENTER);//this.add(selectorBox);
		selectorBox.setEditable(true);
		CommonData.dataManager.addControlledComboBox(selectorBox);
		
		label = new JLabel();
		label.setBounds(0,0,80,20);
		panel.add(label, BorderLayout.NORTH);
		
		JButton buttonAccept = new JButton();
		buttonAccept.setBounds(0, 50, 80, 20);
		buttonAccept.setText("OK");
		buttonAccept.setAction(buttonAcceptAction);
		panel.add(buttonAccept,BorderLayout.SOUTH);//this.add(buttonAccept);
	}
	private static final AbstractAction buttonAcceptAction=new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			DataTable table=null;
			switch(innerState) {
			case innerStateLoad:
				table = DataTable.readFromFile(CommonData.fileName);
				if(table==null)return;
				CommonData.dataManager.addTable(table, selectorBox.getCaption());
				break;
			case innerStateSave:
				table=CommonData.dataManager.getTable(selectorBox.getCaption());
				if(table==null)return;
				table.saveDataToFile(CommonData.fileName, true);
				break;
			case innerStateSaveReport:
				table=CommonData.dataManager.getTable(selectorBox.getCaption());
				if(reportNumber==1)
					Reports.makeResult1_inner(table);
				break;
			}
			innerState=innerStateDefault;
			CommonData.frame_tableSelector.setVisible(false);
		}
	};
}
