package common;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

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

	public static boolean isUNIXSystem(){
		return false;
	}
//VARS BLOCK
	//public static Graphics graphics;
	//public static Graphics2D graphics2d;
	public static Canvas canvas=null;
	public static EntityEditorMouseListener listener;
	public static EntityRenderer renderer=null;
	public static JPanel panel=null;
	public static JFrame frame=null;
	public static EntityEditor scheme=null;
	
	public static parts.PartBasic partSelected=null;
	public static javax.swing.JEditorPane nameedit=null;
	
	public static javax.swing.JEditorPane captionedit=null;
	
	public static TextForum discuss=null;
	
	
	/**last loaded schematic*/
	public static String fileName=null;
}
