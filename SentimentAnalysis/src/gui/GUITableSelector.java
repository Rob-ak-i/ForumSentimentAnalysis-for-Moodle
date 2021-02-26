package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import common.CommonData;
import common.EntityEditorMouseListener;
import common.Reports;
import parts.GenericPhantom;
import util.DataTable;

@SuppressWarnings("serial")
public class GUITableSelector extends GUIAbstractSelector{
	/**was set by random seed*/
	private static final long serialVersionUID = 254623957392575661L;
	public static final int innerState_languageprocessing=5;
	public GUITableSelector() {
		super(common.Lang.InnerTable.Item.windowTableSelectorName, CommonData.dataManager,buttonAcceptAction);
		labelStateNames.add(common.Lang.InnerTable.GUI.TableSelectorLableUpgradeToDataBank);
	}
	private static final AbstractAction buttonAcceptAction=new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			CommonData.frame_tableSelector.performAction();
		}
	};
	public void performAction() {
		DataTable table=null;
		int tableIndex;
		switch(innerState) {
		case innerStateLoad:
			table = DataTable.readFromFile(CommonData.fileName);
			if(table==null)break;
			CommonData.dataManager.addManagedElement(table, selectorBox.getCaption());
			break;
		case innerStateSave:
			table=CommonData.dataManager.getManagedElement(selectorBox.getCaption());
			if(table==null)break;
			table.saveDataToFile(CommonData.fileName, true);
			break;
		case innerStatePrint:
			tableIndex=CommonData.dataManager.getManagedElementIndex(selectorBox.getCaption());
			if(tableIndex==-1)break;
			EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_PUT_PHANTOM_MODE);
			GenericPhantom.prepare(GenericPhantom.phantomType_FORUM, tableIndex);
			break;
		case innerState_languageprocessing:
			table=CommonData.dataManager.getManagedElement(selectorBox.getCaption());
			if(table==null)break;
			CommonData.textManager.addManagedElement(selectorBox.getCaption());
			System.out.println("five/nine work");
			//this tool may be put to MessageBank
			languageprocessing.LanguageProcessor.doAllWIP(null);
			break;
		case innerStateUseForScript:
			table=CommonData.dataManager.getManagedElement(selectorBox.getCaption());
			if(table==null)break;
			System.out.println("Multiscripting maybe WIF...");
			Reports.makeResult1_inner(table);
		default:
			break;
		}
		innerState=innerStateDefault;
		CommonData.frame_tableSelector.setVisible(false);
	}
}
