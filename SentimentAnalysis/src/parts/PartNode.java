package parts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import common.CommonData;
import common.EntityEditor_Helper;

public class PartNode extends PartBasic{
	public static int PART_DOT_SIZE=5;
	public static boolean drawRecurse=false;
	private int elementsCount=0;
	private static boolean lockedKostilIzzaRekursiiVEgenByEIIGenByE=false;
	public ArrayList<PartBinaryBasic>parts=null;
	public ArrayList<Integer> partsOrientations=null;
	public static int partsOrientations_FROMME=-1;
	public static int partsOrientations_TOME=1;
	public PartNode(int x, int y) {
		super(x, y, PartNode.PART_DOT_SIZE, PartNode.PART_DOT_SIZE);
	}
	/*
	public void destroy() {
		parts.clear();
	}
	*/
	public void drawWithColor(Color color) {
		g.setColor(color);
		g.fillOval(x-xr, y-yr, width, height);
		super.drawName(color);
		if(lockedKostilIzzaRekursiiVEgenByEIIGenByE)return;
		lockedKostilIzzaRekursiiVEgenByEIIGenByE=true;
		if((parts!=null)&&drawRecurse==true) {
			for(int i=0;i<parts.size();++i)
				parts.get(i).drawWithColor((color==EntityEditor_Helper.colorMain) ? EntityEditor_Helper.colorMain : EntityEditor_Helper.colorRandom());
		}
		lockedKostilIzzaRekursiiVEgenByEIIGenByE=false;
	}
	public String getCaption() {
		return Integer.toString(elementsCount);		
	}
	@Override
	public void setCaption(String value) {
		// TODO Auto-generated method stub
		System.out.println(common.Lang.InnerTable.Console.partsPartNode_SetCaption);
		
	}	
	public void setCaption(double value) {
		System.out.println(common.Lang.InnerTable.Console.partsPartNode_SetCaption);
		//elementsCount=(int) value;
	}
	public void addPart() {
		elementsCount+=1;
	}
	public void removePart() {
		elementsCount-=1;
	}
	
	public void getPartsForNode(ArrayList<PartBinaryBasic>parts){
		if(this.parts!=null)System.out.println(common.Lang.InnerTable.Console.partsPartNode_getPartsForNode);
		if(this.parts!=null)this.parts.clear();
		if(this.partsOrientations!=null)this.partsOrientations.clear();
		ArrayList<Integer> partsOrientations = new ArrayList<Integer>();
		ArrayList<PartBinaryBasic> result = new ArrayList<PartBinaryBasic>();
		PartBinaryBasic part;
		for(int i=0;i<parts.size();++i) {
			part = parts.get(i);
			if(part.nodeFrom.equals(this)) {result.add(part);partsOrientations.add((partsOrientations_FROMME));}
			if(part.nodeTo.equals(this)) {result.add(part);partsOrientations.add((partsOrientations_TOME));}
		}
		this.parts=result;
		this.partsOrientations=partsOrientations;
	}

	public String printVoltageDifference(PartNode node) {
		String s="";
		s=partName+"-"+node.partName;
		return s;
	}
}
