package parts;

import java.awt.Color;
import java.awt.Graphics;

import common.CommonData;
import common.EntityEditor;
import common.EntityEditorMouseListener;
import common.EntityRenderer;
import common.Lang;
import util.DataTable;
import util.Dot;

public class GenericPhantom {
	public static int phantomType=0;
	public static final int phantomType_FORUM=1;
	public static final int phantomType_DISABLED=0;
	public static String name="";
	public static String caption="";
	public static int discussIndex=-1;
	public static int xi=0;
	public static int yi=0;
	public static void flush(boolean disable){
		discussIndex=-1;
		GenericPhantom.name=Lang.InnerTable.Misc.table_tutorial;
		GenericPhantom.caption="tables count = "+Integer.toString(CommonData.data.tables.size());
		if(disable)
			phantomType=phantomType_DISABLED;
	}
	public static void changeDiscussIndex(int direction) {
		int tablesCount=CommonData.data.tables.size();
		if(tablesCount==0) return;
		discussIndex=discussIndex+direction;
		if(discussIndex<0)discussIndex=tablesCount-1;
		if(discussIndex>=tablesCount)discussIndex=0;
		DataTable table=CommonData.data.tables.get(discussIndex);
		GenericPhantom.name=table.title;
		GenericPhantom.caption="Forum Name = "+table.name+";Forum Messages Count = "+Integer.toString(table.nRows());
	}
	public static void performAction() {
		if(discussIndex<0||discussIndex>=CommonData.data.tables.size())return;
		Dot mathCoords = EntityRenderer.getRealDotFromImageCoords(xi, yi);
		boolean appendMode = EntityEditorMouseListener.isLShiftPressed;
		CommonData.scheme.printDiscuss(mathCoords.x, mathCoords.y, CommonData.data.tables.get(discussIndex),appendMode);
	}
	public static void draw(Graphics g){
		g.setColor(Color.gray);
		switch(phantomType){
		case phantomType_FORUM:
			g.drawLine(xi-30, yi-30, xi+30, yi+30);
			g.drawOval(xi-15, yi-15, 30, 30);
			g.drawString(name, xi+3, yi-6);
			g.drawString(caption, xi+3, yi+6);
			if(EntityEditorMouseListener.isLShiftPressed) {
				g.setColor(Color.red);
				g.drawString(Lang.InnerTable.Misc.table_appendMode, xi+3, yi+12);
			}
			break;
		}
		
	}
}
