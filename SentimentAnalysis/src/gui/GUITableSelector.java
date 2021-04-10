package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import common.CommonData;
import common.EntityEditorMouseListener;
import common.MessageBank;
import common.Reports;
import parts.GenericPhantom;
import util.AdditionalDataObjects;
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
	protected void performActionInner() {
		DataTable table=null;
		int tableIndex;
		switch(innerState) {
		case innerStateLoad:
			processProcessor = CommonData.dataManager;
			processMethod="loadDataTable";
			processParameters=AdditionalDataObjects.pack("fileName", CommonData.fileName).pack("key", selectorBox.getCaption());
			
			//table = DataTable.readFromFile(CommonData.fileName);
			//if(table==null)break;
			//CommonData.dataManager.addManagedElement(table, selectorBox.getCaption());
			break;
		case innerStateSave:
			processProcessor = CommonData.dataManager;
			processMethod="saveDataTable";
			processParameters=AdditionalDataObjects.pack("fileName", CommonData.fileName).pack("key", selectorBox.getCaption());
			
			//table=CommonData.dataManager.getManagedElement(selectorBox.getCaption());
			//if(table==null)break;
			//table.saveDataToFile(CommonData.fileName, true);
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

			processProcessor = CommonData.textManager;
			processMethod="addMessagesBank";
			processParameters=AdditionalDataObjects.pack("key", selectorBox.getCaption());
			
			CommonData.taskManager.addOperation(processProcessor, processMethod, processParameters);

			//this tool may be put to MessageBank
			
			processProcessor = new languageprocessing.LanguageProcessor();
			processMethod="doAll";
			//TODO PipeLine
			processParameters=AdditionalDataObjects.pack("messagesBank", 
					new MessageBank()//CommonData.textManager.getManagedElement(common.Lang.InnerTable.Misc.dataTableToMessageBankAppendix+selectorBox.getCaption())
					);
			
			//languageprocessing.LanguageProcessor.doAllWIP(null);
			break;
		case innerStateUseForScript:
			table=CommonData.dataManager.getManagedElement(selectorBox.getCaption());
			if(table==null)break;
			System.out.println("Multiscripting maybe WIF...");

			processProcessor = new Reports();
			processMethod="makeResult1_inner";
			processParameters=AdditionalDataObjects.pack("selectedTable", table);
			
			//Reports.makeResult1_inner(table);
		default:
			break;
		}
		innerState=innerStateDefault;
		CommonData.frame_tableSelector.setVisible(false);
	}
}
