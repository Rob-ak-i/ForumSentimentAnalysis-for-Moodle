package common;

import languageprocessing.Sequence;
import util.EntityAbstractManager;

public class EntitySequenceManager extends EntityAbstractManager<Sequence>{

	/**allows to create sequence tree from MessageBank(ArrayList of Message)*/
	public void addManagedElement(String dataBankIdentifier, boolean destroyLittleLeaves, int nonLittleLeaveStatement) {
		MessageBank messageBank = CommonData.textManager.getManagedElement(dataBankIdentifier);if(messageBank==null)return;
		Sequence rootTree = Sequence.createSequenceTreeFromMessages(messageBank, destroyLittleLeaves, nonLittleLeaveStatement);if(rootTree==null)return;
		super.addManagedElement(rootTree, dataBankIdentifier);
		/**updating SequenceManager mechanism*/
		//nothing to update
	}

	@Override
	public Class<Sequence> getManagedObjectClass() {
		// TODO Auto-generated method stub
		return Sequence.class;
	}

}
