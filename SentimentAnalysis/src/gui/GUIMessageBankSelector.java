package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JCheckBox;

import common.CommonData;
import common.MessageBank;
import common.Reports;

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
		panel.add(notDestroyLeavesCheck,BorderLayout.EAST);
		// TODO Auto-generated constructor stub
	}
	private static final AbstractAction buttonAcceptAction=new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			CommonData.frame_bankSelector.performAction();
		}
	};
	public void performAction() {
		boolean destroyLeaves=!notDestroyLeavesCheck.isSelected();
		MessageBank messageBank=null;
		switch(innerState) {
		case innerStateUseForScript:
			//TODO put script actions for messagesBank
			System.out.println("MessageBank WIF");
			break;
		case innerState_MakeSequenceTree:
			messageBank=CommonData.textManager.getManagedElement(selectorBox.getCaption());
			if(messageBank==null)break;
			CommonData.sequenceManager.addManagedElement(selectorBox.getCaption(), destroyLeaves, 2);
			break;
		default:
			System.out.println("GUIBankSelector.performAction: five/nine work");
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
