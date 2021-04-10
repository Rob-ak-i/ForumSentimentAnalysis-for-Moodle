package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;

import common.CommonData;
import common.EntityEditorMouseListener;
import common.MessageBank;
import common.Reports;
import parts.GenericPhantom;
import util.AdditionalDataObjects;

@SuppressWarnings("serial")
public class GUIMessageBankSelector extends GUIAbstractSelector {
	/**was set by random seed*/
	private static final long serialVersionUID = -2696903137510152442L;
	public static final int innerState_MakeSequenceTree = 5;
	public JCheckBox notDestroyLeavesCheck;
	
	public GUIMessageBankSelector() {
		super(common.Lang.InnerTable.Item.windowBankSelectorName, CommonData.textManager,buttonAcceptAction);
		labelStateNames.add(common.Lang.InnerTable.GUI.TableSelectorLableUpgradeToSequence);
		notDestroyLeavesCheck = new JCheckBox();
		notDestroyLeavesCheck.setText("notDestroyLeaves");

		Dimension preferredSize = new Dimension(300,50);
		notDestroyLeavesCheck.setSize(preferredSize);
		notDestroyLeavesCheck.setAlignmentX(Container.CENTER_ALIGNMENT);this.getContentPane().add(notDestroyLeavesCheck);
		//addComponent(notDestroyLeavesCheck,BorderLayout.EAST);
		// TODO Auto-generated constructor stub
	}
	private static final AbstractAction buttonAcceptAction=new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			CommonData.frame_bankSelector.performAction();
		}
	};
	protected void performActionInner() {
		boolean destroyLeaves=!notDestroyLeavesCheck.isSelected();
		MessageBank messageBank=null;
		int messageBankIndex=-1;
		switch(innerState) {
		case innerStatePrint:
			messageBankIndex=CommonData.textManager.getManagedElementIndex(selectorBox.getCaption());
			if(messageBankIndex==-1)break;
			EntityEditorMouseListener.setMode(EntityEditorMouseListener.MODE_PUT_PHANTOM_MODE);
			GenericPhantom.prepare(GenericPhantom.phantomType_TESTSYNTAX, messageBankIndex);
			break;
		case innerStateUseForScript:
			//TODO put script actions for messagesBank
			System.out.println("MessageBank WIF");
			break;
		case innerState_MakeSequenceTree:
			messageBank=CommonData.textManager.getManagedElement(selectorBox.getCaption());
			if(messageBank==null)break;
			processProcessor = CommonData.sequenceManager;
			processMethod="addManagedElement";
			processParameters=AdditionalDataObjects.pack("dataBankIdentifier", selectorBox.getCaption()).pack("destroyLittleLeaves", destroyLeaves).pack("nonLittleLeaveStatement", 2);
			//CommonData.sequenceManager.addManagedElement(selectorBox.getCaption(), destroyLeaves, 2);
			break;
		default:
			System.out.println("GUIBankSelector.performAction: no such action:"+innerState);
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
		CommonData.frame_bankSelector.setVisible(false);
	}

}
