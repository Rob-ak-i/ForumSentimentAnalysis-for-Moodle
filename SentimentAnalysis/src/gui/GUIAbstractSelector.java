package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.CommonData;
import util.EntityAbstractManager;

public class GUIAbstractSelector extends JFrame {
	/**was set by random seed*/
	private static final long serialVersionUID = 2704133509628977253L;
	public static final int innerStateDefault=0;
	public static final int innerStateLoad=1;
	public static final int innerStateSave=2;
	public static final int innerStatePrint=3;
	public static final int innerStateUseForScript=4;
	protected int innerState=innerStateDefault;
	protected ArrayList<String> labelStateNames;
	
	public JComboBoxExt selectorBox;
	public JLabel label;
	public JPanel panel;
	/**prepare */
	public void prepare(int stateNumber) {
		label.setText(labelStateNames.get(stateNumber));
		innerState=stateNumber;
		if(innerState==innerStateLoad)this.selectorBox.setEditable(true);else this.selectorBox.setEditable(false);
		this.setVisible(true);
	}
	public GUIAbstractSelector(String selectorName, EntityAbstractManager<?> manager, AbstractAction buttonAcceptAction) {
		super(selectorName);
		this.setSize(CommonData.WIDTH,CommonData.HEIGHT);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);//frame2.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		//frame2.setLayout(null);
		this.setVisible(false);
		this.setFocusable(true);
		this.setBounds(400,400,200,150);
		
		//GridBagLayout layout = new GridBagLayout();
		panel= new JPanel();
		panel.setLayout(new BorderLayout());
		this.setContentPane(panel);
		//this.setLayout(new BorderLayout());
		
		selectorBox = new JComboBoxExt();
		selectorBox.setBounds(0, 25, 80, 20);
		panel.add(selectorBox,BorderLayout.CENTER);//this.add(selectorBox);
		selectorBox.setEditable(true);
		if(manager!=null)manager.addControlledComboBox(selectorBox);
		
		label = new JLabel();
		label.setBounds(0,0,80,20);
		panel.add(label, BorderLayout.NORTH);
		
		JButton buttonAccept = new JButton();
		buttonAccept.setBounds(0, 50, 80, 20);
		buttonAccept.setText("OK");
		buttonAccept.setAction(buttonAcceptAction);
		panel.add(buttonAccept,BorderLayout.SOUTH);//this.add(buttonAccept);
		labelStateNames = new ArrayList<String>();
		labelStateNames.add("Nothing to do");
		labelStateNames.add(common.Lang.InnerTable.GUI.TableSelectorLableLoad);
		labelStateNames.add(common.Lang.InnerTable.GUI.TableSelectorLableSave);
		labelStateNames.add(common.Lang.InnerTable.GUI.TableSelectorLablePrint);
		labelStateNames.add(common.Lang.InnerTable.GUI.TableSelectorLableUseForScript);
		/*
		protected String[] labelStateNames= {
				"Nothing to do", 
				common.Lang.InnerTable.GUI.TableSelectorLableLoad, 
				common.Lang.InnerTable.GUI.TableSelectorLableSave, 
				common.Lang.InnerTable.GUI.TableSelectorLablePrint,
				common.Lang.InnerTable.GUI.TableSelectorLableUseForScript,
				common.Lang.InnerTable.GUI.TableSelectorLableUpgradeToDataBank
				};
		*/
	}
	/*
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
			default:
				table=CommonData.dataManager.getTable(selectorBox.getCaption());
				if(innerState==3)
					Reports.makeResult1_inner(table);
				if(innerState==4)
					languageprocessing.LanguageProcessor.doAll(table);
				break;
			}
			innerState=innerStateDefault;
			CommonData.frame_tableSelector.setVisible(false);
		}
	};
	*/
}
