package parts;

import java.awt.Color;
import java.awt.Graphics;

import common.CommonData;
import common.EntityEditorMouseListener;
import common.EntityRenderer;
import common.Lang;
import util.Dot;
import util.EntityAbstractManager;
import util.ManagedObject;

public class GenericPhantom {
	public static int phantomType=0;
	public static final int phantomType_SEQUENCE=2;
	public static final int phantomType_FORUM=1;
	public static final int phantomType_DISABLED=0;
	public static String name="";
	public static String caption="";
	public static int selectedElementIndex=-1;
	@SuppressWarnings("rawtypes")
	public static EntityAbstractManager selectedManager=null;
	public static int xi=0;
	public static int yi=0;
	public static void flush(boolean disable){
		selectedElementIndex=-1;
		GenericPhantom.name=Lang.InnerTable.Misc.table_tutorial;
		GenericPhantom.caption="DataTables:"+Integer.toString(CommonData.dataManager.getManagedElementsCount())+";DataBanks:"+Integer.toString(CommonData.textManager.getManagedElementsCount())+"Sequences:"+Integer.toString(CommonData.sequenceManager.getManagedElementsCount());
		if(disable) {
			phantomType=phantomType_DISABLED;
			selectedManager=null;
		}
	}
	public static void prepare(int chosenPhantomType, int selectedItem) {
		//flush(false);
		phantomType=chosenPhantomType;
		switch(phantomType) {
		case phantomType_FORUM:
			selectedManager=CommonData.dataManager;
			break;
		case phantomType_SEQUENCE:
			selectedManager=CommonData.sequenceManager;
			break;
		}
		setElementIndex(selectedItem);
	}
	private static void setElementIndex(int elementIndex) {
		selectedElementIndex=elementIndex;
		ManagedObject element=selectedManager.getManagedElement(elementIndex);
		GenericPhantom.name=selectedManager.getManagedElementIdentifier(elementIndex);
		GenericPhantom.caption=selectedManager.getManagedObjectClass().getSimpleName()+" name = "+element.getName()+"; size = "+Integer.toString(element.getMeasurableParameter());
	}
	public static void changeElementIndex(int direction) {
		if(selectedManager==null)return;
		int elementsCount=selectedManager.getManagedElementsCount();
		if(elementsCount==0) return;
		selectedElementIndex=selectedElementIndex+direction;
		if(selectedElementIndex<0)selectedElementIndex=elementsCount-1;
		if(selectedElementIndex>=elementsCount)selectedElementIndex=0;
		setElementIndex(selectedElementIndex);
	}
	public static void performAction() {
		if(selectedElementIndex<0||selectedElementIndex>=selectedManager.getManagedElementsCount())return;
		Dot mathCoords = EntityRenderer.getRealDotFromImageCoords(xi, yi);
		switch(phantomType) {
		case phantomType_FORUM:
			CommonData.scheme.printDiscuss(mathCoords.x, mathCoords.y, CommonData.dataManager.getManagedElement(selectedElementIndex),EntityEditorMouseListener.isLShiftPressed);
			break;
		case phantomType_SEQUENCE:
			CommonData.scheme.printSequenceTree(mathCoords.x, mathCoords.y, CommonData.sequenceManager.getManagedElement(selectedElementIndex));
			break;
		}
		
	}
	public static void draw(Graphics g){
		g.setColor(Color.gray);
		switch(phantomType){
		case phantomType_FORUM:
			g.drawLine(xi-30, yi-30, xi+30, yi+30);
			g.drawOval(xi-15, yi-15, 30, 30);
			g.setColor(Color.black);
			g.drawString(name, xi+3, yi-6);
			g.drawString(caption, xi+3, yi+6);
			if(EntityEditorMouseListener.isLShiftPressed) {
				g.setColor(Color.red);
				g.drawString(Lang.InnerTable.Misc.table_appendMode, xi+3, yi+12);
			}
			break;
		case phantomType_SEQUENCE:
			g.drawOval(xi-30, yi-30, 60, 60);
			g.drawLine(xi-15, yi-15, xi, yi);
			g.drawLine(xi, yi-15, xi, yi+15);
			g.drawLine(xi+15, yi-15, xi, yi);
			g.setColor(Color.black);
			g.drawString(name, xi+3, yi-6);
			g.drawString(caption, xi+3, yi+6);
			
			break;
		}
		
	}
}
