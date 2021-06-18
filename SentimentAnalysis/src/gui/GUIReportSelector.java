package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import common.CommonData;
import util.EntityAbstractManager;

public class GUIReportSelector extends GUIAbstractSelector{

	public GUIReportSelector() {
		super(common.Lang.InnerTable.Item.windowReportSelectorName, null, buttonAcceptAction);
		this.selectorBox.addItem(common.Lang.InnerTable.Action.computationsMenuAction0Name);
		this.selectorBox.addItem(common.Lang.InnerTable.Action.computationsMenuAction01Name);
		this.selectorBox.addItem(common.Lang.InnerTable.Action.computationsMenuAction1Name);
		this.selectorBox.addItem(common.Lang.InnerTable.Action.computationsMenuAction2Name);
		// TODO Auto-generated constructor stub
	}
	private static final AbstractAction buttonAcceptAction=new AbstractAction() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			CommonData.frame_reportSelector.performAction();
		}
	};
	protected void performActionInner() {
		if(innerState!=innerStateSave)return;
		switch(this.selectorBox.getSelectedIndex()) {
		case 0:
			common.Reports.createForumReport();
			break;
		case 1:
			common.Reports.createUserReport();
			break;
		case 2:
			common.Reports.makeResult1();
			break;
		case 3:
			common.Reports.makeNLP();
			break;
		}
		innerState=innerStateDefault;
		this.setVisible(false);
	}

}
