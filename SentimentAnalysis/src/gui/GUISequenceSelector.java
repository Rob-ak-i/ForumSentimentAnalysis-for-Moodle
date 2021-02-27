package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import common.CommonData;
import common.MessageBank;
import common.EntityEditorMouseListener;
import languageprocessing.Sequence;
import parts.GenericPhantom;
import util.EntityAbstractManager;

public class GUISequenceSelector extends GUIAbstractSelector {
	/**was set by random seed*/
	private static final long serialVersionUID = 3375581082850809955L;

	public GUISequenceSelector() {
		super(common.Lang.InnerTable.Item.windowSequenceSelectorName, CommonData.sequenceManager, buttonAcceptAction);
	}
	private static final AbstractAction buttonAcceptAction=new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			CommonData.frame_sequenceSelector.performAction();
		}
	};
	protected void performActionInner() {
		Sequence sequence=null;
		int sequenceIndex;
		switch(innerState) {
		case innerStatePrint:
			sequenceIndex=CommonData.sequenceManager.getManagedElementIndex(selectorBox.getCaption());
			if(sequenceIndex==-1)break;
			EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_PUT_PHANTOM_MODE);
			GenericPhantom.prepare(GenericPhantom.phantomType_SEQUENCE, sequenceIndex);
			break;
		default:
			System.out.println("GUISequenceSelector.performAction: five/nine work");
			//table=CommonData.textManager.//.getTable(selectorBox.getCaption());
			//if(innerState==innerState_makeResult1_inner)
			//	Reports.makeResult1_inner(table);
			//if(innerState==innerState_languageprocessing) {
			//	System.out.println("five/nine work");
			//	languageprocessing.LanguageProcessor.doAllWIP(null);
			//}
			break;
		}
		innerState=innerStateDefault;
		CommonData.frame_sequenceSelector.setVisible(false);
	}
	

}
