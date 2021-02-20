package common;

import java.awt.Canvas;
import java.util.ArrayList;

import gui.GUITableEditor;
import gui.GUITableSelector;
import gui.GUISchematicEditor;
import parts.PartBasic;

public class CommonData {
//SETUP BLOCK
	public static int WIDTH=800;
	public static int HEIGHT=600;
	public static int HEIGHTFRAMEDIFFERENCE=0;//=85;

	public static String dataSubDir = "data//";
	public static String savesSubDir = "saves//";
	public static String reportsSubDir = "reports//";
	public static String appDir;

	public static int selectorMode=0;
	public static final int selectorMode_ELEMENT=0;
	public static final int selectorMode_NODE=1;
	public static final int selectorMode_PART=2;

//VARS BLOCK
	//public static Graphics graphics;
	//public static Graphics2D graphics2d;

	public static GUISchematicEditor frame=null;
	public static GUITableSelector frame_tableSelector=null;
	public static GUITableEditor frame2=null;
	
	public static Canvas canvas=null;
	public static EntityEditorMouseListener listener;
	public static EntityRenderer renderer=null;
	//public static JPanel panel=null;
	public static EntityEditor scheme=null;
	
	//public static PartBasic partSelected=null;
	public static ArrayList<PartBasic> partsSelected=new ArrayList<PartBasic>();
	public static javax.swing.JEditorPane nameedit=null;
	
	public static javax.swing.JEditorPane captionedit=null;

	public static EntityDataManager dataManager=null;
	public static EntityTextManager textManager=null;
	//public static TextForum discuss=null;
	
	
	/**last loaded schematic*/
	public static String fileName=null;
}
