package gui;

import common.EntityEditor;
import common.CommonData;
import common.EntityDataManager;
import common.EntityTextManager;

public class GUI {
	public GUI() {
		CommonData.frame = new GUISchematicEditor();
		CommonData.frame_tableSelector=new GUITableSelector();
		CommonData.frame2 = new GUITableEditor();
		
		//postLoad
		CommonData.scheme = new EntityEditor(CommonData.WIDTH, CommonData.HEIGHT);
		CommonData.dataManager= new EntityDataManager();
		CommonData.textManager = new EntityTextManager();
		
	}

}