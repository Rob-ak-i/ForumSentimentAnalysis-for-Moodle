package gui;

import common.EntityEditor;
import common.EntitySequenceManager;
import common.CommonData;
import common.EntityDataManager;
import common.EntityTextManager;

public class GUI {
	public GUI() {
		//preLoad
		CommonData.dataManager= new EntityDataManager();
		CommonData.textManager = new EntityTextManager();
		CommonData.sequenceManager = new EntitySequenceManager();
		
		//load
		CommonData.frame = new GUISchematicEditor();
		CommonData.frame2 = new GUITableEditor();
		//load - may be lazyloadmode
		CommonData.frame_tableSelector=new GUITableSelector();
		CommonData.frame_bankSelector=new GUIMessageBankSelector();
		CommonData.frame_sequenceSelector=new GUISequenceSelector();
		
		//postLoad
		CommonData.scheme = new EntityEditor(CommonData.WIDTH, CommonData.HEIGHT);
		
	}

}