package common;

import java.util.HashMap;

import languageprocessing.Sequence;
import util.DataTable;
import util.EntityAbstractManager;

public class EntitySequenceManager extends EntityAbstractManager<Sequence> implements ObjectProcessor{

	/**allows to create sequence tree from MessageBank(ArrayList of Message)*/
	public void addManagedElement(String dataBankIdentifier, boolean destroyLittleLeaves, int nonLittleLeaveStatement) {
		MessageBank messageBank = CommonData.textManager.getManagedElement(dataBankIdentifier);if(messageBank==null)return;
		Sequence rootTree = Sequence.createSequenceTreeFromMessages(messageBank, destroyLittleLeaves, nonLittleLeaveStatement);if(rootTree==null)return;
		super.addManagedElement(rootTree, common.Lang.InnerTable.Misc.messageBankToPhrasesTreeAppendix+dataBankIdentifier);
		/**updating SequenceManager mechanism*/
		//nothing to update
	}
	@Override
	public Class<Sequence> getManagedObjectClass() {
		// TODO Auto-generated method stub
		return Sequence.class;
	}

	@Override
	public boolean process(String methodName, HashMap<String, Object> parameters) {
		int nCatches = 0;
		if(methodName.equals("addManagedElement")) {
			addManagedElement((String)parameters.get("dataBankIdentifier"), (Boolean)parameters.get("destroyLittleLeaves"), (Integer)parameters.get("nonLittleLeaveStatement"));
			nCatches++;
		}
		parameters.clear();
		parameters=null;
		if(nCatches!=1)return false;
		return true;
	}

}
